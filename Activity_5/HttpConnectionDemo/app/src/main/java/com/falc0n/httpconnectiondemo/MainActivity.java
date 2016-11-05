package com.falc0n.httpconnectiondemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetData.DataCommunicator{
    private static final String BASE_URL = "http://dev.theappsdr.com/apis/photos/index.php?keyword=";
    final String[] searchKeys ={"UNCC","Android","Winter","Aurora","Wonders"};
    static String selectedItem ;
    ProgressDialog progressDialog;
    ArrayList<String> arrayOfUrls = new ArrayList<>();
    int selectedImage = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckConnection();

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerSearch);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.searchKeywords, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Demo","Selected "+i);
                selectedItem = searchKeys[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner.setAdapter(adapter);

        findViewById(R.id.buttonGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImage = 0;
                progressDialog.setTitle(R.string.loadingDirString);
                progressDialog.show();
                arrayOfUrls.clear();
                new GetData(MainActivity.this).execute(BASE_URL+selectedItem);
            }
        });

        findViewById(R.id.imageViewLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedImage-1 < 0)
                {
                    selectedImage = arrayOfUrls.size()-1;
                }
                else {
                    selectedImage-=1;
                }
                progressDialog.setTitle(R.string.loadingPhotoString);
                progressDialog.show();
                new GetImage(MainActivity.this).execute(arrayOfUrls.get(selectedImage));
                progressDialog.dismiss();
            }
        });

        findViewById(R.id.imageViewRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedImage+1 >= arrayOfUrls.size())
                {
                    selectedImage = 0;
                }
                else {
                    selectedImage+=1;
                }
                progressDialog.setTitle(R.string.loadingPhotoString);
                progressDialog.show();
                new GetImage(MainActivity.this).execute(arrayOfUrls.get(selectedImage));
                progressDialog.dismiss();
            }
        });
    }

    private boolean isConnected()
    {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo().isConnected();
    }
    private void CheckConnection()
    {
        if(!isConnected())
        {
            Toast.makeText(this,"No Internet Connectivity!",Toast.LENGTH_LONG).show();
            while(!isConnected());
        }
    }

    @Override
    public void FillArrayListOfUrl(String data) {
        String[] splitArray = data.split(";");
        for(int i=1;i<splitArray.length;i++)
        {
            arrayOfUrls.add(splitArray[i]);
        }
        if(arrayOfUrls.size()==0)
        {
            (findViewById(R.id.imageViewSearchedImage)).setVisibility(View.INVISIBLE);
            (findViewById(R.id.imageViewLeft)).setVisibility(View.INVISIBLE);
            (findViewById(R.id.imageViewRight)).setVisibility(View.INVISIBLE);
            (findViewById(R.id.textViewNoImage)).setVisibility(View.VISIBLE);

            ((TextView)findViewById(R.id.textViewNoImage)).setText(getString(R.string.errorString));
            progressDialog.dismiss();
        }
        else {
            (findViewById(R.id.textViewNoImage)).setVisibility(View.INVISIBLE);
            (findViewById(R.id.imageViewLeft)).setVisibility(View.VISIBLE);
            (findViewById(R.id.imageViewRight)).setVisibility(View.VISIBLE);
            (findViewById(R.id.imageViewSearchedImage)).setVisibility(View.VISIBLE);
            progressDialog.dismiss();
            progressDialog.setTitle(R.string.loadingPhotoString);
            progressDialog.show();
            new GetImage(this).execute(arrayOfUrls.get(selectedImage));
            progressDialog.dismiss();
        }
    }

    @Override
    public Context GetContext() {
        return this;
    }
}
