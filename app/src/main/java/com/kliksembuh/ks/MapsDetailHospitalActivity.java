package com.kliksembuh.ks;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsDetailHospitalActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private String namaRumahSakit;
    private String alamat = "";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_detail_hospital);
        Bundle b = getIntent().getExtras();
        if(b!=null){
            namaRumahSakit = b.getString("namaRumahSakit");
            alamat = b.getString("alamat");
        }
//        alamat = "Jakarta";

        Toolbar newToolbar = (Toolbar)findViewById(R.id.toolbarMapsDetails);
        setSupportActionBar(newToolbar);
        newToolbar.setTitle("Peta "+namaRumahSakit);
        setSupportActionBar(newToolbar);
        getWindow().setStatusBarColor(ContextCompat.getColor(MapsDetailHospitalActivity.this, R.color.colorPrimaryDark));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapDetailHospital);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.hospitalbuilding);
        double latitude = 0;
        double longitude = 0;



        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(alamat,1);
            int i = 1;
            int lastIndex = 1;
            while (addresses.size()==0 && i < 10) {
                lastIndex = alamat.lastIndexOf(",");
                if(lastIndex == -1){
                    break;
                }
                alamat = alamat.substring(0, lastIndex);
                addresses = geocoder.getFromLocationName(alamat, 1);
                i++;
            }
            if(addresses.size() > 0) {
                latitude= addresses.get(0).getLatitude();
                longitude= addresses.get(0).getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




        // Add a marker in Sydney and move the camera
        LatLng bogor = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(bogor).title(namaRumahSakit).icon(icon));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bogor,16.0f));
        mMap.setMapType(mMap.MAP_TYPE_TERRAIN);
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }
}
