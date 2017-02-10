package com.example.libraryguest2.expensecaluculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_add_expense);
        }
        catch (Exception e)
        {
            Log.d("ExpCalc","Caught exception");

        }

    }
}
