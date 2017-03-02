package com.example.sag14.application01;

/**
 * Created by sag14 on 30/11/16.
 */

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by Harshit on 10/14/2016.
 */
public class DialogHandler extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        PostActivity.timech=6;
        Calendar calendar = Calendar.getInstance();
        int hour= calendar.get(Calendar.HOUR_OF_DAY);
        int minute =calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog;
        TimeSettings timeSettings = new TimeSettings(getActivity());
        dialog= new TimePickerDialog(getActivity(), timeSettings, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));

        // dialog= new TimePickerDialog(getActivity(), timeSettings, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));


        return  dialog;



        // return super.onCreateDialog(savedInstanceState);
    }
}