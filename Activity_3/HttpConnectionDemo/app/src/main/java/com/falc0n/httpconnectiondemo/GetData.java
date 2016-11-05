package com.falc0n.httpconnectiondemo;

import android.os.AsyncTask;
import android.util.Log;

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
    DataCommunicator communicator;

    public GetData(DataCommunicator communicator) {
        this.communicator = communicator;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            Log.d("URL",strings[0]);
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while((line=bufferedReader.readLine())!=null) {
                builder.append(line);

                Log.d("Demo", "" +line);
            }
            Log.d("Total Data",builder.toString());
            communicator.FillArrayListOfUrl(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    interface DataCommunicator
    {
        void FillArrayListOfUrl(String data);
    }
}

