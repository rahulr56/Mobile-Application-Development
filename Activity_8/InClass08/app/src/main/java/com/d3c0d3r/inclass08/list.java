package com.d3c0d3r.inclass08;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class list extends Fragment{
    getDataFromMain dataBinder;
    ArrayList<ExpenseDetails> expenseDetailList;
    ExpenseAdapter adapter;
    public list() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("Rahul","in inflating in onCreateView");
        View view = inflater.inflate(R.layout.fragment_list2, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("Rahul","in onAttach");
        expenseDetailList = new ArrayList<>();
        try
        {
            dataBinder = (getDataFromMain) context;
        }catch (ClassCastException e)
        {
            throw new ClassCastException("implement the interface getDataFromMain");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Rahul","in oncreate");

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Rahul","in on activity created");
        checkList();
        ((ListView)getActivity().findViewById(R.id.listViewShowList)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dataBinder.sendClickedPosition(position);
            }
        });
        ((ListView)getActivity().findViewById(R.id.listViewShowList)).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //expenseDetailList.remove(position);
                dataBinder.sendDeleted(position);
                adapter.notifyDataSetChanged();
                checkList();
                return true;
            }
        });
        getActivity().findViewById(R.id.imageViewAddExpense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Rahul","clicked");
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new AddExpenseFragment(),"ADD_EXPENSE")
                        .addToBackStack(null)
                        .commit();
                Log.d("Rahul","clicked ");
            }
        });

    }
    public void checkList()
    {
        expenseDetailList = dataBinder.getDataIntoListFragment();
        if(expenseDetailList.size()==0)
        {
            getActivity().findViewById(R.id.textViewNoExpenses).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.listViewShowList).setVisibility(View.INVISIBLE);
        }
        else
        {
            getActivity().findViewById(R.id.listViewShowList).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.textViewNoExpenses).setVisibility(View.INVISIBLE);
            adapter = new ExpenseAdapter(getContext(),R.layout.expenselist,expenseDetailList);
            adapter.setNotifyOnChange(true);
            ListView lv = (ListView) getActivity().findViewById(R.id.listViewShowList);
            lv.setAdapter(adapter);
        }
    }

    public interface getDataFromMain
    {
        public ArrayList<ExpenseDetails> getDataIntoListFragment() ;
        public void sendClickedPosition(int pos);
        public void sendDeleted(int pos);
    }

}
