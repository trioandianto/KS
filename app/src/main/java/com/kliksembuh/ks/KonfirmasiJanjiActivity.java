package com.kliksembuh.ks;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

public class KonfirmasiJanjiActivity extends AppCompatActivity {
    private Button btnBuatJanji;
    private ImageView editjadwal;
    private ImageView editdokter;
    private TextView tvNamaDokter;
    private TextView tvSpesial;
    private TextView tvNamaRumahSakit;
    private TextView tvNamaHari;
    private TextView tvDetailTanggal;
    private String userID;
    private String customerID;
    private String facilityCategoryID;
    private String facilityID;
    private String status;
    private String institutionID;;
    private String date;
    private String weekProgramID;
    private String dayProgramID;
    private String dayProgramDetailID;
    private String personnelID;
    private String personnelCD;
    private String namaDokter;
    private String namaRumahSakit;
    private String namaHari;
    private String jam;
    private String detailTanggal;

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
            detailTanggal = b.getString("namaTanggal");
//            dayProgramDetailID = b.getString("DetailID");
            dayProgramDetailID = "4";
            jam = b.getString("jam");
            personnelID = b.getString("idDokter");
            personnelCD = b.getString("personilID");
            namaDokter = b.getString("namaDokter");
            namaRumahSakit = b.getString("namaRumahSakit");
        }

        tvNamaHari = (TextView)findViewById(R.id.tvNamaHari);
        tvNamaHari.setText(namaHari);
        tvDetailTanggal = (TextView) findViewById(R.id.tvDetailTangal);
        tvDetailTanggal.setText(detailTanggal+" @"+jam);
        tvNamaDokter = (TextView)findViewById(R.id.tvnamaDokter);
        tvNamaDokter.setText(namaDokter);
        tvSpesial =(TextView)findViewById(R.id.tvspesial);
        tvSpesial.setText("Dokter Umum");
        tvNamaRumahSakit=(TextView)findViewById(R.id.tvnamaRumahSakit);
        tvNamaRumahSakit.setText(namaRumahSakit);
        btnBuatJanji = (Button)findViewById(R.id.btnbuatjanji);
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
                        date,weekProgramID,dayProgramID,dayProgramDetailID,personnelID).execute();
                Toast.makeText(getApplicationContext(), "Telah berhasil membuat janji.", Toast.LENGTH_SHORT).show();
//                Intent myIntent = new Intent(view.getContext(), MyAppointmentActivity.class);
//                startActivityForResult(myIntent, 0);
            }
        });
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
        KonfirmasiJanjiTask(String userID, String customerID, String facilityCategoryID, String facilityID,
                            String status, String institutionID, String date, String weekProgramID, String dayProgramID,
                            String dayProgramDetailID, String personnelID) {
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
                        Toast.makeText(getApplicationContext(), "Gagal membuat janji.", Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(getApplicationContext(), MyAppointmentActivity.class);
                intent.putExtra("userID",userID);
//                Bundle b = new Bundle();
//                b.putString("userID", userID);
//                startActivityForResult(i, 1);
                startActivity(intent);
                finish();

            } else {
                //:TODO
            }
        }
        @Override
        protected void onCancelled() {

        }
    }
}
