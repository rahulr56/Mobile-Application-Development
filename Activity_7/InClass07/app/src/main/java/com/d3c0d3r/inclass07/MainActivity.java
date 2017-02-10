package com.d3c0d3r.inclass07;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ProgressDialog dialog;
    ListView listView;
    PodcastAdapter adapter;
    ArrayList<PodcastData> poadcastList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        poadcastList = new ArrayList<>();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading News...");
//        dialog.show();
        new ReadPodcasts(this).execute("https://itunes.apple.com/us/rss/toppodcasts/limit=30/xml");
        listView = (ListView) findViewById(R.id.listViewPodcast);
        findViewById(R.id.buttonClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText)findViewById(R.id.editTextSearch)).setText("");
                sortPodcasts();
                for(int i=0;i<poadcastList.size();i++)
                    poadcastList.get(i).setHighlight(false);
                adapter.notifyDataSetChanged();
            }
        });
        findViewById(R.id.buttonGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchList();
//                adapter.notifyDataSetChanged();
            }
        });

    }
    public void setData(PodcastData podcast)
    {
        poadcastList.add(podcast);
        //listView.setAdapter(adapter);
    }
    public void fillPodcasts()
    {
        sortPodcasts();
        dialog.dismiss();
       adapter = new PodcastAdapter(this,R.layout.podcast_layout,poadcastList);
        listView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
       adapter.setNotifyOnChange(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this,"Clicked..---"+position+"---"+poadcastList.get(position).toString(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(),PodcastDetails.class);
                intent.putExtra("PODCASTS",poadcastList.get(position));
                startActivity(intent);
            }
        });
    }
    public void searchList()
    {
        Log.d("update","sorting list...");
        String searchString = ((EditText)findViewById(R.id.editTextSearch)).getText().toString();
        ArrayList<PodcastData> newData = new ArrayList<PodcastData>();
        for(int i=0;i<poadcastList.size();i++)
        {
            String title = ((poadcastList.get(i)).getTitle());
            Log.d("Searching for ",searchString+" in "+title);
            if(title.toLowerCase().contains(searchString.toLowerCase()))
            {
                Log.d("search","match found");
                //newData.add(0,poadcastList.get(i));
                poadcastList.add(0,poadcastList.get(i));
                poadcastList.get(0).setHighlight(true);
                poadcastList.remove(i+1);
            }
            else {
                poadcastList.get(i).setHighlight(false);
            }
            //newData.add(poadcastList.get(i));
            //poadcastList.add(poadcastList.get(i));
        }
        //poadcastList = newData;
       adapter.notifyDataSetChanged();
    }
    public void sortPodcasts()
    {
        Collections.sort(poadcastList, new Comparator<PodcastData>() {
            @Override
            public int compare(PodcastData lhs, PodcastData rhs) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                try {
                    Date startDate = df.parse(lhs.getUpdatedDate());
                    Date startDater = df.parse(rhs.getUpdatedDate());
                    return -(startDate.compareTo(startDater));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }
}
