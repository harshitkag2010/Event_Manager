package com.example.sag14.application01;

/**
 * Created by sag14 on 30/11/16.
 */

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.Toast;

/**
 * Created by Harshit on 10/18/2016.
 */
public class DateSettings1 implements DatePickerDialog.OnDateSetListener {


    Context context;
    static String  st2;
    public DateSettings1(Context context)
    {
        this.context=context;

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

        monthOfYear=monthOfYear+1;
        Toast.makeText(context,"Selected Date :"+dayOfMonth+" / "+monthOfYear+" / "+year,Toast.LENGTH_LONG).show();

        st2= dayOfMonth+"/"+monthOfYear+"/"+year ;
    }



}