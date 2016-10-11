package com.d3c0d3r.homework05;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class getHourlyWeatherData extends AsyncTask<String, Void, String> {
    HourlyWeatherForecast activity;
    String urlString;

    public getHourlyWeatherData(HourlyWeatherForecast activity, String urlString) {
        this.activity = activity;
        this.urlString = urlString;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            Gson gson = new GsonBuilder().create();
            WeatherJSONHolder holder;// = new WeatherJSONHolder();
            holder = gson.fromJson(s.toString(), WeatherJSONHolder.class);
            if (holder.response.error != null) {
                try {
                    activity.setError(holder.response.error.description);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return;
            }
            activity.setData(holder);
        } else {
            Toast.makeText(activity.getBaseContext(), "No data received from Server!", Toast.LENGTH_LONG).show();
        }
    }
}