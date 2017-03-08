package com.kliksembuh.ks;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class ProfileManagementActivity extends AppCompatActivity{
    private static final int RESULT_LOAD_IMAGE=1;
    private static final String SERVER_ADDRESS="";



    private ImageView imageView;
    private ImageView imageClick;
    private TextView editDataDiri;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);

        imageView = (ImageView)findViewById(R.id.profile_image);
        imageClick = (ImageView)findViewById(R.id.cameraupload);
        editDataDiri = (TextView)findViewById(R.id.tvEditProfileManagement);
        editVitalSign =(TextView)findViewById(R.id.tvEditVitalSignProfileManagement);
        gantiPassword = (TextView)findViewById(R.id.tvgantipassword);

        editDataDiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileManagementActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.profile_data_diri,null);
                datadiri = (TextView) mView.findViewById(R.id.tvdatadiri);
                email = (AutoCompleteTextView)mView.findViewById(R.id.tvemail);
                namadepan = (AutoCompleteTextView) mView.findViewById(R.id.tvnamadepan);
                namabelakang = (AutoCompleteTextView) mView.findViewById(R.id.tvnamabelakang);
                nohp = (AutoCompleteTextView) mView.findViewById(R.id.tvnohp);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                Button simpan = (Button) mView.findViewById(R.id.btnsimpandatadiri);
                simpan.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        editVitalSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileManagementActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.profile_vital_sign,null);
                vitalsign = (TextView) mView.findViewById(R.id.tvvitalsign);
                suhuTubuh = (AutoCompleteTextView) mView.findViewById(R.id.tvsuhu);
                denyutNadi = (AutoCompleteTextView) mView.findViewById(R.id.tvnadi);
                tekananDarah = (AutoCompleteTextView) mView.findViewById(R.id.tvtekanandarah);
                pernafasan = (AutoCompleteTextView)mView.findViewById(R.id.tvpernafasan);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                Button simpan = (Button) mView.findViewById(R.id.btnsimpandatadiri);
                simpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
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
                new UploadImage(image);
            }
        });
        gantiPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!=null){
            Uri selectedImage = data.getData();
            imageView.setImageURI(selectedImage);
        }
    }
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
}
