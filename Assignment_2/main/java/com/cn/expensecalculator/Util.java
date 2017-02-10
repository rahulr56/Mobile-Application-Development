package com.cn.expensecalculator;

import android.widget.DatePicker;
import android.support.v7.app.AppCompatActivity;
import 	android.content.Context;
import android.widget.Toast;

/**
 * Created by LibrarayGuest on 9/14/2016.
 */
public class Util {
    public static void showToast(Context baseContext, String str)
    {
        Toast.makeText(baseContext,str,Toast.LENGTH_LONG);
        //DatePicker datePicker = new DatePicker();
    }
}
