package com.d3c0d3r.homework05;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import static com.d3c0d3r.homework05.MainActivity.favCitiesList;

public class HourlyWeatherForecast extends AppCompatActivity implements Serializable {
    public static WeatherJSONHolder cityHourlyForecast;
    ProgressDialog dialog;
    String framedUrl;
    String globalCity = null, globalState = null, globalTemperature = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addFavCity:
                FAVCity f = new FAVCity();
                f.state = globalState;
                f.city = globalCity;
                f.temp = globalTemperature + (char) 0x00B0 + " F";
                Calendar cal = Calendar.getInstance();
                f.date = cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
                if (MainActivity.diaplayFavCities(f)) {
                    favCitiesList.add(f);
                    Toast.makeText(getApplicationContext(), R.string.addedToFavString, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.favCityAlreadyPresentString, Toast.LENGTH_LONG).show();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityHourlyForecast = new WeatherJSONHolder();
        setContentView(R.layout.activity_hourly_weather_forecast);
        dialog = new ProgressDialog(HourlyWeatherForecast.this);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.loadingHourlyData);
        dialog.show();
        if (getIntent().getExtras() != null) {
            cityHourlyForecast.city = getIntent().getStringExtra(MainActivity.CITY_KEY);
            globalCity = getIntent().getStringExtra(MainActivity.CITY_KEY);
            cityHourlyForecast.state = getIntent().getStringExtra(MainActivity.STATE_KEY);
            globalState = getIntent().getStringExtra(MainActivity.STATE_KEY);
            framedUrl = getIntent().getStringExtra(MainActivity.URL_KEY);
            new getHourlyWeatherData(HourlyWeatherForecast.this, framedUrl).execute();
        }
    }

    public void setData(WeatherJSONHolder holder) {
        holder.city = cityHourlyForecast.city;
        holder.state = cityHourlyForecast.state;
        cityHourlyForecast = holder;
        float temp = 0;
        for (int x = 0; x < cityHourlyForecast.hourly_forecast.size(); x++) {
            temp = temp + Float.parseFloat(cityHourlyForecast.hourly_forecast.get(x).temp.english);
        }
        globalTemperature = Integer.toString(Math.round(temp / (cityHourlyForecast.hourly_forecast.size())));
        ((TextView) findViewById(R.id.setCurrentLocation)).setText(cityHourlyForecast.city.toUpperCase() + " , " + cityHourlyForecast.state.toUpperCase());
        fillListView(cityHourlyForecast.hourly_forecast);
        dialog.dismiss();
    }

    void fillListView(final ArrayList<HourlyForecast> hourlyWeatherList) {
        ListView lv = (ListView) findViewById(R.id.listViewHourlyData);
        final ClimateAdapter adapter = new ClimateAdapter(HourlyWeatherForecast.this, R.layout.climate, hourlyWeatherList);
        lv.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
        ((ListView) findViewById(R.id.listViewHourlyData)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HourlyForecast hourlyForecast;// = new HourlyForecast();
                hourlyForecast = hourlyWeatherList.get(position);
                Intent intent = new Intent(HourlyWeatherForecast.this, DisplayCurrentWeather.class);
                intent.putExtra("HOUR", hourlyForecast);
                intent.putExtra(MainActivity.CITY_KEY, cityHourlyForecast.city);
                intent.putExtra(MainActivity.STATE_KEY, cityHourlyForecast.state);
                startActivity(intent);
            }
        });
    }

    void setError(String error) throws InterruptedException {
        Dialog dialogText = new Dialog(getApplicationContext());
        dialog.dismiss();
        dialogText.setTitle(error);
        Toast.makeText(getBaseContext(), "Invalid State or City!", Toast.LENGTH_LONG).show();
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                finish();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}