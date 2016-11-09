package com.falc0n.inclass10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AllExpensesActivity extends AppCompatActivity {
    final String DB_URL ="project/fir-demo-93470/database/data/expenses/"+MainActivity.currentUserId;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref ;
    final List<Expense> expenseList = new ArrayList<>();
    ExpenseAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_expenses);
        final ListView listView = ((ListView)findViewById(R.id.listViewALExp));

        GetExpensesData(expenseList);
        //checkArraySize();
        arrayAdapter = new ExpenseAdapter(this,R.layout.listitem,expenseList);
        arrayAdapter.setNotifyOnChange(true);
        listView.setAdapter(arrayAdapter);


        findViewById(R.id.imageButtonALAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                Intent intent = new Intent(AllExpensesActivity.this,AddExpense.class);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteData(i);
                arrayAdapter.notifyDataSetChanged();
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(MainActivity.LOG_TAG,"Clicked "+expenseList.get(i).toString());
                checkArraySize();
                Intent intent = new Intent(AllExpensesActivity.this,ShowActivity.class);
                intent.putExtra("EXPENSE_DATA", expenseList.get(i));
                startActivity(intent);
            }
        });
    }

    private void deleteData(int i) {
        Log.d(MainActivity.LOG_TAG,"Size of array is "+expenseList.size());
        expenseList.remove(i);
        checkArraySize();
        Log.d(MainActivity.LOG_TAG,"Size of array after deletion is "+expenseList.size());
        ref.setValue(expenseList);
    }

    private void checkArraySize() {
        if(expenseList.size() == 0)
        {
            (findViewById(R.id.listViewALExp)).setVisibility(View.INVISIBLE);
            TextView textView = (TextView) findViewById(R.id.textViewALERROR);
            textView.setVisibility(View.VISIBLE);
            textView.setText(getString(R.string.noExpString));
        }
        else {
            (findViewById(R.id.listViewALExp)).setVisibility(View.VISIBLE);
            (findViewById(R.id.textViewALERROR)).setVisibility(View.INVISIBLE);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    private void GetExpensesData(final List<Expense> expenseList) {
        ref = FirebaseDatabase.getInstance().getReference().child(MainActivity.currentUserId);
        Log.d(MainActivity.LOG_TAG,"Ref is "+ref.toString());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dS:dataSnapshot.getChildren()) {
                    Log.d(MainActivity.LOG_TAG,"Key is "+ref.getKey());
                    Expense expense = dS.getValue(Expense.class);
                    Log.d(MainActivity.LOG_TAG,"Adding : "+expense);
                    expenseList.add(expense);
                }
                checkArraySize();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(MainActivity.LOG_TAG,"The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
