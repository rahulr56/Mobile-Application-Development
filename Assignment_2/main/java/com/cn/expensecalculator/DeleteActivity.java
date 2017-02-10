package com.cn.expensecalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.expensecalculator.Expense;
import com.cn.expensecalculator.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DeleteActivity extends AppCompatActivity {

    int sel =-1;
    String[] expenseNameArray;
    ArrayList<Expense> expenseArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        int i=0;
        ArrayList<Expense> showExpenseList;
        if(getIntent().getExtras() != null && getIntent().getExtras().getParcelableArrayList(MainActivity.EXPENSE_KEY) != null)
        {

        expenseArrayList = getIntent().getExtras().getParcelableArrayList(MainActivity.EXPENSE_KEY);
        Log.d("EDIT","parsing through the list");
        expenseNameArray = new String[expenseArrayList.size()];
        int j=0;
        for (Expense e : expenseArrayList)
        {
            expenseNameArray[j]=e.getName();
            j++;
            Log.d("EDIT","Elements " + (j-1) + expenseNameArray[j-1]);
        }


           findViewById(R.id.buttonSelectExpDE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAlert(expenseNameArray);
                    Log.d("After dialog box",""+sel);
                }
            });
            findViewById(R.id.buttonDeleteDE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!expenseArrayList.isEmpty() && sel != -1)
                        expenseArrayList.remove(sel);
                    Toast.makeText(getApplicationContext(),"Record Deleted!",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra(MainActivity.EXPENSE_KEY,expenseArrayList);
                    //intent.putExtra("Uri",URI.toString());
                    setResult(RESULT_OK,intent);
                    finish();
//                    if(!expenseArrayList.isEmpty())
//                        showExpense(expenseArrayList.get(0));
//                    else {
//                        Toast.makeText(getApplicationContext(),"No more records to delete!",Toast.LENGTH_SHORT).show();
//                    }
                }
            });

            findViewById(R.id.buttonCancelDE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }
    private void showExpense(Expense exp)
    {
        ((TextView) findViewById(R.id.editTextNewExpenseName)).setText(exp.getName());
        ((TextView) findViewById(R.id.spinnerNewCategory)).setText(exp.getCategory().toString());
        //((TextView) findViewById(R.id.editTextShowCategory)).setText("asdadad");
        ((TextView) findViewById(R.id.editTextNewAmount)).setText(Double.toString(exp.getAmount()));
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String currentDate = sdf.format(exp.getDate());
        ((TextView) findViewById(R.id.editTextNewExpenseDate)).setText(currentDate);
        //((ImageView) findViewById(R.id.imageViewShowReceipt)).setImageURI(exp.imageUri);
        ImageView imageView = ((ImageView) findViewById(R.id.imageButtonReceiptDE));
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
        Log.d("onPostUpdate", "" + str1);
    }
}