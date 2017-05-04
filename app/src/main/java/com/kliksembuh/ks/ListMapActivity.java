package com.kliksembuh.ks;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class ListMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button btnList;
    private String alamat;
    private ArrayList<String> namaRumahSakit;
    private String subDistrict;
    private String subDistricDescription;
    private String facilityName;
    private String spesialisasi;
    private String userID;
    private SupportMapFragment mapFragment;
    private ArrayList<String> alamat1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_map);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
            subDistrict = b.getString("subDistrict");
            subDistricDescription = b.getString("SubDistrictDescription");
            spesialisasi = b.getString("facilityID");
            facilityName = b.getString("facilityName");
        }
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

        Toolbar newToolbar = (Toolbar)findViewById(R.id.toolbarListMaps);
        setSupportActionBar(newToolbar);
        newToolbar.setTitle("Peta Rumah Sakit");
        setSupportActionBar(newToolbar);
        getWindow().setStatusBarColor(ContextCompat.getColor(ListMapActivity.this, R.color.colorPrimaryDark));

        new HospitalListAsync(subDistrict,spesialisasi).execute();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String adrs;
        String name;
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.hospitalbuilding);
        for (int j=0;j<alamat1.size();j++){
            double latitude = 0;
            double longitude = 0;
            adrs = alamat1.get(j);
            name = namaRumahSakit.get(j);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocationName(adrs,1);
                int i = 1;
                int lastIndex = 1;
                while (addresses.size()==0 && i < 10) {
                    lastIndex = adrs.lastIndexOf(",");
                    if(lastIndex == -1){
                        break;
                    }
                    adrs = adrs.substring(0, lastIndex);
                    addresses = geocoder.getFromLocationName(adrs, 1);
                    i++;
                }
                if(addresses.size() > 0) {
                    latitude= addresses.get(0).getLatitude();
                    longitude= addresses.get(0).getLongitude();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            LatLng bogor = new LatLng(latitude, longitude);
//            createMarker(bogor,namaRumahSakit,icon);
            mMap.addMarker(new MarkerOptions().position(bogor).title(name).icon(icon));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bogor, 16.0f));
            mMap.setMapType(mMap.MAP_TYPE_TERRAIN);
            mMap.getUiSettings().setZoomControlsEnabled(true);


        }

    }
    public class HospitalListAsync extends AsyncTask<String, Void, String> {
        private String mSubdistrict;
        private String mSpesialisai;
        HospitalListAsync(String subDistrict, String spesialisasi) {
            mSubdistrict = subDistrict;
            mSpesialisai = spesialisasi;
        }

        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://basajans/kliksembuhapi/api/Institutions/SearchInstitutionFromAfterLogin?subDistrict=" + mSubdistrict + "&facility=" + mSpesialisai);
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setRequestProperty("Content-Type", "application/json");
                    urlc.connect();
                    int responseCode = urlc.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                        urlc.getInputStream()));
                        StringBuffer sb = new StringBuffer("");
                        String line = "";
                        while ((line = in.readLine()) != null) {
                            sb.append(line);
                            break;
                        }
                        in.close();
                        // Drawable image1 = LoadImageFromWebOperations(image);
                        return sb.toString();
                    } else {
                        return "";

                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    return "";
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "";
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }
            else {
                return "";
            }
        }


        @Override
        protected void onPostExecute(final String success) {
            String nama;

            if (success!="") {
                try {
                    JSONArray jsonArray = new JSONArray(success);
                    alamat1 = new ArrayList<>();
                    namaRumahSakit = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        nama = jsonObject.getString("InstitutionName");
                        namaRumahSakit.add(nama);
                        alamat = jsonObject.getString("InstitutionAddress");
                        alamat1.add(alamat);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mapFragment.getMapAsync(ListMapActivity.this);


            } else {
                //:TODO
            }
        }
        @Override
        protected void onCancelled() {

        }
    }
}
