package com.example.falc0n.weatherapp;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by d3c0d3R on 19-Oct-16.
 */

public class FavCityTable {
    static final String TABLE_NAME = "cities";
    static final String CITY_COLUMN = "city";
    static final String COUNTRY_COLUMN = "country";
    static final String TEMPERATURE_COLUMN = "temperature";
    static final String FAVOURITE_COLUMN = "favourite";

    static public void onCreate(SQLiteDatabase db)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE "+ TABLE_NAME+" (");
        sb.append(CITY_COLUMN + " text primary key, ");
        sb.append(COUNTRY_COLUMN + " text not null, ");
        sb.append(TEMPERATURE_COLUMN + " text not null, ");
        sb.append(FAVOURITE_COLUMN + " int DEFAULT 0);");
        try
        {
            db.execSQL(sb.toString());
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS"+TABLE_NAME);
        FavCityTable.onCreate(db);
    }
}
