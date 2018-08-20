package com.example.younes.authentificationproject.interfaces;

import com.example.younes.authentificationproject.models.Assignments;
import com.example.younes.authentificationproject.models.UserAssignmentsRequestBody;
import com.example.younes.authentificationproject.models.UserInfo;
import com.example.younes.authentificationproject.models.UserLoginInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by younes on 8/19/2018.
 */

public interface AuthEndPointInterface {

    @POST("api/v1/login")
    Call<UserInfo> getUserInfo(@Body UserLoginInfo userLoginInfo);
    @POST("api/v1/userAssignments")
    Call<Assignments> getUserOrganisationAssignments(@Body UserAssignmentsRequestBody userAssignmentsRequestBody, @Header("Authorization") String authHeader);
}
