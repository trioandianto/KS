package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;

import com.kliksembuh.ks.library.InsuranceListAdapter;
import com.kliksembuh.ks.models.Insurance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InsuranceListActivity extends AppCompatActivity {

    private GridView gvInsurance;
    private InsuranceListAdapter insAdapter;
    private List<Insurance> mInsuranceList;
    private String userID;
    private String instID;
    private String instInsuranceID;
    private String instName;
    private String subDistrict;
    private String spesialisasi;
    private Drawable drawableInsurance[];
    private Context globalContext = null;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_list);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
            instID = b.getString("InstitutionID");
            instInsuranceID = b.getString("InstitutionInsuranceID");
            subDistrict = b.getString("subDistrict");
            spesialisasi = b.getString("facilityID");
            instName = b.getString("Name");
        }
        Toolbar newToolbar = (Toolbar)findViewById(R.id.toolbarInsurance);
        setSupportActionBar(newToolbar);
        newToolbar.setTitle(instName);
        setSupportActionBar(newToolbar);
        getWindow().setStatusBarColor(ContextCompat.getColor(InsuranceListActivity.this, R.color.colorPrimaryDark));

        gvInsurance = (GridView)findViewById(R.id.gv_InsuranceList);
        mInsuranceList = new ArrayList<>();
        new InsuranceListAsync(userID, subDistrict, spesialisasi).execute();


    }

    public Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }

    }

    public class InsuranceListAsync extends AsyncTask<String, Void, String>{
        private String mUserID;
        private String mSubdistrict;
        private String mSpesialisasi;
        InsuranceListAsync(String userID, String subDistrict, String spesialisasi){
            mUserID = userID;
            mSubdistrict = subDistrict;
            mSpesialisasi = spesialisasi;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(InsuranceListActivity.this);
            pDialog.setMessage("Mohon Menunggu...");
            pDialog.setCancelable(false);
            pDialog.show();
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

            if (pDialog.isShowing())
                pDialog.dismiss();

            if (success != ""){
                try {
                    JSONArray jsonArray = new JSONArray(success);

                    for (int i = 0; i < jsonArray.length(); i++ ){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("InstitutionName");
                        if(name.equals(instName)){
                            JSONArray jsonArray1 = jsonObject.getJSONArray("Insurances");
                            for(int j=0;j<jsonArray1.length();j++){
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                String image = jsonObject1.getString("ImageUrl");
                                Drawable imgDrawable = LoadImageFromWebOperations(image);
                                instID = jsonObject1.getString("InstitutionID");
                                instInsuranceID = jsonObject1.getString("InstitutionInsuranceID");
                                instName = jsonObject1.getString("Name");
                                mInsuranceList.add(new Insurance(instID,instInsuranceID, instName, imgDrawable));
                            }
                        }

                    }
                    insAdapter = new InsuranceListAdapter(getApplicationContext(),mInsuranceList);
                    gvInsurance.setAdapter(insAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }



        }

    }

}
