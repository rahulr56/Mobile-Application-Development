package com.falc0n.httpconnectiondemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by fAlc0n on 11/5/16.
 */

public class GetImage extends AsyncTask<String,Void,Bitmap>{
    MainActivity activity;

    public GetImage(MainActivity context) {
        this.activity = context;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Log.d("IMAGE","In getImage");
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ImageView imageView = ((ImageView) activity.findViewById(R.id.imageViewSearchedImage));
        imageView.setImageBitmap(bitmap);
    }
}
