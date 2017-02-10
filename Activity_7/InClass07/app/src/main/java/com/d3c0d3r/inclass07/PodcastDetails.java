package com.d3c0d3r.inclass07;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PodcastDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_details);

        if(getIntent().getExtras()!=null)
        {
            PodcastData setPodcast = (PodcastData) getIntent().getExtras().getSerializable("PODCASTS");
            Log.d("Rahul",""+setPodcast.toString());
            ((TextView)findViewById(R.id.textViewTitle)).setText(setPodcast.getTitle());
            String line = setPodcast.getSummary().replaceAll("\\<.*?>","");
            ((TextView)findViewById(R.id.textViewDate)).setText("");
            if(line.trim().equals("")|| line==null)
            {
                ((TextView)findViewById(R.id.textViewContent)).setTextColor(Color.parseColor("#FF0000"));
                ((TextView)findViewById(R.id.textViewContent)).setText("Nothing to display!");
            }
            else
                ((TextView)findViewById(R.id.textViewContent)).setText(line);
            //DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
          //  Date startDate = df.parse(setPodcast.getUpdatedDate());
            Date startDate;
            try {
                 startDate = df.parse(setPodcast.getUpdatedDate());
                line = df.format(startDate);
                DateFormat newDf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                 line = newDf.format(startDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            ((TextView)findViewById(R.id.textViewDate)).setText(line);


           // ((TextView)findViewById(R.id.textViewDate)).setText(setPodcast.getUpdatedDate());
            ImageView image = (ImageView) findViewById(R.id.imageViewNews);
            Picasso.with(this).load(setPodcast.getUrl()).into(image);
     //       new GetImage(PodcastDetails.this).execute(setPodcast.getUrl());
        }
    }
}
