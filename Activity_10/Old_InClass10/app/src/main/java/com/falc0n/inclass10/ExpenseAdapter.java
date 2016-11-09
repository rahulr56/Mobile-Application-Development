package com.falc0n.inclass10;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fAlc0n on 11/7/16.
 */

public class ExpenseAdapter extends ArrayAdapter<Expense> {

        List<Expense> objects;
        Context context;
        int resource;
        public ExpenseAdapter(Context context, int resource,List<Expense> objects) {
            super(context, resource,objects);
            this.objects = objects;
            this.context = context;
            this.resource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(this.resource,parent,false);
            }

            Expense Expense = objects.get(position);
            TextView textViewExpense = (TextView) convertView.findViewById(R.id.textViewLIExpName);
            textViewExpense.setText(Expense.getName());
            TextView textViewHex = (TextView) convertView.findViewById(R.id.textViewLIExpAmount);
            String cost = "$ "+Expense.getAmount();
            textViewHex.setText(cost);
            return convertView;
        }
}
