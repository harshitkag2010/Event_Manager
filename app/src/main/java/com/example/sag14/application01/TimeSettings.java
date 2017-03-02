package com.example.sag14.application01;

/**
 * Created by sag14 on 30/11/16.
 */

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by Harshit on 10/14/2016.
 */
public class TimeSettings implements TimePickerDialog.OnTimeSetListener {

    Context context;
    static  String st;

    public TimeSettings(Context context)
    {
        this.context=context;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Toast.makeText(context,"Selected time is hour :"+hourOfDay+" Minute: "+minute,Toast.LENGTH_LONG).show();
        // String st;
        st= hourOfDay+":"+minute ;



    }
}
