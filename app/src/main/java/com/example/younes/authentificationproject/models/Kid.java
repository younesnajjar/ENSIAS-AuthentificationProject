package com.example.younes.authentificationproject.models;

import android.database.Cursor;

import com.example.younes.authentificationproject.utils.Logger;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by younes on 8/19/2018.
 */

public class Kid {
    public Kid(Integer id, int organisationId, String firstName, String lastName, String gender, String birthday, String parentEmail, String deletedAt, String createdAt, String updatedAt) {
        this.id = id;
        this.organisationId = organisationId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.parentEmail = parentEmail;
        this.deletedAt = deletedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("parent_email")
    @Expose
    private String parentEmail;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;



    private int organisationId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getOrganisationId() {return organisationId;}

    public void setOrganisationId(int organisationId) { this.organisationId = organisationId; }

    public static Kid getKidFromCursor(Cursor cursor){
        Logger.errorPring("getKidFromCursor is inisialized");
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        int organisationId = cursor.getInt(cursor.getColumnIndex("id_organisation"));
        String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
        String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
        String gender = cursor.getString(cursor.getColumnIndex("gender"));
        String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
        String parentEmail = cursor.getString(cursor.getColumnIndex("parent_email"));
        String deletedAt = cursor.getString(cursor.getColumnIndex("deleted_at"));
        String createdAt = cursor.getString(cursor.getColumnIndex("created_at"));
        String updatedAt = cursor.getString(cursor.getColumnIndex("updated_at"));

        return new Kid(id,organisationId,firstName,lastName,gender,birthday,parentEmail,deletedAt,createdAt,updatedAt);
    }
}
