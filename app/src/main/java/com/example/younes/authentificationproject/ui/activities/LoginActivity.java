package com.example.younes.authentificationproject.ui.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
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
import com.example.younes.authentificationproject.models.Kids;
import com.example.younes.authentificationproject.models.Organisation;
import com.example.younes.authentificationproject.models.UserAssignmentsRequestBody;
import com.example.younes.authentificationproject.models.UserInfo;
import com.example.younes.authentificationproject.models.UserLoginInfo;
import com.example.younes.authentificationproject.services.DatabaseHelper;
import com.example.younes.authentificationproject.utils.Logger;
import com.example.younes.authentificationproject.utils.SaveSharedPreference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText userNameFieldView;
    EditText userPasswordFieldView;
    mehdi.sakout.fancybuttons.FancyButton loginButtonView;
    LinearLayout formLinearLayout;
    ImageView loadingImage;
    ProgressBar myProgress;

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDatabase;
    int successfulAssignmentDataResponse=0,unsuccessfulAssignmentDataResponse=0;
    int AssignmentDataResponseTotal;
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
        }

        // Views Declarations
        userNameFieldView = findViewById(R.id.login_username);
        userPasswordFieldView = findViewById(R.id.login_userpassword);
        loginButtonView = findViewById(R.id.login_button);
        formLinearLayout = findViewById(R.id.form);
        loadingImage = findViewById(R.id.loading_image_view);
        myProgress = findViewById(R.id.progress);

        // Click Listners
        loginButtonView.setOnClickListener(new LoginButtonClickHandler());

        //Database Instences Declarations
        mDBHelper = DatabaseHelper.getInstance(this);
        mDatabase  = mDBHelper.getWritableDatabase();
//        mDatabase.close();
    }

    private class LoginButtonClickHandler implements View.OnClickListener {
        String userNameValue,userPasswordValue;
        @Override
        public void onClick(View v) {
            userNameValue = userNameFieldView.getText().toString();
            loginButtonView.setText("Wait please ...");
            userPasswordValue = userPasswordFieldView.getText().toString();
            UserLoginInfo userInsertedLoginInfo = new UserLoginInfo("t1@test.com","secret","2018_1_5_1");
            RetrofitManager.getInstance(LoginActivity.this).getUserInfo(userInsertedLoginInfo,new userInfoCallBack());
//            userNameFieldView.
//            collapse(formLinearLayout);



        }
    }
    public void goToListActivity(){
        Intent intent = new Intent(LoginActivity.this, UserListActivity.class);
        startActivity(intent);
    }

    private class userInfoCallBack implements Callback<UserInfo> {
        @Override
        public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
            if(response.isSuccessful()){
                UserInfo myUserInfo = response.body();
                String myAccessToken = myUserInfo.getAccessToken();
                AssignmentDataResponseTotal = myUserInfo.getOrganizations().size();

                // Save logging data for next time
                SaveSharedPreference.setLoggedIn(getApplicationContext(),true);
                SaveSharedPreference.putAccessToken(getApplicationContext(),myAccessToken);

                //Store Organisations and Kids In the Local Database
                storeOrganisations(myUserInfo.getOrganizations());

                for(Organisation organisation : myUserInfo.getOrganizations()){
                    getOrganisationKids(organisation,myAccessToken);
                }
                YoYo.with(Techniques.ZoomOut)
                        .duration(400)
                        .playOn(formLinearLayout);
                myProgress.setMax(1000*AssignmentDataResponseTotal);
                myProgress.setProgress(0);
                loadingImage.setVisibility(View.VISIBLE);
                myProgress.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.ZoomIn)
                        .duration(700)
                        .playOn(loadingImage);
                YoYo.with(Techniques.FadeIn)
                        .duration(1000)
                        .playOn(myProgress);


            }
        }

        @Override
        public void onFailure(Call<UserInfo> call, Throwable t) {
            Log.e("On response body : ", "onResponse: Hello Failure ");
        }

    }
    public void getOrganisationKids(Organisation organisation,String Authorization){
        Log.e("Retrofit Request", "getOrganisationKids: "+"Requesting data for user :"+organisation.getPivot().getUserId()+" ; Organisation : "+organisation.getPivot().getOrganizationId());
        RetrofitManager.getInstance(this).getUserAssignments(createRequestBody(organisation),"Bearer "+Authorization,new UserAssignmentsCallBack(organisation.getId()));
    }

    /// Storing Organisations Data In LocalDB
    public void storeOrganisations(List<Organisation> organisationList){
        for(Organisation organisation: organisationList )
            storeOrganisation(organisation);
    }
    public void storeOrganisation(Organisation organisation){
        if (mDatabase != null){
            //prepare the transaction information that will be saved to the database
            ContentValues organisationRow = new ContentValues();
            organisationRow.put("id", organisation.getId());
            organisationRow.put("name", organisation.getName());
            organisationRow.put("description", organisation.getDescription());
            organisationRow.put("phone", organisation.getPhone());
            organisationRow.put("email", organisation.getEmail());
            organisationRow.put("address", organisation.getAddress());
            organisationRow.put("deleted_at", organisation.getDeletedAt());
            organisationRow.put("created_at", organisation.getCreatedAt());
            organisationRow.put("updated_at", organisation.getUpdatedAt());


            try {
                mDatabase.insertOrThrow("organisations", null, organisationRow);
                Log.d("Database Insertion", "Organisation Added");

            } catch (Exception e) {
                Log.e("Database Insertion", "Error " + e.getCause() + " " + e.getMessage());
            }
            }
    }
    ///String Kids Data In localDB
    public void storeOrganisationChildren(Assignments assignments ){
        for(Assignments.Assignment assignment : assignments.getAssignments())
            storeChild(assignment);
    }
    public void storeChild(Assignments.Assignment assignment){
        if (mDatabase != null){
            //prepare the transaction information that will be saved to the database
            ContentValues kidRow = new ContentValues();
            kidRow.put("id", assignment.getId());
            kidRow.put("id_organisation", assignment.getOrganisationId());
            kidRow.put("first_name", assignment.getKids().getFirstName());
            kidRow.put("last_name", assignment.getKids().getLastName());
            kidRow.put("gender", assignment.getKids().getGender());
            kidRow.put("birthday", assignment.getKids().getBirthday());
            kidRow.put("parent_email", assignment.getKids().getParentEmail());
            kidRow.put("deleted_at", assignment.getKids().getDeletedAt());
            kidRow.put("created_at", assignment.getKids().getCreatedAt());
            kidRow.put("updated_at", assignment.getKids().getUpdatedAt());


            try {
                mDatabase.insertOrThrow("kids", null, kidRow);
                Log.d("Database Insertion", "Kid ("+assignment.getId()+") Added");

            } catch (Exception e) {
                Log.e("Database Insertion", "Error " + e.getCause() + " " + e.getMessage());
            }
        }
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
            storeOrganisationChildren(response.body());
            onSuccessfulResponse();
            onResponsesEnd();
        }

        @Override
        public void onFailure(Call<Assignments> call, Throwable t) {
            Logger.errorPring("Failure : type = ");
            onFailureResponse();
        }

        @Override
        public void onSuccessfulResponse() {
            successfulAssignmentDataResponse++;
            progressBarAnimation = new ProgressBarAnimation(myProgress, myProgress.getProgress(), myProgress.getProgress()+1000);
            progressBarAnimation.setDuration(300);
            myProgress.startAnimation(progressBarAnimation);
            showProgress();
        }

        @Override
        public void onFailureResponse() {
            unsuccessfulAssignmentDataResponse++;
            showProgress();
        }

        @Override
        public int responsesCount() {
            return AssignmentDataResponseTotal;
        }

        @Override
        public void onResponsesEnd() {



            if(responsesCount()==successfulAssignmentDataResponse)
                goToListActivity();
        }
        public void showProgress(){
            Logger.errorPring("Success Listner Count :" +successfulAssignmentDataResponse + "| Failure Listner Count : "+unsuccessfulAssignmentDataResponse +" From Total :" + responsesCount());
        }
    }
    public static void expand(final View v) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(350);
        v.startAnimation(a);
    }
    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.INVISIBLE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(350);
        v.startAnimation(a);
    }
}
