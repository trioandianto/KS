package com.kliksembuh.ks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

/**
 * Created by Trio Andianto on 1/23/2017.
 */
public class TabPelayananActivity extends Fragment{
    int year, month, day;
    private AutoCompleteTextView location;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    private String userID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_tab_pelayanan, container, false);
        location = (AutoCompleteTextView) rootView.findViewById(R.id.tvsearchpelayanan);;
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(),SearchLocationActivity.class);
                startActivity(myIntent);
            }
        });

        //Action Button
        Button btnsearch = (Button)rootView.findViewById(R.id.btnsearchpelayanan);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(),DoctorListActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
        return rootView;
    }


//    @Override
//    public void onClick(View v) {
//        Calendar c = Calendar.getInstance();
//        year = c.get(Calendar.YEAR);
//        month = c.get(Calendar.MONTH);
//        day = c.get(Calendar.DAY_OF_MONTH);
//        DatePickerDialog datePickerDialog;
////        datePickerDialog = new DatePickerDialog(Context context, TabPelayananActivity, year, month, day);
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