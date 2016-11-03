package com.falc0n.app1;

import android.content.DialogInterface;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final double PI =3.1415 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.textViewDispCalcArea)).setText("0.0");

        findViewById(R.id.buttonAreaCircle).setOnClickListener(this);
        findViewById(R.id.buttonAreaRectangle).setOnClickListener(this);
        findViewById(R.id.buttonAreaTriangle).setOnClickListener(this);
        findViewById(R.id.buttonAreaSquare).setOnClickListener(this);
        findViewById(R.id.buttonClearAll).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d("Demo","In On click");
        EditText editTextLen1 = ((EditText)findViewById(R.id.editTextLen1));
        EditText editTextLen2 = ((EditText)findViewById(R.id.editTextLen2));
        TextView calcArea = ((TextView)findViewById(R.id.textViewDispCalcArea));
        String len1 = editTextLen1.getText().toString().trim();
        String len2 = editTextLen2.getText().toString().trim();

        if(len1.matches("") || len2.matches(""))
        {
            Toast.makeText(getApplicationContext(),"Invalid lengths entered!",Toast.LENGTH_LONG).show();
            return;
        }
        Double length1 = Double.parseDouble(len1);
        Double length2 = Double.parseDouble(len2);
        Double area = 0.0;
        switch (v.getId())
        {
            case R.id.buttonAreaCircle:
                area = PI*length1*length1;
                calcArea.setText(area+"");
                break;
            case R.id.buttonAreaTriangle:
                area = 0.5*length1*length2;
                calcArea.setText(area+"");
                break;
            case R.id.buttonAreaRectangle:
                area = length1*length2;
                calcArea.setText(area+"");
                break;
            case R.id.buttonAreaSquare:
                area = length1*length1;
                calcArea.setText(area+"");
                break;
            case R.id.buttonClearAll:
                editTextLen1.setText("");
                editTextLen2.setText("");
                calcArea.setText("");
                break;
        }
    }
}
