package com.kliksembuh.ks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class ProfileManagementActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RESULT_LOAD_IMAGE=1;
    private static final String SERVER_ADDRESS="";



    private String userID;
    private ImageView imageView;
    private TextView imageClick;
    private TextView editDataDiri;
    private TextView editFname;
    private TextView editLname;
    private TextView editEmail;
    private TextView editMobile;
    private TextView editVitalSign;
    private TextView gantiPassword;
    private AutoCompleteTextView namadepan;
    private AutoCompleteTextView namabelakang;
    private AutoCompleteTextView nohp;
    private AutoCompleteTextView email;
    private Drawable drawable;
    private Random random;
    private Drawable [] drawables = null;
    private TextView datadiri;
    private TextView vitalsign;
    private AutoCompleteTextView tekananDarah;
    private AutoCompleteTextView suhuTubuh;
    private AutoCompleteTextView denyutNadi;
    private AutoCompleteTextView pernafasan;
    private Button btnSimpanProfile;
    final Context context = this;
    private UpdateProfileManagementTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
        }

        imageView = (ImageView)findViewById(R.id.profile_image);
        imageClick = (TextView) findViewById(R.id.tv_AddPhotoProfile);
        //editDataDiri = (TextView)findViewById(R.id.tvEditProfileManagement);
        editFname = (TextView)findViewById(R.id.tvFName);
        editLname = (TextView)findViewById(R.id.tvLName);
        editEmail = (TextView)findViewById(R.id.tvEditEmail);
        editMobile = (TextView)findViewById(R.id.tvEditMobile);
        btnSimpanProfile = (Button)findViewById(R.id.btnSimpanProfile);
        btnSimpanProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemUpdate();
            }
        });
        new GetProfileManagementTask(userID).execute();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });
        imageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                // TO DO - Create Dialog Take a Photo and From Gallery
//                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(takePicture, 0);
                new UploadImage(image);
            }
        });

//

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!=null){
            Uri selectedImage = data.getData();
            imageView.setImageURI(selectedImage);
        }
    }

    @Override
    public void onClick(View v) {


//        Intent i=new Intent();
//        i.setClass(this,EditNumberProfileActivity.class);
//        startActivity(i);
//        Intent i = new Intent();
//        switch(v.getId()) {
//            case R.id.btnTvUbahNomor: // R.id.textView1
//                EditNumberProfileActivity dialogEditNumber = new EditNumberProfileActivity(ProfileManagementActivity.this);
//                dialogEditNumber.show();
//                break;
//        }
//        startActivity(i);

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.activity_edit_number_profile, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final TextView userInputNomorBaru = (TextView) promptsView.findViewById(R.id.tv_NomorTerdaftar);
        final TextView userInputPassword = (TextView) promptsView.findViewById(R.id.tvEditPasswordProf);


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
//                                result.setText(userInput.getText());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
    public  void attemUpdate(){
        if (mAuthTask != null) {
        return;
        }
        editFname.setError(null);
        editLname.setError(null);
        editEmail.setError(null);
        editMobile.setError(null);

        String fName = editFname.getText().toString();
        String lName = editLname.getText().toString();
        String mobile = editMobile.getText().toString();
        String email = editEmail.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(fName)){
            editFname.setError(getString(R.string.not_null));
            focusView = editFname;
            cancel = true;

        }
        else if(TextUtils.isEmpty(lName)){
            editLname.setError(getString(R.string.not_null));
            focusView = editLname;
            cancel = true;

        }
        else if(TextUtils.isEmpty(mobile)){
            editMobile.setError(getString(R.string.not_null));
            focusView = editMobile;
            cancel = true;

        }
        else if(TextUtils.isEmpty(email)){
            editEmail.setError(getString(R.string.not_null));
            focusView = editEmail;
            cancel = true;

        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            mAuthTask = new UpdateProfileManagementTask(userID,fName,lName,email,mobile);
            mAuthTask.execute((String) null);


        }}

    private class UploadImage extends AsyncTask<Void,Void,Void>{
        Bitmap  image;
        public UploadImage(Bitmap img){
            this.image =img;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("image", encodedImage));

            HttpParams httpParams = getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "");
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    private HttpParams getHttpRequestParams(){
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpParams, 1000 * 30);
        return httpParams;
    }
    private class GetProfileManagementTask extends AsyncTask<String, Void, String>{
        private String uUserID;

        public GetProfileManagementTask(String uUserID){
            this.uUserID = uUserID;
        }
        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/KlikSembuhAPI/api/Users/GetUsers/"+uUserID);
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
                    JSONObject jsonObject = new JSONObject(s);
                    String firstName = jsonObject.getString("FirstName");
                    String lastName = jsonObject.getString("LastName");
                    String email = jsonObject.getString("Email");
                    String noHp = jsonObject.getString("PhoneNbr");
                    editFname.setText(firstName);
                    editLname.setText(lastName);
                    editEmail.setText(email);
                    editMobile.setText(noHp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class UpdateProfileManagementTask extends AsyncTask<String, Void, String>{
        private String uFName;
        private String uLName;
        private String uEmail;
        private String uNoHP;
        private String uUserID;

        public UpdateProfileManagementTask(String uUserID, String uFName, String uLName, String uEmail, String uNoHP){
            this.uFName = uFName;
            this.uLName = uLName;
            this.uEmail = uEmail;
            this.uNoHP = uNoHP;
            this.uUserID = uUserID;

        }
        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/KlikSembuhAPI/api/Users/PatchUsers?UserID="+uUserID);
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("FirstName", uFName);
                    jsonObject.put("LastName", uLName);
                    jsonObject.put("Email", uEmail);
                    jsonObject.put("082117461581", uNoHP);
                    urlc.setConnectTimeout(3000);
                    urlc.setRequestProperty("Content-Type","application/json");
                    urlc.setRequestMethod("PATCH");
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
        }
    }
}
