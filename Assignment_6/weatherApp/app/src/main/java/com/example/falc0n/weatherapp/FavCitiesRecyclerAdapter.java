package com.example.falc0n.weatherapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by d3c0d3R on 19-Oct-16.
 */
public class FavCitiesRecyclerAdapter extends RecyclerView.Adapter<FavCitiesRecyclerAdapter.ViewHolder> implements View.OnClickListener{
    Context context;
    int resource;
    ArrayList<WeatherJSONHolder.City> objects;

    public FavCitiesRecyclerAdapter(Context context, int resource, ArrayList<WeatherJSONHolder.City> objects) {
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onClick(View v) {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView city, temperature, updatedOn;
        ImageView star;
//        OnClickEvents onClickEvents;
        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.imageViewStar:
                    //onClickEvents.onRateClicked(getAdapterPosition(), v);
                    Toast.makeText(v.getContext(),"Star clicked!!!",Toast.LENGTH_LONG).show();
                    break;
                default:
                    //onClickEvents.onClicked(getAdapterPosition());
                    break;
            }

        }
    }
}
