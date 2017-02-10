package com.chatroom.inclass09;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class MainActivity extends AppCompatActivity {

    public static final String URL = "http://ec2-54-166-14-133.compute-1.amazonaws.com/api";
    public static final String PASSWORD = "passKey";
    public static final String EMAIL = "emailKey";
    public static final String LOGIN_TOKEN = "loginToken";
    public static final String AUTHORIZATION = "Authorization";

    static SharedPreferences sharedPref ;
    Context context;
    private String totalName;
    final OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.context;
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

      // if(null != sharedPref.getString(LOGIN_TOKEN,null)) {
           setContentView(R.layout.activity_main);
           findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   String email = ((EditText) findViewById(R.id.editTextSignUpEmail)).getText().toString().trim();
                   String password = ((EditText) findViewById(R.id.editTextPassword)).getText().toString().trim();
                   if (null != email && null != password) {
                       try {
                           callAPI("/login", email, password);
                           Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                           Log.d("final name",""+totalName);
                           intent.putExtra(SignUpActivity.FULL_NAME,totalName);
                           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           intent.putExtra("EXIT", true);
                           startActivity(intent);
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               }
           });
           findViewById(R.id.buttonSignUp).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   intent.putExtra("EXIT", true);
                   startActivity(intent);
               }
           });

    }

    private void callAPI(String ext,String email,String password) throws IOException {
        Log.d("call API",ext);
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(URL+ext)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this,"Unexpected error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d("API response",res);
                LoginResponse loginResponse1 = ParseUtil.parseLoginResponse(res);
                if(loginResponse1.getStatus().matches("error")){
                    Toast.makeText(MainActivity.this,loginResponse1.getMessage(),Toast.LENGTH_SHORT).show();
                }else {
                    totalName = loginResponse1.getUserFname()+" "+loginResponse1.getUserLname();
                    setPreferences(loginResponse1.getToken());
                }
            }
        });
    }
    static void setPreferences(String token){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LOGIN_TOKEN, token);
        editor.commit();
    }
}
