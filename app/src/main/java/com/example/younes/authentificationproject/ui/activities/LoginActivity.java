package com.example.younes.authentificationproject.ui.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.younes.authentificationproject.R;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText userNameFieldView;
    EditText userPasswordFieldView;
    Button loginButtonView;

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDatabase;
    int successfulAssignmentDataResponse=0,unsuccessfulAssignmentDataResponse=0;
    int AssignmentDataResponseTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);



        // View Declarations
        userNameFieldView = findViewById(R.id.login_username);
        userPasswordFieldView = findViewById(R.id.login_userpassword);
        loginButtonView = findViewById(R.id.login_button);
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
            userPasswordValue = userPasswordFieldView.getText().toString();
            UserLoginInfo userInsertedLoginInfo = new UserLoginInfo("t1@test.com","secret","app_id");
//            userInsertedLoginInfo.setApplicationId("app_id");
//            userInsertedLoginInfo.setUsernameValue("t1@test.com");
//            userInsertedLoginInfo.setUserpassValue("secret");
            RetrofitManager.getInstance(LoginActivity.this).getUserInfo(userInsertedLoginInfo,new userInfoCallBack());

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
                storeOrganisations(myUserInfo.getOrganizations());
                AssignmentDataResponseTotal = myUserInfo.getOrganizations().size();
                for(Organisation organisation : myUserInfo.getOrganizations()){
                    getOrganisationKids(organisation,myAccessToken);
                }


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
        userAssignmentsRequestBody.setAppId("2018_2_3_3");
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
}
