package com.d3c0d3r.homework05;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public final static String CITY_KEY = "CITY";
    public final static String FAV_CITY_KEY = "FAV_CITY_LIST";
    public final static String STATE_KEY = "STATE";
    public final static String URL_KEY = "URL";
    protected final static String WEATHER_API_KEY = "10d786ddaefcdb39";   //Key from wunderground.com
    public static ArrayList<FAVCity> favCitiesList;
    public FAVCityAdapter cityAdapter;

    public static boolean diaplayFavCities(FAVCity favCity) {
        for (int i = 0; i < favCitiesList.size(); i++) {
            if ((favCitiesList.get(i).city.equals(favCity.city)) && (favCitiesList.get(i).state.equals(favCity.state))) {
                favCitiesList.get(i).temp = favCity.temp;
                favCitiesList.get(i).date = favCity.date;
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        favCitiesList = new ArrayList<>();
        displayFavCites();
        final String framedUrl = "http://api.wunderground.com/api/" + WEATHER_API_KEY + "/hourly/q/" + STATE_KEY + "/" + CITY_KEY + ".json";
        findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = ((EditText) findViewById(R.id.editTextCity)).getText().toString();
                String state = ((EditText) findViewById(R.id.editTextState)).getText().toString();
                if (city.trim().matches("") || state.trim().matches("")) {
                    Toast.makeText(getApplicationContext(), R.string.invalidString, Toast.LENGTH_LONG).show();
                } else {
                    ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                    dialog.setTitle(R.string.connectingString);
                    dialog.setCancelable(false);
                    dialog.show();
                    while (!isConnected()) ;
                    dialog.dismiss();
                    //start a new intent to query
                    Intent intent = new Intent(MainActivity.this, HourlyWeatherForecast.class);
                    intent.putExtra(CITY_KEY, city);
                    intent.putExtra(STATE_KEY, state);
                    intent.putExtra(FAV_CITY_KEY, favCitiesList);
                    String newUrl = framedUrl.replace(STATE_KEY, state).replace(CITY_KEY, city);
                    intent.putExtra(URL_KEY, newUrl);
                    startActivity(intent);
                }
            }
        });
        ListView lv = (ListView) findViewById(R.id.listViewFavourites);
        cityAdapter = new FAVCityAdapter(this, R.layout.favcity, favCitiesList);
        lv.setAdapter(cityAdapter);
        cityAdapter.setNotifyOnChange(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, HourlyWeatherForecast.class);
                intent.putExtra(CITY_KEY, favCitiesList.get(position).city);
                intent.putExtra(STATE_KEY, favCitiesList.get(position).state);
                String newUrl = framedUrl.replace(STATE_KEY, favCitiesList.get(position).state).replace(CITY_KEY, favCitiesList.get(position).city);
                intent.putExtra(URL_KEY, newUrl);
                startActivity(intent);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                favCitiesList.remove(position);
                Toast.makeText(getBaseContext(), getResources().getString(R.string.favCityDeletedString), Toast.LENGTH_LONG).show();
                cityAdapter.notifyDataSetChanged();
                displayFavCites();
                return false;
            }
        });
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public void displayFavCites() {
        TextView tv = (TextView) findViewById(R.id.textViewNoItems);
        if (favCitiesList.size() == 0) {
            (findViewById(R.id.linearLayoutFav)).setVisibility(View.INVISIBLE);
            tv.setText(getResources().getString(R.string.noFavCitiesString));
            return;
        }
        tv.setText(R.string.favCitiesString);
        (findViewById(R.id.linearLayoutFav)).setVisibility(View.VISIBLE);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayFavCites();
    }
}
