package com.kliksembuh.ks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Trio Andianto on 1/23/2017.
 */
public class TabSpesialisasiActivity extends Fragment implements View.OnClickListener{


    private Context context;
    private String userID;
    private String spesial;
    private String locasi;
    private String spesialID;
    private String lokasiID;
    private TextView location;
    private TextView spesialize;
    View focusView = null;
    public TabSpesialisasiActivity(){

    }

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
        location = (TextView) rootView.findViewById(R.id.tvsearchlocationspesialis) ;
        location.setOnClickListener(this);
        location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                location.setTextColor(getResources().getColor(R.color.black));
                location.setError(null);
                location.clearFocus();

            }
        });

        location.setText(locasi);
        spesialize = (TextView) rootView.findViewById(R.id.tvspesi);
        spesialize.setOnClickListener(this);
        spesialize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                spesialize.setTextColor(getResources().getColor(R.color.black));
                spesialize.setError(null);
                spesialize.clearFocus();
            }
        });


        //Action Button
        Button btnsearch = (Button)rootView.findViewById(R.id.btnsearchspesialisasi);
        btnsearch.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                lokasiID = data.getStringExtra("SubDistrictCD");
                locasi = data.getStringExtra("SubDistrictDescription");
                spesialID = data.getStringExtra("facilityID");
                spesial = data.getStringExtra("facilityName");
            }
        }
        if (requestCode == 4) {
            if(resultCode == RESULT_OK) {
                lokasiID = data.getStringExtra("SubDistrictCD");
                locasi = data.getStringExtra("SubDistrictDescription");
                location.setText(locasi);
            }
        }
        if (requestCode == 5) {
            if(resultCode == RESULT_OK) {
                spesialID = data.getStringExtra("facilityID");
                spesial = data.getStringExtra("facilityName");
                spesialize.setText(spesial);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i==R.id.tvsearchlocationspesialis){
//            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            Intent myIntent = new Intent(getActivity(),SearchLocationActivity.class);
//            Bundle b = new Bundle();
//            b.putString("userID", userID);
//            b.putString("SubDistrictCD",lokasiID);
//            b.putString("SubDistrictDescription",locasi);
//            b.putString("facilityID",spesialID);
//            b.putString("facilityName",spesial);
//            myIntent.putExtras(b);
            startActivityForResult(myIntent,4);

        }
        else if(i==R.id.btnsearchspesialisasi){
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
                startActivityForResult(myIntent,1);
            }else{
                if(lokasiID==null){
                    location.requestFocus();
                    location.setError("Silahkan Pilih Lokasi.");
                    focusView = location;

                }else{
                    spesialize.requestFocus();
                    spesialize.setError("Silahkan Pilih Spesialisasi.");
                    focusView = spesialize;
                }
            }

        }
        else if (i==R.id.tvspesi){
//            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//
            Intent myIntent = new Intent(getActivity(),SearchSpesializationActivity.class);
//            Bundle b = new Bundle();
//            b.putString("userID", userID);
//            b.putString("SubDistrictCD",lokasiID);
//            b.putString("facilityID",spesialID);
//            b.putString("facilityName",spesial);
//            b.putString("SubDistrictDescription",locasi);
//            myIntent.putExtras(b);
            startActivityForResult(myIntent,5);


        }else{

        }

    }

}
