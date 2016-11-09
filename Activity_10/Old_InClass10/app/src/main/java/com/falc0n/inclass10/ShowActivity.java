package com.falc0n.inclass10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        if(getIntent().getExtras()!=null)
        {
            Expense expense = (Expense) getIntent().getSerializableExtra("EXPENSE_DATA");
            ((TextView)findViewById(R.id.textViewSEExpName)).setText(expense.getName());
            ((TextView)findViewById(R.id.textViewSECategory)).setText(expense.category);
            String amount = "$ "+expense.getAmount();
            ((TextView)findViewById(R.id.textViewSEAmount)).setText(amount);
            findViewById(R.id.buttonSEClose).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }
}
