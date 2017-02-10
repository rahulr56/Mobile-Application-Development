package com.example.falc0n.weatherapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d3c0d3R on 19-Oct-16.
 */

public class FavCitiesDAO {
    private SQLiteDatabase db;

    public FavCitiesDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public long save(FavCity city)
    {
        ContentValues values = new ContentValues();
        values.put(FavCityTable.CITY_COLUMN,city.getCityName());
        values.put(FavCityTable.COUNTRY_COLUMN,city.getCountry());
        values.put(FavCityTable.TEMPERATURE_COLUMN,city.getTemperature());
        return db.insert(FavCityTable.TABLE_NAME,null,values);
    }
    public boolean update(FavCity city)
    {
        ContentValues values = new ContentValues();
        values.put(FavCityTable.CITY_COLUMN,city.getCityName());
        values.put(FavCityTable.COUNTRY_COLUMN,city.getCountry());
        values.put(FavCityTable.TEMPERATURE_COLUMN,city.getTemperature());
        values.put(FavCityTable.FAVOURITE_COLUMN,city.getFavourite());

        return db.update(FavCityTable.TABLE_NAME,values,FavCityTable.CITY_COLUMN+"=?",new String[]{city.getCityName()}) >0;
    }
    public boolean delete(String city)
    {
        return db.delete(FavCityTable.TABLE_NAME,FavCityTable.CITY_COLUMN+"=?",new String[]{city}) >0;
    }

    public FavCity get(String city)
    {
        FavCity favCity= null;
        Cursor c =db.query(true,FavCityTable.TABLE_NAME,new String[]{
                    FavCityTable.CITY_COLUMN,
                    FavCityTable.COUNTRY_COLUMN,
                    FavCityTable.TEMPERATURE_COLUMN,
                    FavCityTable.FAVOURITE_COLUMN},
                    FavCityTable.CITY_COLUMN+"=?",new String[]{city},
                    null,null,null,null);
        if(c!=null && c.moveToFirst())
        {
            favCity = buildFavCityFromCursor(c);
            if(!c.isClosed()) {
                c.close();
            }
        }
        return favCity;
    }

    public List<FavCity> getAll()
    {
        List<FavCity> favCityList =new ArrayList<>();
        Cursor c =db.query(FavCityTable.TABLE_NAME,new String[]{
                        FavCityTable.CITY_COLUMN,FavCityTable.COUNTRY_COLUMN,FavCityTable.TEMPERATURE_COLUMN,FavCityTable.FAVOURITE_COLUMN},
                        null,null,null,null,FavCityTable.FAVOURITE_COLUMN+" DESC");
        if(c!=null && c.moveToFirst())
        {
            do {
                FavCity favCity = buildFavCityFromCursor(c);
                if(favCity!=null){
                    favCityList.add(favCity);
                }
            }while (c.moveToNext());
            if(!c.isClosed()) {
                c.close();
            }
        }
        return favCityList;
    }

    public FavCity buildFavCityFromCursor(Cursor c)
    {
        FavCity favCity = null;
        if(c!=null)
        {
            favCity = new FavCity();
            favCity.setCityName(c.getString(0));
            favCity.setCountry(c.getString(1));
            favCity.setTemperature(c.getString(2));
            favCity.setFavourite(c.getInt(3));
        }
        return favCity;
    }
}
