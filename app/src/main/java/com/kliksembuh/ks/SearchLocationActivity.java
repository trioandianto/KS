package com.kliksembuh.ks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kliksembuh.ks.library.SearchAdapter;
import com.kliksembuh.ks.models.Location;

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

import javax.net.ssl.HttpsURLConnection;

public class SearchLocationActivity extends AppCompatActivity implements ListView.OnItemClickListener{

    private List<Location> mLocation;
    private SearchAdapter searchAdapter;
    private ProgressDialog pDialog;
    private android.support.v7.widget.SearchView searchView;
    private ListView listItem;
    private String idLocation;
    private String nameLocation;
    private String nameSpesialisasi;
    private String idSpesialisasi;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        getWindow().setStatusBarColor(Color.BLACK);
        Bundle b = getIntent().getExtras();
        if(b!=null){
            nameSpesialisasi = b.getString("facilityName");
            idSpesialisasi = b.getString("facilityID");
        }

        //location = (AutoCompleteTextView)findViewById(R.id.tvlocation);
        searchView = (android.support.v7.widget.SearchView)findViewById(R.id.svLocation);
        searchView.onActionViewExpanded();
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.setQueryHint("Cari lokasi klinik, rumah sakit terdekat Anda");
        searchView.requestFocusFromTouch();

        mLocation = new ArrayList<>();
        listItem = (ListView) findViewById(R.id.listlocation);
        listItem.setTextFilterEnabled(true);
        listItem.setOnItemClickListener(this);
//        location =(AutoCompleteTextView) searchView.findViewById(R.id.tvlocation);
//        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                listAdapter.getFilter().filter( newText );
//                return false;
//            }
//        } );

        // Getting JSON Array node

        new GetContacts().execute();
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(searchAdapter!=null){
                    searchAdapter.filter(newText);
                }
                return false;
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object object = parent.getAdapter().getItem(position);
        Location location = (Location) object;
        idLocation = location.getId();
        nameLocation = location.getName();
        Intent myIntent = new Intent();
        Bundle b = new Bundle();
        b.putString("SubDistrictCD", idLocation);
        b.putString("SubDistrictDescription", nameLocation);
        b.putString("tab","0");
        myIntent.putExtras(b);
        setResult(Activity.RESULT_OK, myIntent);
        finish();


    }
    private class GetContacts extends AsyncTask<String, Void, String> {
        private Context context;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Showing progress dialog
//            pDialog = new ProgressDialog(SearchLocationActivity.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://cloud.abyor.com:11080/kliksembuhapi/api/SubDistricts/GetSubDistrict");
//                    URL url = new URL("http://cloud.basajans.com:8868/BS.HealthCare.Application/api/SubDistricts/GetSubDistrict");
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
                }

            } else {
                return "";
            }
        }
        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
//            if (pDialog.isShowing())
//                pDialog.dismiss();
            if(result!=""){
                try {
                    JSONArray jsonArray = new JSONArray(result);
//                    Iterator<String> keys = jsonObj.keys();
//                    jsonObj.keys();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String subDistrictID = jsonObject1.getString("SubDistrictID");
                        String subDistrictCD = jsonObject1.getString("SubDistrictCD");
                        String subDistrictDescription = jsonObject1.getString("SubDistrictDescription");
                        mLocation.add(new Location(subDistrictDescription,subDistrictID,subDistrictCD));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//            adapter = new SimpleAdapter(
//                    SearchLocationActivity.this,formList,
//                    R.layout.list_item, new String[]{"name", "code"}, new int[]{R.id.name,
//                    R.id.email});
            searchAdapter = new SearchAdapter(getApplicationContext(), mLocation);
//            listAdapter = new ArrayAdapter<String>(  SearchLocationActivity.this,
//                    android.R.layout.simple_list_item_1, da);
//            listItem.setAdapter( listAdapter );


            listItem.setAdapter(searchAdapter);

        }
    }
}
