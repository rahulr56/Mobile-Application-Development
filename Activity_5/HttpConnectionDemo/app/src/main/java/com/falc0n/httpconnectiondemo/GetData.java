package com.falc0n.httpconnectiondemo;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by fAlc0n on 11/5/16.
 */

public class GetData extends AsyncTask<String, Void, String>{
    DataCommunicator context;

    public GetData(DataCommunicator context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while((line=bufferedReader.readLine())!=null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        context.FillArrayListOfUrl(s);
    }
    static public interface DataCommunicator
    {
        void FillArrayListOfUrl(String data);
        Context GetContext();
    }
}

