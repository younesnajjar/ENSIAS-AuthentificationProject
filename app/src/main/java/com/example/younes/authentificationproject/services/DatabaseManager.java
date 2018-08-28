package com.example.younes.authentificationproject.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.younes.authentificationproject.models.Assignments;
import com.example.younes.authentificationproject.models.Enrollment;
import com.example.younes.authentificationproject.models.Organisation;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by younes on 8/19/2018.
 */

public class DatabaseManager {
    private DatabaseHelper mDBHelper;
    private static DatabaseManager mDatabaseManager;
    private SQLiteDatabase mDatabase;
    Context mContext;
    SQLiteDatabase db;
    Cursor authentificationDataCursor;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Game";
    // Table name
    private static final String TABLE_GAME_i = "Game_Infos";
    DatabaseManager(Context context){
        mContext = context;
        mDBHelper = DatabaseHelper.getInstance(context);
        mDatabase  = mDBHelper.getWritableDatabase();
    }
    public static DatabaseManager getInstance(Context context){
        if (mDatabaseManager == null){
            mDatabaseManager = new DatabaseManager(context.getApplicationContext());
        }
        return mDatabaseManager;
    }
//    private DatabaseHelper mDBHelper;
//    private SQLiteDatabase mDatabase;

//    DatabaseManager(Context context){
//        mContext = context;
//        mDatabase =
//    }

//create table if not exists Organisations (id_Organisation INTEGER NOT NULL,name_organisation TEXT NOT NULL);
//create table if not exists Kid (id_kid INTEGER NOT NULL,name_kid TEXT NOT NULL);
//    }
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
    ///String Kid Data In localDB
    public void storeOrganisationsChildren(Assignments assignments ){
        for(Assignments.Assignment assignment : assignments.getAssignments())
            storeOrganisationChild(assignment);
    }
    public void storeParentChildren(List<Enrollment> enrollments ){
        for(Enrollment enrollment : enrollments)
            storeParentChild(enrollment);
    }
    public void storeOrganisationChild(Assignments.Assignment assignment){
        if (mDatabase != null){
            //prepare the transaction information that will be saved to the database
            ContentValues kidRow = new ContentValues();
            kidRow.put("id", assignment.getId());

            kidRow.put("id_organisation", assignment.getOrganisationId());

            kidRow.put("first_name", assignment.getKid().getFirstName());
            kidRow.put("last_name", assignment.getKid().getLastName());
            kidRow.put("gender", assignment.getKid().getGender());
            kidRow.put("birthday", assignment.getKid().getBirthday());
            kidRow.put("parent_email", assignment.getKid().getParentEmail());
            kidRow.put("deleted_at", assignment.getKid().getDeletedAt());
            kidRow.put("created_at", assignment.getKid().getCreatedAt());
            kidRow.put("updated_at", assignment.getKid().getUpdatedAt());


            try {
                mDatabase.insertOrThrow("kids", null, kidRow);
                Log.d("Database Insertion", "Kid ("+assignment.getId()+") Added");

            } catch (Exception e) {
                Log.e("Database Insertion", "Error " + e.getCause() + " " + e.getMessage());
            }
        }
    }
    public void storeParentChild(Enrollment enrollment){
        if (mDatabase != null){
            //prepare the transaction information that will be saved to the database
            ContentValues kidRow = new ContentValues();
            kidRow.put("id", enrollment.getKid().getId());

//            kidRow.put("id_organisation", assignment.getOrganisationId());

            kidRow.put("first_name", enrollment.getKid().getFirstName());
            kidRow.put("last_name", enrollment.getKid().getLastName());
            kidRow.put("gender", enrollment.getKid().getGender());
            kidRow.put("birthday", enrollment.getKid().getBirthday());
            kidRow.put("parent_email", enrollment.getKid().getParentEmail());
            kidRow.put("deleted_at", enrollment.getKid().getDeletedAt());
            kidRow.put("created_at", enrollment.getKid().getCreatedAt());
            kidRow.put("updated_at", enrollment.getKid().getUpdatedAt());


            try {
                mDatabase.insertOrThrow("kids", null, kidRow);
                Log.d("Database Insertion", "Kid ("+enrollment.getKid().getId()+") Added");

            } catch (Exception e) {
                Log.e("Database Insertion", "Error " + e.getCause() + " " + e.getMessage());
            }
        }
    }
}
