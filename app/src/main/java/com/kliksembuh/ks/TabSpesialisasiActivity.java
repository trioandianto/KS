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

    private String userID;
    private String spesial;
    private String locasi;
    private String spesialID;
    private String lokasiID;
    private AutoCompleteTextView location;
    private AutoCompleteTextView spesialize;
    View focusView = null;

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
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(),SearchLocationActivity.class);
                Bundle b = new Bundle();
                b.putString("userID", userID);
                b.putString("SubDistrictCD",lokasiID);
                b.putString("SubDistrictDescription",locasi);
                b.putString("facilityID",spesialID);
                b.putString("facilityName",spesial);
                myIntent.putExtras(b);
                startActivity(myIntent);
            }
        });
        location.setText(locasi);
        spesialize = (AutoCompleteTextView)rootView.findViewById(R.id.tvsearchtypespesialis);
        spesialize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(),SearchSpesializationActivity.class);
                Bundle b = new Bundle();
                b.putString("userID", userID);
                b.putString("SubDistrictCD",lokasiID);
                b.putString("facilityID",spesialID);
                b.putString("facilityName",spesial);
                b.putString("SubDistrictDescription",locasi);
                myIntent.putExtras(b);
                startActivity(myIntent);
            }
        });
        spesialize.setText(spesial);

        //Action Button
        Button btnsearch = (Button)rootView.findViewById(R.id.btnsearchspesialisasi);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lokasiID!=null && spesialID!=null){
                    Intent myIntent = new Intent(getActivity(),HospitalList.class);
                    Bundle b = new Bundle();
                    b.putString("userID", userID);
                    b.putString("subDistrict",lokasiID);
                    b.putString("facilityID",spesialID);
                    b.putString("facilityName",spesial);
                    b.putString("SubDistrictDescription",locasi);//Your id
                    //.putExtra("userID",userID);
                    myIntent.putExtras(b);
                    startActivity(myIntent);
                }else{
                    if(lokasiID==null){
                        location.setError("Silahkan Pilih Lokasi.");
                        focusView = location;

                    }else{
                        spesialize.setError("Silahkan Pilih Spesialisasi.");
                        focusView = spesialize;
                    }
                }
            }
        });



        return rootView;
    }
}
