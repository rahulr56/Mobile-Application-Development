package com.cn.expensecalculator;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddExpenseActivity extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    public static final int MEDIA_CODE = 101;
    public static final int WRITE_EXTERNAL_STORAGE_CODE=102;
    boolean flag = true;

    Double amount  = 0.0;
    String expenseName;
    String category;
    Date date = null;
    Uri uri ;
    ArrayList<Expense> expenseArrayList;
    final String IMAGE_TYPES = "images/*";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                getApplicationContext().getPackageName() + '/' +
                getBaseContext().getResources().getResourceTypeName(R.drawable.receipt_scan1) + '/' +
                getBaseContext().getResources().getResourceEntryName(R.drawable.receipt_scan1));
        Log.d("Rahul","The uri set is "+uri.toString());
        if(getIntent().getExtras() != null)
        {
            expenseArrayList = getIntent().getExtras().getParcelableArrayList(MainActivity.EXPENSE_KEY);
            Log.d("Expense Activity",""+expenseArrayList.size());
            ((Button) findViewById(R.id.buttonAddExpense)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expenseName = ((EditText) findViewById(R.id.editTextExpenseName)).getText().toString();
                     amount  = null;
                    if (expenseName == null || expenseName.length() > 50 || expenseName.matches("")) {
                        Toast.makeText(getApplicationContext(), "Invalid name for expense!", Toast.LENGTH_LONG).show();
                        flag = false;
                    }
                    Spinner spinner = ((Spinner) findViewById(R.id.spinnerCategory));
                    category = (String) spinner.getSelectedItem();
                    if(((EditText) findViewById(R.id.editTextAmount)).getText().toString().matches("")){
                        Toast.makeText(getApplicationContext(), "Invalid amount!", Toast.LENGTH_LONG).show();
                        flag = false;
                    }
                    else
                    {
                        amount = Double.parseDouble(((EditText) findViewById(R.id.editTextAmount)).getText().toString());
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    /*String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
                    if (dateString == null || dateString.matches("")) {
                        Toast.makeText(getApplicationContext(), "Invalid date!", Toast.LENGTH_LONG).show();
                        flag = false;
                    }*/

                    try {
                        // date = sdf.parse(dateString);
                        date = sdf.parse(((EditText) findViewById(R.id.editTextAddDate)).getText().toString());
                        Log.d("Date",""+date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Invalid date!", Toast.LENGTH_LONG).show();
                        flag = false;
                    }
                    //URI1 =Uri.parse( ((ImageButton)findViewById(R.id.imageButtonReceipt)).toString());
                    if (flag)
                    {
                        Log.d("Expense Activity:Inputs", "" + expenseName + " : " + category + " : " + date + " : " + amount + " : "+uri );
                        Expense expense = new Expense(expenseName, category, amount, date,uri);
                        //Log.d("Inside var",""+expense.toString());
                        //date.setMonth(date.getMonth()+1);
                        expenseArrayList.add(expense);
                        Log.d("Expense Activity ", "after adding to list"+expense.toString());
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra(MainActivity.EXPENSE_KEY, expenseArrayList);
                        // intent.putExtra("Uri", URI1);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    flag = true;
                }
            });
        }
        findViewById(R.id.imageButtonSelectDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDatePicker(R.id.editTextAddDate);
            }
        });
        findViewById(R.id.editTextAddDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(R.id.editTextAddDate);
            }
        });
    }
    public void showDatePicker(final int id){

        final Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(AddExpenseActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear+1, dayOfMonth);
                //Log.d("Date",""+newDate.get(Calendar.YEAR)+newDate.get(Calendar.MONTH)+newDate.get(Calendar.DAY_OF_MONTH))  ;
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String date = null , setDate =null;
                try {

                    date = dateFormat.format(dateFormat.parse(+(newDate.get(Calendar.MONTH))+"/"+newDate.get(Calendar.DAY_OF_MONTH)+"/"+newDate.get(Calendar.YEAR)));
                    //setDate = dateFormat.format(dateFormat.parse(+(newDate.get(Calendar.MONTH)+1)+"/"+newDate.get(Calendar.DAY_OF_MONTH)+"/"+newDate.get(Calendar.YEAR)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ((EditText)findViewById(id)).setText(date);
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    static final int REQUEST_IMAGE_GET = 1;

/*    public void photoPicker(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image*//*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }*/

    public void photoPicker(View view)
    {
        if (Build.VERSION.SDK_INT >= 23) {

            int permissionCheck = 0;
            permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
            } else {
                Intent gallIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallIntent, MEDIA_CODE);
            }
        }
        else
        {
            Intent gallIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallIntent, MEDIA_CODE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MEDIA_CODE && resultCode == RESULT_OK && data != null){
            uri = data.getData();
            Log.d("retrieved uri",""+uri.toString());
            ImageView imageView = (ImageView)findViewById(R.id.imageButtonReceipt);
            imageView.setImageURI(uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case WRITE_EXTERNAL_STORAGE_CODE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //  callMethod();
                }
                break;

            default:
                break;
        }
    }
}
