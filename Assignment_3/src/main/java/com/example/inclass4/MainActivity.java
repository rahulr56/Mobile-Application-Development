package com.example.inclass4;


import android.app.AlertDialog;
        import android.app.ProgressDialog;
        import android.content.DialogInterface;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Message;
        import android.util.Log;
        import android.view.View;
        import 	android.support.v7.app.AppCompatActivity;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListAdapter;
        import android.widget.SeekBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.concurrent.ExecutorService;
        import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ExecutorService threadPool;
    ProgressDialog passwordGenProgress;
    ProgressDialog progressDialog;
    android.os.Handler handler;
    String passwordList[] ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        threadPool = Executors.newFixedThreadPool(2);
        ((Button)findViewById(R.id.buttonGeneratePassword)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadPool.execute(new GeneratePasswords());

            }
        });
        final TextView textViewCountOutput = (TextView) findViewById(R.id.textViewPasswordCountOutput);
        textViewCountOutput.setText("1");
        ((SeekBar) findViewById(R.id.seekBarPasswordCount)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewCountOutput.setText(String.valueOf(new Integer(progress + 1)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        final TextView textViewLengthOutput = (TextView) findViewById(R.id.textViewPasswordLengthOutput);
        textViewLengthOutput.setText("8");
        ((SeekBar) findViewById(R.id.seekBarPasswordLength)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textViewLengthOutput.setText(String.valueOf(new Integer(progress + 8)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        handler = new android.os.Handler(new android.os.Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what)
                {
                    case GeneratePasswords.STATUS_START:
                        initializeProgressBar();
                        break;
                    case GeneratePasswords.STATUS_STEP:
                        passwordGenProgress.setProgress((Integer) msg.obj);
                        break;
                    case GeneratePasswords.STATUS_END:
                        passwordGenProgress.dismiss();

                        showAlert((String[]) msg.obj);
                        break;
                }
                int len  = passwordList.length;
                Log.d("belowShowAlert",""+len);


                return false;
            }
        });
        findViewById(R.id.buttonGeneratePasswordAsync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int countSeekBar = ((SeekBar)findViewById(R.id.seekBarPasswordCount)).getProgress() + 1;;
                int lengthSeekBar = ((SeekBar)findViewById(R.id.seekBarPasswordLength)).getProgress() + 8 ;
                Log.d("countSeekBar",""+countSeekBar);
                Log.d("lengthSeekBar",""+lengthSeekBar);
                new DoWork().execute(countSeekBar,lengthSeekBar);
            }
        });
    }
    class GeneratePasswords implements Runnable
    {
        static final int STATUS_START = 0x00;
        static final int STATUS_END = 0x02;
        static final int STATUS_STEP = 0x01;
        @Override
        public void run() {

            Message msg = new Message();
            msg.what = STATUS_START;
            handler.sendMessage(msg);
            int numPasswords = ((SeekBar)findViewById(R.id.seekBarPasswordCount)).getProgress() + 1;
            Log.d("PasswordGen","Num of passworrds = "+numPasswords);
            int passwordLength = ((SeekBar)findViewById(R.id.seekBarPasswordLength)).getProgress() + 8 ;
            Log.d("PasswordGen","Password Length is "+passwordLength);
            passwordList = new String[numPasswords];
            for ( int i=0 ; i < numPasswords ; i++)
            {
                //Log.d("PasswordGen","Generating passwords");
                passwordList[i] = Utility.getPassword(passwordLength);
                //Log.d("PasswordGen","Password "+(i+1)+"generated is "+passwordList[i]);
                msg = new Message();
                msg.what = STATUS_STEP;
                //Log.d("PasswordGen","Progress is ("+((i+1)*100)+")/"+numPasswords);
                msg.obj = (int) Math.round((((i)*100.0)/numPasswords));
                //Log.d("PasswordGen","Progress is "+(int) Math.round((((i+1)*100.0)/numPasswords)));
                handler.sendMessage(msg);
            }
            Log.d("passwordList length : ",""+passwordList.length);
            for(int i=0;i<passwordList.length;i++){
                Log.d("password"+i,""+passwordList[i]);
            }

            msg = new Message();
            msg.obj = passwordList;
            msg.what = STATUS_END;
            handler.sendMessage(msg);
        }
    }
    private void initializeProgressBar()
    {
        passwordGenProgress = new ProgressDialog(MainActivity.this);
        passwordGenProgress.setMax(100);
        passwordGenProgress.setCancelable(false);
        passwordGenProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        passwordGenProgress.setMessage(getText(R.string.genPasswords));
        passwordGenProgress.show();
        passwordGenProgress.setProgress(0);
    }
    private String showAlert(final String[] str1)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(getText(R.string.passwords));
        builder.setItems(str1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TextView textView = (TextView)findViewById(R.id.textViewShowPassword);
                textView.setText(str1[i]);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Log.d("onPostUpdate", "" + str1);


        return null;
    }
    class DoWork extends AsyncTask<Integer,Integer,String[]> {

        @Override
        protected String[] doInBackground(Integer... integers) {

            int val = integers[0];
            String[] str = new String[val];
            for(int i=0;i<val;i++) {
                str[i]=Utility.getPassword(integers[1]);
                Log.d("in background",""+i);
                publishProgress(((i+1)*100)/val);
            }
            return str;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setMessage(getText(R.string.genPasswords));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(final String[] str3) {
            super.onPostExecute(str3);
            showAlert(str3);
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("onProgressUpdate",""+values[0]);
            progressDialog.setProgress(values[0]);
        }
    }
}