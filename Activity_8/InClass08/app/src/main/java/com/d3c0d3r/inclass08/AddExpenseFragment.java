package com.d3c0d3r.inclass08;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddExpenseFragment extends Fragment {
    final public String[] categories ={"Groceries","Invoice","Transportation","Shopping","Rent","Trips","Utilities","Other"};
    ExpenseDetails newExpense;
    SendExpenseDetails mListner;
    public AddExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Rahul","in oncreate of addexpense");
        return inflater.inflate(R.layout.fragment_add_expense, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("Rahul","in onAttach of addexpense");
        try
        {
            mListner = (SendExpenseDetails) context;
        }catch (ClassCastException e)
        {
            throw new ClassCastException("implement interface transferExpense");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newExpense = new ExpenseDetails();
        final Spinner spinner = (Spinner) getView().findViewById(R.id.spinnerAddExpCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getView().getContext(),
                R.array.planets_array, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        getActivity().findViewById(R.id.buttonAddExpense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText)getView().findViewById(R.id.editTextAddExpName)).getText().toString().trim();
                String amount = ((EditText)getView().findViewById(R.id.editTextAddExpAmount)).getText().toString().trim();
                int pos = ((Spinner)getView().findViewById(R.id.spinnerAddExpCategory)).getSelectedItemPosition();
                if(name.matches("") || amount.matches(""))
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Invalid name or amount",Toast.LENGTH_LONG).show();
                }
                else {
                    newExpense.setName(name);
                    newExpense.setCategory(categories[pos]);
                    newExpense.setAmount(amount);
//                    Calendar cal = Calendar.getInstance();
//                    newExpense.setDate(cal.getTime());
                    newExpense.setDate(new Date());
                    Log.d("Rahul",newExpense.toString());
                    mListner.addExpense(newExpense);
                }
            }
        });
        getActivity().findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

    public interface SendExpenseDetails
    {
        public void addExpense(ExpenseDetails expense);
    }
}
