package com.falc0n.areacalculatoewithradiobuttons;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Math.PI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RadioGroup rg =((RadioGroup)findViewById(R.id.radioGroup));
        Button calc = ((Button)findViewById(R.id.buttonCalculate));
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = rg.getCheckedRadioButtonId();
                EditText editTextLen1 = ((EditText) findViewById(R.id.editTextLen1));
                EditText editTextLen2 = ((EditText) findViewById(R.id.editTextLen2));
                TextView calcArea = ((TextView) findViewById(R.id.textViewDispCalcArea));
                String len1 = editTextLen1.getText().toString().trim();
                String len2 = editTextLen2.getText().toString().trim();
                Double length2=  0.0;
                if (id != R.id.radioButtonClearAll) {
                    if (len1.matches("")) {
                        Toast.makeText(getApplicationContext(), "Invalid length 1 entered!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if ((id == R.id.radioButtonSquare || id == R.id.radioButtonCircle) )
                    {
                        editTextLen2.setText("");
                    }else {
                        if(len2.matches("")) {
                            Toast.makeText(getApplicationContext(), "Invalid length 2 entered!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        length2 = Double.parseDouble(len2);
                    }


                    Double length1 = Double.parseDouble(len1);
                    Double area = 0.0;

                    switch (id) {
                        case R.id.radioButtonCircle:
                            area = PI * length1 * length1;
                            editTextLen2.setText("");
                            calcArea.setText(area + "");

                            break;
                        case R.id.radioButtonTriangle:
                            area = 0.5 * length1 * length2;
                            calcArea.setText(area + "");
                            break;
                        case R.id.radioButtonRectangle:
                            area = length1 * length2;
                            calcArea.setText(area + "");
                            break;
                        case R.id.radioButtonSquare:
                            area = length1 * length1;
                            editTextLen2.setText("");
                            calcArea.setText(area + "");
                            break;
                    }
                }else{
                    editTextLen1.setText("");
                    editTextLen2.setText("");
                    calcArea.setText("");
                    return;
                }
            }
        });
    }
}