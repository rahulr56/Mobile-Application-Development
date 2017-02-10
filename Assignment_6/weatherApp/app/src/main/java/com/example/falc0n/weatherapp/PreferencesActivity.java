package com.example.falc0n.weatherapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        findViewById(R.id.linearLayoutPreferences).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedItem=0;
                Dialog dialog = new Dialog(PreferencesActivity.this);
                final CharSequence[] units = {(char) 0x00B0 + " C",(char)0x00B0 + " F"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(PreferencesActivity.this);
                builder.setTitle(R.string.selTempString);
                builder.setCancelable(false);
                builder.setSingleChoiceItems(units,selectedItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PreferencesActivity.this, "Selected Unit is " + units[which], Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        MainActivity.setTemperatureUnit(which);
                        finish();
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });
    }
}
