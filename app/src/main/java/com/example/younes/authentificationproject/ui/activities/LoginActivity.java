package com.example.younes.authentificationproject.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.younes.authentificationproject.R;
import com.example.younes.authentificationproject.animations.ProgressBarAnimation;
import com.example.younes.authentificationproject.api.RetrofitManager;
import com.example.younes.authentificationproject.interfaces.ResponseListner;
import com.example.younes.authentificationproject.models.Assignments;
import com.example.younes.authentificationproject.models.Organisation;
import com.example.younes.authentificationproject.models.UserAssignmentsRequestBody;
import com.example.younes.authentificationproject.models.UserInfo;
import com.example.younes.authentificationproject.models.UserLoginInfo;
import com.example.younes.authentificationproject.services.DatabaseManager;
import com.example.younes.authentificationproject.utils.Logger;
import com.example.younes.authentificationproject.utils.SaveSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    //Initialise Views
    EditText userNameFieldView;
    EditText userPasswordFieldView;
    mehdi.sakout.fancybuttons.FancyButton loginButtonView;
    LinearLayout loginFormLinearLayout;
    ImageView loadingImage;
    ProgressBar myProgress;

    //in the organisation Case (Login type tutor) I tried to extract data from the server for each organisation
    //and counted the success and failure operation
    int successfulAssignmentDataResponse=0,unsuccessfulAssignmentDataResponse=0;
    int AssignmentDataResponseTotal;

    // progress of success / Total
    private ProgressBarAnimation progressBarAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        if(SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
            startActivity(intent);
        } else {
            //loginForm.setVisibility(View.VISIBLE);
            // Views Declarations
            userNameFieldView = findViewById(R.id.login_username);
            userPasswordFieldView = findViewById(R.id.login_userpassword);
            loginButtonView = findViewById(R.id.login_button);
            loginFormLinearLayout = findViewById(R.id.form);
            loadingImage = findViewById(R.id.loading_image_view);
            myProgress = findViewById(R.id.progress);

            // Click Listners
            loginButtonView.setOnClickListener(new LoginButtonClickHandler());

        }



    }
    public void goToListActivity(){
        Intent intent = new Intent(LoginActivity.this, UserListActivity.class);
        startActivity(intent);
    }
    public void startProgressBarAnimation(int initialPosition,int endPosition){
        progressBarAnimation = new ProgressBarAnimation(myProgress, initialPosition, endPosition);
        progressBarAnimation.setDuration(300);
        myProgress.startAnimation(progressBarAnimation);
    }

    private class LoginButtonClickHandler implements View.OnClickListener {
        String userNameValue,userPasswordValue;
        @Override
        public void onClick(View v) {

            // get input Values
            userNameValue = userNameFieldView.getText().toString();
            userPasswordValue = userPasswordFieldView.getText().toString();

            loginButtonView.setText("Wait please ...");

            // ask for user info from the server
            UserLoginInfo userInsertedLoginInfo = new UserLoginInfo(userNameValue,userPasswordValue,"2018_1_5_1");
            RetrofitManager.getInstance(LoginActivity.this).getUserInfo(userInsertedLoginInfo,new userInfoCallBack());

        }
    }


    private class userInfoCallBack implements Callback<UserInfo> {
        @Override
        public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
            if(response.isSuccessful()){

                if(response.body().getStatus() != null)//status == "failed" //username error ; userpass error ; applicationId error ;
                {
                    loginButtonView.setText("Problem Occured ,Try Again ...");
                }else //status == null // No problem
                    {

                    UserInfo myUserInfo = response.body();
                    String myAccessToken = myUserInfo.getAccessToken();
                    String myUserType = myUserInfo.getType();



                    // Save logging data for next time
                    SaveSharedPreference.setLoggedIn(getApplicationContext(),true);
                    SaveSharedPreference.setAccessToken(getApplicationContext(),myAccessToken);
                    SaveSharedPreference.setUserType(getApplicationContext(),myUserType);

                    if(myUserType.equals("tutor")){
                        DatabaseManager.getInstance(getApplicationContext()).storeOrganisations(myUserInfo.getOrganizations());
                        AssignmentDataResponseTotal = myUserInfo.getOrganizations().size();
                        for(Organisation organisation : myUserInfo.getOrganizations()){
                            getOrganisationKids(organisation,myAccessToken);
                        }
                        initialiseLoadingScreen(1000*AssignmentDataResponseTotal);

                    }
                    else{
                        DatabaseManager.getInstance(getApplicationContext()).storeParentChildren(myUserInfo.getEnrollments());
                        goToListActivity();
                    }
                    //Store Organisations and Kid In the Local Database
                }
            }

        }

        @Override
        public void onFailure(Call<UserInfo> call, Throwable t) {
            Log.e("On response body : ", "onResponse: Hello Failure ");
        }

        private void initialiseLoadingScreen(int progressBarMaximum){
            YoYo.with(Techniques.ZoomOut).duration(400) .playOn(loginFormLinearLayout);
            YoYo.with(Techniques.ZoomIn).duration(700)  .playOn(loadingImage);
            YoYo.with(Techniques.FadeIn).duration(1000) .playOn(myProgress);
            loadingImage.setVisibility(View.VISIBLE);
            initialiseProgressBar(1000*AssignmentDataResponseTotal);
        }
        private void initialiseProgressBar(int progressBarMaximum){
            myProgress.setMax(progressBarMaximum);
            myProgress.setProgress(0);
            myProgress.setVisibility(View.VISIBLE);
        }

    }



    public void getOrganisationKids(Organisation organisation,String Authorization){
        Log.e("Retrofit Request", "getOrganisationKids: "+"Requesting data for user :"+organisation.getPivot().getUserId()+" ; Organisation : "+organisation.getPivot().getOrganizationId());
        RetrofitManager.getInstance(this).getUserAssignments(createRequestBody(organisation),"Bearer "+Authorization,new UserAssignmentsCallBack(organisation.getId()));
    }



    private UserAssignmentsRequestBody createRequestBody(Organisation organisation){
        int organisationId = Integer.parseInt(organisation.getPivot().getOrganizationId());
        int userId = Integer.parseInt(organisation.getPivot().getUserId());
        UserAssignmentsRequestBody userAssignmentsRequestBody = new UserAssignmentsRequestBody();
        userAssignmentsRequestBody.setAppId("2018_1_5_1");
        userAssignmentsRequestBody.setOrganizationId(organisationId);
        userAssignmentsRequestBody.setUserId(userId);
        return userAssignmentsRequestBody;
    }
    private class UserAssignmentsCallBack implements Callback<Assignments>,ResponseListner {
        int organisationId;
        UserAssignmentsCallBack(int organisationId){
            this.organisationId = organisationId;
        }
        @Override
        public void onResponse(Call<Assignments> call, Response<Assignments> response) {
            Logger.errorPring("Success : type = "+response.body().getType());
            DatabaseManager.getInstance(getApplicationContext()).storeOrganisationsChildren(response.body());
            // add a successful attempt to get organisation data
            onSuccessfulResponse();
            // verify if all responses ended and all of them are successfull
            onResponsesEnd();
        }

        @Override
        public void onFailure(Call<Assignments> call, Throwable t) {
            onFailureResponse();
        }

        @Override
        public void onSuccessfulResponse() {
            successfulAssignmentDataResponse++;
            startProgressBarAnimation(myProgress.getProgress(),myProgress.getProgress()+1000);
            showProgressInLog();
        }


        @Override
        public void onFailureResponse() {
            unsuccessfulAssignmentDataResponse++;
            showProgressInLog();
        }

        @Override
        public int responsesCount() {
            return AssignmentDataResponseTotal;
        }

        @Override
        public void onResponsesEnd() {
            if(responsesCount()==successfulAssignmentDataResponse)
                goToListActivity();
            else if (responsesCount()==successfulAssignmentDataResponse +unsuccessfulAssignmentDataResponse){
                Logger.errorPring("Retreiving data from the server was not totally successful , some data is missing");
            }
        }
        public void showProgressInLog(){
            Logger.errorPring("Success Listner Count :" +successfulAssignmentDataResponse + "| Failure Listner Count : "+unsuccessfulAssignmentDataResponse +" From Total :" + responsesCount());
        }
    }
}
