package com.example.falc0n.weatherapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by d3c0d3R on 19-Oct-16.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    static final  String DB_NAME = "favcities.db";
    static final  int DB_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        FavCityTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        FavCityTable.onUpgrade(db,oldVersion,newVersion);
    }
}
