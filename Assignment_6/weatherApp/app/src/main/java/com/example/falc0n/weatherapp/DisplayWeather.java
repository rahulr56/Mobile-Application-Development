package com.example.falc0n.weatherapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DisplayWeather extends AppCompatActivity implements DailyWeatherAdapter.OnClickDataListener{
    ProgressDialog progressDialog;
    WeatherJSONHolder weatherJSONHolder;
    String globalCity, globalState;
    private RecyclerView recView;
    private DailyWeatherAdapter dailyWeatherAdapter;
    DailyWeatherAdapter dailyAdapter;
    HourlyWeatherAdapter hourlyAdapter;
    RecyclerView dailyForecast;
    List<WeatherJSONHolder.ListBean> listData;
    List<WeatherJSONHolder.ListBean> dayListData;
    SimpleDateFormat format;
    Date date;
    DatabaseDataManager dm;
    Map<String, Integer> datesMap = new TreeMap<>();
    String cityname;
    List finalList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.favourites,menu);
        dm = new DatabaseDataManager(getApplicationContext());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        if(item.getItemId() ==  R.id.menuSaveCity)
        {
            FavCity city = new FavCity();
            if(dm.getFavCity(weatherJSONHolder.getCity().getName())==null) {
                city.setCityName(weatherJSONHolder.getCity().getName());
                city.setCountry(weatherJSONHolder.getCity().getCountry());
                city.setFavourite(0);
                city.setTemperature(twoDForm.format(weatherJSONHolder.getList().get(0).getMain().getTemp()-273.15));
                dm.saveFavCity(city);

                Toast.makeText(this, "City Saved", Toast.LENGTH_SHORT).show();
                Log.d("after save", "" + dm.getAllFavCities().size());
            }else{
                city.setCityName(weatherJSONHolder.getCity().getName());
                city.setCountry(weatherJSONHolder.getCity().getCountry());
                city.setFavourite(0);
                city.setTemperature(twoDForm.format(weatherJSONHolder.getList().get(0).getMain().getTemp()-273.15));
                dm.updateFavCity(city);
                Toast.makeText(this, "City updated", Toast.LENGTH_SHORT).show();
                Log.d("after save", "" + dm.getAllFavCities().size());
            }
        }else if(item.getItemId() ==  R.id.menuSettings){
            Log.d("menu","settings");
        }
        return true;
    }
    private void callDayAdapter() {

        dailyAdapter = new DailyWeatherAdapter(DisplayWeather.this,R.layout.dailyclimate,listData);
        dailyAdapter.setOnClickDataListener(this);
        dailyForecast = (RecyclerView) findViewById(R.id.recyclerViewDailyForecast);
        dailyForecast.setAdapter(dailyAdapter);
        dailyForecast.setLayoutManager(new LinearLayoutManager(DisplayWeather.this,LinearLayoutManager.HORIZONTAL,false));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("WeatherAppDemo","in DisplayWeather");
        setContentView(R.layout.activity_display_weather);
        progressDialog =  new ProgressDialog(DisplayWeather.this);
        progressDialog.setTitle(R.string.dataLoadingString);
        progressDialog.setCancelable(false);
        if(getIntent().getExtras()!=null)
        {
            globalCity = getIntent().getStringExtra(MainActivity.CITY_KEY);
            globalState = getIntent().getStringExtra(MainActivity.STATE_KEY);
            String url = getIntent().getStringExtra(MainActivity.URL_KEY);
            Log.d("WeatherAppDemo","Framed URL is "+url);
            progressDialog.show();
            /*recView = (RecyclerView) findViewById(R.id.recyclerViewDailyForecast);
            recView.setLayoutManager(new LinearLayoutManager(this));
            dailyWeatherAdapter = new CityAdapter(listData,this);
            recView.setAdapter(dailyWeatherAdapter);
            dailyWeatherAdapter.setItemClickCallBack(this);*/
            new GetJSONWeatherData(DisplayWeather.this).execute(url);
        }

    }

    public void setData(WeatherJSONHolder holder)
    {
        weatherJSONHolder = holder;
        Log.d("WeatherAppDemo","GSON Loaded");
        Log.d("WeatherAppDemo","GSON City " + holder.getCity().getName());
        Log.d("WeatherAppDemo","GSON City " + holder.getCity().getId());
        Log.d("WeatherAppDemo","GSON Country " + holder.getCity().getCountry());
        cityname = holder.getCity().getName();
        ((TextView)findViewById(R.id.textViewDisplayCityState)).setText("Daily Forecast for "+holder.getCity().getName()+", "+holder.getCity().getCountry());

       // dailyForecast.addOnItemTouchListener(new DailyWeatherAdapter.RecyclerViewTouchListner(getApplicationContext(),dailyForecast));
;
        listData = holder.getList();
        callDayAdapter();
        WeatherJSONHolder.ListBean w = listData.get(0);
        dayListData = new ArrayList<>();
        for(WeatherJSONHolder.ListBean l :listData){
            if(l.getDt_txt().split(" ")[0].equals(w.getDt_txt().split(" ")[0])){
                dayListData.add(l);
            }
        }
        format = new SimpleDateFormat("yyyy-MM-dd");
        date = new Date();
        try {
            date = format.parse(w.getDt_txt().trim().split(" ")[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        format = new SimpleDateFormat("MMM dd,yyyy");
        ((TextView) findViewById(R.id.textViewHourlyForecastDetails)).setText("Three Hourly Forecast on "+format.format(date));
        hourlyAdapter = new HourlyWeatherAdapter(DisplayWeather.this,R.layout.hourlyclimate,dayListData);
        RecyclerView hourlyForecast = (RecyclerView) findViewById(R.id.recyclerViewHourlyForecast);
        hourlyForecast.setAdapter(hourlyAdapter);
        hourlyForecast.setLayoutManager(new LinearLayoutManager(DisplayWeather.this,LinearLayoutManager.HORIZONTAL,false));
        progressDialog.dismiss();
    }

    @Override
    public void setOnClickData(int p) {
        filterDates();
        WeatherJSONHolder.ListBean w = (WeatherJSONHolder.ListBean)finalList.get(p);
        dayListData = new ArrayList<>();
        for(WeatherJSONHolder.ListBean l :listData){
            if(l.getDt_txt().split(" ")[0].equals(w.getDt_txt().split(" ")[0])){
                dayListData.add(l);
            }
        }
        format = new SimpleDateFormat("yyyy-MM-dd");
        date = new Date();
        try {
            date = format.parse(w.getDt_txt().trim().split(" ")[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        format = new SimpleDateFormat("MMM dd,yyyy");
        cityname = weatherJSONHolder.getCity().getName();
        ((TextView) findViewById(R.id.textViewHourlyForecastDetails)).setText("Three Hourly Forecast on "+format.format(date));
        hourlyAdapter = new HourlyWeatherAdapter(DisplayWeather.this,R.layout.hourlyclimate,dayListData);
        RecyclerView hourlyForecast = (RecyclerView) findViewById(R.id.recyclerViewHourlyForecast);
        hourlyForecast.setAdapter(hourlyAdapter);
        hourlyForecast.setLayoutManager(new LinearLayoutManager(DisplayWeather.this,LinearLayoutManager.HORIZONTAL,false));
    }

    private void filterDates() {
        WeatherJSONHolder.ListBean wl;
        String dateKey;
        int start =0;
        if(datesMap.isEmpty()) {
            for (int i = 0; i < listData.size(); i++) {
                wl = listData.get(i);
                dateKey = wl.getDt_txt().trim().split(" ")[0];

                int end = i;
                int mid = 0;
                if (datesMap.containsKey(dateKey)) {
                    end = i;
                    mid = (end + start) / 2;
                    datesMap.put(dateKey, mid);
                } else {
                    start = i;
                    datesMap.put(dateKey, i);
                }
            }
        }
        finalList = new ArrayList<>();
        for (String s : datesMap.keySet()) {
            finalList.add(listData.get(datesMap.get(s)));
        }

    }
}
