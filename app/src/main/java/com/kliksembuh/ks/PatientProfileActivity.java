package com.kliksembuh.ks;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.kliksembuh.ks.library.PatientListAdapter;
import com.kliksembuh.ks.models.Patient;

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

public class PatientProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lvPatient;
    private List<Patient> mPatientList;
    private PatientListAdapter pAdapter;
    private Button btnAddPatient;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
        }
        lvPatient = (ListView) findViewById(R.id.listview_patientProfile);
        btnAddPatient = (Button)findViewById(R.id.btnAddNewPatientProf);
        btnAddPatient.setOnClickListener(this);
        mPatientList = new ArrayList<>();


        lvPatient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = parent.getAdapter().getItem(position);
                Patient patient = (Patient) object;
                int personalInfoID = patient.getPat_id();
                String fName = patient.getPatFirstName();
                String lName = patient.getPatLastName();
                String gender = patient.getPatGender();
                String cellPhoneNbr = patient.getPatCellPhoneNbr();
                String bPJSNbr = patient.getPatBPJSNbr();
                String address = patient.getPatAddress();
                String closeRelativeName = patient.getPatCloseRelativeName();
                String closeRelativePhoneNbr = patient.getPatCloseRelativePhoneNbr();
                String birthOfDate = patient.getPatBirthday();
                String relativeStatus = patient.getPatRelativeStatus();
                Intent myIntent = new Intent(getApplicationContext(),PatientFormActivity.class);
                Bundle b = new Bundle();
                b.putInt("personalInfoID", personalInfoID);
                b.putString("fName", fName);//Your id
                b.putString("lName", lName);
                b.putString("userID",userID);
                b.putString("gender", gender);
                b.putString("cellPhoneNbr", cellPhoneNbr);
                b.putString("bPJSNbr", bPJSNbr);
                b.putString("address", address);
                b.putString("closeRelativeName", closeRelativeName);
                b.putString("closeRelativePhoneNbr", closeRelativePhoneNbr);
                b.putString("birthOfDate", birthOfDate);
                b.putString("relativeStatus", relativeStatus);
                //.putExtra("userID",userID);
                myIntent.putExtras(b);
                //.putExtra("userID",userID);
                startActivityForResult(myIntent, 1);
            }
        });
        new PatientListAsyncTask(userID).execute();


    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnAddNewPatientProf) {
            Intent intent=new Intent();
            intent.setClass(this,PatientFormActivity.class);
            Bundle b = new Bundle();
            b.putString("userID", userID);
            intent.putExtras(b);
            startActivity(intent);
        }

    }

    public class PatientListAsyncTask extends AsyncTask<String, Void, String> {
        String pUserID;


        public PatientListAsyncTask(String pUserID){
            this.pUserID = pUserID;


        }
        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/KlikSembuhAPI/api/PersonalInfoes/GetPersonalInfoByUserID?UserID="+userID);
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
                }catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }else {
                return "";
            }
        }
        @Override
        protected void onPostExecute(String success) {
            super.onPostExecute(success);
            if(success!=""){
                try{
                    JSONArray jsonArray = new JSONArray(success);
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String personalInfoID = jsonObject.getString("PersonalInfoID");
                        String fName = jsonObject.getString("FirstName");
                        String lName = jsonObject.getString("LastName");
                        String gender = jsonObject.getString("Gender");
                        String cellPhoneNbr = jsonObject.getString("CellPhoneNbr");
                        String bPJSNbr = jsonObject.getString("BPJSNbr");
                        String address = jsonObject.getString("Address");
                        String closeRelativeName = jsonObject.getString("CloseRelativeName");
                        String closeRelativePhoneNbr = jsonObject.getString("CloseRelativePhoneNbr");
                        String birthOfDate = jsonObject.getString("BirthOfDate");
                        String relativeStatus = jsonObject.getString("relativeStatusDesc");
                        int relativeStatusID = jsonObject.getInt("RelativeStatus");

                        mPatientList.add(new Patient(Integer.parseInt(personalInfoID),closeRelativeName,relativeStatus,fName,lName,
                               cellPhoneNbr,gender,birthOfDate,bPJSNbr,address,closeRelativePhoneNbr, relativeStatusID));

                    }
                    pAdapter = new PatientListAdapter(getApplicationContext(),mPatientList);
                    lvPatient.setAdapter(pAdapter);


                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
            else {
                //TODO
            }

        }
    }
}
