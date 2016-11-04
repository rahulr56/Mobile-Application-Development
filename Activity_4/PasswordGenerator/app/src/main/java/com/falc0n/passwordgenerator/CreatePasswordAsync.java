package com.falc0n.passwordgenerator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by fAlc0n on 11/3/16.
 */

public class CreatePasswordAsync extends AsyncTask<Void, Integer, ArrayList<String>> {
    MainActivity activity;
    int passwordLength, passwordCount;
    ArrayList<String> passwordList = new ArrayList<>();
    ProgressDialog progressDialog ;

    public CreatePasswordAsync(MainActivity activity, int passwordLength, int passwordCount) {
        this.activity = activity;
        this.passwordLength = passwordLength;
        this.passwordCount = passwordCount;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        for (int i=0; i<passwordCount;i++)
        {
            passwordList.add(Util.getPassword(passwordLength));
            publishProgress(((Integer) ((i+1)*100)/passwordCount));
        }
        return passwordList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        passwordLength+=8;
        passwordCount+=1;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle(R.string.genPasswords);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);
        progressDialog.dismiss();
        activity.setData(passwordList);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int progress = values[0];
        progressDialog.setProgress(progress);
    }
}
