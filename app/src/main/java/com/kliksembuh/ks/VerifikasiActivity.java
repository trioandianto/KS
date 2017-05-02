package com.kliksembuh.ks;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class VerifikasiActivity extends AppCompatActivity {
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    private String userID;
    private String email;
    private EditText activeCode;
    private UserVerifyTask mAuthTask = null;
    private TextView tvVerifikasi;
    private  UserKirimUlangTask kirimUlangTask =null;
//    public VerifikasiActivity(String userID){
//        this.setUserID(userID);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
            email = b.getString("Email");
        }
        activeCode =(EditText)findViewById(R.id.kd_verifikasi);
        tvVerifikasi = (TextView) findViewById(R.id.tvVerifikasi);
        tvVerifikasi.setText("Kode verifikasi telah dikirimkan ke "+email+". Masukan kode untuk melakukan verifikasi akun.");



        Button btnVerify = (Button)findViewById(R.id.btnverify);
        btnVerify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                attemptVerify();
//                Intent myIntent = new Intent(view.getContext(), HomeActivity.class);
//                startActivityForResult(myIntent, 0);
            }
        });

        TextView tvkirimulang = (TextView)findViewById(R.id.linkkirimulang);
        tvkirimulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempKirimUlang();

            }
        });
        TextView textView = (TextView)findViewById(R.id.linkgantinohp);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RegisterActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });


    }
    private void attempKirimUlang(){
        if(kirimUlangTask!=null){
            return;
        }
//        activeCode.setError(null);
//        String active = activeCode.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if(userID == null){
            activeCode.setError(getString(R.string.error_active_code));
            focusView = activeCode;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }else{
            kirimUlangTask = new UserKirimUlangTask(userID);
            kirimUlangTask.execute((String) null);
        }
    }
    private void attemptVerify() {
        if (mAuthTask != null) {
            return;
        }
        activeCode.setError(null);
        String active = activeCode.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if(active == ""){
            activeCode.setError(getString(R.string.error_active_code));
            focusView = activeCode;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }else{
            mAuthTask = new UserVerifyTask(userID,active);
            mAuthTask.execute((String) null);
        }
    }

    public class UserVerifyTask extends AsyncTask<String, Void, String> {
        private String userID;
        private String code;
        UserVerifyTask(String userID, String code) {
            this.userID = userID;
            this.code = code;
        }


        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://192.168.1.6/klikSembuhAPI/api/activationcodes/CodeValidation");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("UserID",userID);
                    jsonObject.put("ActivactionCodeCD",code);
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

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while(itr.hasNext()){

                String key= itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
        @Override
        protected void onPostExecute(final String success) {
            View focusView = null;
            activeCode.setError(null);
            mAuthTask = null;
            if (success!="") {
                try {
                    JSONObject jsonObj = new JSONObject(success);
                    String userID = jsonObj.getString("UserID");

                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    Bundle b = new Bundle();
                    b.putString("userID", userID); //Your id
                    //.putExtra("userID",userID);
                    i.putExtras(b);
                    startActivity(i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //:TODO
//                activeCode.setError(getString(R.string.error_active_code_expiret));
                activeCode.setError(getString(R.string.error_active_code));
                focusView = activeCode;
                focusView.requestFocus();
            }
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
    public class UserKirimUlangTask extends AsyncTask<String, Void, Boolean> {
        private String userID;
        UserKirimUlangTask(String userID) {
            this.userID = userID;
        }


        @Override
        protected Boolean doInBackground(String ... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://192.168.1.6/KlikSembuhAPI/api/users/GetNewActivationCode/"+userID);
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }

        protected void onPostExecute(Boolean success) {
            View focusView = null;
            activeCode.setError(null);
            mAuthTask = null;
            if (success) {
//                activeCode.setError(getString(R.string.error_active_code_kirim));
                activeCode.setError(getString(R.string.error_active_code));
                focusView = activeCode;
                focusView.requestFocus();
                //: TODO
            } else {
                kirimUlangTask=null;
            }
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
