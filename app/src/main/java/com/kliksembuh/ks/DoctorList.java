package com.kliksembuh.ks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.kliksembuh.ks.models.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;

public class DoctorList extends FragmentActivity implements OnMapReadyCallback,View.OnTouchListener {

    private GoogleMap mMap;
    ViewPager viewPager;
    private int[] layouts;
    private ScrollView scrollView;
    private ObservableScrollView mScrollView;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private ViewPagerAdapter viewPagerAdapter;

    private ListView lvDoctor;
    private DoctorListAdapter dAdapter;
    private List<Doctor> mDoctorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

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

        lvDoctor = (ListView)findViewById(R.id.listview_doctor);
        mDoctorList = new ArrayList<>();
        // Add sample data
        // We can get data by DB, or web service
        mDoctorList.add(new Doctor(1, R.drawable.rs_bogor_medical_centre , "dr. Ilma Suraya", "Dokter Umum"));
        mDoctorList.add(new Doctor(2, R.drawable.rs_pmi_bogor , "dr. Indah Kusuma", "Dokter Umum"));

        // Test adapter
        dAdapter = new DoctorListAdapter(getApplicationContext(), mDoctorList);
        lvDoctor.setAdapter(dAdapter);



        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ScrollPositionObserver());

        lvDoctor.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something
                // Ex. display msg with hospital id from view.getTag
                Toast.makeText(getApplicationContext(), "Clicked doctor id = " + view.getTag(), Toast.LENGTH_SHORT).show();
            }

        });
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

}
