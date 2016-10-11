package com.d3c0d3r.homework05;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = ((EditText)findViewById(R.id.editTextCity)).getText().toString();
                String state = ((EditText)findViewById(R.id.editTextState)).getText().toString();
                if(city.trim().matches("") || state.trim().matches("") )
                {
                    Toast.makeText(getApplicationContext(),"Invalid state or city!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    //start a new intent to query
                    if(isConnected()) {
                        new getHourlyWeatherData(MainActivity.this).execute();
                    }else{
                        while(!isConnected());
                        new getHourlyWeatherData(MainActivity.this).execute();
                    }
                }
            }
        });
    }
    public boolean isConnected()
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
