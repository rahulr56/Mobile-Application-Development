package com.example.falc0n.weatherapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.R.attr.format;

/**
 * Created by d3c0d3R on 16-Oct-16.
 */

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.DailyWeatherViewHolder> {
    Context mContext;
    int resource;
    List<WeatherJSONHolder.ListBean> weatherList;
    static final String ICON_URL = "http://openweathermap.org/img/w/";
    Map<String, Integer> datesMap = new TreeMap<>();
    SimpleDateFormat format;
    public OnClickDataListener onClickDataListener;
    List finalList;

    public DailyWeatherAdapter(Context context, int resource, List<WeatherJSONHolder.ListBean> objects){//,RecyclerViewOnItemClickListner listner) {
        this.mContext = context;
        this.resource = resource;
        this.weatherList = objects;
//        this.mListner = listner;
    }
    public void setOnClickDataListener(OnClickDataListener onClickDataListener){
        this.onClickDataListener= onClickDataListener;
    }
    @Override
    public DailyWeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dailyclimate, parent, false);
        return new DailyWeatherViewHolder(view);
    }

    private void filterDates() {
        WeatherJSONHolder.ListBean wl;
        String dateKey;
        int start =0;
        if(datesMap.isEmpty()) {
            for (int i = 0; i < weatherList.size(); i++) {
                wl = weatherList.get(i);
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
            finalList.add(weatherList.get(datesMap.get(s)));
        }

    }

    private int findMedian(String dateKey) {
        int temp = datesMap.get(dateKey) / 2;

        return temp;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(DailyWeatherViewHolder holder, int position) {
        //Find dates that are unique Map to store number of occurances
        //filterDates();
        //for (String s : datesMap.keySet()) {
            //WeatherJSONHolder.ListBean wl = weatherList.get(findMedian(s));
            WeatherJSONHolder.ListBean wl = (WeatherJSONHolder.ListBean)finalList.get(position);
            format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            try {
                date = format.parse(wl.getDt_txt().trim().split(" ")[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            format = new SimpleDateFormat("MMM dd,yyyy");
           TextView textViewDate = holder.dailyDateText;
            textViewDate.setText(format.format(date));

            String imageUrl = ICON_URL + wl.getWeather().get(0).getIcon() + ".png";
            Picasso.with(mContext).load(imageUrl).into(holder.dailyWeatherIcon);
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            double temperature = Double.valueOf(twoDForm.format(wl.getMain().getTemp() - 273.15));
            TextView temp = holder.dailyTemperature;
            if (MainActivity.temperatureUnit == MainActivity.CELSIUS) {
                temp.setText(Double.toString(temperature)+(char)0x00B0+ " C");
            } else if (MainActivity.temperatureUnit == MainActivity.FAHRENHEIT) {
                temp.setText(Double.toString(Double.valueOf(twoDForm.format(((temperature * 1.8) + 32)))) +(char)0x00B0+ " F");
            }
        //}
    }

    @Override
    public int getItemCount() {
        filterDates();
        return finalList.size();
    }

    class DailyWeatherViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        public TextView dailyDateText;
        public TextView dailyTemperature;
        public ImageView dailyWeatherIcon;
        private View container;

        public DailyWeatherViewHolder(View itemView) {
            super(itemView);
            dailyDateText = (TextView) itemView.findViewById(R.id.textViewDailyDate);
            dailyTemperature = (TextView) itemView.findViewById(R.id.textViewDailyTemperature);
            //Log.d("on Holder",""+dailyTemperature);
            dailyWeatherIcon = (ImageView) itemView.findViewById(R.id.imageViewDailyWeather);
            container = itemView.findViewById(R.id.cont_day_climate);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickDataListener.setOnClickData(getAdapterPosition());
        }
    }
    public interface OnClickDataListener{
        void setOnClickData(int p);
    }
}

