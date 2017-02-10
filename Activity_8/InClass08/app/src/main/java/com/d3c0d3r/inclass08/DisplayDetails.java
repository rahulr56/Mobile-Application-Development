package com.d3c0d3r.inclass08;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayDetails extends Fragment {

    dataToDisplayDetails interfaceObject;
    public DisplayDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_show_expense, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            interfaceObject = (dataToDisplayDetails) context;
        }catch (ClassCastException e)
        {
            throw new ClassCastException("Implement interface dataToDisplayDetails");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ExpenseDetails ed = interfaceObject.getExpenseDetails();
        ((TextView)getView().findViewById(R.id.textViewShowExpName)).setText(getResources().getString(R.string.nameString) + "     "+ed.getName());
        ((TextView)getView().findViewById(R.id.textViewShowExpCategory)).setText(getResources().getString(R.string.categoryString) + "     "+ed.getCategory());
        ((TextView)getView().findViewById(R.id.textViewShowExpAmount)).setText(getResources().getString(R.string.amountString)+ "     "+ed.getAmount());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        ((TextView)getView().findViewById(R.id.textViewShowExpDate)).setText(getResources().getString(R.string.dateString)+ "     "+dateFormat.format(ed.getDate()));
        getActivity().findViewById(R.id.buttonDisplayCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

    public  interface dataToDisplayDetails
    {
        public ExpenseDetails getExpenseDetails();
    }
}
