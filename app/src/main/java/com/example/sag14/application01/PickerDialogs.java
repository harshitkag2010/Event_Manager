package com.example.sag14.application01;

/**
 * Created by sag14 on 30/11/16.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by Harshit on 10/14/2016.
 */
public class PickerDialogs extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        PostActivity.datech=6;
        DateSettings dateSettings= new DateSettings(getActivity());

        Calendar calendar = Calendar.getInstance();
        int year= calendar.get(Calendar.YEAR);
        int month= calendar.get(Calendar.MONTH);
        int day= calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog;
        month=month+1;
        dialog= new DatePickerDialog(getActivity(),dateSettings, year, month, day);




        return dialog ;
    }
}
