package com.d3c0d3r.homework05;

import java.io.Serializable;
import java.util.ArrayList;

public class WeatherJSONHolder {
    String city, state;
    Response response;
    ArrayList<HourlyForecast> hourly_forecast;
}

class Response {
    String version;
    String termsofService;
    Features features;
    ErrorObject error = null;
}

class Features {
    int hourly;
}

class ErrorObject {
    String type = null, description = null;
}

class HourlyForecast implements Serializable {
    FCTTime FCTTIME;
    WindDirection wdir;
    WeatherData temp;
    WeatherData dewpoint;
    String condition;
    String icon;
    String icon_url;
    String fctcode;
    String sky;
    WeatherData wspd;
    String wx;
    String uvi;
    String humidity;
    WeatherData windchill;
    WeatherData heatindex;
    WeatherData feelslike;
    WeatherData qpf;
    WeatherData snow;
    String pop;
    WeatherData mslp;
}

class WindDirection implements Serializable {
    String dir, degrees;
}

class FCTTime implements Serializable {
    String hour;
    String hour_padded;
    String min;
    String min_unpadded;
    String sec;
    String year;
    String mon;
    String mon_padded;
    String mon_abbrev;
    String mday;
    String mday_padded;
    String yday;
    String isdst;
    String epoch;
    String pretty;
    String civil;
    String month_name;
    String month_name_abbrev;
    String weekday_name;
    String weekday_name_night;
    String weekday_name_abbrev;
    String weekday_name_unlang;
    String weekday_name_night_unlang;
    String ampm;
    String tz;
    String age;
    String UTCDATE;
}

class WeatherData implements Serializable {
    String english, metric;
}
