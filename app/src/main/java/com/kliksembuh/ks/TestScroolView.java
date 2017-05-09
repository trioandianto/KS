package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.kliksembuh.ks.library.DoctorListAdapter;
import com.kliksembuh.ks.library.ObservableScrollView;
import com.kliksembuh.ks.models.Doctor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;


public class TestScroolView extends AppCompatActivity{
    public static final String EXTRA_NAME = "cheese_name";

    ViewPager viewPager;
    private Drawable[] layouts;
    private ScrollView scrollView;
    private ObservableScrollView mScrollView;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private List<Doctor> mDokterList;
    private List specialtyString;
    List<String> list;
    private ViewPagerAdapter viewPagerAdapter;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ListView lvDokter;
    private DoctorListAdapter dAdapter;
    private ProgressDialog pDialog;
    private String spesial;
    private Spinner spinner;
    private String rumahSakitID;
    private String facility;
    private String toolbarTitle;
    private String [] idDokter;
    public String [] urlImage;
    private int [] idDokterInt;
    private int[] listArr;
    private String userID;
    private NestedScrollView nsDokter;
    private Drawable drawableDokter[];
    private Bitmap bitMapDokter[];
    private String [] praktekDokter;
    private TextView ivMaps;
    private String alamat;
    // Slider for ViewPager
    int currentPage = 0;
    int NUM_PAGES = 4;
    Timer timer;
    final long DELAY_MS = 5000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 10000; // time in milliseconds between successive task executions.


    int a = 0;

    private static final String[]paths = {"Dokter Umum", "Dokter Gigi", "Dokter Mata"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
            rumahSakitID = b.getString("institution");
            facility = b.getString("facilityID");
            toolbarTitle = b.getString("tittle");
            alamat = b.getString("alamat");
            spesial = b.getString("facilityName");
        }
        setContentView(R.layout.activity_test_scrool_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(toolbarTitle);
        setSupportActionBar(toolbar);


        mDokterList = new ArrayList<>();
        list = new ArrayList<String>();
//        nsDokter = (NestedScrollView)findViewById(R.id.nsDokter);
//        nsDokter.setFillViewport(true);
//        nsDokter.setClickable(true);

        lvDokter = (ListView)findViewById(R.id.lvDetailRumahSakit);
        spinner = (Spinner)findViewById(R.id.spn_SpecialtyDoc);
        List<String> list = new ArrayList<String>();
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                spesial =  parent.getItemAtPosition(position).toString();
//                new DokterListAsync(rumahSakitID,facility).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            lvDokter.setNestedScrollingEnabled(true);
//        }
//        lvDokter.setNestedScrollingEnabled(true);

        lvDokter.setNestedScrollingEnabled(true);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TestScroolView.this,
                android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TextView tvLhtFasilitas = (TextView) findViewById(R.id.tvLihatFasilitas);
        tvLhtFasilitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(TestScroolView.this,FacilityActivity.class);
                startActivity(i);
            }
        });
        ivMaps = (TextView)findViewById(R.id.ivMaps);
        ivMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(TestScroolView.this ,MapsDetailHospitalActivity.class);
                Bundle b = new Bundle();
                b.putString("namaRumahSakit", toolbarTitle);
                b.putString("alamat", alamat);
                myIntent.putExtras(b);
                //.putExtra("userID",userID);
                startActivityForResult(myIntent, 1);
            }
        });
        new SlideShowAsync(rumahSakitID).execute();
        viewPager = (ViewPager)findViewById(R.id.backdrop);

        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES-1) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapdoctorlistcoba);
//        mapFragment.getMapAsync(this);
//        cardView = (CardView)findViewById(R.id.cvdoktera);
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myIntent = new Intent(view.getContext(), BookingActivity.class);
//                startActivityForResult(myIntent, 0);
//            }
//        });
        //toolbar.addView(spinner);

//        new GetContacts().execute();
        new DokterListAsync(rumahSakitID,facility).execute();
        lvDokter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = parent.getAdapter().getItem(position);
                Doctor dokter = (Doctor) object;
                String dokterID =  dokter.getDoc_id();
                String namaDokter = dokter.getNameDoc();
                Drawable imgUrl = dokter.getDoc_pic_id();
                BookingActivity bookingActivity = new BookingActivity();
                bookingActivity.setImageDokter(imgUrl);

                Intent myIntent = new Intent(TestScroolView.this ,BookingActivity.class);
                Bundle b = new Bundle();
                b.putString("idDokter", dokterID);
                b.putString("userID", userID);
                b.putString("personalID",idDokter[position]);
                b.putString("namaDokter",namaDokter);
                b.putString("urlImage",urlImage[position]);
                b.putString("rumahSakitID",rumahSakitID);
                b.putString("facilityID", facility);
                b.putString("namaRumahSakit", toolbarTitle);
                b.putStringArray("praktekDokter", praktekDokter);
                //Your id
                //.putExtra("userID",userID);
                myIntent.putExtras(b);
                //.putExtra("userID",userID);
                startActivityForResult(myIntent, 1);
            }
        });


    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                userID = data.getStringExtra("userID");
                rumahSakitID = data.getStringExtra("institution");
                facility = data.getStringExtra("facilityID");
                toolbarTitle = data.getStringExtra("tittle");
            }
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        Context context;
        public ViewPagerAdapter(Context context){
            this.context = context;
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position){

            View itemView = layoutInflater.inflate(R.layout.doctor_image_slide, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.doctorImageView);
            imageView.setImageDrawable(layouts[position]);

            container.addView(itemView);

            return itemView;

        }
        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("dokter.json");
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
    public Bitmap StringToBitMap(String encodedString){
        try {
            URL url = new URL(encodedString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
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
    public class DokterListAsync extends AsyncTask<String, Void, String> {
        private String mInstitution;
        private String mFacility;
        DokterListAsync(String institution, String facility) {
            mInstitution = institution;
            mFacility = facility;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(TestScroolView.this);
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
                    URL url = new URL("http://cloud.abyor.com:11080/kliksembuhapi/api/MedicalPersonnel/SearchMedicalPersonnelBasedOnInstitutions?institution="+mInstitution+"&facility=");
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
                        JSONArray jsonArray = new JSONArray(sb.toString());
                        drawableDokter = new Drawable[jsonArray.length()];
                        bitMapDokter = new Bitmap[jsonArray.length()];
                        urlImage=new String[(jsonArray.length())];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String image= jsonObject.getString("ImgUrl");
                            bitMapDokter[i]=StringToBitMap(image);
//                            drawableDokter[i]=LoadImageFromWebOperations(image);
                            urlImage[i]=image;
//
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
            List<String> list = new ArrayList<String>();
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (success!="") {

                try{
                    JSONArray jsonArray = new JSONArray(success);
                    idDokter=new String[jsonArray.length()];
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("MedicalPersonnelID");
                        String personelCD = jsonObject.getString("MedicalPersonnelCD");
                        String image = jsonObject.getString("ImgUrl");


                        idDokter[i]=personelCD;
                        String name = jsonObject.getString("Name");
                        // to do; change alamat to Doctor Specialty
                        String alamat = jsonObject.getString("HealthFacilityDesc");

                         //List specialty doctor in Spinner
                        if(list.contains(alamat)){
                            //TODO
                        }
                        else{
                            //TODO
                            list.add(alamat);
                        }

                        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_style,list);
                        arrayAdapter.setDropDownViewResource
                                (R.layout.spinner_dropdown_item);
                        spinner.setAdapter(arrayAdapter);
                        JSONArray jsonArray1 = jsonObject.getJSONArray("Institute");
                        for (int j=0;j<jsonArray1.length();j++){
                            praktekDokter = new String[jsonArray1.length()];
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                            praktekDokter[j] = jsonObject1.getString("InstitutionName");
                            TextView tvNameHosp = (TextView)findViewById(R.id.tvHospitalName);
                            tvNameHosp.setText(jsonObject1.getString("InstitutionName"));
                        }
                        if(spesial.equals(alamat)){
                            mDokterList.add(new Doctor(id, name, alamat, image));
                        }

                        dAdapter = new DoctorListAdapter(getApplicationContext(), mDokterList);
                        lvDokter.setAdapter(dAdapter);
                        
//                        new ImageDrawable(mDokterList).execute();
                    }
                    for (Doctor currentDokter : mDokterList) {
                        new ImageDrawable(currentDokter).execute();
                    }


                }catch (JSONException e){
                    e.printStackTrace();
                }

            } else {
                //:TODO
            }
        }
        @Override
        protected void onCancelled() {

        }
    }
    public class SlideShowAsync extends AsyncTask<String , Void, String>    {
        String idInstitution;
        public SlideShowAsync(String idInstitution){
            this.idInstitution = idInstitution;
        }

        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/KlikSembuhAPI/api/Institutions/SearchInstitutionById/"+idInstitution);
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
        protected void onPostExecute(final String success) {
            if (success!="") {

                try{
                    JSONArray jsonArray = new JSONArray(success);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONArray jsonArray1 = jsonObject.getJSONArray("InstitutionImages");
                        layouts = new Drawable[jsonArray1.length()];
                        for (int j=0;j<jsonArray1.length();j++){
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                            String img = jsonObject1.getString("ImagePath");
                            layouts[j] = LoadImageFromWebOperations(img);
                        }
                    }
                    viewPagerAdapter = new ViewPagerAdapter(TestScroolView.this);
                    viewPager.setAdapter(viewPagerAdapter);

                }catch (JSONException e){
                    e.printStackTrace();
                }

            } else {
                //:TODO
            }


        }
    }
    public class ImageDrawable extends AsyncTask<String, Void, Drawable>{

        Doctor doctor;
        public ImageDrawable(Doctor doctor){
            this.doctor  = doctor;
        }
        @Override
        protected Drawable doInBackground(String... params) {
            //return null;
            Drawable imageDrawable = LoadImageFromWebOperations(this.doctor.getImageUrl());

            return imageDrawable;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {

            super.onPostExecute(drawable);
            this.doctor.setDoc_pic_id(drawable);
        }
    }
}
