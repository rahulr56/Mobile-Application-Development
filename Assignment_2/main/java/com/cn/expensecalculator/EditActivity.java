package com.cn.expensecalculator;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class EditActivity extends AppCompatActivity {
    int sel =0;
    String[] expenseNameArray;
    DatePickerDialog datePickerDialog;
    ArrayList<Expense> expenseArrayList;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                getApplicationContext().getPackageName() + '/' +
                getBaseContext().getResources().getResourceTypeName(R.drawable.receipt_scan1) + '/' +
                getBaseContext().getResources().getResourceEntryName(R.drawable.receipt_scan1));
        int i=0;
        if(getIntent().getExtras() != null && getIntent().getExtras().getParcelableArrayList(MainActivity.EXPENSE_KEY) != null)
        {

            expenseArrayList = getIntent().getExtras().getParcelableArrayList(MainActivity.EXPENSE_KEY);
            expenseNameArray = new String[expenseArrayList.size()];
            int j=0;
            for (Expense e : expenseArrayList)
            {
                expenseNameArray[j]=e.getName();
                j++;
            }

            findViewById(R.id.buttonSelectExpense).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAlert(expenseNameArray);
                }
            });
            findViewById(R.id.buttonCancelEditExpense).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            findViewById(R.id.buttonSaveEditExpense).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    Expense expense = expenseArrayList.get(sel);
                    expense = modifyExpense(expense);
                    if(expense != null) {
                        expenseArrayList.set(sel, expense);
                        intent.putParcelableArrayListExtra(MainActivity.EXPENSE_KEY, expenseArrayList);
                        intent.putExtra("URI_IMAGE", uri.toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            });
            findViewById(R.id.imageButtonSelectNewDate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showDatePicker(R.id.editTextNewExpenseDate);
                }
            });
            findViewById(R.id.editTextNewExpenseDate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePicker(R.id.editTextNewExpenseDate);
                }
            });
        }

    }
    private Expense modifyExpense(Expense e){
        boolean flag = true;
        String expenseName = ((EditText)findViewById(R.id.editTextNewExpenseName)).getText().toString();
        if (expenseName == null || expenseName.length() > 50 || expenseName.matches("")) {
            Toast.makeText(getApplicationContext(), "Invalid name for expense!", Toast.LENGTH_LONG).show();
            flag = false;
        }
        else
        {
            e.setName(expenseName);
        }
        if(((EditText) findViewById(R.id.editTextNewAmount)).getText().toString().matches("")){
            Toast.makeText(getApplicationContext(), "Invalid amount!", Toast.LENGTH_LONG).show();
            flag = false;
        }
        else
        {
            e.setAmount(Double.parseDouble(((EditText)findViewById(R.id.editTextNewAmount)).getText().toString()));
           // amount = Double.parseDouble(((EditText) findViewById(R.id.editTextAmount)).getText().toString());
        }

        e.setCategory(((Spinner)findViewById(R.id.spinnerNewCategory)).getSelectedItem().toString());

        DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date date = null;
        try {
            date=format.parse( ((EditText)findViewById(R.id.editTextNewExpenseDate)).getText().toString());
        } catch (ParseException e1) {
            Toast.makeText(getApplicationContext(), "Invalid date!", Toast.LENGTH_LONG).show();
            flag = false;
            e1.printStackTrace();
        }
        e.setDate(date);
        e.setImageUri(uri);
        if(flag) {
            return e;
        }
        flag = true;
        return null;
    }
    private void showExpense(Expense exp)
    {
        ((TextView) findViewById(R.id.editTextNewExpenseName)).setText(exp.getName());
        Spinner categorySpinner = ((Spinner) findViewById(R.id.spinnerNewCategory));
        switch (exp.getCategory())
        {
            case "Groceries":
                categorySpinner.setSelection(0);
                break;
            case "Invoice":
                categorySpinner.setSelection(1);
                break;
            case "Transportation":
                categorySpinner.setSelection(2);
                break;
            case "Shopping":
                categorySpinner.setSelection(3);
                break;
            case "Rent":
                categorySpinner.setSelection(4);
                break;
            case "Trips":
                categorySpinner.setSelection(5);
                break;
            case "Utilities":
                categorySpinner.setSelection(6);
                break;
            case "Others":
                categorySpinner.setSelection(7);
                break;
        }
        ((TextView) findViewById(R.id.editTextNewAmount)).setText(Double.toString(exp.getAmount()));
           // Date date = Calendar.getInstance();
           SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String currentDate = sdf.format(exp.getDate());

        ((TextView) findViewById(R.id.editTextNewExpenseDate)).setText(currentDate);
        //((ImageView) findViewById(R.id.imageViewShowReceipt)).setImageURI(exp.imageUri);
        ImageButton imageView = ((ImageButton) findViewById(R.id.imageButtonNewReceipt));
        uri = exp.getImageUri();
        imageView.setImageURI(exp.getImageUri());
    }

    public void showAlert(final String[] str1)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Pick an Expense");
        builder.setItems(str1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sel = i;
                showExpense(expenseArrayList.get(sel));
            }
        });
        builder.create();
        builder.show();
    }
    public void showDatePicker(final int id){

        final Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String date = null;
                try {

                    date = dateFormat.format(dateFormat.parse(+(newDate.get(Calendar.MONTH)+1)+"/"+newDate.get(Calendar.DAY_OF_MONTH)+"/"+newDate.get(Calendar.YEAR)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ((EditText)findViewById(id)).setText(date);
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public void photoPicker(View view)
    {
        if (Build.VERSION.SDK_INT >= 23) {

            int permissionCheck = 0;
            permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AddExpenseActivity.WRITE_EXTERNAL_STORAGE_CODE);
            } else {
                Intent gallIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallIntent, AddExpenseActivity.MEDIA_CODE);
            }
        }
        else
        {
            Intent gallIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallIntent, AddExpenseActivity.MEDIA_CODE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AddExpenseActivity.MEDIA_CODE && resultCode == RESULT_OK && data != null){
            uri = data.getData();
            ImageButton imageView = ((ImageButton) findViewById(R.id.imageButtonNewReceipt));
            imageView.setImageURI(uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case AddExpenseActivity.WRITE_EXTERNAL_STORAGE_CODE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //  callMethod();
                }
                break;

            default:
                break;
        }
    }

}
