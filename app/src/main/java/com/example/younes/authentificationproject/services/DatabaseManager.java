package com.example.younes.authentificationproject.services;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by younes on 8/19/2018.
 */

public class DatabaseManager {
    Context mContext;
    SQLiteDatabase db;
    Cursor authentificationDataCursor;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Game";
    // Table name
    private static final String TABLE_GAME_i = "Game_Infos";
//    private DatabaseHelper mDBHelper;
//    private SQLiteDatabase mDatabase;

//    DatabaseManager(Context context){
//        mContext = context;
//        mDatabase =
//    }

//create table if not exists Organisations (id_Organisation INTEGER NOT NULL,name_organisation TEXT NOT NULL);
//create table if not exists Kids (id_kid INTEGER NOT NULL,name_kid TEXT NOT NULL);
//    }
}
