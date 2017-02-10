package com.example.falc0n.weatherapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by d3c0d3R on 16-Oct-16.
 */
public class HourlyWeatherAdapter extends RecyclerView.Adapter<ListViewHolder> {
    Context mContext ;
    int resource;
    List<WeatherJSONHolder.ListBean> mData ;//= (ArrayList<WeatherJSONHolder.ListBean>) Collections.EMPTY_LIST;
    String dateToBeDisplayed;

    public HourlyWeatherAdapter(Context context, int resource, List<WeatherJSONHolder.ListBean> objects) {
        this.mContext = context;
        this.resource = resource;
        this.mData = objects;
    }
    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourlyclimate, parent, false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        //   for(int i=0 ; i<mData.size();i++)
        WeatherJSONHolder.ListBean l1 = mData.get(position);
        Picasso.with(mContext).load(DailyWeatherAdapter.ICON_URL+l1.getWeather().get(0).getIcon()+".png").into(holder.hourlyWeatherIcon);
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        double temp = Double.valueOf(twoDForm.format(l1.getMain().getTemp() - 273.15));
        //Log.d("WeatherAppDemo","temperature : "+temp+"======="+MainActivity.CELSIUS+"----"+MainActivity.temperatureUnit);
        if(MainActivity.temperatureUnit == MainActivity.CELSIUS)
        {
            //Log.d("WeatherAppDemo","In celcius");
            holder.temperature.setText("Temperature : "+temp+(char) 0x00B0+" C");
        }
        else if(MainActivity.temperatureUnit == MainActivity.FAHRENHEIT)
        {
           // Log.d("WeatherAppDemo","In fahrenhiet");
            holder.temperature.setText("Temperature : "+Double.valueOf(twoDForm.format(((temp*1.8)+32)))+(char) 0x00B0+" F");
        }
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
        Date date = new Date();
        try {
            date = format.parse(l1.getDt_txt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        format = new SimpleDateFormat("HH:MM a");
        holder.time.setText(format.format(date));
        holder.condition.setText("Condition :"+l1.getWeather().get(0).getMain());
        holder.pressure.setText("Pressure : "+l1.getMain().getPressure()+" hPa");
        holder.humidity.setText("Humidity : "+l1.getMain().getHumidity()+"%");
        String wind = l1.getWind().getSpeed()+" mps"+l1.getWind().getDeg()+(char) 0x00B0+" ESE";
        holder.windSpeed.setText("Winds : "+wind);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}