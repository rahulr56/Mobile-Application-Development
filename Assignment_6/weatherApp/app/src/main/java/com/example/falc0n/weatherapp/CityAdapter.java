package com.example.falc0n.weatherapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Raghavan on 20-Oct-16.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityHolder> {

    private List<FavCity> listData;
    private LayoutInflater inflater;
    private ItemClickCallBack itemClickCallBack;
    private SimpleDateFormat format;

    public interface ItemClickCallBack{
        void onItemClick(int p);
        void onSecondaryIconClick(int p);
        public void deleteExpense(int pos);

    }
    public void setItemClickCallBack(final ItemClickCallBack itemClickCallBack){
        this.itemClickCallBack = itemClickCallBack;
    }
    public CityAdapter(List<FavCity> listData, Context c){
        this.inflater = LayoutInflater.from(c);
        this.listData = listData;
    }
    @Override
    public CityAdapter.CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new CityHolder(view);
    }

    @Override
    public void onBindViewHolder(CityAdapter.CityHolder holder, int position) {
        FavCity item = listData.get(position);
        holder.city.setText(item.getCityName()+", "+item.getCountry());
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        if (MainActivity.temperatureUnit == MainActivity.CELSIUS) {
            holder.temperature.setText(item.getTemperature() + (char) 0x00B0 + " C");
        } else if (MainActivity.temperatureUnit == MainActivity.FAHRENHEIT) {
            String temp = Double.toString(Double.valueOf(twoDForm.format(((Double.parseDouble(item.getTemperature()) * 1.8) + 32))));
            holder.temperature.setText(temp+ (char) 0x00B0 + " F");
        }
        format = new SimpleDateFormat("MM/dd/YYYY");
        Date date = new Date();

        holder.updatedOn.setText("Updated on:"+ format.format(date));
        if(item.getFavourite()==1){
            holder.favImage.setImageResource(R.drawable.star_gold);
        }else{
            holder.favImage.setImageResource(R.drawable.star_gray);
        }
    }
    public void setListData(ArrayList<FavCity> exerciseList){
        this.listData = null;
        this.listData= exerciseList;
    }
    @Override
    public int getItemCount() {
        return listData.size();
    }

    class CityHolder extends RecyclerView.ViewHolder implements  View.OnClickListener,View.OnLongClickListener{

        private TextView city;
        private TextView temperature;
        private TextView updatedOn;
        private ImageView favImage;
        private View container;

        public CityHolder(View itemView) {
            super(itemView);
            city = (TextView) itemView.findViewById(R.id.textViewCityCountry);
            temperature = (TextView) itemView.findViewById(R.id.textViewTemp);
            updatedOn = (TextView) itemView.findViewById(R.id.textViewUpdatedDate);
            favImage = (ImageView) itemView.findViewById(R.id.imageViewFav);
            favImage.setOnClickListener(this);
            container = itemView.findViewById(R.id.cont_item_root);
            container.setOnClickListener(this);
            container.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.cont_item_root){
                itemClickCallBack.onItemClick(getAdapterPosition());
            }else{
                itemClickCallBack.onSecondaryIconClick(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickCallBack.deleteExpense(getAdapterPosition());
            return true;
        }
    }
}

