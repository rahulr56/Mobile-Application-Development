package com.d3c0d3r.inclass07;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by d3c0d3R on 03-Oct-16.
 */

public class ReadPodcasts extends AsyncTask<String, Void, ArrayList<PodcastData>> {

    MainActivity activity;

    //    PodcastDetails newsActivity;
    public ReadPodcasts(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<PodcastData> doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode = con.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream in = con.getInputStream();
                return PodcastUtil.PodcastPullParser.parseNews(in);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<PodcastData> strings) {
        super.onPostExecute(strings);
 /*       Collections.sort(strings, new Comparator<PodcastData>() {
            @Override
            public int compare(PodcastData newsData, PodcastData t1) {
                int x = newsData.getUpdatedDate().compareTo(t1.getUpdatedDate());
                if (x < 0) {
                    return 1;
                } else if (x == 0) {
                    return 0;
                } else {
                    return -1;
                }

                //return x;
            }
        });
 */
        for (int i = 0; i < strings.size(); i++) {
            activity.setData(strings.get(i));
        }
        activity.fillPodcasts();
//        activity.dialog.dismiss();
        Log.d("demo", "came here");
        Log.d("demo", strings.toString());
        activity.fillPodcasts();
    }

}
