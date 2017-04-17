package com.kliksembuh.ks;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ListMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button btnList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_map);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        toolbar.setTitle("Jumat, 24 Februari 2017");
        btnList = (Button) findViewById(R.id.btnlist);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ListMapActivity.this,HospitalList.class);
                startActivity(myIntent);
            }
        });


//        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"),Locale.getDefault());
//        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//        String currentDate = df.format(c.getTime());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng bogor = new LatLng(-6.595038, 106.816635);
        mMap.addMarker(new MarkerOptions().position(bogor).title("RS PMI Bogor"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bogor,16.0f));
        mMap.setMapType(mMap.MAP_TYPE_TERRAIN);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
