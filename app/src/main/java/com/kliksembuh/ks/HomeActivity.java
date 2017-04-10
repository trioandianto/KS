package com.kliksembuh.ks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity
        implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {
    ViewPager viewPager;
    private ViewPager mViewPager;
//    private Intromanager intromanager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPagerAdapter viewPagerAdapter;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private int positionTab = 0;
    private String tab;
    private String userID;
    private String facilityID;
    private String facilityName;
    private String locationName;
    private String locationID;

    public class MyLayout{
        public MyLayout(int layoutId, int imgId){
            this.layoutId = layoutId;
            this.imgId = imgId;
        }
        public int layoutId;
        public int imgId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
            facilityID = b.getString("facilityID");
            facilityName = b.getString("facilityName");
            locationID = b.getString("SubDistrictCD");
            locationName = b.getString("SubDistrictDescription");
            tab = b.getString("tab");
            if(tab!=null){
                positionTab = Integer.parseInt(tab);
            }
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(R.id.view_pager_home);
        dotsLayout=(LinearLayout)findViewById(R.id.layoutdotshome);
        layouts = new int[]{
                    R.layout.activity_home_screen1,
                R.layout.activity_home_screen2,
                R.layout.activity_home_screen3,
                R.layout.activity_home_screen4};
        addBottomDots(0);
        changeStatusBarColor();
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewListener);
        mViewPager = (ViewPager) findViewById(R.id.containerhome);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabshome);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(positionTab);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent i=new Intent();
        i.setClass(this,ProfileManagementActivity.class);
        startActivity(i);
//
//        Intent intent;
//        switch(v.getId()) {
//            case R.id.edit_datadiri: // R.id.textView1
//                intent = new Intent(this, ProfileManagementActivity.class);
//                break;
//            case R.id.editProfile: // R.id.textView2
//                intent = new Intent(this, Third.class);
//                break;
//            case R.id.editText: // R.id.textView3
//                intent = new Intent();
//        }
//        startActivity(intent);
    }

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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        Class fragmentClass;
        int i = item.getItemId();
        if(i == R.id.nav_setting){
            Intent home = new Intent(this, SettingActivity.class);
            startActivityForResult(home, 0);
            //fragment = new SettingActivity();
        }
        else if (i == R.id.nav_search){
            Intent home = new Intent(this, HomeActivity.class);
            startActivityForResult(home, 0);
        }
        else if (i == R.id.nav_search){

        }
        else if (i == R.id.nav_favorite){
            Intent favorite = new Intent(this, FavoriteActivity.class);
            startActivityForResult(favorite, 0);

        }
        else if (i == R.id.nav_appointment){
            Intent appointment = new Intent(this, MyAppointmentActivity.class);
            startActivityForResult(appointment, 0);

        }
        else{
            fragmentClass = HomeActivity.class;
        }
        if(fragment!=null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();
        }


        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_search) {
//            // Handle the camera action
//            Fragment nes = new TabPelayananActivity();
//        } else if (id == R.id.nav_favorite) {
//
//        } else if (id == R.id.nav_appointment) {
//
//        } else if (id == R.id.nav_tek_darah) {
//
//        } else if (id == R.id.nav_det_jantung) {
//
//        } else if (id == R.id.nav_temp) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
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
    public class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, int position){
            layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position],container,false);



            //ImageView img = (ImageView)view.findViewById(layouts[position].imgId);
//            img.setImageDrawable(HospitalList.LoadImageFromWebOperations("http://kliksembuhbeta.000webhostapp.com/img/homescreen1.jpg"));
            container.addView(view);
            return view;

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
            View view = (View)object;
            container.removeView(view);
        }
    }

//    Konfigurasi buat tabb

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public int mState = 0;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            mState = position;
            String mSpecialization;
            switch (position){
                case 0:
                    TabSpesialisasiActivity tabSpesialisasi= new TabSpesialisasiActivity();
                    try{
                        tabSpesialisasi.setSpesial(facilityName);
                        tabSpesialisasi.setSpesialID(facilityID);
                        tabSpesialisasi.setUserID(userID);
                        tabSpesialisasi.setLokasiID(locationID);
                        tabSpesialisasi.setLocasi(locationName);
                    }
                    catch (Exception ex){

                    }finally {

                    }


                     return tabSpesialisasi;
                case 1:
                    TabPelayananActivity tabPelayanan = new TabPelayananActivity();
                    return tabPelayanan;

                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Spesialisasi";
                case 1:
                    return "Pelayanan";
            }
            return null;
        }
        public void setText(String text){

        }
    }

}
