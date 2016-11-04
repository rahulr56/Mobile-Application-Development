package com.falc0n.passwordgenerator;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.falc0n.passwordgenerator.R.id.textViewDispPassLength;

public class MainActivity extends AppCompatActivity {
    String selectedPassword;
    ExecutorService threadPool ;
    public final int THREAD_POOL_SIZE = 4;
    ArrayList<String > passwordsList= new ArrayList<>();
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        final SeekBar seekBarPasswordCount = ((SeekBar)findViewById(R.id.seekBarPassCount));
        final SeekBar seekBarPasswordLength = ((SeekBar)findViewById(R.id.seekBarPassLength));
        final TextView textViewPasswordCount = ((TextView)findViewById(R.id.textViewDispPassCount));
        final TextView textViewPasswordLength = ((TextView)findViewById(textViewDispPassLength));
        seekBarPasswordCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textViewPasswordCount.setText((i+1)+getString(R.string.emptyString));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarPasswordLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textViewPasswordLength .setText((i+8)+getString(R.string.emptyString));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        findViewById(R.id.buttonAsync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int length = seekBarPasswordLength.getProgress();
                int count = seekBarPasswordCount.getProgress();
                new CreatePasswordAsync(MainActivity.this,length,count).execute();
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setProgress(0);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

                switch (message.what)
                {
                    case CreatePasswordThreads.STATUS_START:
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setTitle(R.string.genPasswords);
                        progressDialog.setCancelable(false);
                        progressDialog.setMax(100);
                        progressDialog.setProgress(0);
                        progressDialog.show();
                        break;
                    case CreatePasswordThreads.STATUS_STEP:
                        progressDialog.setProgress((Integer) message.obj);
                        break;
                    case CreatePasswordThreads.STATUS_STOP:
                        progressDialog.setProgress(0);
                        progressDialog.dismiss();
                        setData(passwordsList);
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.buttonThread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int length = seekBarPasswordLength.getProgress();
                int count = seekBarPasswordCount.getProgress();
                threadPool.execute(new CreatePasswordThreads(count+1,length+8));
            }
        });
    }
    public void setData(final ArrayList<String> passwordList)
    {
        final TextView textViewSelecetdPassword =  ((TextView)findViewById(R.id.textViewDispSelPassword));
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.genPasswords);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,passwordList);
            dialog.setAdapter(stringArrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    selectedPassword = passwordList.get(i);
                    textViewSelecetdPassword.setText(selectedPassword);
                }
            });

        dialog.create()
        .show();
    }
    public class CreatePasswordThreads implements Runnable {
        ProgressDialog progressDialog ;
        int passwordCount,passwordLength;

        public CreatePasswordThreads(int passwordCount, int passwordLength) {
            this.passwordCount = passwordCount;
            this.passwordLength = passwordLength;
        }

        public final static int STATUS_START = 0x00;
        public final static int STATUS_STEP = 0x01;
        public final static int STATUS_STOP = 0x02;
        @Override
        public void run() {
            passwordsList.clear();
            Message message = new Message();
            message.what= STATUS_START;
            handler.sendMessage(message);
            for(int i=0;i<passwordCount;i++)
            {
                Util.getPassword(passwordLength);
                message = new Message();
                message.what = STATUS_STEP;
                message.obj = (((i+1)*100)/passwordCount);
                passwordsList.add(Util.getPassword(passwordLength));
                handler.sendMessage(message);
            }
            message = new Message();
            message.what= STATUS_STOP;
            handler.sendMessage(message);
        }
    }
}
