package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.kliksembuh.ks.library.SearchSpesializationAdapter;
import com.kliksembuh.ks.models.Spesialization;

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

public class SearchSpesializationActivity extends AppCompatActivity implements ListView.OnItemClickListener{

    private List<Spesialization> mSpesialize;
    private SearchSpesializationAdapter searchAdapter;
    private ProgressDialog pDialog;
    private ListView listItem;
    private SearchView searchView;
    private String locationID;
    private String locationName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_spesialization);
        Bundle b = getIntent().getExtras();
        if(b!=null){
            locationID = b.getString("SubDistrictCD");
            locationName = b.getString("SubDistrictDescription");
        }

        searchView = (SearchView)findViewById(R.id.svSpesialization);
        mSpesialize = new ArrayList<>();
        listItem = (ListView)findViewById(R.id.listSpesialization);
        listItem.setTextFilterEnabled(true);
        listItem.setOnItemClickListener(this);

        new SearchSpesializationTask().execute();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object object = parent.getAdapter().getItem(position);
        Spesialization spesialisasi = (Spesialization)object;
        String idSpesialisasi = spesialisasi.getId();
        String nameSpesialisasi = spesialisasi.getName();
        Intent myIntent = new Intent(getApplicationContext(),HomeActivity.class);
        Bundle b = new Bundle();
        b.putString("facilityID", idSpesialisasi);
        b.putString("facilityName", nameSpesialisasi);
        b.putString("SubDistrictCD",locationID);
        b.putString("SubDistrictDescription",locationName);
        b.putString("tab","0");
        myIntent.putExtras(b);
        startActivity(myIntent);

    }

    private class SearchSpesializationTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SearchSpesializationActivity.this);
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
                    URL url = new URL("http://192.168.1.2/kliksembuhapi/api/HealthFacilities/GetHealthFacilities");
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
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(result!="") {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String healthFacilityID = jsonObject1.getString("HealthFacilityID");
                        String name = jsonObject1.getString("Name");
                        mSpesialize.add(new Spesialization(name, healthFacilityID));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            searchAdapter = new SearchSpesializationAdapter(getApplicationContext(), mSpesialize);


            listItem.setAdapter(searchAdapter);

        }
    }

}
