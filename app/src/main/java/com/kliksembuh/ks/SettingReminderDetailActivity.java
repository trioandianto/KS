package com.kliksembuh.ks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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

public class SettingReminderDetailActivity extends AppCompatActivity {
    private Button btnSimpan;
    private Spinner spnType, spnDosage, spnTImings,spnDays, spnDuration;
    private String userID;
    private AutoCompleteTextView atTittle;
    private ReminderDetailAsync mAuthTask = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_reminder_detail);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
        }

        atTittle = (AutoCompleteTextView)findViewById(R.id.tvEditTitleRemidner);
        btnSimpan = (Button)findViewById(R.id.btnSimpanReminder);
        spnType = (Spinner)findViewById(R.id.spnReminderType);
        spnDosage = (Spinner)findViewById(R.id.spnDosage);
        spnTImings = (Spinner)findViewById(R.id.spnTimings);
        spnDays = (Spinner)findViewById(R.id.spnDays);
        spnDuration = (Spinner)findViewById(R.id.spnDuration);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSubmit();
            }
        });
    }
    private void attemptSubmit(){
        if (mAuthTask != null) {
            return;
        }

        String tittle = atTittle.getText().toString();
        String dosage = spnDosage.getSelectedItem().toString();
        String timings = spnTImings.getSelectedItem().toString();
        String type = spnType.getSelectedItem().toString();
        String days = spnDays.getSelectedItem().toString();
        String duration = spnDuration.getSelectedItem().toString();
        String medicalId = "1";
        String reminderID = "1";

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(tittle)){
            atTittle.setError(getString(R.string.not_null));
            focusView = atTittle;
            cancel = true;

        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

                mAuthTask = new ReminderDetailAsync(userID,dosage, tittle, timings,duration, medicalId,reminderID ,days);
                mAuthTask.execute((String) null);
            }



    }

    public class ReminderDetailAsync extends AsyncTask<String, Void, String>{
        private String pUserID;
        private String pDosage;
        private String pTitle;
        private String pTimings;
        private String pDuration;
        private String pMedicineID;
        private String pReminderTypeID;
        private String pDays;



        public ReminderDetailAsync(String pUserID, String pDosage, String pTitle,String pTimings, String pDuration, String pMedicineID,
                                   String pReminderTypeID, String pDays){
            this.pUserID=pUserID;
            this.pDosage = pDosage;
            this.pTimings = pTimings;
            this.pTitle = pTitle;
            this.pDuration = pDuration;
            this.pMedicineID = pMedicineID;
            this.pReminderTypeID = pReminderTypeID;
            this.pDays = pDays;
        }

        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/KlikSembuhAPI/api/MedicationReminders/PostMedicationReminder");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Dosage", pDosage);
                    jsonObject.put("Title", pTitle);
                    jsonObject.put("Timings", pTimings);
                    jsonObject.put("Duration", pDuration);
                    jsonObject.put("CreatedByID", pUserID);
                    jsonObject.put("MedicineID", pMedicineID);
                    jsonObject.put("ReminderTypeID", pReminderTypeID);
                    jsonObject.put("Days", pDays);
                    urlc.setConnectTimeout(3000);
                    urlc.setRequestProperty("Content-Type","application/json");
                    urlc.setRequestMethod("POST");
                    urlc.setDoInput(true);
                    urlc.setDoOutput(true);
                    DataOutputStream os = new DataOutputStream(urlc.getOutputStream());

                    os.writeBytes(jsonObject.toString());


                    int responseCode=urlc.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_CREATED) {

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
            if(s!=""){
                Toast.makeText(getApplicationContext(), "Simpan Data Berhasil", Toast.LENGTH_LONG).show();
            }
        }
    }
}
