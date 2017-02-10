package com.d3c0d3r.inclass07;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by d3c0d3R on 03-Oct-16.
 */

public class PodcastAdapter extends ArrayAdapter<PodcastData> {
    List<PodcastData> mdata;
    Context context;
    int mResource;

    public PodcastAdapter(Context context, int resource, List<PodcastData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.mResource = resource;
        this.mdata = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);

        }
        ImageView iv = (ImageView) convertView.findViewById(R.id.imageViewThumbnail);
        TextView textView = (TextView) convertView.findViewById(R.id.textViewTitle);

        PodcastData podcast =  mdata.get(position);
        if(podcast.isHighlight())
        {
            textView.setBackgroundResource(R.color.colorPrimary);
        }
        else
        {
            textView.setBackgroundResource(R.color.colorWhite);
        }

        textView.setText(podcast.getTitle());
        Picasso.with(context).load(podcast.getUrl()).into(iv);

        return convertView;
    }
}
