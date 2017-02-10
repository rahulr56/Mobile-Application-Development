package com.example.falc0n.weatherapp;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fAlc0n on 10/15/16.
 */

public class WeatherJSONHolder {

    /**
     * id : 2643743
     * name : London
     * coord : {"lon":-0.12574,"lat":51.50853}
     * country : GB
     * population : 0
     * sys : {"population":0}
     */

    private City city;
    private String cod;
    private String message;
    private int cnt;

    private List<ListBean> list;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class City {
        private int id;
        private String name;
        /**
         * lon : -0.12574
         * lat : 51.50853
         */

        private CoOrdinates coord;
        private String country;
        private int population;
        /**
         * population : 0
         */

        private System sys;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public CoOrdinates getCoord() {
            return coord;
        }

        public void setCoord(CoOrdinates coord) {
            this.coord = coord;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getPopulation() {
            return population;
        }

        public void setPopulation(int population) {
            this.population = population;
        }

        public System getSys() {
            return sys;
        }

        public void setSys(System sys) {
            this.sys = sys;
        }

        public static class CoOrdinates {
            private double lon;
            private double lat;

            public double getLon() {
                return lon;
            }

            public void setLon(double lon) {
                this.lon = lon;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }
        }

        public static class System {
            private int population;

            public int getPopulation() {
                return population;
            }

            public void setPopulation(int population) {
                this.population = population;
            }
        }
    }

    public static class ListBean{
        private int dt;
        private MainData main;
        private CloudsData clouds;
        private WindData wind;
        private RainData rain;
        private SystemData sys;
        private String dt_txt;

        private List<CurrentWeatherData> weather;

        public int getDt() {
            return dt;
        }

        public void setDt(int dt) {
            this.dt = dt;
        }

        public MainData getMain() {
            return main;
        }

        public void setMain(MainData main) {
            this.main = main;
        }

        public CloudsData getClouds() {
            return clouds;
        }

        public void setClouds(CloudsData clouds) {
            this.clouds = clouds;
        }

        public WindData getWind() {
            return wind;
        }

        public void setWind(WindData wind) {
            this.wind = wind;
        }

        public RainData getRain() {
            return rain;
        }

        public void setRain(RainData rain) {
            this.rain = rain;
        }

        public SystemData getSys() {
            return sys;
        }

        public void setSys(SystemData sys) {
            this.sys = sys;
        }

        public String getDt_txt() {
            return dt_txt;
        }

        public void setDt_txt(String dt_txt) {
            this.dt_txt = dt_txt;
        }

        public List<CurrentWeatherData> getWeather() {
            return weather;
        }

        public void setWeather(List<CurrentWeatherData> weather) {
            this.weather = weather;
        }

        public static class MainData {
            private double temp;
            private double temp_min;
            private double temp_max;
            private double pressure;
            private double sea_level;
            private double grnd_level;
            private int humidity;
            private double temp_kf;

            public double getTemp() {
                return temp;
            }

            public void setTemp(double temp) {
                this.temp = temp;
            }

            public double getTemp_min() {
                return temp_min;
            }

            public void setTemp_min(double temp_min) {
                this.temp_min = temp_min;
            }

            public double getTemp_max() {
                return temp_max;
            }

            public void setTemp_max(double temp_max) {
                this.temp_max = temp_max;
            }

            public double getPressure() {
                return pressure;
            }

            public void setPressure(double pressure) {
                this.pressure = pressure;
            }

            public double getSea_level() {
                return sea_level;
            }

            public void setSea_level(double sea_level) {
                this.sea_level = sea_level;
            }

            public double getGrnd_level() {
                return grnd_level;
            }

            public void setGrnd_level(double grnd_level) {
                this.grnd_level = grnd_level;
            }

            public int getHumidity() {
                return humidity;
            }

            public void setHumidity(int humidity) {
                this.humidity = humidity;
            }

            public double getTemp_kf() {
                return temp_kf;
            }

            public void setTemp_kf(double temp_kf) {
                this.temp_kf = temp_kf;
            }
        }

        public static class CloudsData {
            private int all;

            public int getAll() {
                return all;
            }

            public void setAll(int all) {
                this.all = all;
            }
        }

        public static class WindData {
            private double speed;
            private float deg;

            public double getSpeed() {
                return speed;
            }

            public void setSpeed(double speed) {
                this.speed = speed;
            }

            public float getDeg() {
                return deg;
            }

            public void setDeg(float deg) {
                this.deg = deg;
            }
        }

        public static class RainData {
        }

        public static class SystemData {
            private String pod;

            public String getPod() {
                return pod;
            }

            public void setPod(String pod) {
                this.pod = pod;
            }
        }

        public static class CurrentWeatherData {
            private int id;
            private String main;
            private String description;
            private String icon;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getMain() {
                return main;
            }

            public void setMain(String main) {
                this.main = main;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }
    }
}
