package com.d3c0d3r.inclass07;

import android.util.Log;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by d3c0d3R on 03-Oct-16.
 */

public class PodcastUtil {

    static public class PodcastPullParser
    {
        static int podcastId = 0;
        static ArrayList<PodcastData> podcastList;

        static public ArrayList<PodcastData> parseNews(InputStream in) throws IOException, SAXException, XmlPullParserException, ParseException {

            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in,"UTF-8");
            PodcastData podcast = null;
            podcastList = new ArrayList<PodcastData>();
            int event = parser.getEventType();
            while(event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("entry")) {
                            podcast = new PodcastData();
                        }else if(parser.getName().equals("id"))
                        {
                            if(podcast!=null)
                                podcast.setId(parser.nextText());
                        }
                        else if (parser.getName().equals("title")) {
                            if(podcast != null)
                                podcast.setTitle(parser.nextText());
                        } else if (parser.getName().equals("summary")) {
                            if(podcast != null)
                                podcast.setSummary(parser.nextText().toString());
                        } /*else if (parser.getName().equals("link")) {
                            podcast.setUrl(parser.nextText());
                        }*/
                        else if (parser.getName().equals("im:image")) {
                            if(podcast != null) {
                                String height ;
                                height = parser.getAttributeValue(null,"height");
                                //Choose min url and max url
                                String ss = parser.nextText();
                                if("55".equals(height)) {
                                    Log.d("Inside small image ", "" +ss );
                                    podcast.setThumbnailUrl(ss);
                                }else if("170".equals(height)){
                                    podcast.setUrl(ss);
                                }
                            }
                        }
                        else if (parser.getName().equals("im:releaseDate")) {
                            Log.d("Rahul","lable date "+parser.getAttributeValue(null,"label"));
                            if(podcast != null) {
                                String date = parser.nextText();
                              //  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                              //  Date d1= formatter.parse(date);
                                podcast.setUpdatedDate(date);
                                Log.d("Rahul","lable date "+podcast.getUpdatedDate()+"---"+date);
                            }

                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("entry"))
                        {
                            if(podcast != null)
                                podcastList.add(podcast);
                            podcast = null;
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();
            }
            return podcastList;
        }
    }
}
