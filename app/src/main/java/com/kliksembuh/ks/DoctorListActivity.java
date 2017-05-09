package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class DoctorListActivity extends AppCompatActivity implements OnMapReadyCallback,View.OnTouchListener {

    private GoogleMap mMap;
    ViewPager viewPager;
    private int[] layouts;
    private ScrollView scrollView;
    private ObservableScrollView mScrollView;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private ViewPagerAdapter viewPagerAdapter;
    private ListView lvDokter;
    private List<Doctor> mDokterList;
    private DoctorListAdapter dAdapter;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);
        lvDokter = (ListView)findViewById(R.id.listview_doctor);
        mDokterList = new ArrayList<>();

        scrollView = (ScrollView)findViewById(R.id.scrollViewdoctorlist);
        dotsLayout=(LinearLayout)findViewById(R.id.layoutslideshow);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapdoctorlist);
        mapFragment.getMapAsync(this);






        layouts = new int[]{
                R.drawable.doctorlist1,
                R.drawable.doctorlist2,
                R.drawable.doctorlist3};

        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager = (ViewPager)findViewById(R.id.view_pager_doctorlist);
        viewPager.setAdapter(viewPagerAdapter);
        //viewPager.addOnPageChangeListener(viewListener);


        // Add sample data
        // We can get data by DB, or web service
//        mDoctorList.add(new Doctor(1, R.drawable.rs_bogor_medical_centre , "dr. Ilma Suraya", "Dokter Umum"));
//        mDoctorList.add(new Doctor(2, R.drawable.rs_pmi_bogor , "dr. Indah Kusuma", "Dokter Umum"));

        // Test adapter
//        dAdapter = new DoctorListAdapter(getApplicationContext(), mDoctorList);
//        lvDoctor.setAdapter(dAdapter);



        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ScrollPositionObserver());

        lvDokter.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something
                // Ex. display msg with hospital id from view.getTag
                Toast.makeText(getApplicationContext(), "Clicked doctor id = " + view.getTag(), Toast.LENGTH_SHORT).show();
            }

        });
        //new GetContacts().execute();
    }
    private class ScrollPositionObserver implements ViewTreeObserver.OnScrollChangedListener {

        private int mImageViewHeight;

        public ScrollPositionObserver() {
            mImageViewHeight = getResources().getDimensionPixelSize(R.dimen.contact_photo_height);
        }

        @Override
        public void onScrollChanged() {
            int scrollY = Math.min(Math.max(mScrollView.getScrollY(), 0), mImageViewHeight);

            // changing position of ImageView
            viewPager.setTranslationY(scrollY / 2);

            // alpha you could set to ActionBar background
            float alpha = scrollY / (float) mImageViewHeight;
        }
    }


//    public void onZoom(View view){
//        if(view.getId()==R.id.btnzoomin){
//            mMap.animateCamera(CameraUpdateFactory.zoomIn());
//        }
//        if(view.getId()==R.id.btnzoomout){
//            mMap.animateCamera(CameraUpdateFactory.zoomOut());
//        }
//    }
    private void addBottomDots(int position){
        dots = new TextView[layouts.length];
        int[] colorActive = getResources().getIntArray(R.array.dot_active);
        int[] colorInactive = getResources().getIntArray(R.array.dot_inactive);
        dotsLayout.removeAllViews();
        for (int i =0;i<dots.length;i++) {
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive[position]);
            dotsLayout.addView(dots[i]);
        }
        if(dots.length>0){
            dots[position].setTextColor(colorActive[position]);
        }
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addBottomDots(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

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

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int dragthreshold = 30;
        int downX = 0;
        int downY = 0;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getRawX();
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceX = Math.abs((int) event.getRawX() - downX);
                int distanceY = Math.abs((int) event.getRawY() - downY);

                if (distanceY > distanceX && distanceY > dragthreshold) {
                    viewPager.getParent().requestDisallowInterceptTouchEvent(false);
                    scrollView.getParent().requestDisallowInterceptTouchEvent(true);
                } else if (distanceX > distanceY && distanceX > dragthreshold) {
                    viewPager.getParent().requestDisallowInterceptTouchEvent(true);
                    scrollView.getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                scrollView.getParent().requestDisallowInterceptTouchEvent(false);
                viewPager.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return false;

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

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Showing progress dialog
//            pDialog = new ProgressDialog(DoctorListActivity.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }

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


                    String name = c.getString("name");
                    String id = c.getString("id");
                    String image = c.getString("imgUrl");
                    Drawable image1 = LoadImageFromWebOperations(image);
                    String alamat = c.getString("alamat");

//                        da =new ArrayList<>();
//                        da.add( name );
//                        da.add( code );


                    // Phone node is JSON Object
//                    JSONObject phone = c.getJSONObject("phone");
//                    String mobile = phone.getString("mobile");
//                    String home = phone.getString("home");
//                    String office = phone.getString("office");

                    // tmp hash map for single contact
//                    mDokterList.add(new Doctor(id, image1, name, alamat));


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

}
