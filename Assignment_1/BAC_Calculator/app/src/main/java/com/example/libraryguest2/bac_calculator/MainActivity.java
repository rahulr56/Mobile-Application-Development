/********************************************************
 * Assignment # :   Homework 1                          *
 * FileName     :   MainActivity.java                   *
 * Full Names   :   Rahul Rachapalli                    *
 *                  Raghavan Kondapuram                 *
 ********************************************************/

package com.example.libraryguest2.bac_calculator;

import android.app.Activity;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static double weight = 0;
    private static double totalBACLevel = 0;
    private static String gender = "Female";
    private static int totalDrinkSize = 0;
    private int alcoholContenet = 0;
    private int totalAlcoholContenet = 0;
    boolean buttonsDisabled = false;
    boolean weightChanged = false;
    boolean intializing = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Demo","Main- resetting...");
        setup();
        if(weight != 0)
        {
            setWeight();
        }
        Button btn = (Button) findViewById(R.id.buttonSave);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weightChanged = true;
                readWeight();
                readGender();
                totalBACLevel=0;
                calculateBACLevel();
            }
        });
        btn = (Button) findViewById(R.id.buttonAddDrink);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readWeight();
                readGender();
                calculateBACLevel();
            }
        });
        btn = (Button) findViewById(R.id.buttonReset);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAll();
            }
        });
        SeekBar alcoholSeekBar = (SeekBar) findViewById(R.id.seekBarAlcohol);
        final TextView seekBarChangedText = (TextView) findViewById(R.id.textViewAlcoholPercent);
        alcoholSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarChangedText.setText(String.valueOf(progress)+"%");
                seekBarChangedText.setTextSize(25);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    public void setup()
    {
        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setDisplayShowTitleEnabled(false);
        menu.setDisplayShowHomeEnabled(false);
        menu.setLogo(R.drawable.bac_launcher_icon);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        menu.setCustomView(mCustomView);
        menu.setDisplayShowCustomEnabled(true);
        resetAll();
        setStatus();
        intializing = false;
    }
    public void readWeight()
    {
        EditText inputWeight = (EditText) findViewById(R.id.inputTextWeight);
        if((inputWeight.getText().toString().matches("")))
        {
            inputWeight.setError(getString(R.string.stringErrorMessage));
        }
        else
        {
            double temp = Double.parseDouble(inputWeight.getText().toString());
            if(temp <= 0)
            {
                inputWeight.setError(getString(R.string.stringErrorMessage));
            }
            else
            {
                weight = temp;
                setWeight();
            }
        }
    }
    public void setWeight()
    {
        EditText inputWeight = (EditText) findViewById(R.id.inputTextWeight);
        inputWeight.setText(Double.toString(Math.round(weight*100.0)/100.0));
        Log.d("Demo","Weight is set to "+weight);
    }
    public void readGender()
    {
        Switch genderSwitch = (Switch) findViewById(R.id.switchGender);
        if(genderSwitch.isChecked())
        {
            gender = "Male";
        }
        else
        {
            gender = "Female";
        }
        Log.d("Demo","The entered Gender is " + gender);
    }
    public int readDrinkSize()
    {
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroupDrinkSize);
        int checkedRadioButtonId = rg.getCheckedRadioButtonId();
        int drinkSize;
        switch (checkedRadioButtonId)
        {
            case R.id.radioButton1oz:
                drinkSize = 1;
                break;
            case R.id.radioButton5oz:
                drinkSize = 5;
                break;
            case R.id.radioButton12oz:
                drinkSize = 12;
                break;
            default:
                drinkSize = 0;
                break;
        }
        totalDrinkSize += drinkSize;
        Log.d("Demo","The drink size is "+Integer.toString(drinkSize));
        return drinkSize;
    }
    public int readAlcoholContent()
    {
        final SeekBar seekBarAlcohoPerc = (SeekBar) findViewById(R.id.seekBarAlcohol);
        alcoholContenet = seekBarAlcohoPerc.getProgress();
        Log.d("Demo","Read Alcohol content is "+Integer.toString(alcoholContenet));
        return alcoholContenet;
    }
    public void calculateBACLevel()
    {
        final double maleBac = 0.68;
        final double femaleBac = 0.55;
        final double conversionFactor = 6.24;
        double bacLevel=0,A;
        if(weight==0)
        {
            return;
        }
        if(weightChanged) {
            Log.d("Demo","Calculating A = "+totalDrinkSize+"*("+totalAlcoholContenet+"/100.0)");
            A = readDrinkSize() * (readAlcoholContent() / 100.0);
        }
        else
        {
            Log.d("Demo","Calculating A = "+totalDrinkSize+"*("+totalAlcoholContenet+"/100.0)");
            A = totalDrinkSize * (totalAlcoholContenet / 100.0);
        }
        Log.d("Demo","Calculated A value is "+A);
        Log.d("Demo","The Gender is "+gender);
        if(gender.matches("Male"))
        {
            bacLevel = (A*conversionFactor/(weight*maleBac));
            Log.d("Demo","Calculating "+A+"*"+conversionFactor+"/("+weight+"*"+maleBac);
        }
        else if(gender.matches("Female"))
        {
            bacLevel = (A*conversionFactor/(weight*femaleBac));
            Log.d("Demo","Calculating "+A+"*"+conversionFactor+"/("+weight+"*"+maleBac);
        }
        TextView setBACLevel = (TextView) findViewById(R.id.textBACLevel);
        totalBACLevel+=bacLevel;
        totalAlcoholContenet+=alcoholContenet;
        Log.d("Demo","the BAC level obtained is"+String.valueOf(bacLevel));
        Log.d("Demo","Total BAC level obtained is"+String.valueOf(totalBACLevel));
        setBACLevel.setText(Double.toString(Math.round(totalBACLevel*100.0)/100.0));
        setBACLevelProgressBar();
    }
    public  void setBACLevelProgressBar()
    {
        ProgressBar bacProgress = (ProgressBar) findViewById(R.id.progressBACLevel);
        int x =(int)(totalBACLevel*100);
        bacProgress.setProgress(x);
        if(totalBACLevel>=0.25)
        {
            Log.d("Demo","BAC is high. Diabling buttons.");
            diasbleAndToast();
        }
        setStatus();
    }
    public void setStatus()
    {
        TextView status = (TextView) findViewById(R.id.textFinalStatus);
        if(totalBACLevel <= 0.08)
        {
            status.setText(R.string.stringSafe);
            status.setHighlightColor(Color.parseColor("#ffffff"));
            status.setBackgroundColor(Color.parseColor("#00cc00"));
        }
        else if(totalBACLevel > 0.08 && totalBACLevel <= 0.20)
        {
            status.setText(R.string.stringCareful);
            status.setHighlightColor(Color.parseColor("#ffffff"));
            status.setBackgroundColor(Color.parseColor("#ffcc00"));
        }
        else if(totalBACLevel > 0.20)
        {
            status.setText(R.string.stringOverLimit);
            status.setHighlightColor(Color.parseColor("#ffffff"));
            status.setBackgroundColor(Color.parseColor("#FF0A0A"));
        }
    }
    public void diasbleAndToast()
    {
        buttonsDisabled = true;
        ((Button) findViewById(R.id.buttonAddDrink)).setEnabled(false);
        ((Button) findViewById(R.id.buttonSave)).setEnabled(false);
        Toast.makeText(getApplicationContext(), R.string.stringToastMessage, Toast.LENGTH_SHORT).show();
    }
    public void resetAll()
    {
        totalBACLevel=0;
        totalDrinkSize=0;
        gender="Female";
        alcoholContenet=0;
        weight=0;
        ((EditText) findViewById(R.id.inputTextWeight)).setText("");
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroupDrinkSize);
        rg.clearCheck();
        rg.check(R.id.radioButton1oz);
        ((Switch) findViewById(R.id.switchGender)).setChecked(false);
        final SeekBar seekBarAlcohoPerc = (SeekBar) findViewById(R.id.seekBarAlcohol);
        seekBarAlcohoPerc.setProgress(0);
        seekBarAlcohoPerc.post(new Runnable() {
            @Override
            public void run() {
                //seekBarAlcohoPerc.setMax(0);
                seekBarAlcohoPerc.setMax(25);
                seekBarAlcohoPerc.setProgress(5);
            }
        });
        ((TextView) findViewById(R.id.textBACLevel)).setText("0.0");
        ((ProgressBar) findViewById(R.id.progressBACLevel)).setProgress(0);
        if(buttonsDisabled)
        {
            buttonsDisabled = false;
            ((Button) findViewById(R.id.buttonAddDrink)).setEnabled(true);
            ((Button) findViewById(R.id.buttonSave)).setEnabled(true);
        }
        setStatus();
    }
}
