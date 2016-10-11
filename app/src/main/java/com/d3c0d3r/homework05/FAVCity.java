package com.d3c0d3r.homework05;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class FAVCity implements Serializable {
    String city, state, temp, date;

    @Override
    public String toString() {
        return "FAVCity{" +
                "city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", temp='" + temp + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}

class FAVCityAdapter extends ArrayAdapter<FAVCity> {
    Context mContext;
    int mResource;
    ArrayList<FAVCity> mFavCity;

    public FAVCityAdapter(Context context, int resource, ArrayList<FAVCity> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.mFavCity = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }
        TextView city = ((TextView) convertView.findViewById(R.id.textViewSetFavCityState));
        city.setText(mFavCity.get(position).city.toUpperCase() + " , " + mFavCity.get(position).state.toUpperCase());
        TextView temperature = ((TextView) convertView.findViewById(R.id.textViewFavSetTemp));
        temperature.setText(mFavCity.get(position).temp);
        TextView updatedDate = ((TextView) convertView.findViewById(R.id.textViewFavSetUpdatedDate));
        updatedDate.setText(convertView.getResources().getString(R.string.updatedOnString) + mFavCity.get(position).date);
        return convertView;
    }
}