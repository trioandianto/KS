package com.kliksembuh.ks;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Trio Andianto on 1/23/2017.
 */
public class TabPelayanan extends Fragment{
    int year, month, day;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_tab_pelayanan, container, false);
//        Button btnspesial = (Button)rootView.findViewById(R.id.btndatespesialis);
//        btnspesial.setOnClickListener(this);
        return rootView;
    }


//    @Override
//    public void onClick(View v) {
//        Calendar c = Calendar.getInstance();
//        year = c.get(Calendar.YEAR);
//        month = c.get(Calendar.MONTH);
//        day = c.get(Calendar.DAY_OF_MONTH);
//        DatePickerDialog datePickerDialog;
////        datePickerDialog = new DatePickerDialog(Context context, TabPelayanan, year, month, day);
////        datePickerDialog.show();
//
//    }
//
//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//    }
//
//    @Override
//    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//
//    }
}