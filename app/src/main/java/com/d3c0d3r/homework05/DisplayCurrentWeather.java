package com.d3c0d3r.homework05;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DisplayCurrentWeather extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_current_weather);
        if (getIntent().getExtras() != null) {
            HourlyForecast hourlyForecast = (HourlyForecast) getIntent().getSerializableExtra("HOUR");
            String city = getIntent().getStringExtra(MainActivity.CITY_KEY);
            String state = getIntent().getStringExtra(MainActivity.STATE_KEY);
            ((TextView) findViewById(R.id.textViewSetCurrentLocation)).setText(city.toUpperCase() + " , " + state.toUpperCase() + " , (" + hourlyForecast.FCTTIME.civil + ")");
            Picasso.with(getApplicationContext()).load(hourlyForecast.icon_url).into((ImageView) findViewById(R.id.imageViewCurrentCondition));
            ((TextView) findViewById(R.id.textViewFillMaxTemp)).setText("");
            ((TextView) findViewById(R.id.textViewMaxTemp)).setText(hourlyForecast.wx);
            ((TextView) findViewById(R.id.textViewFeelsLike)).setText(hourlyForecast.feelslike.english + (char) 0x00B0 + getResources().getString(R.string.fahrenheitString));
            ((TextView) findViewById(R.id.textViewHumidity)).setText(hourlyForecast.humidity + getResources().getString(R.string.percentageString));
            ((TextView) findViewById(R.id.textViewWinds)).setText(hourlyForecast.wspd.english + getResources().getString(R.string.mphString) + hourlyForecast.wdir.degrees + (char) 0x00B0 + hourlyForecast.wdir.dir);
            ((TextView) findViewById(R.id.textViewPressure)).setText(hourlyForecast.mslp.english + " hpa");
            ((TextView) findViewById(R.id.textViewDewPoint)).setText(hourlyForecast.dewpoint.english + (char) 0x00B0 + getResources().getString(R.string.fahrenheitString));
            ((TextView) findViewById(R.id.textViewClouds)).setText(hourlyForecast.condition);
        }
    }
}
