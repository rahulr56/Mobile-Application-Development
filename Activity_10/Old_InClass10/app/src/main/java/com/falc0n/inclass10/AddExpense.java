package com.falc0n.inclass10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddExpense extends AppCompatActivity {
    final String[] categoryArray = {"Groceries","Invoice","Transportation","Shopping","Rent","Trips","Utilities","Others"};
    static int expenseCount = 0;
    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerAddCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categoryArray, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        findViewById(R.id.buttonAddAddExpense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText)findViewById(R.id.editTextAddExpName)).getText().toString().trim();
                String amount = ((EditText)findViewById(R.id.editTextAddAmount)).getText().toString().trim();
                if(name.matches("") || name == null)
                {
                    Toast.makeText(AddExpense.this,"Name of an expense cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }else if(amount.matches("") || amount == null)
                {
                    Toast.makeText(AddExpense.this,"Amount for an expense cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                int sel =spinner.getSelectedItemPosition();
                String category = categoryArray[sel];
                Expense expense = new Expense(name,amount,category,MainActivity.currentUserId);
                writeNewExpense(expense);
                Toast.makeText(AddExpense.this,"Added expense!",Toast.LENGTH_LONG);
                finish();
            }
        });
        findViewById(R.id.buttonAddCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void writeNewExpense(Expense expense) {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", expense.getName());
        map.put("amount", expense.getAmount());
        map.put("category", expense.getCategory());
        mDatabaseReference.child(MainActivity.currentUserId).push().setValue(map);
        expenseCount++;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
