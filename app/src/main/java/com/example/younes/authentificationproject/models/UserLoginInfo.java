package com.example.younes.authentificationproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by younes on 8/19/2018.
 */

public class UserLoginInfo {


    public UserLoginInfo(String usernameValue, String userpassValue, String applicationId) {
        this.usernameValue = usernameValue;
        this.userpassValue = userpassValue;
        this.applicationId = applicationId;
    }

    @SerializedName("username")
    @Expose
    String usernameValue;
    @SerializedName("password")
    @Expose
    String userpassValue;
    @SerializedName("app_id")
    @Expose
    String applicationId;

    // Getters

//    public String getUsernameValue() {
//        return usernameValue;
//    }
//
//    public String getUserpassValue() {
//        return userpassValue;
//    }
//
//    public String getApplicationId() {
//        return applicationId;
//    }

    // Setters
//    public void setUsernameValue(String usernameValue) {
//        this.usernameValue = usernameValue;
//    }
//
//    public void setUserpassValue(String userpassValue) {
//        this.userpassValue = userpassValue;
//    }
//
//    public void setApplicationId(String applicationId) {
//        this.applicationId = applicationId;
//    }

}
