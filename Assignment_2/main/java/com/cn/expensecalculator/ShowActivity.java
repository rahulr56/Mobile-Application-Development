package com.cn.expensecalculator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ShowActivity extends AppCompatActivity implements View.OnClickListener {
    static int i = 0;
    ArrayList<Expense> showExpenseList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        if(getIntent().getExtras() != null && getIntent().getExtras().getParcelableArrayList(MainActivity.EXPENSE_KEY) != null) {
            Log.d("Inside showActivity",""+ getIntent().getExtras().getParcelableArrayList(MainActivity.EXPENSE_KEY));
            showExpenseList= getIntent().getExtras().getParcelableArrayList(MainActivity.EXPENSE_KEY);
            Log.d("SHOW","Retrieving an element");
            if(showExpenseList.size()>0) {
                showExpense(showExpenseList.get(0));

                findViewById(R.id.imageSwitchFirst).setOnClickListener(this);
                findViewById(R.id.imageSwitchLast).setOnClickListener(this);
                findViewById(R.id.imageSwitchNext).setOnClickListener(this);
                findViewById(R.id.imageSwitchPrev).setOnClickListener(this);
                findViewById(R.id.buttonFinishShowExpenses).setOnClickListener(this);
            }else{

            }
        }
    }

    private void showExpense(Expense exp) {
        ((TextView) findViewById(R.id.editTextShowName)).setText(exp.getName());
        ((TextView) findViewById(R.id.editTextShowCategory)).setText(exp.getCategory().toString());
        ((TextView) findViewById(R.id.editTextShowAmount)).setText(Double.toString(exp.getAmount()));
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String currentDate = sdf.format(exp.getDate());
        ((TextView) findViewById(R.id.editTextShowDate)).setText(currentDate);
        Log.d("show Expense ssssss",""+exp.getImageUri().getPath());
        //((ImageView) findViewById(R.id.imageViewShowReceipt)).setImageURI(null);
        ImageView imageView = ((ImageView) findViewById(R.id.imageViewShowReceipt));
        imageView.setImageURI(exp.getImageUri());
       /* BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            Uri uri = exp.getImageUri();
            BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
            options.inSampleSize = calculateInSampleSize(options, 100, 100);
            options.inJustDecodeBounds = false;
            Bitmap image = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
            imageView.setImageBitmap(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.imageSwitchFirst:
                i=0;
                showExpense(showExpenseList.get(i));
                break;
            case R.id.imageSwitchLast:
                i= showExpenseList.size() - 1;
                showExpense(showExpenseList.get(i));
                break;
            case R.id.imageSwitchNext:
                if(++i >= showExpenseList.size()) {
                    i=0;
                    Util.showToast(getBaseContext(), "No more entries!!");
                }else{
                    showExpense(showExpenseList.get(i));
                }
                break;
            case R.id.imageSwitchPrev:
                if(--i < 0) {
                    i=0;
                    Util.showToast(getBaseContext(), "No more entries!");
                }else{
                    showExpense(showExpenseList.get(i));
                }
                break;
            default:
                finish();
        }
    }
}
