package com.kliksembuh.ks;

import android.app.ProgressDialog;
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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private String firstName;
    private String lastName;
    private String email;
    private String noHp;
    private ProgressDialog pDialog;
    private String password;
    private String confirmPassword;

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
        Bundle b = getIntent().getExtras();
        if(b != null) {
            firstName = b.getString("firstName");
            mFirstName.setText(firstName);
            lastName = b.getString("lastName");
            mLastName.setText(lastName);
            email = b.getString("email");
            mEmail.setText(email);
            noHp = b.getString("noHp");
            mNoHp.setText(noHp);
            password = b.getString("password");
            mPasswordView.setText(password);
            confirmPassword = b.getString("confirmPassword");
            mConfirmPasswordView.setText(confirmPassword);

        }


        Button btncreate = (Button) findViewById(R.id.btncreate);
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
//                Intent myIntent = new Intent(view.getContext(), VerifikasiActivity.class);
//                startActivityForResult(myIntent, 0);
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
                Bundle b = new Bundle();
                b.putString("firstName", firstName); //Your id
                b.putString("lastName", lastName);
                b.putString("email",email);
                b.putString("noHp",noHp);
                b.putString("password",password);
                b.putString("confirmPassword",confirmPassword);
                //.putExtra("userID",userID);
                myIntent.putExtras(b);
                startActivityForResult(myIntent, 1);
            }
        });


        TextView tvtermandpolicy = (TextView) findViewById(R.id.termsandpol);
        tvtermandpolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), TermAndPolicyActivity.class);
                Bundle b = new Bundle();
                b.putString("firstName", firstName); //Your id
                b.putString("lastName", lastName);
                b.putString("email",email);
                b.putString("noHp",noHp);
                b.putString("password",password);
                b.putString("confirmPassword",confirmPassword);
                //.putExtra("userID",userID);
                myIntent.putExtras(b);
                startActivityForResult(myIntent, 1);
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                firstName = data.getStringExtra("firstName");
                lastName = data.getStringExtra("lastName");
                email = data.getStringExtra("email");
                noHp = data.getStringExtra("noHp");
                password = data.getStringExtra("password");
                confirmPassword = data.getStringExtra("confirmPassword");
            }
        }
    }
    private void attemptRegister() {
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

        if(firstName.length() < 2){
            mFirstName.setError(getString(R.string.errro_firstname_ivalid));
            focusView = mFirstName;
            cancel =true;

        }
        else if(lastName.length()<2){
            mLastName.setError(getString(R.string.errro_lastname_ivalid));
            focusView = mFirstName;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        else if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
        }

        else if (!isEmailValid(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            cancel = true;
        }
        //Check for a Valid No Hp
        else if(NoHp== null){
            mNoHp.setError(getString(R.string.not_null));
            focusView = mNoHp;
            cancel = true;
        }
        else if(NoHp.length()< 8){
//            mNoHp.setError(getString(R.string.error_length));
            mNoHp.setError(getString(R.string.error_invalid_nohp));
            focusView = mNoHp;
            cancel = true;
        }
        else if( NoHp.length()>12){
            mNoHp.setError(getString(R.string.error_invalid_hp));
            focusView = mNoHp;
            cancel = true;
        }
        else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        //Check for a Valid Confirmasi Password
        else if(confirmPassword==null){
            mConfirmPasswordView.setError(getString(R.string.error_invalid_confirm_password_not_null));
            cancel = true;
            focusView = mConfirmPasswordView;
        }
        else if(!confirmPassword.equals(password)){
            mConfirmPasswordView.setError(getString(R.string.error_invalid_confirm_password));
            focusView = mConfirmPasswordView;
            cancel = true;
        }
//        else if(confirmPassword != password){
//            mConfirmPasswordView.setError(getString(R.string.error_invalid_confirm_password));
//        }
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
        return password.length() > 7;
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
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Mohon Menunggu...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/kliksembuhapi/api/users/register");
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
            mAuthTask = null;
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (success!="") {
                try {
                    JSONObject jsonObj = new JSONObject(success);
                    JSONObject jsd = jsonObj.getJSONObject("Result");
                    String userID = jsd.getString("Id");
                    String email = jsd.getString("Email");

//                    VerifikasiActivity as = new VerifikasiActivity(userID);
                    Intent i = new Intent(getApplicationContext(), VerifikasiActivity.class);
                    Bundle b = new Bundle();
                    b.putString("userID", userID); //Your id
                    b.putString("Email", email);
                    //.putExtra("userID",userID);
                    i.putExtras(b);
                    startActivityForResult(i, 1);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                //:TODO
                Intent i = new Intent(getApplicationContext(), EmailTerdaftarActivity.class);
                Bundle b = new Bundle();
                b.putString("Email", email);
                i.putExtras(b);
                startActivityForResult(i, 1);

            }
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
