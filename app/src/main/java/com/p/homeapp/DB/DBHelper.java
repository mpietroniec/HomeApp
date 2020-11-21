package com.p.homeapp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBName = "HomeApp.db";

    public DBHelper(@Nullable Context context) {
        super(context, DBName, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase MyDB) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {

    }
}
