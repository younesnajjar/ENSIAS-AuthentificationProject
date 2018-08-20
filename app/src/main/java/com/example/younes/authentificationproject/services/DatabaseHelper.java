package com.example.younes.authentificationproject.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by younes on 8/19/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper mDatabaseInstance = null;
    private Context mContext;
    private static final String DATABASE_NAME = "Game.db";
    private static final int DATABASE_VERSION = 1;

    public static DatabaseHelper getInstance(Context context){
        if (mDatabaseInstance == null){
            mDatabaseInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return mDatabaseInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table BasicInfoTable (" +
                "mac_address Text Not NULL," +
                "id_user Integer Not NULL," +
                "anomalie_state INTEGER NOT NULL );");
        db.execSQL("create table Organisations (" +
                "id INTEGER Not NULL," +
                "name TEXT Not NULL," +
                "description TEXT Not NULL," +
                "phone TEXT ," +
                "email TEXT ," +
                "address TEXT ," +
                "deleted_at TEXT ," +
                "created_at TEXT ," +
                "updated_at TEXT);");
        db.execSQL("create table Kids (" +
                "id INTEGER Not NULL," +
                "id_organisation INTEGER," +
                "first_name TEXT," +
                "last_name TEXT," +
                "gender TEXT," +
                "birthday TEXT," +
                "parent_email TEXT," +
                "deleted_at TEXT," +
                "created_at TEXT," +
                "updated_at TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
