package com.example.falc0n.weatherapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by d3c0d3R on 19-Oct-16.
 */

public class DatabaseDataManager {
    private Context context;
    private DatabaseOpenHelper databaseOpenHelper;
    private SQLiteDatabase db;
    private FavCitiesDAO favCitiesDAO;

    public DatabaseDataManager(Context context) {
        this.context = context;
        this.databaseOpenHelper =  new DatabaseOpenHelper(this.context);
        this.db = databaseOpenHelper.getWritableDatabase();
        this.favCitiesDAO = new FavCitiesDAO(db);
    }
    public void close()
    {
        if(db!=null)
            db.close();
    }

    public FavCitiesDAO getFavCitiesDAO() {
        return favCitiesDAO;
    }

    public long saveFavCity(FavCity city)
    {
        return this.favCitiesDAO.save(city);
    }
    public boolean updateFavCity(FavCity city)
    {
        return this.favCitiesDAO.update(city);
    }
    public boolean deleteFavCity(String city)
    {
        return this.favCitiesDAO.delete(city);
    }
    public FavCity getFavCity(String city)
    {
        return this.favCitiesDAO.get(city);
    }
    public List<FavCity> getAllFavCities()
    {
        return this.favCitiesDAO.getAll();
    }
}
