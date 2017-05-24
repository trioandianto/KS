package com.kliksembuh.ks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.kliksembuh.ks.library.FaciltyListAdapter;
import com.kliksembuh.ks.models.Facilty;

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

public class FaciltyInstitutionActivity extends AppCompatActivity {
    private String userID;
    private FaciltyListAdapter fAdapter;
    private String instID;
    private String instInsuranceID;
    private String instName;
    private String subDistrict;
    private String spesialisasi;
    private ListView lvFacility;
    private List<Facilty> faciltyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilty_institution);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
            instID = b.getString("InstitutionID");
            instInsuranceID = b.getString("InstitutionInsuranceID");
            subDistrict = b.getString("subDistrict");
            spesialisasi = b.getString("facilityID");
            instName = b.getString("Name");
        }
        Toolbar newToolbar = (Toolbar)findViewById(R.id.toolbarFacilty);
        setSupportActionBar(newToolbar);
        newToolbar.setTitle("Spesialisasi "+instName);
        setSupportActionBar(newToolbar);
        getWindow().setStatusBarColor(ContextCompat.getColor(FaciltyInstitutionActivity.this, R.color.colorPrimaryDark));

        lvFacility = (ListView) findViewById(R.id.listFacilty);
        faciltyList = new ArrayList<>();

        new FaciltyListAsync(userID, subDistrict, spesialisasi).execute();
    }
    public class FaciltyListAsync extends AsyncTask<String, Void, String> {
        private String mUserID;
        private String mSubdistrict;
        private String mSpesialisasi;
        FaciltyListAsync(String userID, String subDistrict, String spesialisasi){
            mUserID = userID;
            mSubdistrict = subDistrict;
            mSpesialisasi = spesialisasi;
        }

        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()){
                try {
                    URL url = new URL("http://cloud.abyor.com:11080/kliksembuhapi/api/Institutions/SearchInstitutionFromAfterLogin?subDistrict=" + mSubdistrict + "&facility=" + mSpesialisasi);
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setRequestProperty("Content-Type", "application/json");
                    urlc.connect();
                    int responseCode = urlc.getResponseCode();
                    if (responseCode == urlc.getResponseCode()){
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

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return "";
                } catch (IOException e) {
                    e.printStackTrace();
                    return "";
                }
            }

            return "";
        }

        @Override
        protected void onPostExecute(String success) {
            super.onPostExecute(success);
            if (success != ""){
                try {
                    JSONArray jsonArray = new JSONArray(success);

                    for (int i = 0; i < jsonArray.length(); i++ ){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("InstitutionName");
                        if(name.equals(instName)){
                            JSONArray jsonArray1 = jsonObject.getJSONArray("HealthFacilities");
                            for (int j=0;j<jsonArray1.length();j++){
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                String facility = jsonObject1.getString("Name");
                                int id = jsonObject1.getInt("HealthFacilityID");
                                faciltyList.add(new Facilty(id,facility));
                            }
                        }

                    }
                    fAdapter = new FaciltyListAdapter(getApplication(), faciltyList);
                    lvFacility.setAdapter(fAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }



        }

    }
}
