package com.d3c0d3r.homework05;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by d3c0d3R on 06-Oct-16.
 */
public class getHourlyWeatherData extends AsyncTask<Object, Object, ArrayList<WeatherData>> {
    MainActivity activity;

    public getHourlyWeatherData(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<WeatherData> doInBackground(Object... params) {
        String urlString = "http://api.wunderground.com/api/10d786ddaefcdb39/hourly/q/nc/charlotte.json";
        String json = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            if(con.getResponseCode()==HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder buffer = new StringBuilder();
                String line ="";
                while ((line=reader.readLine()) != null) {
                    buffer.append(line);
                    Log.d("Data", line);
                }
                return WeatherParserUtil.WeatherJsonParser(buffer.toString());
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

        /*try {
            json = IOUtils.toString(new URL(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonParser parser = new JsonParser();
        // The JsonElement is the root node. It can be an object, array, null or
        // java primitive.
        JsonElement element = parser.parse(json);
        // use the isxxx methods to find out the type of jsonelement. In our
        // example we know that the root object is the Albums object and
        // contains an array of dataset objects
        if (element.isJsonObject()) {
            JsonObject albums = element.getAsJsonObject();
            Log.d("Rahul",albums.get("hourly_forecast").getAsString());
            JsonArray datasets = albums.getAsJsonArray("0");
            for (int i = 0; i < datasets.size(); i++) {
                JsonObject dataset = datasets.get(i).getAsJsonObject();
                Log.d("Rahul",dataset.get("album_title").getAsString());
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Gson.toJson() example: \n");
        sb.append("Cart Object: ").append(gson.fromJson().append("\n");*/
        return null;
    }
}
