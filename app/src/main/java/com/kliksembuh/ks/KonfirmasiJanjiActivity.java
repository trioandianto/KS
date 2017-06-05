package com.kliksembuh.ks;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class KonfirmasiJanjiActivity extends AppCompatActivity {
    private Button btnBuatJanji;
    private TextView tvTambahPasien, tvEditPasien;
    private ImageView ivDoc_Pic;
    private TextView tvLihatPeta,tvFirstTitleDoc,tvNamaDokter,tvSpesial,tvNamaRumahSakit,
            tvAlamatRS,tvNamaHari,tvWaktuBerobat,tvJamBerobat,tvDetailTanggal,tvNamaPasien,tvNOHP;
    private String userID, customerID, facilityCategoryID, facilityID, status, institutionID;
    private String date;
    private String weekProgramID;
    private String dayProgramID;
    private String dayProgramDetailID;
    private String personnelID;
    private String personnelCD;
    private String specialtyDoc;
    private String firstTitleDoc;
    private String namaDokter;
    private String alamat;
    private String namaRumahSakit;
    private String namaHari;
    private String waktuBerobat;
    private String jamMulai;
    private String jamBerakhir;
    private String urlImg;
    private String detailTanggal;
    private boolean cek=false;
    private Spinner spnDataPasien;
    private int [] idStatus;
    private String[] namaPasien;
    private String [] noHpPasien;
    private int relativeStatusID;
    private List<String> list;
    private String shiftScheduleCD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_janji);
        Toolbar newToolbar = (Toolbar)findViewById(R.id.toolbarKonfirmasiJanji);
        newToolbar.setTitle("Konfirmasi Janji");
        setSupportActionBar(newToolbar);
        getWindow().setStatusBarColor(ContextCompat.getColor(KonfirmasiJanjiActivity.this, R.color.colorPrimaryDark));

        Bundle b = getIntent().getExtras();
        if(b!=null){
            userID = b.getString("userID");
            customerID = "1";
            facilityCategoryID = "1";
            facilityID = b.getString("facilityID");
            status = "1";
            institutionID = b.getString("rumahSakitID");
            date = b.getString("date");
            weekProgramID = b.getString("WPID");
            namaHari = b.getString("namaHari");
            dayProgramID = b.getString("DPID");
            shiftScheduleCD = b.getString("ShiftScheduleCD");
            detailTanggal = b.getString("namaTanggal");
//            dayProgramDetailID = b.getString("DetailID");

            dayProgramDetailID = b.getString("DetailID");
            waktuBerobat = b.getString("waktuBerobat");
            jamMulai = b.getString("jamMulai");
            jamBerakhir = b.getString("jamBerakhir");
            //jam = b.getString("jam");
            specialtyDoc = b.getString("dokterSpesialisasi");
            personnelID = b.getString("idDokter");
            personnelCD = b.getString("personilID");
            urlImg = b.getString("urlImage");
            firstTitleDoc = b.getString("firstTitle");
            namaDokter = b.getString("namaDokter");
            alamat = b.getString("alamat");
            namaRumahSakit = b.getString("namaRumahSakit");
        }

        tvNamaHari = (TextView)findViewById(R.id.tvNamaHari);
        tvNamaHari.setText(namaHari + ",");
        tvDetailTanggal = (TextView) findViewById(R.id.tvDetailTangal);
        tvDetailTanggal.setText(detailTanggal);
        tvWaktuBerobat = (TextView)findViewById(R.id.tv_wktBerobat);
        tvWaktuBerobat.setText(waktuBerobat + ",");
        tvJamBerobat = (TextView) findViewById(R.id.tv_jamBerobat);
        tvJamBerobat.setText(jamMulai + " - " + jamBerakhir);
        ivDoc_Pic = (ImageView)findViewById(R.id.iv_DocPic_Confirm);
        tvFirstTitleDoc = (TextView) findViewById(R.id.tvtitleDokter);
        tvLihatPeta = (TextView)findViewById(R.id.tvLihatPeta);
        tvLihatPeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(KonfirmasiJanjiActivity.this, MapsKonfirmasiJanji.class);
                Bundle b = new Bundle();
                b.putString("alamat",alamat);
                b.putString("namaRumahSakit",namaRumahSakit);
                myIntent.putExtras(b);
                startActivityForResult(myIntent, 7);
            }
        });
        new ImageDrawable(urlImg).execute();
        tvFirstTitleDoc.setText(firstTitleDoc);
        tvNamaDokter = (TextView)findViewById(R.id.tvnamaDokter);
        tvNamaDokter.setText(namaDokter);
        tvSpesial =(TextView)findViewById(R.id.tvspesial);
        tvSpesial.setText(specialtyDoc);
        tvAlamatRS = (TextView)findViewById(R.id.tvAlamatRS);
        tvAlamatRS.setText(alamat);
        tvNamaRumahSakit=(TextView)findViewById(R.id.tvnamaRumahSakit);
        tvNamaRumahSakit.setText(namaRumahSakit);
        btnBuatJanji = (Button)findViewById(R.id.btnbuatjanji);
        spnDataPasien = (Spinner)findViewById(R.id.spnDataPasien);
        tvNOHP = (TextView)findViewById(R.id.tvNOHP);
        tvNamaPasien = (TextView)findViewById(R.id.tvNamaPasien);
        list = new ArrayList<String>();
        list.add("Pilih pasien");
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        tvTambahPasien = (TextView)findViewById(R.id.btnTambahPasien);
        tvTambahPasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PatientFormActivity.class);
                Bundle b = new Bundle();
                b.putString("userID",userID);
                b.putInt("kodeAkses",1);
                intent.putExtras(b);
                startActivityForResult(intent, 8);
            }
        });
        spnDataPasien.setAdapter(arrayAdapter);
        spnDataPasien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(idStatus != null && namaPasien != null && noHpPasien!=null){
                    relativeStatusID = idStatus[position];
                    tvNamaPasien.setText(namaPasien[position]);
                    tvNOHP.setText(noHpPasien[position]);
                    btnBuatJanji.setEnabled(true);
                }
                else{
                    btnBuatJanji.setEnabled(false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        editdokter = (ImageView)findViewById(R.id.iveditdokter);
//        editjadwal = (ImageView)findViewById(R.id.iveditjadwal);
//        editdokter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myIntent = new Intent(view.getContext(), TestScroolView.class);
//                startActivityForResult(myIntent, 0);
//            }
//        });
//        editjadwal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myIntent = new Intent(view.getContext(), BookingActivity.class);
//                startActivityForResult(myIntent, 0);
//            }
//        });

        btnBuatJanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new KonfirmasiJanjiTask(userID,customerID,facilityCategoryID,facilityID,status,institutionID,
                        date,weekProgramID,dayProgramID,dayProgramDetailID,personnelID, relativeStatusID).execute();
//                Intent myIntent = new Intent(view.getContext(), MyAppointmentActivity.class);
//                startActivityForResult(myIntent, 0);
            }
        });
        new PatientListAsyncTask(userID).execute();
    }
    public class KonfirmasiJanjiTask extends AsyncTask<String, Void, String> {
        private String mUserID;
        private String mCustomerID;
        private String mFacilityCategoryID;
        private String mFacilityID;
        private String mStatus;
        private String mInstitutionID;;
        private String mDate;
        private String mWeekProgramID;
        private String mDayProgramID;
        private String mDayProgramDetailID;
        private String mPersonnelID;
        private int mFamilyMemberType;
        KonfirmasiJanjiTask(String userID, String customerID, String facilityCategoryID, String facilityID,
                            String status, String institutionID, String date, String weekProgramID, String dayProgramID,
                            String dayProgramDetailID, String personnelID, int familyMemberType) {
            this.mUserID = userID;
            this.mCustomerID = customerID;
            this.mFacilityCategoryID = facilityCategoryID;
            this.mFacilityID = facilityID;
            this.mStatus = status;
            this.mInstitutionID = institutionID;
            this.mDate = date;
            this.mWeekProgramID = weekProgramID;
            this.mDayProgramID = dayProgramID;
            this.mDayProgramDetailID = dayProgramDetailID;
            this.mPersonnelID = personnelID;
            this.mFamilyMemberType = familyMemberType;
        }


        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/KlikSembuhapi/api/transactions/PostAppointmentTransaction");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("FacilityCategoryID",mFacilityCategoryID);
                    jsonObject.put("FacilityID",mFacilityID);
                    jsonObject.put("UserID",mUserID);
                    jsonObject.put("CustomerID",mCustomerID);
                    jsonObject.put("Status",mStatus);
                    jsonObject.put("InstitutionID",mInstitutionID);
                    jsonObject.put("date",mDate);
                    jsonObject.put("weekProgramID",mWeekProgramID);
                    jsonObject.put("dayProgramID",mDayProgramID);
                    jsonObject.put("dayProgramDetailID",mDayProgramDetailID);
                    jsonObject.put("personnelID",mPersonnelID);
                    jsonObject.put("familyMemberType", mFamilyMemberType);

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
                        if(responseCode == 404){
                            cek = true;
                        }
                        else {
                            cek =false;
                        }
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
            if (success!="") {
                Toast.makeText(getApplicationContext(), "Telah berhasil membuat janji.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MyAppointmentActivity.class);
                Bundle b = new Bundle();
                b.putString("userID",userID);
                intent.putExtras(b);
                startActivity(intent);
                finish();

            } else {
                //:TODO
                if(cek==true){
                    Toast.makeText(getApplicationContext(), "Data pasien belum terisi.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), PatientFormActivity.class);
                    Bundle b = new Bundle();
                    b.putString("userID",userID);
                    intent.putExtras(b);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Gagal membuat janji.", Toast.LENGTH_LONG).show();
                }

            }
        }
        @Override
        protected void onCancelled() {

        }
    }

    public Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public class ImageDrawable extends AsyncTask<String, Void, Drawable>{

        String image;
        public ImageDrawable(String image){
            this.image  = image;
        }
        @Override
        protected Drawable doInBackground(String... params) {
            //return null;
            Drawable imageDrawable = LoadImageFromWebOperations(image);

            return imageDrawable;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {

            super.onPostExecute(drawable);
            ivDoc_Pic.setImageDrawable(drawable);

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8) {
            if(resultCode == RESULT_OK) {
                userID = data.getStringExtra("userID");
                new PatientListAsyncTask(userID).execute();
            }
        }
    }
    public class PatientListAsyncTask extends AsyncTask<String, Void, String> {
        String pUserID;


        public PatientListAsyncTask(String pUserID){
            this.pUserID = pUserID;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            list = new ArrayList<String>();
            if(success!=""){
                try{
                    JSONArray jsonArray = new JSONArray(success);
                    idStatus = new int[jsonArray.length()];
                    namaPasien = new String[jsonArray.length()];
                    noHpPasien = new String[jsonArray.length()];
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String fName = jsonObject.getString("FirstName");
                        String lName = jsonObject.getString("LastName");
                        String closeRelativePhoneNbr = jsonObject.getString("CloseRelativePhoneNbr");
                        String status = jsonObject.getString("relativeStatusDesc");
                        int relativeStatus = jsonObject.getInt("RelativeStatus");
                        idStatus [i]=relativeStatus;
                        namaPasien [i]= fName + " " + lName;
                        noHpPasien[i] = closeRelativePhoneNbr;
                        if(list.contains(status)){
                            //TODO
                        }
                        else{
                            //TODO
                            list.add(status);
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(KonfirmasiJanjiActivity.this,android.R.layout.simple_spinner_item,list);
                        spnDataPasien.setAdapter(arrayAdapter);

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
            else {
                //TODO
                list.add("Tidak ada data pasien");
            }
//            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,list);
//            spnDataPasien.setAdapter(arrayAdapter);

        }
    }
}
