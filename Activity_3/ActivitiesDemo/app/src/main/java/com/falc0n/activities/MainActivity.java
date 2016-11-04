package com.falc0n.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String STUDENT_KEY = "studentKey";
    public static final String CHANGED_KEY = "editStudentKey";
    Student student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editTextName = ((EditText)findViewById(R.id.editTextName));
        final EditText editTextEmail = ((EditText)findViewById(R.id.editTextEmail));
        final RadioGroup deptRg = ((RadioGroup)findViewById(R.id.radioGroup));
        final SeekBar moodSeekBar = ((SeekBar)findViewById(R.id.seekBarMood));
        moodSeekBar.setProgress(0);
        moodSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ((TextView)findViewById(R.id.textViewDisplaySeekBarMood)).setText(i+getString(R.string.emptyString));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        if(getIntent().getExtras()!=null)
        {
            ((Button)findViewById(R.id.buttonSubmit)).setText(R.string.saveString);
            final String key = (String) getIntent().getExtras().get(CHANGED_KEY);
            student = (Student) getIntent().getExtras().get(MainActivity.STUDENT_KEY);
            if(key.matches("NAME")){
                editTextEmail.setVisibility(View.INVISIBLE);
                deptRg.setVisibility(View.INVISIBLE);
                findViewById(R.id.textViewDeptHead).setVisibility(View.INVISIBLE);
                findViewById(R.id.textViewMoodHead).setVisibility(View.INVISIBLE);
                findViewById(R.id.textViewDisplaySeekBarMood).setVisibility(View.INVISIBLE);
                moodSeekBar.setVisibility(View.INVISIBLE);
            }
            else if(key.matches("EMAIL"))
            {
                editTextName.setVisibility(View.INVISIBLE);
                deptRg.setVisibility(View.INVISIBLE);
                findViewById(R.id.textViewDisplaySeekBarMood).setVisibility(View.INVISIBLE);
                findViewById(R.id.textViewDeptHead).setVisibility(View.INVISIBLE);
                findViewById(R.id.textViewMoodHead).setVisibility(View.INVISIBLE);
                moodSeekBar.setVisibility(View.INVISIBLE);
            }
            else if(key.matches("DEPT"))
            {
                editTextEmail.setVisibility(View.INVISIBLE);
                editTextName.setVisibility(View.INVISIBLE);
                findViewById(R.id.textViewMoodHead).setVisibility(View.INVISIBLE);
                moodSeekBar.setVisibility(View.INVISIBLE);
                findViewById(R.id.textViewDisplaySeekBarMood).setVisibility(View.INVISIBLE);
            }
            else if(key.matches("MOOD"))
            {
                editTextEmail.setVisibility(View.INVISIBLE);
                editTextName.setVisibility(View.INVISIBLE);
                findViewById(R.id.textViewDeptHead).setVisibility(View.INVISIBLE);
                deptRg.setVisibility(View.INVISIBLE);
            }
            findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(key.matches("NAME")) {
                        String name = editTextName.getText().toString().trim();
                        if(!checkFields(name,student.getEmail()))
                        {
                            return;
                        }
                        student.setName(name);
                    }else if(key.matches("EMAIL")) {
                        String email = editTextEmail.getText().toString().trim();
                        if(!checkFields(student.getName(),email))
                        {
                            return;
                        }
                        student.setEmail(email);
                    }else if(key.matches("DEPT")) {
                        String department = getDeptName(deptRg.getCheckedRadioButtonId());
                        Log.d("Demo","Updating dept is "+department);
                        student.setDepartment(department);
                    }else if(key.matches("MOOD")) {
                        int mood = moodSeekBar.getProgress();
                        student.setMood(mood);
                    }
                    StartIntent();
                }
            });
        }
        else {
            ((Button)findViewById(R.id.buttonSubmit)).setText(R.string.submitString);
            findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = editTextName.getText().toString().trim();
                    String email = editTextEmail.getText().toString().trim();
                    if(!checkFields(name,email))
                    {
                        return;
                    }
                    String department = getDeptName(deptRg.getCheckedRadioButtonId());
                    int mood = moodSeekBar.getProgress();
                    student = new Student(name,email,department,mood);
                    StartIntent();
                }
            });
        }
    }

    @Nullable
    private String getDeptName(int sel) {
        String department = null;
        switch (sel)
        {
            case R.id.radioButtonSis:
                Log.d("Demo","SIS");
                department = "SIS";
                break;
            case R.id.radioButtonBio:
                Log.d("Demo","BIO");
                department = "BIO";
                break;
            case R.id.radioButtonCS:
                Log.d("Demo","CS");
                department = "CS";
                break;
            case R.id.radioButtonOthers:
                Log.d("Demo","Others");
                department = "OTHERS";
                break;

        }
        Log.d("Demo","Sleected dept"+sel+" is "+department);
        return department;
    }

    private void StartIntent() {
        Intent displayActivity = new Intent(getApplicationContext(),DisplayActivity.class);
        displayActivity.putExtra(STUDENT_KEY, this.student);
        displayActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(displayActivity);
    }
    private boolean checkFields(String name, String email)
    {
        if(name.matches("") ){
            Toast.makeText(getApplicationContext(),"Name cannot be empty!",Toast.LENGTH_SHORT).show();
            return false;
        }else if(email.matches("") ){
            Toast.makeText(getApplicationContext(),"Email cannot be empty!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
