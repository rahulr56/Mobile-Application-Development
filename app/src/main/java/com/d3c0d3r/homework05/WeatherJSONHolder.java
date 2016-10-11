package com.d3c0d3r.homework05;

import java.util.ArrayList;

/**
 * Created by d3c0d3R on 07-Oct-16.
 */

public class WeatherJSONHolder{
    Response response;
    ArrayList<HourlyForecast> hourlyForecastObject;
}

class Response {
    private String version;
    private String termsOfService;
    private Features featureObject;

    @Override
    public String toString() {
        return "Response{" +
                "version='" + version + '\'' +
                ", termsOfService='" + termsOfService + '\'' +
                ", featureObject=" + featureObject +"}";
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTermsOfService() {
        return termsOfService;
    }

    public void setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
    }

    public Features getFeatureObject() {
        return featureObject;
    }

    public void setFeatureObject(Features featureObject) {
        this.featureObject = featureObject;
    }
};

class Features {
        private String hourly;
}