package com.example.younes.authentificationproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by younes on 8/19/2018.
 */

public class UserAssignmentsRequestBody {
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("organization_id")
    @Expose
    private Integer organizationId;
    @SerializedName("app_id")
    @Expose
    private String appId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
