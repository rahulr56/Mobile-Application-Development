package com.example.falc0n.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements  CityAdapter.ItemClickCallBack {
    static final String CITY_KEY = "CITY";
    static final String STATE_KEY = "STATE";
    static final String URL_KEY = "URL";
    static final int CELSIUS = 0 ;
    static final int FAHRENHEIT = 1 ;
    static int temperatureUnit = CELSIUS ;
    final String FIXED_URL = "http://api.openweathermap.org/data/2.5/forecast?q=";
    final String API_KEY = "&appid="+"6f65a05333bfbb5225d8dd034ff977b0";
    final String PARSER_MODE ="&mode=json";

    private RecyclerView recView;
    private CityAdapter cityAdapter;
    private ArrayList listData;
    DatabaseDataManager dm;
    static ArrayList<FavCity> favCityList = new ArrayList<>();
    static RecyclerView savedCities;
    static FavCitiesRecyclerAdapter favCitiesRecyclerAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionmenu, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        callAdapter();
        super.onRestart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.preferences)
        {
            Intent intent  = new Intent(MainActivity.this, PreferencesActivity.class);
            intent.putExtra( PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName() );
            intent.putExtra( PreferenceActivity.EXTRA_NO_HEADERS, true );
            startActivityForResult(intent, 200);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dm = new DatabaseDataManager(this);
        boolean connStatus = true;
        while (!isConnected())
        {
            if(connStatus)
                Toast.makeText(MainActivity.this, "Please Check Network Connectivity and try again!",Toast.LENGTH_LONG).show();
            connStatus = false;
        }

        callAdapter();

        findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked submit button", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(MainActivity.this,DisplayWeather.class);
                String city = ((EditText)findViewById(R.id.editTextCity)).getText().toString().toLowerCase().trim();
                String state = ((EditText)findViewById(R.id.editTextState)).getText().toString().toLowerCase().trim();
                if(city.matches("") || state.matches(""))
                {
                    Toast.makeText(MainActivity.this,R.string.invalidString,Toast.LENGTH_LONG).show();
                }
                else {
                    String framedUrl = FIXED_URL+city+","+state+PARSER_MODE+API_KEY;
                    intent.putExtra(URL_KEY, framedUrl);
                    intent.putExtra(CITY_KEY, city);
                    intent.putExtra(STATE_KEY, state);
                    startActivity(intent);
                }

            }
        });

    }

    public void callAdapter() {
        listData=  (dm.getAllFavCities()!=null)?(ArrayList)dm.getAllFavCities():new ArrayList<>();
        if(null!= listData && listData.size()>0){
            ((TextView)findViewById(R.id.textViewNoItems)).setText(getString(R.string.savedCities));
        }else{
            ((TextView)findViewById(R.id.textViewNoItems)).setText(getString(R.string.noFavCitiesString));
        }
        recView = (RecyclerView) findViewById(R.id.rec_list);
        recView.setLayoutManager(new LinearLayoutManager(this));
        cityAdapter = new CityAdapter(listData,this);
        recView.setAdapter(cityAdapter);
        cityAdapter.setItemClickCallBack(this);
    }

    public static void setTemperatureUnit(int unit)
    {
        Log.d("Unit",""+unit);
        temperatureUnit = unit;
    }
    public boolean isConnected()
    {
        ConnectivityManager connectivityManager =  (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null  && networkInfo.isConnected())
        {
            return true;
        }
        return  false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dm.close();
    }
    @Override
    public void onItemClick(int p) {
    }

    @Override
    public void onSecondaryIconClick(int p) {
        FavCity item = (FavCity) listData.get(p);
        if(item.getFavourite()==1){
            item.setFavourite(0);
        }else{
            item.setFavourite(1);
        }
        dm.updateFavCity(item);
        reArrangeListBasedOnFav(listData);
        callAdapter();
        cityAdapter.setListData(listData);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteExpense(int pos) {
        dm.deleteFavCity(((FavCity) listData.get(pos)).getCityName());
        listData.remove(pos);
        if(listData.size()==0)((TextView)findViewById(R.id.textViewNoItems)).setText(getString(R.string.noFavCitiesString));
        else ((TextView)findViewById(R.id.textViewNoItems)).setText(getString(R.string.savedCities));
        callAdapter();
        Toast.makeText(getApplicationContext(),"Item Deleted Successfully!",Toast.LENGTH_LONG).show();
    }

    private void reArrangeListBasedOnFav(ArrayList<FavCity> listData){
        ArrayList<FavCity> favsList = new ArrayList<>();
        ArrayList<FavCity> nonFavsList = new ArrayList<>();
        for(FavCity c: listData){
            if(c.getFavourite()==1){
                favsList.add(c);
            }else {
                nonFavsList.add(c);
            }
        }
        listData = new ArrayList<>();
        if(favsList!=null)listData.addAll(favsList);
        if(nonFavsList!=null)listData.addAll(nonFavsList);
    }
}
