package com.example.falc0n.weatherapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by d3c0d3R on 15-Oct-16.
 */

public class GetJSONWeatherData extends AsyncTask<String , Void, String> {
    DisplayWeather mContext;

    public GetJSONWeatherData(DisplayWeather mContext) {
        this.mContext = mContext;
    }

    @Override
    protected String doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL(params[0]);
            Log.d("WeatherAppDemo","Inside getJsonWeatherData. url is  "+params[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                Log.d("WeatherAppDemo","Connnectd!!");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line ;
                while((line=bufferedReader.readLine())!=null)
                {
                    sb.append(line);
                    Log.d("WeatherAppDemo","Data from url : "+line);
                }
                return sb.toString();
            }
            else
            {
                Toast.makeText(mContext, R.string.errorConnectionString,Toast.LENGTH_LONG).show();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("WeatherAppDemo","In post execute");

        if(s!=null)
        {
            Gson gson = new GsonBuilder().create();
            WeatherJSONHolder jsonHolder = new WeatherJSONHolder() ;
            Log.d("WeatherAppDemo","Calling GSON");
            jsonHolder = gson.fromJson(s,WeatherJSONHolder.class);
            mContext.setData(jsonHolder);
            Log.d("WeatherAppDemo","GSON Loaded");
            Log.d("WeatherAppDemo","GSON City" + jsonHolder.getCity().getName());
            Log.d("WeatherAppDemo","GSON City" + jsonHolder.getCity().getId());
            Log.d("WeatherAppDemo","GSON Country" + jsonHolder.getCity().getCountry());
        }
        else
        {
            Toast.makeText(mContext, R.string.errorConnectionString,Toast.LENGTH_LONG).show();
        }
    }
}
