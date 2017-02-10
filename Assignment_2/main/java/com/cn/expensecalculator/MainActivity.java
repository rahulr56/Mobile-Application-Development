package com.cn.expensecalculator;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;;
import java.util.ArrayList;

import static java.lang.System.exit;

public class MainActivity extends AppCompatActivity {
    ArrayList<Expense> expenseArrayList;
    public final static String EXPENSE_KEY = "EXPENSE_LIST";


    public static final int ADD_EXPENSE_CODE = 100;
    public static final int DELETE_EXPENSE_CODE = 101;
    public static final int EDIT_EXPENSE_CODE = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expenseArrayList = new ArrayList<>();
        ((Button) findViewById(R.id.buttonAddExpense)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddExpenseActivity.class);
                intent.putExtra(EXPENSE_KEY,expenseArrayList);
                startActivityForResult(intent,ADD_EXPENSE_CODE);
            }
        });
        ((Button) findViewById(R.id.buttonEditExpense)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expenseArrayList.size()>0) {
                    Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                    intent.putExtra(EXPENSE_KEY, expenseArrayList);
                    startActivityForResult(intent,EDIT_EXPENSE_CODE);
                }else{
                    Toast.makeText(getApplicationContext(),"No Expenses to edit",Toast.LENGTH_SHORT).show();
                }
            }
        });
        ((Button) findViewById(R.id.buttonShowExpense)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expenseArrayList.size()>0) {
                    Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
                    Log.d("Inside show", "" + expenseArrayList.size());
                    intent.putExtra(EXPENSE_KEY, expenseArrayList);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"No Expenses to show",Toast.LENGTH_LONG).show();
                }
            }
        });
        ((Button) findViewById(R.id.buttonDelExpense)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expenseArrayList.size()>0) {
                    Intent intent = new Intent(getApplicationContext(), DeleteActivity.class);
                    intent.putExtra(EXPENSE_KEY, expenseArrayList);
                    startActivityForResult(intent,DELETE_EXPENSE_CODE);
                }else{
                    Toast.makeText(getApplicationContext(),"No Expenses to delete",Toast.LENGTH_LONG).show();
                }
            }
        });
        ((Button) findViewById(R.id.buttonFinish)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if (requestCode == ADD_EXPENSE_CODE) {
                expenseArrayList = data.getExtras().getParcelableArrayList(EXPENSE_KEY);
                Log.d("expense object",""+expenseArrayList.get(0).toString());
                Log.d("After assigning",""+expenseArrayList.size());
            }else if (requestCode == DELETE_EXPENSE_CODE) {
                expenseArrayList = data.getExtras().getParcelableArrayList(EXPENSE_KEY);
                Log.d("After assigning",""+expenseArrayList.size());
            }else if (requestCode == EDIT_EXPENSE_CODE) {
                expenseArrayList = data.getExtras().getParcelableArrayList(EXPENSE_KEY);
                Log.d("After assigning",""+expenseArrayList.size());
            }
        }
    }
}
