package com.kliksembuh.ks;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class PatientFormActivity extends AppCompatActivity {

    private PatientAsyncTask mAuthTask = null;
    private UpdatePatientAsyncTask mUpdateAuthTask = null;
    private Spinner spnStatus, spnAsuransi;
    private RadioButton rbJenisKelaminL, rbJenisKelaminP;
    private AutoCompleteTextView etLname, etMobile, etFname, etTanggalLahir,etNoBPJSKes, etAlamat, etNamaKerabat, etNoHPKerabat;
    private Button btnSimpan;
    private String userID;
    private String gender;
    private RadioGroup radioGroup;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private Spinner spnStatusSaya;
    private String statusSaya;
    private String fName;
    private String lName;
    private String cellPhoneNbr ;
    private String bPJSNbr;
    private String address ;
    private String closeRelativeName;
    private String closeRelativePhoneNbr;
    private String birthOfDate;
    private String relativeStatus;
    private int personalInfoID;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_form);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
            personalInfoID = b.getInt("personalInfoID");
            fName = b.getString("fName");//Your id
            lName = b.getString("lName");
            gender = b.getString("gender");
            cellPhoneNbr = b.getString("cellPhoneNbr");
            bPJSNbr = b.getString("bPJSNbr");
            address = b.getString("address");
            closeRelativeName = b.getString("closeRelativeName");
            closeRelativePhoneNbr = b.getString("closeRelativePhoneNbr");
            birthOfDate = b.getString("birthOfDate");
            relativeStatus = b.getString("relativeStatus");

        }
        spnStatus = (Spinner) findViewById(R.id.spnStatusSaya);
        spnAsuransi = (Spinner) findViewById(R.id.spnAsuransi);
        rbJenisKelaminL = (RadioButton) findViewById(R.id.radioBtnJenisL);
        rbJenisKelaminP = (RadioButton) findViewById(R.id.radioBtnJenisP);
        etFname = (AutoCompleteTextView)findViewById(R.id.etFNameForm);
        etLname = (AutoCompleteTextView)findViewById(R.id.etLNameForm);
        etMobile= (AutoCompleteTextView)findViewById(R.id.etEditMobileForm);
        etTanggalLahir = (AutoCompleteTextView) findViewById(R.id.etTanggalLahirForm);
        etNoBPJSKes= (AutoCompleteTextView) findViewById(R.id.etNoBPJSKesehatan);
        etAlamat= (AutoCompleteTextView)findViewById(R.id.etAlamatForm);
        etNamaKerabat = (AutoCompleteTextView)findViewById(R.id.etNamaKerabatForm);
        etNoHPKerabat = (AutoCompleteTextView)findViewById(R.id.etNoHPKerabat);
        radioGroup = (RadioGroup)findViewById(R.id.rgGender);
        rbFemale = (RadioButton)findViewById(R.id.radioBtnJenisP);
        rbMale = (RadioButton)findViewById(R.id.radioBtnJenisL);
        spnStatusSaya = (Spinner)findViewById(R.id.spnStatusSaya);
        spnStatusSaya.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusSaya = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (rbMale.isChecked()){
            gender = "Male";
        }
        else {
            gender = "Female";

        }

        btnSimpan = (Button) findViewById(R.id.btnSimpanProfileForm);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSubmit();
            }
        });
        setData();

    }
    private void setData(){
        if(fName!=null && fName != ""){
            etFname.setText(fName);
        }
        if(lName!=null && lName!=""){
            etLname.setText(lName);
        }
        if(cellPhoneNbr!=null && cellPhoneNbr!=""){
            etMobile.setText(cellPhoneNbr);
        }
        if(birthOfDate!=null && birthOfDate!=""){
            etTanggalLahir.setText(birthOfDate);
        }
        if(address!=null && address!=""){
            etAlamat.setText(address);
        }
        if(closeRelativeName!=null && closeRelativeName!=""){
            etNamaKerabat.setText(closeRelativeName);
        }
        if(closeRelativePhoneNbr!=null && closeRelativePhoneNbr!=""){
            etNoHPKerabat.setText(closeRelativePhoneNbr);
        }
        if(relativeStatus!=null && relativeStatus!="null"){
            if(relativeStatus == "Pasien"){
                spnStatusSaya.setSelection(1);
            }
            else if(relativeStatus == "Anak"){
                spnStatusSaya.setSelection(2);
            }
            else if(relativeStatus == "Orang Tua"){
                spnStatusSaya.setSelection(3);
            }
            else if(relativeStatus=="Keluarga"){
                spnStatusSaya.setSelection(4);
            }
            else{
                spnStatusSaya.setSelection(5);
            }
        }
        if(gender!=null && gender!="null"){
            if(gender=="Male"){
                rbMale.setEnabled(true);
            }
            else {
                rbFemale.setEnabled(true);
            }
        }

    }
    private void attemptSubmit(){
        if (mAuthTask != null) {
            return;
        }
        etFname.setError(null);
        etLname.setError(null);
        etMobile.setError(null);
        etAlamat.setError(null);
        etNamaKerabat.setError(null);
        etNoBPJSKes.setError(null);
        etNoHPKerabat.setError(null);
        etTanggalLahir.setError(null);

        String fName = etFname.getText().toString();
        String lName = etLname.getText().toString();
        String mobile = etMobile.getText().toString();
        String alamat = etAlamat.getText().toString();
        String namaKerabat = etNamaKerabat.getText().toString();
        String noBPJS = etNoBPJSKes.getText().toString();
        String noHPKerabat = etNoHPKerabat.getText().toString();
        String tanggalLahir = etTanggalLahir.getText().toString();
        String relativeStatus = statusSaya;
        if(rbFemale.isChecked()){
            gender = "Female";
        }
        else {
            gender = "Male";
        }

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(fName)){
            etFname.setError(getString(R.string.not_null));
            focusView = etFname;
            cancel = true;

        }
        else if(TextUtils.isEmpty(lName)){
            etLname.setError(getString(R.string.not_null));
            focusView = etLname;
            cancel = true;

        }
        else if(TextUtils.isEmpty(mobile)){
            etMobile.setError(getString(R.string.not_null));
            focusView = etMobile;
            cancel = true;

        }
        else if(TextUtils.isEmpty(alamat)){
            etAlamat.setError(getString(R.string.not_null));
            focusView = etAlamat;
            cancel = true;

        }
        else if(TextUtils.isEmpty(namaKerabat)){
            etNamaKerabat.setError(getString(R.string.not_null));
            focusView = etNamaKerabat;
            cancel = true;

        }
        else if(TextUtils.isEmpty(noBPJS)){
            etNoBPJSKes.setError(getString(R.string.not_null));
            focusView = etNoBPJSKes;
            cancel = true;

        }
        else if(TextUtils.isEmpty(noHPKerabat)){
            etNoHPKerabat.setError(getString(R.string.not_null));
            focusView = etNoHPKerabat;
            cancel = true;

        }
        else if (TextUtils.isEmpty(tanggalLahir)){
            etTanggalLahir.setError(getString(R.string.not_null));
            focusView = etTanggalLahir;
            cancel = true;

        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if(personalInfoID>0){
                mUpdateAuthTask = new UpdatePatientAsyncTask(personalInfoID,userID,fName,lName,gender,mobile,noBPJS,alamat,namaKerabat,
                        noHPKerabat, tanggalLahir, relativeStatus);
                mUpdateAuthTask.execute((String) null);
            }
            else{
                mAuthTask = new PatientAsyncTask(userID,fName,lName,gender,mobile,noBPJS,alamat,namaKerabat,
                        noHPKerabat, tanggalLahir, relativeStatus);
                mAuthTask.execute((String) null);
            }

        }

    }

    public class PatientAsyncTask extends AsyncTask<String, Void, String>{
        String pUserID;
        String pFName;
        String pLName;
        String pGender;
        String pCellPhoneNbr;
        String pBPJSNbr;
        String pAddress;
        String pCloseRelativeName;
        String pCloseRelativePhoneNbr;
        String pBirthOfDate;
        String pRelativeStatus;

        public PatientAsyncTask(String pUserID, String pFName,String pLName, String pGender, String pCellPhoneNbr, String pBPJSNbr, String pAddress,
                           String pCloseRelativeName, String pCloseRelativePhoneNbr, String pBirthOfDate, String pRelativeStatus){
            this.pUserID = pUserID;
            this.pFName = pFName;
            this.pLName = pLName;
            this.pGender = pGender;
            this.pBPJSNbr = pBPJSNbr;
            this.pAddress = pAddress;
            this.pCellPhoneNbr = pCellPhoneNbr;
            this.pCloseRelativeName = pCloseRelativeName;
            this.pCloseRelativePhoneNbr = pCloseRelativePhoneNbr;
            this.pBirthOfDate = pBirthOfDate;
            this.pRelativeStatus = pRelativeStatus;

        }
        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/KlikSembuhAPI/api/PersonalInfoes/PostPersonalInfo");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("UserID", pUserID);
                    jsonObject.put("FirstName", pFName);
                    jsonObject.put("LastName", pLName);
                    jsonObject.put("Gender", pGender);
                    jsonObject.put("CellPhoneNbr", pCellPhoneNbr);
                    jsonObject.put("BPJSNbr", pBPJSNbr);
                    jsonObject.put("Address" ,pAddress);
                    jsonObject.put("CloseRelativeName", pCloseRelativeName);
                    jsonObject.put("CloseRelativePhoneNbr", pCloseRelativePhoneNbr);
                    jsonObject.put("BirthOfDate", pBirthOfDate);
                    jsonObject.put("RelativeStatus",pRelativeStatus);

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
            super.onPostExecute(s);
            if(s!=""){
                Intent i = new Intent(getApplicationContext(), PatientProfileActivity.class);
                Bundle b = new Bundle();
                b.putString("userID", userID);
                i.putExtras(b);
                startActivityForResult(i, 1);

                Toast.makeText(getApplicationContext(), "Simpan Data Berhasil", Toast.LENGTH_LONG).show();
            }
            else {
                //TODO
            }

        }
    }
    public class UpdatePatientAsyncTask extends AsyncTask<String, Void, String>{
        String pUserID;
        int pPersonalInfoID;
        String pFName;
        String pLName;
        String pGender;
        String pCellPhoneNbr;
        String pBPJSNbr;
        String pAddress;
        String pCloseRelativeName;
        String pCloseRelativePhoneNbr;
        String pBirthOfDate;
        String pRelativeStatus;

        public UpdatePatientAsyncTask(int pPersonalInfoID, String pUserID, String pFName,String pLName, String pGender, String pCellPhoneNbr, String pBPJSNbr, String pAddress,
                                String pCloseRelativeName, String pCloseRelativePhoneNbr, String pBirthOfDate, String pRelativeStatus){
            this.pPersonalInfoID = pPersonalInfoID;
            this.pUserID = pUserID;
            this.pFName = pFName;
            this.pLName = pLName;
            this.pGender = pGender;
            this.pBPJSNbr = pBPJSNbr;
            this.pAddress = pAddress;
            this.pCellPhoneNbr = pCellPhoneNbr;
            this.pCloseRelativeName = pCloseRelativeName;
            this.pCloseRelativePhoneNbr = pCloseRelativePhoneNbr;
            this.pBirthOfDate = pBirthOfDate;
            this.pRelativeStatus = pRelativeStatus;

        }
        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/KlikSembuhAPI/api/PersonalInfoes/PostPersonalInfo");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("personalInfoID",pPersonalInfoID);
                    jsonObject.put("UserID", pUserID);
                    jsonObject.put("FirstName", pFName);
                    jsonObject.put("LastName", pLName);
                    jsonObject.put("Gender", pGender);
                    jsonObject.put("CellPhoneNbr", pCellPhoneNbr);
                    jsonObject.put("BPJSNbr", pBPJSNbr);
                    jsonObject.put("Address" ,pAddress);
                    jsonObject.put("CloseRelativeName", pCloseRelativeName);
                    jsonObject.put("CloseRelativePhoneNbr", pCloseRelativePhoneNbr);
                    jsonObject.put("BirthOfDate", pBirthOfDate);
                    jsonObject.put("RelativeStatus",pRelativeStatus);

                    urlc.setConnectTimeout(3000);
                    urlc.setRequestProperty("Content-Type","application/json");
                    urlc.setRequestMethod("PUT");
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
            super.onPostExecute(s);
            if(s!=""){
                Intent i = new Intent(getApplicationContext(), PatientProfileActivity.class);
                Bundle b = new Bundle();
                b.putString("userID", userID);
                i.putExtras(b);
                startActivityForResult(i, 1);

                Toast.makeText(getApplicationContext(), "Simpan Data Berhasil", Toast.LENGTH_LONG).show();
            }
            else {
                //TODO
            }

        }
    }
}
