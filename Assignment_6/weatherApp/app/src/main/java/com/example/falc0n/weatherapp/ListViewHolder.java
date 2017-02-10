package com.example.falc0n.weatherapp;

import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import static android.R.attr.name;

/**
 * Created by d3c0d3R on 16-Oct-16.
 */

public class ListViewHolder extends RecyclerView.ViewHolder {
    protected TextView temperature;
    protected TextView pressure;
    protected TextView humidity;
    protected TextView windSpeed;
    protected TextView condition;
    protected TextView time;
    protected ImageView hourlyWeatherIcon;

    public ListViewHolder(View itemView) {
        super(itemView);
        hourlyWeatherIcon = (ImageView) itemView.findViewById(R.id.imageViewHourlyWeather);
        temperature = ((TextView) itemView.findViewById(R.id.textViewHourlyTemperatureValue));
        pressure = ((TextView) itemView.findViewById(R.id.textViewHourlyPressureValue));
        humidity = ((TextView) itemView.findViewById(R.id.textViewHourlyHumidityValue));
        windSpeed = ((TextView) itemView.findViewById(R.id.textViewHourlyWindValue));
        condition = ((TextView) itemView.findViewById(R.id.textViewHouryConditionValue));
        time = ((TextView) itemView.findViewById(R.id.textViewHourlyTime));
    }
}
