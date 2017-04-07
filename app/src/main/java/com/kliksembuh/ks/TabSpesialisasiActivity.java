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
public class TabSpesialisasiActivity extends Fragment implements View.OnClickListener{

    private String userID;
    private String spesial;
    private String locasi;
    private String spesialID;
    private String lokasiID;
    private AutoCompleteTextView location;
    private AutoCompleteTextView spesialize;

    public String getSpesial() {
        return spesial;
    }
    public void setSpesial(String spesial) {
        this.spesial = spesial;
    }
    public String getLocasi() {
        return locasi;
    }
    public void setLocasi(String locasi) {
        this.locasi = locasi;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getSpesialID() {
        return spesialID;
    }
    public void setSpesialID(String spesialID) {
        this.spesialID = spesialID;
    }
    public String getLokasiID() {
        return lokasiID;
    }
    public void setLokasiID(String lokasiID) {
        this.lokasiID = lokasiID;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.activity_tab_spesialis, container, false);
        location = (AutoCompleteTextView)rootView.findViewById(R.id.tvsearchlocationspesialis) ;
        location.setOnClickListener(this);
        location.setText(locasi);
        spesialize = (AutoCompleteTextView)rootView.findViewById(R.id.tvsearchtypespesialis);
        spesialize.setText(spesial);
        spesialize.setOnClickListener(this);

        //Action Button
        Button btnsearch = (Button)rootView.findViewById(R.id.btnsearchspesialisasi);
        btnsearch.setOnClickListener(this);



        return rootView;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i==R.id.tvsearchlocationspesialis){
            Intent myIntent = new Intent(getActivity(),SearchLocationActivity.class);
            Bundle b = new Bundle();
            b.putString("userID", userID);
            b.putString("lokasiID",lokasiID);
            b.putString("spesialisasi",spesialID);
            b.putString("spesialisasiName",spesial);
            b.putString("locationName",locasi);
            startActivity(myIntent);
        }
        else if(i==R.id.btnsearchspesialisasi){
            Intent myIntent = new Intent(getActivity(),HospitalList.class);
            Bundle b = new Bundle();
            b.putString("userID", userID);
            b.putString("subDistrict",lokasiID);
            b.putString("spesialisasi",spesialID);//Your id
            //.putExtra("userID",userID);
            myIntent.putExtras(b);
            startActivity(myIntent);
        }
        else if(i == R.id.tvsearchtypespesialis){
            Intent myIntent = new Intent(getActivity(),SearchSpesializationActivity.class);
            Bundle b = new Bundle();
            b.putString("userID", userID);
            b.putString("lokasiID",lokasiID);
            b.putString("spesialisasi",spesialID);
            b.putString("spesialisasiName",spesial);
            b.putString("locationName",locasi);
            startActivity(myIntent);

        }
        else{

        }

    }
}
