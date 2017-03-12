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
public class TabSpesialisasiActivity extends Fragment{
    private AutoCompleteTextView location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.activity_tab_spesialis, container, false);
        location = (AutoCompleteTextView)rootView.findViewById(R.id.tvsearchspesialis) ;
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(),SearchLocationActivity.class);
                startActivity(myIntent);
            }
        });

        //Action Button
        Button btnsearch = (Button)rootView.findViewById(R.id.btnsearchspesialisasi);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(),HospitalList.class);
                startActivity(myIntent);
            }
        });



        return rootView;
    }
}
