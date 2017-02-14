package com.kliksembuh.ks;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class RegisterActivity extends AppCompatActivity {
    private AutoCompleteTextView mFirstName;
    private AutoCompleteTextView mLastName;
    private AutoCompleteTextView mEmail;
    private AutoCompleteTextView mNoHp;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;

    private UserRegisterTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = (AutoCompleteTextView) findViewById(R.id.email);
        mFirstName = (AutoCompleteTextView) findViewById(R.id.tvfirstname);
        mLastName = (AutoCompleteTextView) findViewById(R.id.tvlastname);
        mNoHp = (AutoCompleteTextView) findViewById(R.id.tvhp);
        mPasswordView =(EditText) findViewById(R.id.password);
        mConfirmPasswordView =(EditText) findViewById(R.id.confirmpassword);


        Button btncreate = (Button) findViewById(R.id.btncreate);
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        TextView tvtermsandpolicy = (TextView)findViewById(R.id.termsandpol);
        tvtermsandpolicy.setText(Html.fromHtml("Dengan menekan tombol buat akun baru, anda sudah menyetujui " +
                "<a href='com.kliksembuh.ks.TermANdPolicyActivity://Phone'>Syarat & Ketentuan.</a>"));


        Button btnlogin = (Button) findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });


        TextView tvtermandpolicy = (TextView) findViewById(R.id.termsandpol);
        tvtermandpolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), TermAndPolicyActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }
    private void attemptLogin() {
        if (mAuthTask!= null) {
            return;
        }
        // Reset errors.
        mEmail.setError(null);
        mPasswordView.setError(null);
        mFirstName.setError(null);
        mLastName.setError(null);
        mNoHp.setError(null);
        mConfirmPasswordView.setError(null);
        // Store values at the time of the login attempt.
        String email = mEmail.getText().toString();
        String password = mPasswordView.getText().toString();
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String NoHp = mNoHp.getText().toString();
        String confirmPassword = mConfirmPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }else{
            mAuthTask = new UserRegisterTask(firstName,lastName, NoHp, email, password, confirmPassword);
            mAuthTask.execute((String) null);
        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 8;
    }
    public class UserRegisterTask extends AsyncTask<String, Void, String> {
        private String mFirstName;
        private String mLastName;
        private String mEmail;
        private String mNoHp;
        private String mPassword;
        private String mConfirmPassword;
        UserRegisterTask(String firstName, String lastName,String hp, String email, String password,String confirmPassword) {
            mEmail = email;
            mPassword = password;
            mFirstName = firstName;
            mLastName = lastName;
            mNoHp = hp;
            mConfirmPassword = confirmPassword;
        }


        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://192.168.1.5/UserAPI/api/users/register");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Email",mEmail);
                    jsonObject.put("Password",mPassword);
                    jsonObject.put("ConfirmPassword",mConfirmPassword);
                    jsonObject.put("Username",mEmail);
                    jsonObject.put("FirstName",mFirstName);
                    jsonObject.put("LastName",mLastName);
                    jsonObject.put("PhoneNbr",mNoHp);
                    urlc.setConnectTimeout(3000);
                    urlc.setRequestMethod("POST");
                    urlc.setDoInput(true);
                    urlc.setDoOutput(true);
                    OutputStream os = urlc.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(jsonObject));
                    writer.flush();
                    writer.close();
                    os.close();
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
            mAuthTask = null;
            if (success!="") {
                try {
                    JSONObject jsonObj = new JSONObject(success);
                    JSONObject jsd = jsonObj.getJSONObject("Result");
                    String userID = jsd.getString("Id");

//                    VerifikasiActivity as = new VerifikasiActivity(userID);
                    Intent i = new Intent(getApplicationContext(), VerifikasiActivity.class);
                    Bundle b = new Bundle();
                    b.putString("userID", userID); //Your id
                    //.putExtra("userID",userID);
                    i.putExtras(b);
                    startActivityForResult(i, 0);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                //:TODO
            }
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
