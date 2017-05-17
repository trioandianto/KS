package com.kliksembuh.ks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SettingVitalSignDetailActivity extends AppCompatActivity {
    private EditText etTekananDarahs;
    private EditText etTekananDarahD;
    private EditText etSuhuTubuh;
    private EditText etDenyutNadi;
    private EditText etPernafasan;
    private Button btnsimpanvitalsign;
    private String userID;
    private int vitalSignID;
    private PostVitalSignTask postAuthTask = null;
    private UpdateVitalSignTask updateAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_vital_sign_detail);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
        }

        Toolbar newToolbar = (Toolbar)findViewById(R.id.toolbarvitalsigndetail);
        setSupportActionBar(newToolbar);
        newToolbar.setTitle("Pengaturan Vital Sign");
        setSupportActionBar(newToolbar);
        getWindow().setStatusBarColor(ContextCompat.getColor(SettingVitalSignDetailActivity.this, R.color.colorPrimaryDark));

        etTekananDarahs = (EditText)findViewById(R.id.tv_ValueOfTekDarah);
        etTekananDarahD = (EditText)findViewById(R.id.tv_ValueOfTekDarah2);
        etSuhuTubuh = (EditText)findViewById(R.id.tv_ValueOfSuhu);
        etDenyutNadi = (EditText)findViewById(R.id.tv_ValueOfDenyutNadi);
        etPernafasan = (EditText)findViewById(R.id.tv_ValueOfPernafasan);
        btnsimpanvitalsign = (Button)findViewById(R.id.btnsimpanvitalsign);
        btnsimpanvitalsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptVitalSign();
            }
        });
        new GetVitalSignTask(userID).execute();
    }
    public void attemptVitalSign(){
        if (postAuthTask != null) {
            return;
        }
        etTekananDarahs.setError(null);
        etTekananDarahD.setError(null);
        etSuhuTubuh.setError(null);
        etDenyutNadi.setError(null);
        etPernafasan.setError(null);
        String tekananDarahD = etTekananDarahD.getText().toString();
        String tekananDarahS = etTekananDarahs.getText().toString();
        String suhuTubuh = etSuhuTubuh.getText().toString();
        String denyutNadi = etDenyutNadi.getText().toString();
        String pernafasan = etPernafasan.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(tekananDarahD)){
            etTekananDarahD.setError(getString(R.string.not_null));
            focusView = etTekananDarahD;
            cancel = true;

        }
        else if(TextUtils.isEmpty(tekananDarahS)){
            etTekananDarahs.setError(getString(R.string.not_null));
            focusView = etTekananDarahs;
            cancel = true;

        }
        else if(TextUtils.isEmpty(suhuTubuh)){
            etSuhuTubuh.setError(getString(R.string.not_null));
            focusView = etSuhuTubuh;
            cancel = true;

        }
        else if(TextUtils.isEmpty(denyutNadi)){
            etDenyutNadi.setError(getString(R.string.not_null));
            focusView = etDenyutNadi;
            cancel = true;

        }
        else if(TextUtils.isEmpty(pernafasan)){
            etPernafasan.setError(getString(R.string.not_null));
            focusView = etPernafasan;
            cancel = true;

        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else {
            postAuthTask = new PostVitalSignTask(tekananDarahD, tekananDarahS, suhuTubuh, denyutNadi, pernafasan,userID);
            postAuthTask.execute((String)null);
        }

    }
    private class GetVitalSignTask extends AsyncTask<String, Void, String> {
        private String uUserID;

        public GetVitalSignTask(String uUserID){
            this.uUserID = uUserID;
        }
        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/KlikSembuhAPI/api/VitalSigns/GetVitalSignByUserID/"+uUserID);
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setRequestProperty("Content-Type", "application/json");
                    urlc.connect();
                    int responseCode=urlc.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        BufferedReader in=new BufferedReader(
                                new InputStreamReader(
                                        urlc.getInputStream()));
                        StringBuffer sb = new StringBuffer("");
                        String line="";
                        while((line = in.readLine()) != null) {
                            sb.append(line);
                            break;
                        }
                        in.close();

                        return sb.toString();

                    }
                    else {
//                        Toast.makeText(getApplicationContext(), "Gagal membuat janji.", Toast.LENGTH_LONG).show();
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
            }else {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=""){
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    if (jsonArray.length()==0) {
                    }
                    else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String tekananDarahS = jsonObject.getString("BloodPressureSiastolic");
                            String tekananDarahD = jsonObject.getString("BloodPressureDiastolic");
                            String suhuTubuh = jsonObject.getString("BodyTemperature");
                            String denyutNadi = jsonObject.getString("Pulse");
                            String pernafasan = jsonObject.getString("Respiration");
                            vitalSignID = jsonObject.getInt("VitalSignID");
                            etTekananDarahs.setText(tekananDarahS);
                            etTekananDarahD.setText(tekananDarahD);
                            etSuhuTubuh.setText(suhuTubuh);
                            etDenyutNadi.setText(denyutNadi);
                            etPernafasan.setText(pernafasan);

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class UpdateVitalSignTask extends AsyncTask<String, Void, String>{
        int uVitalSignID;
        String tekananDarahD ;
        String tekananDarahS ;
        String suhuTubuh ;
        String denyutNadi;
        String pernafasan;
        String uUserID;

        public UpdateVitalSignTask(int uVitalSignID, String tekananDarahD, String tekananDarahS, String suhuTubuh, String denyutNadi, String pernafasan, String  uUserID){
            this.tekananDarahD = tekananDarahD;
            this.tekananDarahS = tekananDarahS;
            this.suhuTubuh = suhuTubuh;
            this.denyutNadi = denyutNadi;
            this.pernafasan = pernafasan;
            this.uUserID = uUserID;
            this.uVitalSignID = uVitalSignID;

        }
        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/UserAPI/api/VitalSigns/PutVitalSign/"+uVitalSignID);
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("VitalSignID", uVitalSignID);
                    jsonObject.put("UserID", uUserID);
                    jsonObject.put("BloodPressureSiastolic", tekananDarahS);
                    jsonObject.put("BloodPressureDiastolic", tekananDarahD);
                    jsonObject.put("BodyTemperature", suhuTubuh);
                    jsonObject.put("Pulse", denyutNadi);
                    jsonObject.put("Respiration", pernafasan);
                    urlc.setConnectTimeout(3000);
                    urlc.setRequestProperty("Content-Type","application/json");
                    urlc.setRequestMethod("PUT");
                    urlc.setDoInput(true);
                    urlc.setDoOutput(true);
                    DataOutputStream os = new DataOutputStream(urlc.getOutputStream());

                    os.writeBytes(jsonObject.toString());


                    int responseCode=urlc.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        BufferedReader in=new BufferedReader(
                                new InputStreamReader(
                                        urlc.getInputStream()));
                        StringBuffer sb = new StringBuffer("");
                        String line="";
                        while((line = in.readLine()) != null) {
                            sb.append(line);
                            break;
                        }
                        in.close();
                        os.flush();
                        os.close();

                        return sb.toString();

                    }
                    else {
//                        Toast.makeText(getApplicationContext(), "Gagal membuat janji.", Toast.LENGTH_LONG).show();
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
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "";
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }else {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != "") {
                Toast.makeText(getApplicationContext(), "Data Berhasil di Simpan.", Toast.LENGTH_LONG).show();

            }
            else{
                postAuthTask = null;
            }
        }
    }
    private class PostVitalSignTask extends AsyncTask<String, Void, String>{
        String tekananDarahD ;
        String tekananDarahS ;
        String suhuTubuh ;
        String denyutNadi;
        String pernafasan;
        String uUserID;

        public PostVitalSignTask(String tekananDarahD, String tekananDarahS, String suhuTubuh, String denyutNadi, String pernafasan, String  uUserID){
            this.tekananDarahD = tekananDarahD;
            this.tekananDarahS = tekananDarahS;
            this.suhuTubuh = suhuTubuh;
            this.denyutNadi = denyutNadi;
            this.pernafasan = pernafasan;
            this.uUserID = uUserID;

        }
        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/KlikSembuhAPI/api/VitalSigns/PostVitalSign");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("UserID", uUserID);
                    jsonObject.put("BloodPressureSiastolic", tekananDarahS);
                    jsonObject.put("BloodPressureDiastolic", tekananDarahD);
                    jsonObject.put("BodyTemperature", suhuTubuh);
                    jsonObject.put("Pulse", denyutNadi);
                    jsonObject.put("Respiration", pernafasan);
                    urlc.setConnectTimeout(3000);
                    urlc.setRequestProperty("Content-Type","application/json");
                    urlc.setRequestMethod("POST");
                    urlc.setDoInput(true);
                    urlc.setDoOutput(true);
                    DataOutputStream os = new DataOutputStream(urlc.getOutputStream());

                    os.writeBytes(jsonObject.toString());


                    int responseCode=urlc.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        BufferedReader in=new BufferedReader(
                                new InputStreamReader(
                                        urlc.getInputStream()));
                        StringBuffer sb = new StringBuffer("");
                        String line="";
                        while((line = in.readLine()) != null) {
                            sb.append(line);
                            break;
                        }
                        in.close();
                        os.flush();
                        os.close();

                        return sb.toString();

                    }
                    else {
//                        Toast.makeText(getApplicationContext(), "Gagal membuat janji.", Toast.LENGTH_LONG).show();
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
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "";
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }else {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != "") {

            }
            else{
                postAuthTask = null;
            }
        }
    }
}
