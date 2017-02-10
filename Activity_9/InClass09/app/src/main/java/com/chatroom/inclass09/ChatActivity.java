package com.chatroom.inclass09;

import android.content.SharedPreferences;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    private final OkHttpClient chatClient = new OkHttpClient();
    List<Message.MessagesBean> listMessages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        listMessages =  new ArrayList<>();

        findViewById(R.id.imageButtonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = MainActivity.sharedPref.edit();
                editor.remove(MainActivity.LOGIN_TOKEN);
                editor.commit();
                finish();
            }
        });

        try {
            String name = getIntent().getExtras().getString(SignUpActivity.FULL_NAME);
            Log.d("name",""+name);
            ((TextView)findViewById(R.id.textViewName)).setText(name);
            callAPI("/messages");
            Log.d("after on response",""+listMessages.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void callAPI(String ext) throws IOException {
        String header = MainActivity.sharedPref.getString(MainActivity.LOGIN_TOKEN,null);
        Log.d("call API",ext+" :: "+header);

        Request request = new Request.Builder()
                .url(MainActivity.URL+ext)
                .addHeader(MainActivity.AUTHORIZATION,"BEARER "+header)
                .build();
        chatClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(ChatActivity.this,"Unexpected error",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d("API response",res);
                Message message = ParseUtil.parseMessageResponse(res);
                if(message.getStatus().matches("error")){
                    //Toast.makeText(ChatActivity.this,"Error",Toast.LENGTH_SHORT).show();
                }else {
                    listMessages = message.getMessages();
                    Log.d("iside on response",""+listMessages.size());
                    ListView listView = (ListView)findViewById(R.id.listViewitem);
                    MessageAdapter msgAdapter = new MessageAdapter(ChatActivity.this,R.layout.row_item_layout,listMessages);
                    msgAdapter.setNotifyOnChange(true);
                    listView.setAdapter(msgAdapter);
                }
            }
        });
    }
}
