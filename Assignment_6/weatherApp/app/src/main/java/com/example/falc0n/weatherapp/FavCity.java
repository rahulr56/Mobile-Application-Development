package com.example.falc0n.weatherapp;

/**
 * Created by d3c0d3R on 19-Oct-16.
 */

public class FavCity {
    String cityName, country , temperature;
    int favourite;

    public FavCity()
    {

    }

    public FavCity(String cityName, String country, String temperature, int favourite) {
        this.cityName = cityName;
        this.country = country;
        this.temperature = temperature;
        this.favourite = favourite;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }
}
