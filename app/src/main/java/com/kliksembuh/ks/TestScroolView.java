package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kliksembuh.ks.library.DoctorListAdapter;
import com.kliksembuh.ks.library.HttpHandler;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class TestScroolView extends AppCompatActivity implements OnMapReadyCallback {
    public static final String EXTRA_NAME = "cheese_name";
    private GoogleMap mMap;
    ViewPager viewPager;
    private int[] layouts;
    private ScrollView scrollView;
    private ObservableScrollView mScrollView;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private List<Doctor> mDokterList;
    List<String> list;
    private ViewPagerAdapter viewPagerAdapter;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ListView lvDokter;
    private DoctorListAdapter dAdapter;
    private ProgressDialog pDialog;
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

    int a =0;

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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            lvDokter.setNestedScrollingEnabled(true);
//        }
//        lvDokter.setNestedScrollingEnabled(true);

        //lvDokter.setNestedScrollingEnabled(true);
        spinner = (Spinner)findViewById(R.id.dplistdokter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TestScroolView.this,
                android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        layouts = new int[]{
                R.drawable.doctorlist1,
                R.drawable.doctorlist2,
                R.drawable.doctorlist3};
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager = (ViewPager)findViewById(R.id.backdrop);
        viewPager.setAdapter(viewPagerAdapter);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapdoctorlistcoba);
        mapFragment.getMapAsync(this);
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
                //Your id
                //.putExtra("userID",userID);
                myIntent.putExtras(b);
                //.putExtra("userID",userID);
                startActivityForResult(myIntent, 0);
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng bogor = new LatLng(-6.595038, 106.816635);
        mMap.addMarker(new MarkerOptions().position(bogor).title("Bogor"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bogor,16.0f));
        mMap.setMapType(mMap.MAP_TYPE_TERRAIN);
        mMap.getUiSettings().setZoomControlsEnabled(true);

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
            imageView.setImageResource(layouts[position]);

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
    private class GetContacts extends AsyncTask<Void, Void, Void> {
        private Context context;

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
        protected Void doInBackground(Void... args0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            try{
                JSONObject obj = new JSONObject(loadJSONFromAsset());

                // Getting JSON Array node
                JSONArray contacts = obj.getJSONArray("dokter");

                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);

                    if(c.getString("Rs").equals(rumahSakitID)){
                        String name = c.getString("name");
//                        idDokter[a] = c.getString("id");
//                        idDokterInt[a]=a;
                        String id = c.getString("id");
                        String image = c.getString("imgUrl");
                        urlImage[i] = image;
                        Drawable image1 = LoadImageFromWebOperations(image);
                        String alamat = c.getString("alamat");
                        mDokterList.add(new Doctor(id, image1, name, alamat));
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            dAdapter = new DoctorListAdapter(getApplicationContext(), mDokterList);
//            hAdapter.getFilter().filter(searchView);

            lvDokter.setAdapter(dAdapter);
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
                    URL url = new URL("http://cloud.basajans.com:8868/BS.HealthCare.Application/api/MedicalPersonnel/SearchMedicalPersonnelBasedOnInstitutions?institution="+mInstitution+"&facility="+mFacility);
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
                        urlImage=new String[(jsonArray.length())];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            String id = jsonObject.getString("MedicalPersonnelID");
//                            String personelCD = jsonObject.getString("MedicalPersonnelID");
//                            idDokter[i]=personelCD;
//                            String name = jsonObject.getString("Name");
                            String image= jsonObject.getString("ImgUrl");
                            drawableDokter[i]=LoadImageFromWebOperations(image);
                            urlImage[i]=image;
//                            Drawable image1 = LoadImageFromWebOperations(image);
//                            String alamat = jsonObject.getString("Address");
//                            mDokterList.add(new Doctor(id, image1, name, alamat));
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
                        idDokter[i]=personelCD;
                        String name = jsonObject.getString("Name");
                        String alamat = jsonObject.getString("Address");
                        if(drawableDokter.length <= 0){
                            mDokterList.add(new Doctor(id, null, name, alamat));

                        }else{
                            mDokterList.add(new Doctor(id, drawableDokter[i], name, alamat));
                        }
                        dAdapter = new DoctorListAdapter(getApplicationContext(), mDokterList);
                        lvDokter.setAdapter(dAdapter);
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

}
