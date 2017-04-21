package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.AutoCompleteTextView;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class AppointmentDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ProgressDialog pDialog;
    private AutoCompleteTextView tvName;
    private AutoCompleteTextView tvDate;
    private AutoCompleteTextView tvTime;
    private String alamat;
    private String namaRumahSakit;
    private SupportMapFragment mapFragment;
    private String transaksiID;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        Bundle b = getIntent().getExtras();
        if(b!=null){
            userID = b.getString("userID");
            transaksiID = b.getString("transaksiID");
        }

        tvName = (AutoCompleteTextView)findViewById(R.id.tvNameDetailAppointment);
        tvDate = (AutoCompleteTextView)findViewById(R.id.tvDateDetailAppointment);
        tvTime = (AutoCompleteTextView) findViewById(R.id.tvTimeDetailAppointment);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        new AppointmentAsync(transaksiID).execute();

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
    public class AppointmentAsync extends AsyncTask<String, Void, String> {
        private String mTransaksi;
        private String mSpesialisai;
        AppointmentAsync(String transaksi) {
            mTransaksi = transaksi;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AppointmentDetailActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://basajans/KlikSembuhAPI/api/Transactions/GetDetailAppointmentTransaction?transactionId=2");
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
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (success!="") {
                try {
                    JSONArray jsonArray = new JSONArray(success);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONArray personalMedical = jsonObject.getJSONArray("TransactionMedicalPerson");
                        for(int j=0;j<personalMedical.length();j++){
                            JSONObject jsonObject1 = personalMedical.getJSONObject(j);
                            String name = jsonObject1.getString("Name");
                            tvName.setText(name);
                            JSONArray institute = jsonObject1.getJSONArray("Institute");
                            for (int k=0;k<institute.length();k++){
                                JSONObject jsonObject2 = institute.getJSONObject(k);
                                alamat = jsonObject2.getString("InstitutionAddress");
                                namaRumahSakit = jsonObject2.getString("InstitutionName");
                            }
                        }
                        String startTime = jsonObject.getString("ScheduleDate");
                        if(startTime!=null){
                            SimpleDateFormat schdlDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date scheduleDate = schdlDateFormatter.parse(startTime);
                            SimpleDateFormat newSchdlFormat = new SimpleDateFormat("dd-MMM-yyyy");
                            String schdlDatePars = newSchdlFormat.format(scheduleDate);
                            tvDate.setText(schdlDatePars);

                        }


                        String jam = jsonObject.getString("EstimationTime");
                        tvTime.setText(jam);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mapFragment.getMapAsync(AppointmentDetailActivity.this);



            } else {
                //:TODO
            }
        }
        @Override
        protected void onCancelled() {

        }
    }
}
