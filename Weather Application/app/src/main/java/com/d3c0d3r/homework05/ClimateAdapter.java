package com.d3c0d3r.homework05;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ClimateAdapter extends ArrayAdapter<HourlyForecast> {
    Context mContext;
    int mResource;
    ArrayList<HourlyForecast> mData;

    public ClimateAdapter(Context context, int resource, ArrayList<HourlyForecast> mData) {
        super(context, resource, mData);
        this.mContext = context;
        this.mResource = resource;
        this.mData = mData;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }
        HourlyForecast hl = mData.get(position);
        TextView time = (TextView) convertView.findViewById(R.id.textViewTime);
        TextView temp = (TextView) convertView.findViewById(R.id.textViewTemperature);
        TextView condition = (TextView) convertView.findViewById(R.id.textViewCondition);
        time.setText(hl.FCTTIME.civil);
        condition.setText(hl.condition);
        temp.setText(hl.temp.english + (char) 0x00B0 + " F");
        ImageView iv = (ImageView) convertView.findViewById(R.id.imageViewCurrentWeather);
        Picasso.with(mContext).load(hl.icon_url).into(iv);
        return convertView;
    }
}