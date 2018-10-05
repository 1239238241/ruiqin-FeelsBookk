package com.CMPUT301.ruiqin.FeelsBook;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
/**
 * learned this function from youtube videos
 * name: code in flow
 * published date: 2017-10-27
 * url: https://www.youtube.com/watch?v=33BFCdL0Di0
 * license: https://developer.android.com/license/w3c.txt
 */

public class DatePicker extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day =calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener) getActivity(), year, month,day );
    }
}
