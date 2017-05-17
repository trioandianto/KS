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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

public class ResetPasswordActivity extends AppCompatActivity {
    private String email;
    private AutoCompleteTextView mEmailView;

    private ResetPasswordTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            email = b.getString("Email");
        }
        mEmailView = (AutoCompleteTextView)findViewById(R.id.forget_forgot_password_email);
        if(email!=null && email!="null"){
            mEmailView.setText(email);
        }
        Button btnverify = (Button) findViewById(R.id.btnresetpassword);
        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptReset();
            }
        });
    }
    private void attemptReset() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);


        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            mAuthTask = new ResetPasswordTask(email);
            mAuthTask.execute((String) null);
        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    public class ResetPasswordTask extends AsyncTask<String, Void, String> {
        private final String mEmail;
        ResetPasswordTask(String email) {
            mEmail = email;
        }
        @Override
        protected String doInBackground(String ... params) {
            // TODO: attempt authentication against a network service.
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://cloud.abyor.com:11080/KlikSembuhAPI/api/Users/ForgotPassword/");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Email",mEmail);

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
        protected void onPostExecute(final String success) {
            mEmailView.setError(null);
            View focusView = null;
            if (success!="") {

                Toast.makeText(getApplicationContext(), "Silahkan cek email anda.", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
            } else {
                mEmailView.setError(getString(R.string.error_invalid_email));
                focusView = mEmailView;
                focusView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
}
