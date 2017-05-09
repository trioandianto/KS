package com.kliksembuh.ks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

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
    private Spinner spnStatus, spnAsuransi;
    private RadioButton rbJenisKelaminL, rbJenisKelaminP;
    private EditText etLname, etMobile, etFname, etTanggalLahir,etNoBPJSKes, etAlamat, etNamaKerabat, etNoHPKerabat;
    private Button btnSimpan;
    private String userID;
    private String gender;
    private RadioGroup radioGroup;
    private RadioButton rbMale;
    private RadioButton rbFemale;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_form);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
        }
        spnStatus = (Spinner) findViewById(R.id.spnStatusSaya);
        spnAsuransi = (Spinner) findViewById(R.id.spnAsuransi);
        rbJenisKelaminL = (RadioButton) findViewById(R.id.radioBtnJenisL);
        rbJenisKelaminP = (RadioButton) findViewById(R.id.radioBtnJenisP);
        etFname = (EditText)findViewById(R.id.etFNameForm);
        etLname = (EditText)findViewById(R.id.etLNameForm);
        etMobile= (EditText)findViewById(R.id.etEditMobileForm);
        etLname = (EditText)findViewById(R.id.etLNameForm);
        etMobile= (EditText) findViewById(R.id.etEditMobileForm);
        etTanggalLahir = (EditText) findViewById(R.id.etTanggalLahirForm);
        etNoBPJSKes= (EditText) findViewById(R.id.etNoBPJSKesehatan);
        etAlamat= (EditText)findViewById(R.id.etAlamatForm);
        etNamaKerabat = (EditText)findViewById(R.id.etNamaKerabatForm);
        etNoHPKerabat = (EditText)findViewById(R.id.etNoHPKerabat);
        radioGroup = (RadioGroup)findViewById(R.id.rgGender);
        rbFemale = (RadioButton)findViewById(R.id.radioBtnJenisP);
        rbMale = (RadioButton)findViewById(R.id.radioBtnJenisL);
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
        if(rbFemale.isChecked()){
            gender = "Female";
        }
        else {
            gender = "Male";
        }
        String fullName = fName+" "+lName;

        boolean cancel = false;
        View focusView = null;

        if(!TextUtils.isEmpty(fName)){
            etFname.setError(getString(R.string.not_null));
            focusView = etFname;
            cancel = true;

        }
        else if(!TextUtils.isEmpty(lName)){
            etLname.setError(getString(R.string.not_null));
            focusView = etLname;
            cancel = true;

        }
        else if(!TextUtils.isEmpty(mobile)){
            etMobile.setError(getString(R.string.not_null));
            focusView = etMobile;
            cancel = true;

        }
        else if(!TextUtils.isEmpty(alamat)){
            etAlamat.setError(getString(R.string.not_null));
            focusView = etAlamat;
            cancel = true;

        }
        else if(!TextUtils.isEmpty(namaKerabat)){
            etNamaKerabat.setError(getString(R.string.not_null));
            focusView = etNamaKerabat;
            cancel = true;

        }
        else if(!TextUtils.isEmpty(noBPJS)){
            etNoBPJSKes.setError(getString(R.string.not_null));
            focusView = etNoBPJSKes;
            cancel = true;

        }
        else if(!TextUtils.isEmpty(noHPKerabat)){
            etNoHPKerabat.setError(getString(R.string.not_null));
            focusView = etNoHPKerabat;
            cancel = true;

        }
        else if (!TextUtils.isEmpty(tanggalLahir)){
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
            mAuthTask = new PatientAsyncTask(userID,fullName,gender,mobile,noBPJS,alamat,namaKerabat,
                    noHPKerabat, tanggalLahir);
            mAuthTask.execute((String) null);
        }

    }

    public class PatientAsyncTask extends AsyncTask<String, Void, String>{
        String pUserID;
        String pFullName;
        String pGender;
        String pCellPhoneNbr;
        String pBPJSNbr;
        String pAddress;
        String pCloseRelativeName;
        String pCloseRelativePhoneNbr;
        String pBirthOfDate;

        public PatientAsyncTask(String pUserID, String pFullName, String pGender, String pCellPhoneNbr, String pBPJSNbr, String pAddress,
                           String pCloseRelativeName, String pCloseRelativePhoneNbr, String pBirthOfDate){
            this.pUserID = pUserID;
            this.pFullName =pFullName;
            this.pGender = pGender;
            this.pBPJSNbr = pBPJSNbr;
            this.pAddress = pAddress;
            this.pCellPhoneNbr = pCellPhoneNbr;
            this.pCloseRelativeName = pCloseRelativeName;
            this.pCloseRelativePhoneNbr = pCloseRelativePhoneNbr;
            this.pBirthOfDate = pBirthOfDate;

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
                    jsonObject.put("FullName", pFullName);
                    jsonObject.put("Gender", pGender);
                    jsonObject.put("CellPhoneNbr", pCellPhoneNbr);
                    jsonObject.put("BPJSNbr", pBPJSNbr);
                    jsonObject.put("Address" ,pAddress);
                    jsonObject.put("CloseRelativeName", pCloseRelativeName);
                    jsonObject.put("CloseRelativePhoneNbr", pCloseRelativePhoneNbr);
                    jsonObject.put("BirthOfDate", pBirthOfDate);

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
    }
}
