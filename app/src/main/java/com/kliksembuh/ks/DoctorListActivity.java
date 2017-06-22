package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.kliksembuh.ks.library.ObservableScrollView;
import com.kliksembuh.ks.models.Doctor;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;


public class DoctorListActivity extends AppCompatActivity{
    public static final String EXTRA_NAME = "cheese_name";

    ViewPager viewPager;
    private String[] layouts;
    private String [] tvLayouts;
    private ScrollView scrollView;
    private NestedScrollView nsDokter;
    private ObservableScrollView mScrollView;
    private LinearLayout dotsLayout;
    private TextView tvPesan;
    private ArrayList<Doctor> mDokterList;
    List<String> list;
    private ViewPagerAdapter viewPagerAdapter;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView lvDokter;
    private RecycleAdapter rAdapter;
    private ProgressDialog pDialog;
    private String spesial;
    private Spinner spinner, spnFilter;
    private String rumahSakitID;
    private String facility;
    private String toolbarTitle;
    private String [] idDokter;
    private String userID;
    private String [] praktekDokter;
    private android.support.design.widget.FloatingActionButton ivMaps;
    private Drawable load;
    private int sorting;
    private String alamat;
    // Slider for ViewPager
    int currentPage = 0;
    int NUM_PAGES = 4;
    Timer timer;
    final long DELAY_MS = 5000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.

    // Google Maps
    private SupportMapFragment mapFragment;

    int a = 0;

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
        setContentView(R.layout.activity_doctor_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(toolbarTitle);



        mDokterList = new ArrayList<>();
        list = new ArrayList<String>();
        load = getResources().getDrawable(R.drawable.pic_loading_small);
        //nsDokter = (NestedScrollView)findViewById(R.id.nsDokter);
//        nsDokter.setFillViewport(true);
//        nsDokter.getParent().requestChildFocus(nsDokter, nsDokter);
//        nsDokter.setClickable(true);

        lvDokter = (RecyclerView)findViewById(R.id.rvDetailRumahSakit);
        rAdapter = new RecycleAdapter(getApplicationContext(), mDokterList);
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new  LinearLayoutManager(getApplicationContext());
        lvDokter.setLayoutManager(mLayoutManager);
        lvDokter.setHasFixedSize(true);
        lvDokter.scrollToPosition(rAdapter.NON_VISIBLE_ITEMS);
        lvDokter.setNestedScrollingEnabled(false);
       //lvDokter.setLayoutManager(mLayoutManager);
        lvDokter.setAdapter(rAdapter);
        spinner = (Spinner)findViewById(R.id.spn_SpecialtyDoc);
        spnFilter = (Spinner)findViewById(R.id.spn_filter);
        List<String> list1 = new ArrayList<String>();
        list1.add("A-Z");
        list1.add("Z-A");
        ArrayAdapter arrayAdapter1 = new ArrayAdapter<String>(this,R.layout.spinner_style,list1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFilter.setAdapter(arrayAdapter1);
        spnFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sorting = position;
                if(rAdapter!=null){
                    rAdapter.filterAndSorting(position, spesial);
                    lvDokter.setAdapter(rAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapListHospital);
        List<String> list = new ArrayList<String>();
        list.add(spesial);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_style,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spesial =  parent.getItemAtPosition(position).toString();
                if (rAdapter!=null){
                    rAdapter.filterAndSorting(sorting, spesial);
                    lvDokter.setAdapter(rAdapter);
                }
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
        TextView tvLhtFasilitas = (TextView) findViewById(R.id.tvLihatFasilitas);
        tvLhtFasilitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(DoctorListActivity.this,FacilityActivity.class);
                startActivity(i);
            }
        });

        ivMaps = (android.support.design.widget.FloatingActionButton)findViewById(R.id.ivMaps);
        ivMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DoctorListActivity.this ,MapsDetailHospitalActivity.class);
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
        new DokterListAsync(rumahSakitID).execute();
//        lvDokter.clic(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Object object = parent.getAdapter().getItem(position);
//                Doctor dokter = (Doctor) object;
//                String dokterID =  dokter.getDoc_id();
//                String firstTtlDoc = dokter.getFrontTtlDoc();
//                String namaDokter = dokter.getNameDoc();
//                String specialtyDoc = dokter.getSpecialty();
//                //Drawable imageDr = dokter.getDoc_pic_id();
//                String urlImg = dokter.getImageUrl();
//                BookingActivity bookingActivity = new BookingActivity();
//               // bookingActivity.setImageDokter(imageDr);
//
//                Intent myIntent = new Intent(DoctorListActivity.this ,BookingActivity.class);
//                Bundle b = new Bundle();
//                b.putString("idDokter", dokterID);
//                b.putString("userID", userID);
//                b.putString("personalID",idDokter[position]);
//                b.putString("firstTtlDoc", firstTtlDoc);
//                b.putString("namaDokter",namaDokter);
//                b.putString("specialtyDoc", specialtyDoc);
//                b.putString("urlImage",urlImg);
//                b.putString("alamat", alamat);
//                b.putString("rumahSakitID",rumahSakitID);
//                b.putString("facilityID", facility);
//                b.putString("namaRumahSakit", toolbarTitle);
//                b.putStringArray("praktekDokter", praktekDokter);
//                //Your id
//                //.putExtra("userID",userID);
//                myIntent.putExtras(b);
//                //.putExtra("userID",userID);
//                startActivityForResult(myIntent, 1);
//            }
//        });

        // Kirim Pesan
        tvPesan = (TextView)findViewById(R.id.tv_KirimPesan);
        tvPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendSMS("085280065285","Coba coba aja.!");
            }
        });

    }
    // Send message
    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
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
            TextView tv_num_view =(TextView)itemView.findViewById(R.id.tv_num_view);
            Picasso.with(context).load(layouts[position]).into(imageView);
            tv_num_view.setText(tvLayouts[position]);

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
            container.removeView((RelativeLayout) object);
        }
    }
    public class DokterListAsync extends AsyncTask<String, Void, String> {
        private String mInstitution;
        DokterListAsync(String institution) {
            mInstitution = institution;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(DoctorListActivity.this);
            pDialog.setMessage("Mohon menunggu...");
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
                        String tittle = "MSC, Sp. MK";
                        idDokter[i]=personelCD;
                        String frontTitle = jsonObject.getString("FrontTitle");
                        String name = jsonObject.getString("Name");
                        // to do; change alamat to Doctor Specialty
                        String spesiality = jsonObject.getString("HealthFacilityDesc");

                         //List specialty doctor in Spinner
                        if(list.contains(spesiality)){
                            //TODO
                        }
                        else{
                            //TODO
                            list.add(spesiality);
                        }

                        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(DoctorListActivity.this,R.layout.spinner_style,list);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(arrayAdapter);
                        JSONArray jsonArray1 = jsonObject.getJSONArray("Institute");
                        for (int j=0;j<jsonArray1.length();j++){
                            praktekDokter = new String[jsonArray1.length()];
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                            praktekDokter[j] = jsonObject1.getString("InstitutionName");
//                            TextView tvNameHosp = (TextView)findViewById(R.id.tvHospitalName);
//                            tvNameHosp.setText(jsonObject1.getString("InstitutionName"));
                        }

                        mDokterList.add(new Doctor(personelCD, image, frontTitle, name, spesiality, image,"Lihat Kualifikasi",tittle));


                        
//                        new ImageDrawable(mDokterList).execute();
                    }
                    Collections.sort(mDokterList, new Comparator<Doctor>(){
                        @Override
                        public int compare(Doctor lhs, Doctor rhs) {
                            return lhs.getNameDoc().compareTo(rhs.getNameDoc());
                        }

                    });
                    rAdapter = new RecycleAdapter(getApplicationContext(), mDokterList);
//                        dAdapter.filter(spesial);
                    lvDokter.setAdapter(rAdapter);
//                    for (Doctor currentDokter : mDokterList) {
//                        new ImageDrawable(currentDokter).execute();
//                    }


                }catch (JSONException e){
                    e.printStackTrace();
                }
                //mapFragment.getMapAsync(AppointmentDetailActivity.this);

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
                        int length = jsonArray1.length();
                        layouts = new String[length];
                        tvLayouts = new String[length];
                        for (int j=0;j<jsonArray1.length();j++){
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                            String img = jsonObject1.getString("ImagePath");
                            layouts[j] = img;
                            tvLayouts[j] = j+1 +"/"+ length;
                        }
                    }
                    viewPagerAdapter = new ViewPagerAdapter(DoctorListActivity.this);
                    viewPager.setAdapter(viewPagerAdapter);

                }catch (JSONException e){
                    e.printStackTrace();
                }

            } else {
                //:TODO
            }


        }
    }
    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.DoctorViewHolder> {
        private List<Doctor> mDoctorListAdaper;
        private Context context;
        private ArrayList<Doctor> mOriginalValues;
        public static final int VISIBLE_ITEMS = 7;
        public static final int NON_VISIBLE_ITEMS = 150;

        public RecycleAdapter (Context context,List<Doctor> mDoctorList){
            this.mDoctorListAdaper = mDoctorList;
            this.context = context;
            this.mOriginalValues = new ArrayList<Doctor>();
            this.mOriginalValues.addAll(mDoctorList);
        }

        public class DoctorViewHolder extends RecyclerView.ViewHolder {
            public ImageView imgDview ;
            public TextView tvFrontTitle;
            public TextView tvTittle;
            public TextView tvDrname;
            public TextView tvDrspecialty;
            public TextView btnKualiifikasi;

            public DoctorViewHolder(View newDview) {
                super(newDview);
                imgDview = (ImageView)newDview.findViewById(R.id.iv_doc_pic_list);
                tvFrontTitle = (TextView)newDview.findViewById(R.id.tv_FrontTitleDr);
                tvTittle = (TextView)newDview.findViewById(R.id.tv_specialty_list) ;
                tvDrname = (TextView)newDview.findViewById(R.id.tv_list_drname);
                tvDrspecialty = (TextView)newDview.findViewById(R.id.tv_tittle_list);
                btnKualiifikasi = (TextView) newDview.findViewById(R.id.btn_kualiifikasi);
            }

        }

        @Override
        public DoctorViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {


            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.doctor_list, parent, false);
            final DoctorViewHolder viewHolder = new DoctorViewHolder(listItem);
            listItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = viewHolder.getAdapterPosition();
                    Object object = mDoctorListAdaper.get(i);
                    Doctor dokter = (Doctor) object;
                    String dokterID =  dokter.getDoc_id();
                    String firstTtlDoc = dokter.getFrontTtlDoc();
                    String namaDokter = dokter.getNameDoc();
                    String specialtyDoc = dokter.getSpecialty();
                    //Drawable imageDr = dokter.getDoc_pic_id();
                    String urlImg = dokter.getImageUrl();
                    BookingActivity bookingActivity = new BookingActivity();
                    //bookingActivity.setImageDokter(imageDr);

                    Intent myIntent = new Intent(DoctorListActivity.this ,BookingActivity.class);
                    Bundle b = new Bundle();
                    b.putString("idDokter", dokterID);
                    b.putString("userID", userID);
                    b.putString("personalID",dokterID);
                    b.putString("firstTtlDoc", firstTtlDoc);
                    b.putString("namaDokter",namaDokter);
                    b.putString("specialtyDoc", specialtyDoc);
                    b.putString("urlImage",urlImg);
                    b.putString("alamat", alamat);
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

            return viewHolder;
        }


        @Override
        public void onBindViewHolder(DoctorViewHolder holder, int position) {
//            holder.imgDview.setImageDrawable(mDoctorList.get(position).getDoc_pic_id());

            Picasso.with(context).load(mDoctorListAdaper.get(position).getDoc_pic_id()).into(holder.imgDview);
            holder.tvFrontTitle.setText(mDoctorListAdaper.get(position).getFrontTtlDoc());
            holder.tvDrname.setText(mDoctorListAdaper.get(position).getNameDoc());
            holder.tvDrspecialty.setText(mDoctorListAdaper.get(position).getSpecialty());
            holder.btnKualiifikasi.setText(mDoctorListAdaper.get(position).getKualifikasi());
            holder.tvTittle.setText(mDoctorListAdaper.get(position).getTittle());

        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return mDoctorListAdaper.size();
        }
        public void filterAndSorting (int sort, String spesial){
            spesial = spesial.toLowerCase(Locale.getDefault());
            mDoctorListAdaper.clear();
            if (spesial.length() == 0) {
                mDoctorListAdaper.addAll(mOriginalValues);
            }
            else{
                for (Doctor wp : mOriginalValues){
                    if (wp.getSpecialty().toLowerCase(Locale.getDefault()).contains(spesial)) {
                        mDoctorListAdaper.add(wp);
                    }
                }
            }
            if(sort == 0){

                Collections.sort(mDoctorListAdaper, new Comparator<Doctor>(){
                    @Override
                    public int compare(Doctor lhs, Doctor rhs) {
                        return lhs.getNameDoc().compareTo(rhs.getNameDoc());
                    }

                });
            }
            else{
                Collections.sort(mDoctorListAdaper, new Comparator<Doctor>(){
                    @Override
                    public int compare(Doctor lhs, Doctor rhs) {
                        return rhs.getNameDoc().compareTo(lhs.getNameDoc());
                    }

                });
            }
            notifyDataSetChanged();

        }

//        @Override
//        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//            super.onAttachedToRecyclerView(recyclerView);
//        }
    }
}
