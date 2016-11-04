package com.falc0n.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayActivity extends AppCompatActivity implements View.OnClickListener{
    Student student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        if(getIntent().getExtras()!=null)
        {
            student = (Student) getIntent().getExtras().get(MainActivity.STUDENT_KEY);
            ((TextView)findViewById(R.id.textViewDispName)).setText(student.getName());
            ((TextView)findViewById(R.id.textViewDispEmail)).setText(student.getEmail());
            ((TextView)findViewById(R.id.textViewDispDept)).setText(student.getDepartment());
            ((TextView)findViewById(R.id.textViewDispMood)).setText(student.getMood()+" % Positive");

            findViewById(R.id.imageButtonEditName).setOnClickListener(this);
            findViewById(R.id.imageButtonEditEmail).setOnClickListener(this);
            findViewById(R.id.imageButtonEditDept).setOnClickListener(this);
            findViewById(R.id.imageButtonEditMood).setOnClickListener(this);
        }else {
            Toast.makeText(getApplicationContext(),"Error Occured!",Toast.LENGTH_LONG).show();
            finish();
        }
        findViewById(R.id.buttonFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
                finish();
                startActivity(mainActivity);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String key="";
        switch (id)
        {
            case R.id.imageButtonEditName:
                key = "NAME";
                break;
            case R.id.imageButtonEditDept:
                key = "DEPT";
                break;
            case R.id.imageButtonEditEmail:
                key = "EMAIL";
                break;
            case R.id.imageButtonEditMood:
                key = "MOOD";
                break;
        }
        Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
        mainActivity.putExtra(MainActivity.STUDENT_KEY,student);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainActivity.putExtra(MainActivity.CHANGED_KEY,key);
        startActivity(mainActivity);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
