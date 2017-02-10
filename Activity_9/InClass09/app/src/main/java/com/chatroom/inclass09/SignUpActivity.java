package com.chatroom.inclass09;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {

    private static String fullName;
    public static final String FULL_NAME = "fullName";
    private final OkHttpClient signUpclient = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.buttonSendSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText)findViewById(R.id.editTextSignUpEmail)).getText().toString().trim();
                String fName = ((EditText)findViewById(R.id.editTextFName)).getText().toString().trim();
                String lName = ((EditText)findViewById(R.id.editTextlName)).getText().toString().trim();
                String pass = ((EditText)findViewById(R.id.editTextChSignUpPass)).getText().toString().trim();
                String repPass = ((EditText)findViewById(R.id.editTextRepSignUpPass)).getText().toString().trim();
                if(null == email || null == fName || null == lName || null == pass ||null == repPass || (!pass.matches(repPass) )){
                    Toast.makeText(SignUpActivity.this,"Error with provided information",Toast.LENGTH_SHORT).show();
                }else{
                    fullName = fName+" "+lName;
                    RequestBody formBody = new FormBody.Builder()
                            .add("email", email)
                            .add("fname", fName)
                            .add("lname", lName)
                            .add("password", pass)
                            .build();
                    try {
                        callAPI(formBody,"/signup");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void callAPI(RequestBody formBody,String ext) throws IOException {
        Log.d("call API",ext);

        Request request = new Request.Builder()
                .url(MainActivity.URL+ext)
                .post(formBody)
                .build();
        signUpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(SignUpActivity.this,"Unexpected error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d("API response",res);
                SignUpResponse signUpResponse = ParseUtil.parseSignUpResponse(res);
                if(signUpResponse.getStatus().matches("error")){
                    Toast.makeText(SignUpActivity.this,signUpResponse.getMessage(),Toast.LENGTH_SHORT).show();
                }else{
                    MainActivity.setPreferences(signUpResponse.getToken());
                    //Toast.makeText(SignUpActivity.this,"Sign Up Sucess",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this,ChatActivity.class);
                    intent.putExtra(FULL_NAME,fullName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                }
            }
        });
    }
}
