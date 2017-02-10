package com.d3c0d3r.inclass08;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by d3c0d3R on 17-Oct-16.
 */

public class ExpenseAdapter extends ArrayAdapter<ExpenseDetails> {
    Context mContext;
    ArrayList<ExpenseDetails> mData;
    int mResource;
    public ExpenseAdapter(Context context, int resource, ArrayList<ExpenseDetails> objects) {
        super(context, resource, objects);
        mData=objects;
        mContext=context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }
        ExpenseDetails ed = mData.get(position);
        ((TextView)convertView.findViewById(R.id.textViewListExpName)).setText(ed.getName());
        ((TextView)convertView.findViewById(R.id.textViewListExpAmount)).setText(ed.getAmount());
        return convertView;
    }
}