package com.kliksembuh.ks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.kliksembuh.ks.library.JadwalDokterAdapter;
import com.kliksembuh.ks.models.JadwalDokter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class BookingActivity extends AppCompatActivity {
    private TextView jadwal;
    private String personalID;
    private String year;
    private String week="1";
    private List<JadwalDokter> mJadwalDokterList;
    private JadwalDokterAdapter jAdapter;
    private ListView lvJadwal;
    private Button btnMingguIni;
    private Button btnMingguDepan;
    private Spinner spnHari;
    private String sequence="minggu 1";
    private ImageView imgDokter;
    private TextView tvNamaDokter;
    private TextView tvJenisSpesialisasi;

    private int cDay;
    private int cMonth;
    private int cYear;
    private int day;
    private int lastDay;
    private Date tanggal;
    private String namaDokter;
    private String idDokter;
    private String urlImage;
    private String dokterSpesialisasi;

    public Drawable getImageDokter() {
        return imageDokter;
    }

    public void setImageDokter(Drawable imageDokter) {
        this.imageDokter = imageDokter;
    }

    private Drawable imageDokter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            personalID = b.getString("userID");
            namaDokter = b.getString("namaDokter");
            idDokter = b.getString("idDokter");
            urlImage = b.getString("urlImage");
            dokterSpesialisasi = "Dokter Umum";
        }
        imgDokter = (ImageView)findViewById(R.id.iv_doc_picdetail);
        Drawable img = LoadImageFromWebOperations(urlImage);
        int id = getResources().getIdentifier("com.kliksembuh.ks:drawable/"+urlImage,null,null);
        imgDokter.setImageResource(id);
//        imgDokter.setImageResource();
        tvNamaDokter = (TextView) findViewById(R.id.tv_drname_detail);
        tvNamaDokter.setText(namaDokter);
        tvJenisSpesialisasi = (TextView)findViewById(R.id.tv_drspecialty_detail);
        tvJenisSpesialisasi.setText(dokterSpesialisasi);

        lvJadwal = (ListView)findViewById(R.id.lvjadwal);
        mJadwalDokterList = new ArrayList<>();
        spnHari = (Spinner)findViewById(R.id.spnHari);
        List<String> list = new ArrayList<String>();
        list.add("Minggu");
        list.add("Senin");
        list.add("Selasa");
        list.add("Rabu");
        list.add("Kamis");
        list.add("Jumat");
        list.add("Sabtu");

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        spnHari.setAdapter(arrayAdapter);


        Calendar calendar = Calendar.getInstance();
        cDay = calendar.get(Calendar.DAY_OF_MONTH);
        cMonth = calendar.get(Calendar.MONTH);
        cYear = calendar.get(Calendar.YEAR);
        day = calendar.get(Calendar.DAY_OF_WEEK);
        lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        tanggal = new Date(cYear,cMonth,cDay);

        personalID="MD0001";
        year = Integer.toString(cYear);
        spnHari.setSelection(day-1);
        spnHari.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mJadwalDokterList = new ArrayList<>();
                day = position+1;
                new JadwalDokterAsync(personalID, year, week).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//        new JadwalDokterAsync(personalID, year, week).execute();
        btnMingguIni = (Button)findViewById(R.id.btnMingguIni);
        btnMingguIni.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJadwalDokterList = new ArrayList<>();
                sequence = "minggu 1";
                new JadwalDokterAsync(personalID, year, week).execute();

            }
        });
        btnMingguDepan = (Button)findViewById(R.id.btnMingguDepan);
        btnMingguDepan.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                mJadwalDokterList = new ArrayList<>();
                sequence = "minggu 2";
                new JadwalDokterAsync(personalID, year, week).execute();
            }
        });

        //  http://basajans/kliksembuhapi/api/Schedules/GetWeeklySchedule?personnelId=MD0001&year=2017&startweek=1


    }
    public class JadwalDokterAsync extends AsyncTask<String, Void, String> {
        private String mPersonilID;
        private String mYear;
        private String mWeek;
        JadwalDokterAsync(String personilID, String year, String week) {
            mPersonilID = personilID;
            mYear = year;
            mWeek=week;
        }


        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.basajans.com:8868/BS.HealthCare.Application/api/Schedules/GetWeeklySchedule?personnelId="+mPersonilID+"&year="+mYear+"&startweek="+mWeek);
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setRequestProperty("Content-Type","application/json");
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
                        JSONArray jsonArray = new JSONArray(sb.toString());
                        int length  = jsonArray.length();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if(jsonObject.getString("Sequence").equals(sequence)){
                                if(day ==1 ){
                                    JSONObject sunday = (JSONObject) jsonObject.get("SundayDPID");
                                    JSONArray detailArray = sunday.getJSONArray("DayProgramDetail");
                                    for (int j=0;j<detailArray.length();j++){
                                        JSONObject c = detailArray.getJSONObject(j);
                                        String starDate = c.getString("StartTime");
                                        String endDate = c.getString("EndTime");
                                        String date = starDate+" - "+endDate;
                                        mJadwalDokterList.add(new JadwalDokter(date));
                                    }


                                }
                                else if(day==2){
                                    JSONObject monday = (JSONObject) jsonObject.get("MondayDPID");
                                    JSONArray detailArray = monday.getJSONArray("DayProgramDetail");
                                    for (int j=0;j<detailArray.length();j++){
                                        JSONObject c = detailArray.getJSONObject(j);
                                        String starDate = c.getString("StartTime");
                                        String endDate = c.getString("EndTime");
                                        String date = starDate+" - "+endDate;
                                        mJadwalDokterList.add(new JadwalDokter(date));
                                    }
                                }

                                else if (day ==3 ){
                                    JSONObject tuesday = (JSONObject) jsonObject.get("TuesdayDPID");
                                    JSONArray detailArray = tuesday.getJSONArray("DayProgramDetail");
                                    for (int j=0;j<detailArray.length();j++){
                                        JSONObject c = detailArray.getJSONObject(j);
                                        String starDate = c.getString("StartTime");
                                        String endDate = c.getString("EndTime");
                                        String date = starDate+" - "+endDate;
                                        mJadwalDokterList.add(new JadwalDokter(date));
                                    }

                                }
                                else if(day ==4 ){
                                    JSONObject wednesday = (JSONObject) jsonObject.get("WednesdayDPID");
                                    JSONArray detailArray = wednesday.getJSONArray("DayProgramDetail");
                                    for (int j=0;j<detailArray.length();j++){
                                        JSONObject c = detailArray.getJSONObject(j);
                                        String starDate = c.getString("StartTime");
                                        String endDate = c.getString("EndTime");
                                        String date = starDate+" - "+endDate;
                                        mJadwalDokterList.add(new JadwalDokter(date));
                                    }

                                }
                                else if(day ==5 ){
                                    JSONObject thursday = (JSONObject) jsonObject.get("ThursdayDPID");
                                    JSONArray detailArray = thursday.getJSONArray("DayProgramDetail");
                                    for (int j=0;j<detailArray.length();j++){
                                        JSONObject c = detailArray.getJSONObject(j);
                                        String starDate = c.getString("StartTime");
                                        String endDate = c.getString("EndTime");
                                        String date = starDate+" - "+endDate;
                                        mJadwalDokterList.add(new JadwalDokter(date));
                                    }

                                }
                                else if(day ==6 ){
                                    JSONObject friday = (JSONObject) jsonObject.get("FridayDPID");
                                    JSONArray detailArray = friday.getJSONArray("DayProgramDetail");
                                    for (int j=0;j<detailArray.length();j++){
                                        JSONObject c = detailArray.getJSONObject(j);
                                        String starDate = c.getString("StartTime");
                                        String endDate = c.getString("EndTime");
                                        String date = starDate+" - "+endDate;
                                        mJadwalDokterList.add(new JadwalDokter(date));
                                    }

                                }
                                else{
                                    JSONObject saturday = (JSONObject) jsonObject.get("SaturdayDPID");
                                    JSONArray detailArray = saturday.getJSONArray("DayProgramDetail");
                                    for (int j=0;j<detailArray.length();j++){
                                        JSONObject c = detailArray.getJSONObject(j);
                                        String starDate = c.getString("StartTime");
                                        String endDate = c.getString("EndTime");
                                        String date = starDate+" - "+endDate;
                                        mJadwalDokterList.add(new JadwalDokter(date));
                                    }

                                }

                            }
                        }

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

            if (success!="") {

                jAdapter = new JadwalDokterAdapter(getApplicationContext(),mJadwalDokterList);
                lvJadwal.setAdapter(jAdapter);
            } else {
                //:TODO
                mJadwalDokterList = new ArrayList<>();
                jAdapter = new JadwalDokterAdapter(getApplicationContext(),mJadwalDokterList);
                lvJadwal.setAdapter(jAdapter);
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
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("hospital.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
