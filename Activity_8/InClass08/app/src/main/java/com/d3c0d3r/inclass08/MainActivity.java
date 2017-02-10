package com.d3c0d3r.inclass08;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddExpenseFragment.SendExpenseDetails,list.getDataFromMain,DisplayDetails.dataToDisplayDetails {
    ArrayList<ExpenseDetails> expenseList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expenseList = new ArrayList<>();

//        findViewById(R.id.textViewNoExpenses).setVisibility(View.INVISIBLE);
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new list(), "VIEW_FRAGMENT")
                    .commit();

    }


    @Override
    public void addExpense(ExpenseDetails expense) {
        Log.d("Rahul","In main activity "+expense.toString());
        expenseList.add(expense);
        Log.d("Rahul","In main activity new expenselist SIZE"+expenseList.size());
        getFragmentManager().popBackStack();
    }

    @Override
    public ArrayList<ExpenseDetails> getDataIntoListFragment() {
        return expenseList;
    }
    int expensePosition ;
    @Override
    public void sendClickedPosition(int pos) {
        expensePosition = pos;
        getFragmentManager().beginTransaction()
                .replace(R.id.container,new DisplayDetails(),"DISPLAY_EXPENSE")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void sendDeleted(int pos) {
        Log.d("Rahul","Deleting item in position "+pos+"----"+expenseList.size());
        expenseList.remove(pos);
        Toast.makeText(getApplicationContext(),"Item Deleted Successfully!",Toast.LENGTH_LONG).show();
    }

    @Override
    public ExpenseDetails getExpenseDetails() {
        Log.d("Rahul",expenseList.get(expensePosition).toString());
        Log.d("Rahul","Sending expense to display");
        return expenseList.get(expensePosition);
    }


}
