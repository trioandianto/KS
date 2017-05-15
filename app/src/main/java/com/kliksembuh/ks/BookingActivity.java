package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kliksembuh.ks.library.JadwalDokterAdapter;
import com.kliksembuh.ks.models.JadwalDokter;
import com.kliksembuh.ks.models.PraktekDokter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class BookingActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    private TextView jadwal;
    private String personalID;
    private String year;
    private String month;
    private String sday;
    private String week="1";
    private List<JadwalDokter> mJadwalDokterList;
    private List<PraktekDokter> mPraktekDokter;
    private PraktekDokter pAdapter;
    private ListView lvPraktekDokter;
    private ProgressDialog pDialog;
    private JadwalDokterAdapter jAdapter;
    private ListView lvJadwal;
    private Button btnMingguIni;
    private Button btnMingguDepan;
    private Spinner spnHari;
    private String sequence="minggu 1";
    private ImageView imgDokter;
    private TextView tvFirstTitle;
    private TextView tvNamaDokter;
    private TextView tvJenisSpesialisasi;
    private TextView tvDate;

    private int cDay;
    private int cMonth;
    private int cYear;
    private int day;
    private int lastDay;
    private Date tanggal;
    private String namaDokter;
    private String idDokter;
    private String urlImage;
    private String firstTitle;
    private String dokterSpesialisasi;
    private int seninMingguIni;
    private int selasaMingguIni;
    private int rabuMingguIni;
    private int kamisMingguIni;
    private int jumatMingguIni;
    private int sabtuMingguIni;
    private int mingguMingguIni;
    private int hariMingguini;
    private String bulanIni;
    private int tahunIni;
    private int seninMingguDepan;
    private int selasaMingguDepan;
    private int rabuMingguDepan;
    private int kamisMingguDepan;
    private int jumatMingguDepan;
    private int sabtuMingguDepan;
    private int mingguMingguDepan;
    private String weekProgramID;
    private String dayProgramID;
    private String scheduleShift;
    private String programDetailID;
    private String rumahSakitID;
    private String facilityID;
    private String userID;
    private String alamat;
    private String namaRumahSakit;
    private String date;
    private String namaDate;
    private int tanggala;
    private int bulan;
    private int tahun;
    private String waktuBerobat;
    private String namaHari;
    private String[] praktekDokter;
    public Context contextInstance;
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
            userID = b.getString("userID");
            personalID = b.getString("personalID");
            namaDokter = b.getString("namaDokter");
            idDokter = b.getString("idDokter");
            urlImage = b.getString("urlImage");
            firstTitle = b.getString("firstTtlDoc");
            dokterSpesialisasi = b.getString("specialtyDoc");
            rumahSakitID = b.getString("rumahSakitID");
            facilityID = b.getString("facilityID");
            alamat = b.getString("alamat");
            namaRumahSakit = b.getString("namaRumahSakit");
            praktekDokter = b.getStringArray("praktekDokter");

        }
        Toolbar newToolbar = (Toolbar)findViewById(R.id.toolbarJadwalDokter);
        setSupportActionBar(newToolbar);
        newToolbar.setTitle("Jadwal Dokter");
        setSupportActionBar(newToolbar);
        getWindow().setStatusBarColor(ContextCompat.getColor(BookingActivity.this, R.color.colorPrimaryDark));
        imgDokter = (ImageView)findViewById(R.id.iv_doc_picdetail);
        tvFirstTitle = (TextView) findViewById(R.id.tv_frontTitleDoc);
        tvFirstTitle.setText(firstTitle);
        tvNamaDokter = (TextView) findViewById(R.id.tv_drname_detail);
        tvNamaDokter.setText(namaDokter);
        tvJenisSpesialisasi = (TextView)findViewById(R.id.tv_drspecialty_detail);
        tvJenisSpesialisasi.setText(dokterSpesialisasi);
        tvDate = (TextView)findViewById(R.id.tvDate);

        new ImageDrawable(urlImage).execute();



        lvJadwal = (ListView)findViewById(R.id.lvjadwal);
        lvJadwal.setOnItemClickListener(this);
        mJadwalDokterList = new ArrayList<>();
        //lvPraktekDokter = (ListView)findViewById(R.id.lvRumahSakitDokter);
        mPraktekDokter = new ArrayList<>();
        spnHari = (Spinner)findViewById(R.id.spnHari);
        List<String> list = new ArrayList<String>();
        list.add("Senin");
        list.add("Selasa");
        list.add("Rabu");
        list.add("Kamis");
        list.add("Jumat");
        list.add("Sabtu");
        list.add("Minggu");

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_dropdown_item,list);
        spnHari.setAdapter(arrayAdapter);

        Date dt = new Date();
        int as = dt.getYear();
        int ab = dt.getMonth();
        int at = dt.getDay();

        Calendar calendar = Calendar.getInstance();
        cDay = calendar.get(Calendar.DAY_OF_MONTH);
        cMonth = calendar.get(Calendar.MONTH);
        cYear = calendar.get(Calendar.YEAR);
        day = calendar.get(Calendar.DAY_OF_WEEK);
        hariMingguini = day;
        lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        if(day == 1){
            mingguMingguIni = cDay;
            seninMingguIni = mingguMingguIni + 1;
            if (seninMingguIni > lastDay){
                seninMingguIni = seninMingguIni - lastDay;
            }
            selasaMingguIni = seninMingguIni + 1;
            if (selasaMingguIni > lastDay){
                selasaMingguIni = selasaMingguIni - lastDay;
            }
            rabuMingguIni = selasaMingguIni + 1;
            if (rabuMingguIni > lastDay){
                rabuMingguIni = rabuMingguIni - lastDay;
            }
            kamisMingguIni = rabuMingguIni + 1;
            if (kamisMingguIni > lastDay){
                kamisMingguIni = kamisMingguIni - lastDay;
            }
            jumatMingguIni = kamisMingguIni + 1;
            if (jumatMingguIni > lastDay){
                jumatMingguIni = jumatMingguIni - lastDay;
            }
            sabtuMingguIni = jumatMingguIni + 1;
            if (sabtuMingguIni > lastDay){
                sabtuMingguIni = sabtuMingguIni - lastDay;
            }
            mingguMingguDepan = sabtuMingguIni + 1;
            if (mingguMingguDepan > lastDay){
                mingguMingguDepan = mingguMingguDepan - lastDay;
            }
            seninMingguDepan = mingguMingguDepan + 1;
            if (seninMingguDepan > lastDay){
                seninMingguDepan = seninMingguDepan - lastDay;
            }
            selasaMingguDepan = seninMingguDepan + 1;
            if (seninMingguIni > lastDay){
                selasaMingguDepan = selasaMingguDepan - lastDay;
            }
            rabuMingguDepan = selasaMingguDepan + 1;
            if (rabuMingguDepan > lastDay){
                rabuMingguDepan = rabuMingguDepan - lastDay;
            }
            kamisMingguDepan = rabuMingguDepan+ 1;
            if (kamisMingguDepan > lastDay){
                kamisMingguDepan = kamisMingguDepan - lastDay;
            }
            jumatMingguDepan = kamisMingguDepan + 1;
            if (jumatMingguDepan > lastDay){
                jumatMingguDepan = jumatMingguDepan - lastDay;
            }
            sabtuMingguDepan = jumatMingguDepan + 1;
            if (sabtuMingguDepan > lastDay){
                sabtuMingguDepan = sabtuMingguDepan - lastDay;
            }

        }
        else if(day == 2){
            seninMingguIni=cDay;
            selasaMingguIni = seninMingguIni + 1;
            if (selasaMingguIni > lastDay){
                selasaMingguIni = selasaMingguIni - lastDay;
            }
            rabuMingguIni = selasaMingguIni + 1;
            if (rabuMingguIni > lastDay){
                rabuMingguIni = rabuMingguIni - lastDay;
            }
            kamisMingguIni = rabuMingguIni + 1;
            if (kamisMingguIni > lastDay){
                kamisMingguIni = kamisMingguIni - lastDay;
            }
            jumatMingguIni = kamisMingguIni + 1;
            if (jumatMingguIni > lastDay){
                jumatMingguIni = jumatMingguIni - lastDay;
            }
            sabtuMingguIni = jumatMingguIni + 1;
            if (seninMingguIni > lastDay){
                seninMingguIni = seninMingguIni - lastDay;
            }
            mingguMingguIni = sabtuMingguIni + 1;
            if (seninMingguIni > lastDay){
                seninMingguIni = seninMingguIni - lastDay;
            }
            seninMingguDepan = mingguMingguIni + 1;
            if (seninMingguDepan > lastDay){
                seninMingguDepan = seninMingguDepan - lastDay;
            }
            selasaMingguDepan = seninMingguDepan + 1;
            if (seninMingguIni > lastDay){
                selasaMingguDepan = selasaMingguDepan - lastDay;
            }
            rabuMingguDepan = selasaMingguDepan + 1;
            if (rabuMingguDepan > lastDay){
                rabuMingguDepan = rabuMingguDepan - lastDay;
            }
            kamisMingguDepan = rabuMingguDepan+ 1;
            if (kamisMingguDepan > lastDay){
                kamisMingguDepan = kamisMingguDepan - lastDay;
            }
            jumatMingguDepan = kamisMingguDepan + 1;
            if (jumatMingguDepan > lastDay){
                jumatMingguDepan = jumatMingguDepan - lastDay;
            }
            sabtuMingguDepan = jumatMingguDepan + 1;
            if (sabtuMingguDepan > lastDay){
                sabtuMingguDepan = sabtuMingguDepan - lastDay;
            }
            mingguMingguDepan = sabtuMingguDepan + 1;
            if(mingguMingguDepan > lastDay){
                mingguMingguDepan = mingguMingguDepan - lastDay;
            }
        }
        else if(day == 3){
            selasaMingguIni = cDay;
            seninMingguIni = selasaMingguIni - 1;
            if (seninMingguIni < 1){
                seninMingguIni = seninMingguIni - lastDay;
            }
            rabuMingguIni = selasaMingguIni + 1;
            if (rabuMingguIni > lastDay){
                rabuMingguIni = rabuMingguIni - lastDay;
            }
            kamisMingguIni = rabuMingguIni + 1;
            if (kamisMingguIni > lastDay){
                kamisMingguIni = kamisMingguIni - lastDay;
            }
            jumatMingguIni = kamisMingguIni + 1;
            if (jumatMingguIni > lastDay){
                jumatMingguIni = jumatMingguIni - lastDay;
            }
            sabtuMingguIni = jumatMingguIni + 1;
            if (seninMingguIni > lastDay){
                seninMingguIni = seninMingguIni - lastDay;
            }
            mingguMingguIni = sabtuMingguIni + 1;
            if (seninMingguIni > lastDay){
                seninMingguIni = seninMingguIni - lastDay;
            }
            seninMingguDepan = mingguMingguIni + 1;
            if (seninMingguDepan > lastDay){
                seninMingguDepan = seninMingguDepan - lastDay;
            }
            selasaMingguDepan = seninMingguDepan + 1;
            if (seninMingguIni > lastDay){
                selasaMingguDepan = selasaMingguDepan - lastDay;
            }
            rabuMingguDepan = selasaMingguDepan + 1;
            if (rabuMingguDepan > lastDay){
                rabuMingguDepan = rabuMingguDepan - lastDay;
            }
            kamisMingguDepan = rabuMingguDepan+ 1;
            if (kamisMingguDepan > lastDay){
                kamisMingguDepan = kamisMingguDepan - lastDay;
            }
            jumatMingguDepan = kamisMingguDepan + 1;
            if (jumatMingguDepan > lastDay){
                jumatMingguDepan = jumatMingguDepan - lastDay;
            }
            sabtuMingguDepan = jumatMingguDepan + 1;
            if (sabtuMingguDepan > lastDay){
                sabtuMingguDepan = sabtuMingguDepan - lastDay;
            }
            mingguMingguDepan = sabtuMingguDepan + 1;
            if(mingguMingguDepan > lastDay){
                mingguMingguDepan = mingguMingguDepan - lastDay;
            }
        }
        else if(day == 4){
            rabuMingguIni = cDay;
            selasaMingguIni = rabuMingguIni - 1;
            if (selasaMingguIni < 1){
                selasaMingguIni = selasaMingguIni - lastDay;
            }
            seninMingguIni = selasaMingguIni - 1;
            if (seninMingguIni < 1){
                seninMingguIni = seninMingguIni - lastDay;
            }
            kamisMingguIni = rabuMingguIni+ 1;
            if (kamisMingguIni > lastDay){
                kamisMingguIni = kamisMingguIni - lastDay;
            }
            jumatMingguIni = kamisMingguIni + 1;
            if (jumatMingguIni > lastDay){
                jumatMingguIni = jumatMingguIni - lastDay;
            }
            sabtuMingguIni = jumatMingguIni + 1;
            if (sabtuMingguIni > lastDay){
                sabtuMingguIni = sabtuMingguIni - lastDay;
            }
            mingguMingguIni = sabtuMingguIni + 1;
            if(mingguMingguIni > lastDay){
                mingguMingguIni = mingguMingguIni - lastDay;
            }
            seninMingguDepan = mingguMingguIni + 1;
            if (seninMingguDepan > lastDay){
                seninMingguDepan = seninMingguDepan - lastDay;
            }
            selasaMingguDepan = seninMingguDepan + 1;
            if (seninMingguIni > lastDay){
                selasaMingguDepan = selasaMingguDepan - lastDay;
            }
            rabuMingguDepan = selasaMingguDepan + 1;
            if (rabuMingguDepan > lastDay){
                rabuMingguDepan = rabuMingguDepan - lastDay;
            }
            kamisMingguDepan = rabuMingguDepan+ 1;
            if (kamisMingguDepan > lastDay){
                kamisMingguDepan = kamisMingguDepan - lastDay;
            }
            jumatMingguDepan = kamisMingguDepan + 1;
            if (jumatMingguDepan > lastDay){
                jumatMingguDepan = jumatMingguDepan - lastDay;
            }
            sabtuMingguDepan = jumatMingguDepan + 1;
            if (sabtuMingguDepan > lastDay){
                sabtuMingguDepan = sabtuMingguDepan - lastDay;
            }
            mingguMingguDepan = sabtuMingguDepan + 1;
            if(mingguMingguDepan > lastDay){
                mingguMingguDepan = mingguMingguDepan - lastDay;
            }

        }
        else if(day == 5){
            kamisMingguIni =cDay;
            rabuMingguIni = kamisMingguIni - 1;
            if (rabuMingguIni < 1){
                rabuMingguIni = rabuMingguIni - lastDay;
            }
            selasaMingguIni = rabuMingguIni - 1;
            if (selasaMingguIni < 1){
                selasaMingguIni = selasaMingguIni - lastDay;
            }
            seninMingguIni = selasaMingguIni - 1;
            if (seninMingguIni < 1){
                seninMingguIni = seninMingguIni - lastDay;
            }
            jumatMingguIni = kamisMingguIni + 1;
            if (jumatMingguIni > lastDay){
                jumatMingguIni = jumatMingguIni - lastDay;
            }
            sabtuMingguIni = jumatMingguIni + 1;
            if (sabtuMingguIni > lastDay){
                sabtuMingguIni = sabtuMingguIni - lastDay;
            }
            mingguMingguIni = sabtuMingguIni + 1;
            if(mingguMingguIni > lastDay){
                mingguMingguIni = mingguMingguIni - lastDay;
            }
            seninMingguDepan = mingguMingguIni + 1;
            if (seninMingguDepan > lastDay){
                seninMingguDepan = seninMingguDepan - lastDay;
            }
            selasaMingguDepan = seninMingguDepan + 1;
            if (seninMingguIni > lastDay){
                selasaMingguDepan = selasaMingguDepan - lastDay;
            }
            rabuMingguDepan = selasaMingguDepan + 1;
            if (rabuMingguDepan > lastDay){
                rabuMingguDepan = rabuMingguDepan - lastDay;
            }
            kamisMingguDepan = rabuMingguDepan+ 1;
            if (kamisMingguDepan > lastDay){
                kamisMingguDepan = kamisMingguDepan - lastDay;
            }
            jumatMingguDepan = kamisMingguDepan + 1;
            if (jumatMingguDepan > lastDay){
                jumatMingguDepan = jumatMingguDepan - lastDay;
            }
            sabtuMingguDepan = jumatMingguDepan + 1;
            if (sabtuMingguDepan > lastDay){
                sabtuMingguDepan = sabtuMingguDepan - lastDay;
            }
            mingguMingguDepan = sabtuMingguDepan + 1;
            if(mingguMingguDepan > lastDay){
                mingguMingguDepan = mingguMingguDepan - lastDay;
            }

        }
        else if(day == 6){
            jumatMingguIni = cDay;
            kamisMingguIni = jumatMingguIni - 1;
            if (kamisMingguIni < 1){
                kamisMingguIni = kamisMingguIni - lastDay;
            }
            rabuMingguIni = kamisMingguIni - 1;
            if (rabuMingguIni < 1){
                rabuMingguIni = rabuMingguIni - lastDay;
            }
            selasaMingguIni = rabuMingguIni - 1;
            if (selasaMingguIni < 1){
                selasaMingguIni = selasaMingguIni - lastDay;
            }
            seninMingguIni = selasaMingguIni - 1;
            if (seninMingguIni < 1){
                seninMingguIni = seninMingguIni - lastDay;
            }
            sabtuMingguIni = jumatMingguIni + 1;
            if (sabtuMingguIni > lastDay){
                sabtuMingguIni = sabtuMingguIni - lastDay;
            }
            mingguMingguIni = sabtuMingguIni + 1;
            if(mingguMingguIni > lastDay){
                mingguMingguIni = mingguMingguIni - lastDay;
            }
            seninMingguDepan = mingguMingguIni + 1;
            if (seninMingguDepan > lastDay){
                seninMingguDepan = seninMingguDepan - lastDay;
            }
            selasaMingguDepan = seninMingguDepan + 1;
            if (seninMingguIni > lastDay){
                selasaMingguDepan = selasaMingguDepan - lastDay;
            }
            rabuMingguDepan = selasaMingguDepan + 1;
            if (rabuMingguDepan > lastDay){
                rabuMingguDepan = rabuMingguDepan - lastDay;
            }
            kamisMingguDepan = rabuMingguDepan+ 1;
            if (kamisMingguDepan > lastDay){
                kamisMingguDepan = kamisMingguDepan - lastDay;
            }
            jumatMingguDepan = kamisMingguDepan + 1;
            if (jumatMingguDepan > lastDay){
                jumatMingguDepan = jumatMingguDepan - lastDay;
            }
            sabtuMingguDepan = jumatMingguDepan + 1;
            if (sabtuMingguDepan > lastDay){
                sabtuMingguDepan = sabtuMingguDepan - lastDay;
            }
            mingguMingguDepan = sabtuMingguDepan + 1;
            if(mingguMingguDepan > lastDay){
                mingguMingguDepan = mingguMingguDepan - lastDay;
            }

        }
        else{
            sabtuMingguIni = cDay;
            jumatMingguIni = sabtuMingguIni - 1;
            if (jumatMingguIni < 1){
                jumatMingguIni = jumatMingguIni - lastDay;
            }
            kamisMingguIni = jumatMingguIni - 1;
            if (kamisMingguIni < 1){
                kamisMingguIni = kamisMingguIni - lastDay;
            }
            rabuMingguIni = kamisMingguIni - 1;
            if (rabuMingguIni < 1){
                rabuMingguIni = rabuMingguIni - lastDay;
            }
            selasaMingguIni = rabuMingguIni - 1;
            if (selasaMingguIni < 1){
                selasaMingguIni = selasaMingguIni - lastDay;
            }
            seninMingguIni = selasaMingguIni - 1;
            if (seninMingguIni < 1){
                seninMingguIni = seninMingguIni - lastDay;
            }
            mingguMingguIni = sabtuMingguIni + 1;
            if(mingguMingguIni > lastDay){
                mingguMingguIni = mingguMingguIni - lastDay;
            }
            seninMingguDepan = mingguMingguIni + 1;
            if (seninMingguDepan > lastDay){
                seninMingguDepan = seninMingguDepan - lastDay;
            }
            selasaMingguDepan = seninMingguDepan + 1;
            if (seninMingguIni > lastDay){
                selasaMingguDepan = selasaMingguDepan - lastDay;
            }
            rabuMingguDepan = selasaMingguDepan + 1;
            if (rabuMingguDepan > lastDay){
                rabuMingguDepan = rabuMingguDepan - lastDay;
            }
            kamisMingguDepan = rabuMingguDepan+ 1;
            if (kamisMingguDepan > lastDay){
                kamisMingguDepan = kamisMingguDepan - lastDay;
            }
            jumatMingguDepan = kamisMingguDepan + 1;
            if (jumatMingguDepan > lastDay){
                jumatMingguDepan = jumatMingguDepan - lastDay;
            }
            sabtuMingguDepan = jumatMingguDepan + 1;
            if (sabtuMingguDepan > lastDay){
                sabtuMingguDepan = sabtuMingguDepan - lastDay;
            }
            mingguMingguDepan = sabtuMingguDepan + 1;
            if(mingguMingguDepan > lastDay){
                mingguMingguDepan = mingguMingguDepan - lastDay;
            }

        }



        year = Integer.toString(cYear);
        month = Integer.toString(cMonth+1);
        sday = Integer.toString(cDay);
        tanggal = new Date(cYear,cMonth,cDay);
        spnHari.setSelection(day-2);
        spnHari.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mJadwalDokterList = new ArrayList<>();
                day = position+2;
                namaHari = spnHari.getSelectedItem().toString();
                tahun = cYear;
                if (sequence=="minggu 1"){
                    if(day ==8){
                        tanggala = mingguMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==2){
                        tanggala = seninMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==3){
                        tanggala = selasaMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==4){
                        tanggala = rabuMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==5){
                        tanggala = kamisMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==6){
                        tanggala = jumatMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else{
                        tanggala = sabtuMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                }
                else{
                    if(day ==1){
                        tanggala = mingguMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==2){
                        tanggala = seninMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==3){
                        tanggala = selasaMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==4){
                        tanggala = rabuMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==5){
                        tanggala = kamisMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }


                    }
                    else if(day ==6){
                        tanggala = jumatMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }


                    }
                    else {
                        tanggala = sabtuMingguDepan;
                        bulan = cMonth + 1;
                        if (tanggala > lastDay) {
                            bulan = bulan + 1;
                        } else if (tanggala < 1) {
                            bulan = bulan - 1;
                        }
                    }

                }
                if (bulan ==1){
                    bulanIni ="Januari";
                }
                else if(bulan ==2){
                    bulanIni ="Februari";
                }
                else if(bulan ==3){

                    bulanIni ="Maret";
                }
                else if(bulan ==4){

                    bulanIni ="April";
                }
                else if(bulan ==5){

                    bulanIni ="Mei";
                }
                else {
                    bulanIni ="Juni";

                }
                tvDate.setText(tanggala+" "+bulanIni+" "+tahun);
                namaDate = tanggala+" "+bulanIni+" "+tahun;
                date = bulan+"-"+tanggala+"-"+tahun;

                new JadwalDokterAsync(personalID, year, week).execute();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        new JadwalDokterAsync(personalID, year, week).execute();
        btnMingguIni = (Button)findViewById(R.id.btnMingguIni);
        btnMingguIni.setBackgroundTintList(ContextCompat.getColorStateList(BookingActivity.this, R.color.signup_color));
        btnMingguDepan = (Button)findViewById(R.id.btnMingguDepan);

        btnMingguIni.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJadwalDokterList = new ArrayList<>();
                sequence = "minggu 1";
                if (sequence=="minggu 1"){
                    if(day ==8){
                        tanggala = mingguMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==2){
                        tanggala = seninMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==3){
                        tanggala = selasaMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==4){
                        tanggala = rabuMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==5){
                        tanggala = kamisMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==6){
                        tanggala = jumatMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else{
                        tanggala = sabtuMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                }
                else{
                    if(day ==1){
                        tanggala = mingguMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==2){
                        tanggala = seninMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==3){
                        tanggala = selasaMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==4){
                        tanggala = rabuMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==5){
                        tanggala = kamisMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }


                    }
                    else if(day ==6){
                        tanggala = jumatMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }


                    }
                    else {
                        tanggala = sabtuMingguDepan;
                        bulan = cMonth + 1;
                        if (tanggala > lastDay) {
                            bulan = bulan + 1;
                        } else if (tanggala < 1) {
                            bulan = bulan - 1;
                        }
                    }

                }
                if (bulan ==1){
                    bulanIni ="Januari";
                }
                else if(bulan ==2){
                    bulanIni ="Februari";
                }
                else if(bulan ==3){

                    bulanIni ="Maret";
                }
                else if(bulan ==4){

                    bulanIni ="April";
                }
                else if(bulan ==5){

                    bulanIni ="Mei";
                }
                else {
                    bulanIni ="Juni";

                }
                tvDate.setText(tanggala+" "+bulanIni+" "+tahun);
                namaDate = tanggala+" "+bulanIni+" "+tahun;
                date = bulan+"-"+tanggala+"-"+tahun;
                new JadwalDokterAsync(personalID, year, week).execute();
                btnMingguIni.setBackgroundTintList(ContextCompat.getColorStateList(BookingActivity.this, R.color.signup_color));
                btnMingguDepan.setBackgroundTintList(ContextCompat.getColorStateList(BookingActivity.this, R.color.frameBackground));


            }
        });


        btnMingguDepan.setBackgroundTintList(ContextCompat.getColorStateList(BookingActivity.this, R.color.frameBackground));
        btnMingguDepan.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                mJadwalDokterList = new ArrayList<>();
                btnMingguIni.setBackgroundTintList(ContextCompat.getColorStateList(BookingActivity.this, R.color.frameBackground));
                btnMingguDepan.setBackgroundTintList(ContextCompat.getColorStateList(BookingActivity.this, R.color.signup_color));
                sequence = "minggu 2";
                if (sequence=="minggu 1"){
                    if(day ==8){
                        tanggala = mingguMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==2){
                        tanggala = seninMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==3){
                        tanggala = selasaMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==4){
                        tanggala = rabuMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==5){
                        tanggala = kamisMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==6){
                        tanggala = jumatMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else{
                        tanggala = sabtuMingguIni;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                }
                else{
                    if(day ==1){
                        tanggala = mingguMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==2){
                        tanggala = seninMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==3){
                        tanggala = selasaMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==4){
                        tanggala = rabuMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }
                    }
                    else if(day ==5){
                        tanggala = kamisMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }


                    }
                    else if(day ==6){
                        tanggala = jumatMingguDepan;
                        bulan = cMonth+ 1;
                        if(tanggala > lastDay){
                            bulan = bulan + 1;
                        }
                        else if (tanggala < 1){
                            bulan = bulan -1;
                        }


                    }
                    else {
                        tanggala = sabtuMingguDepan;
                        bulan = cMonth + 1;
                        if (tanggala > lastDay) {
                            bulan = bulan + 1;
                        } else if (tanggala < 1) {
                            bulan = bulan - 1;
                        }
                    }

                }
                if (bulan ==1){
                    bulanIni ="Januari";
                }
                else if(bulan ==2){
                    bulanIni ="Februari";
                }
                else if(bulan ==3){

                    bulanIni ="Maret";
                }
                else if(bulan ==4){

                    bulanIni ="April";
                }
                else if(bulan ==5){

                    bulanIni ="Mei";
                }
                else {
                    bulanIni ="Juni";

                }
                tvDate.setText(tanggala+" "+bulanIni+" "+tahun);
                namaDate = tanggala+" "+bulanIni+" "+tahun;
                date = bulan+"-"+tanggala+"-"+tahun;
                new JadwalDokterAsync(personalID, year, week).execute();
            }
        });

        //  http://basajans/kliksembuhapi/api/Schedules/GetWeeklySchedule?personnelId=MD0001&year=2017&startweek=1


    }
    public static void setButtonTint(Button button, ColorStateList tint) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP && button instanceof AppCompatButton) {
            ((AppCompatButton) button).setBackgroundTintList(tint);
        } else {
            ViewCompat.setBackgroundTintList(button, tint);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                userID = data.getStringExtra("userID");
                personalID = data.getStringExtra("personalID");
                date = data.getStringExtra("date");
                namaDokter = data.getStringExtra("namaDokter");
                idDokter = data.getStringExtra("idDokter");
                urlImage = data.getStringExtra("urlImage");
                dokterSpesialisasi = data.getStringExtra("specialtyDoc");
                namaHari = data.getStringExtra("namaHari");
                rumahSakitID = data.getStringExtra("rumahSakitID");
                facilityID = data.getStringExtra("facilityID");
                namaRumahSakit = data.getStringExtra("namaRumahSakit");
                namaDate = data.getStringExtra("namaTanggal");
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object object = parent.getAdapter().getItem(position);
        JadwalDokter jadwalDokter = (JadwalDokter)object;
        String wpId = jadwalDokter.getWeekProgramID();
        String dpID = jadwalDokter.getDayProdramID();
        String dpdetail = jadwalDokter.getDetailProgramID();
        String waktuBerobat = scheduleShift;
        String jamMulai = jadwalDokter.getStartDate();
        String jamBerakhir = jadwalDokter.getEndDate();
        if(sequence=="minggu 1" && hariMingguini > day){
            Toast.makeText(getApplicationContext(), "Jadwal Kadaluarsa", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent myIntent = new Intent(getApplicationContext(),KonfirmasiJanjiActivity.class);
            Bundle b = new Bundle();
            b.putString("DPID",dpID);
            b.putString("WPID",wpId);
            b.putString("date", date);
            b.putString("DetailID",dpdetail);
            b.putString("dokterSpesialisasi", dokterSpesialisasi);
            b.putString("namaHari", namaHari);
            b.putString("namaTanggal", namaDate);
            b.putString("personilID", personalID);
            b.putString("facilityID", facilityID);
            b.putString("programDetailID", programDetailID);
            b.putString("waktuBerobat", waktuBerobat);
            b.putString("jamMulai", jamMulai);
            b.putString("jamBerakhir", jamBerakhir);
            b.putString("rumahSakitID", rumahSakitID);
            b.putString("urlImage", urlImage);
            b.putString("idDokter", idDokter);
            b.putString("userID",userID);
            b.putString("firstTitle", firstTitle);
            b.putString("alamat", alamat);
            b.putString("namaDokter",namaDokter);
            b.putString("namaRumahSakit",namaRumahSakit);
            myIntent.putExtras(b);
            startActivityForResult(myIntent, 1);
        }


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
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(BookingActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/kliksembuhapi/api/Schedules/GetWeeklySchedule?personnelId="+mPersonilID+"&year="+mYear+"&startweek="+mWeek);
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

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if(jsonObject.getString("Sequence").equals(sequence)){
                                weekProgramID = jsonObject.getString("name");
                                if(day ==1 ){
                                    JSONObject sunday = (JSONObject) jsonObject.get("SundayDPID");
                                    dayProgramID = sunday.getString("Name");
                                    JSONArray detailArray = sunday.getJSONArray("DayProgramDetail");
                                    for (int j=0;j<detailArray.length();j++){
                                        JSONObject c = detailArray.getJSONObject(j);
                                        programDetailID = c.getString("DPLineID");
                                        String starDate = c.getString("StartTime");
                                        String endDate = c.getString("EndTime");
                                        scheduleShift = c.getJSONObject("ShiftSchedule").getString("ShiftScheduleCD");

                                        // Get date from string
                                        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
                                        Date dateTimeStart = dateFormatter.parse(starDate);
                                        Date dateTimeEnd = dateFormatter.parse(endDate);

                                        // Get time from date
                                        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
                                        String timeStart = timeFormatter.format(dateTimeStart);
                                        String timeEnd = timeFormatter.format(dateTimeEnd);
                                        mJadwalDokterList.add(new JadwalDokter(timeStart, timeEnd,dayProgramID,weekProgramID,programDetailID));
                                    }


                                }
                                else if(day==2){
                                    JSONObject monday = (JSONObject) jsonObject.get("MondayDPID");
                                    dayProgramID = monday.getString("Name");
                                    JSONArray detailArray = monday.getJSONArray("DayProgramDetail");
                                    for (int j=0;j<detailArray.length();j++){
                                        JSONObject c = detailArray.getJSONObject(j);
                                        programDetailID = c.getString("DPLineID");
                                        String starDate = c.getString("StartTime");
                                        String endDate = c.getString("EndTime");
//                                        JSONArray shiftArray = c.getJSONArray("ShiftSchedule");
//                                        for (int k = 0; k < shiftArray.length(); k++){
//                                            JSONObject shiftObjectInner = shiftArray.getJSONObject(k);
//                                            scheduleShift = shiftObjectInner.getString("ShiftScheduleCD");
//                                        }
                                        scheduleShift = c.getJSONObject("ShiftSchedule").getString("ShiftScheduleCD");

                                        // Get date from string
                                        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
                                        Date dateTimeStart = dateFormatter.parse(starDate);
                                        Date dateTimeEnd = dateFormatter.parse(endDate);

                                        // Get time from date
                                        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
                                        String timeStart = timeFormatter.format(dateTimeStart);
                                        String timeEnd = timeFormatter.format(dateTimeEnd);
                                        mJadwalDokterList.add(new JadwalDokter(timeStart, timeEnd,dayProgramID,weekProgramID,programDetailID));
                                    }
                                }

                                else if (day ==3 ){
                                    JSONObject tuesday = (JSONObject) jsonObject.get("TuesdayDPID");
                                    dayProgramID = tuesday.getString("Name");
                                    JSONArray detailArray = tuesday.getJSONArray("DayProgramDetail");
                                    for (int j=0;j<detailArray.length();j++){
                                        JSONObject c = detailArray.getJSONObject(j);
                                        programDetailID = c.getString("DPLineID");
                                        String starDate = c.getString("StartTime");
                                        String endDate = c.getString("EndTime");
                                        scheduleShift = c.getJSONObject("ShiftSchedule").getString("ShiftScheduleCD");

                                        // Get date from string
                                        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
                                        Date dateTimeStart = dateFormatter.parse(starDate);
                                        Date dateTimeEnd = dateFormatter.parse(endDate);

                                        // Get time from date
                                        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
                                        String timeStart = timeFormatter.format(dateTimeStart);
                                        String timeEnd = timeFormatter.format(dateTimeEnd);
                                        mJadwalDokterList.add(new JadwalDokter(timeStart, timeEnd,dayProgramID,weekProgramID,programDetailID));
                                    }

                                }
                                else if(day ==4 ){
                                    JSONObject wednesday = (JSONObject) jsonObject.get("WednesdayDPID");
                                    dayProgramID = wednesday.getString("Name");
                                    JSONArray detailArray = wednesday.getJSONArray("DayProgramDetail");
                                    for (int j=0;j<detailArray.length();j++){
                                        JSONObject c = detailArray.getJSONObject(j);
                                        programDetailID = c.getString("DPLineID");
                                        String starDate = c.getString("StartTime");
                                        String endDate = c.getString("EndTime");
                                        scheduleShift = c.getJSONObject("ShiftSchedule").getString("ShiftScheduleCD");

                                        // Get date from string
                                        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
                                        Date dateTimeStart = dateFormatter.parse(starDate);
                                        Date dateTimeEnd = dateFormatter.parse(endDate);

                                        // Get time from date
                                        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
                                        String timeStart = timeFormatter.format(dateTimeStart);
                                        String timeEnd = timeFormatter.format(dateTimeEnd);
                                        mJadwalDokterList.add(new JadwalDokter(timeStart, timeEnd,dayProgramID,weekProgramID,programDetailID));
                                    }

                                }
                                else if(day ==5 ){
                                    JSONObject thursday = (JSONObject) jsonObject.get("ThursdayDPID");
                                    dayProgramID = thursday.getString("Name");
                                    JSONArray detailArray = thursday.getJSONArray("DayProgramDetail");
                                    for (int j=0;j<detailArray.length();j++){
                                        JSONObject c = detailArray.getJSONObject(j);
                                        programDetailID = c.getString("DPLineID");
                                        String starDate = c.getString("StartTime");
                                        String endDate = c.getString("EndTime");
                                        scheduleShift = c.getJSONObject("ShiftSchedule").getString("ShiftScheduleCD");

                                        // Get date from string
                                        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
                                        Date dateTimeStart = dateFormatter.parse(starDate);
                                        Date dateTimeEnd = dateFormatter.parse(endDate);

                                        // Get time from date
                                        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
                                        String timeStart = timeFormatter.format(dateTimeStart);
                                        String timeEnd = timeFormatter.format(dateTimeEnd);
                                        mJadwalDokterList.add(new JadwalDokter(timeStart, timeEnd,dayProgramID,weekProgramID,programDetailID));
                                    }

                                }
                                else if(day ==6 ){
                                    JSONObject friday = (JSONObject) jsonObject.get("FridayDPID");
                                    dayProgramID = friday.getString("Name");
                                    JSONArray detailArray = friday.getJSONArray("DayProgramDetail");
                                    for (int j=0;j<detailArray.length();j++){
                                        JSONObject c = detailArray.getJSONObject(j);
                                        programDetailID = c.getString("DPLineID");
                                        String starDate = c.getString("StartTime");
                                        String endDate = c.getString("EndTime");
                                        scheduleShift = c.getJSONObject("ShiftSchedule").getString("ShiftScheduleCD");

                                        // Get date from string
                                        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
                                        Date dateTimeStart = dateFormatter.parse(starDate);
                                        Date dateTimeEnd = dateFormatter.parse(endDate);

                                        // Get time from date
                                        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
                                        String timeStart = timeFormatter.format(dateTimeStart);
                                        String timeEnd = timeFormatter.format(dateTimeEnd);
                                        mJadwalDokterList.add(new JadwalDokter(timeStart, timeEnd,dayProgramID,weekProgramID,programDetailID));
                                    }

                                }
                                else{
                                    JSONObject saturday = (JSONObject) jsonObject.get("SaturdayDPID");
                                    dayProgramID = saturday.getString("Name");
                                    JSONArray detailArray = saturday.getJSONArray("DayProgramDetail");
                                    for (int j=0;j<detailArray.length();j++){
                                        JSONObject c = detailArray.getJSONObject(j);
                                        programDetailID = c.getString("DPLineID");
                                        String starDate = c.getString("StartTime");
                                        String endDate = c.getString("EndTime");
                                        scheduleShift = c.getJSONObject("ShiftSchedule").getString("ShiftScheduleCD");

                                        // Get date from string
                                        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
                                        Date dateTimeStart = dateFormatter.parse(starDate);
                                        Date dateTimeEnd = dateFormatter.parse(endDate);

                                        // Get time from date
                                        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
                                        String timeStart = timeFormatter.format(dateTimeStart);
                                        String timeEnd = timeFormatter.format(dateTimeEnd);
                                        mJadwalDokterList.add(new JadwalDokter(timeStart, timeEnd,dayProgramID,weekProgramID,programDetailID));
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

        @Override
        protected void onPostExecute(final String success) {
            if (pDialog.isShowing())
                pDialog.dismiss();
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
            imgDokter.setImageDrawable(drawable);

        }
    }
}
