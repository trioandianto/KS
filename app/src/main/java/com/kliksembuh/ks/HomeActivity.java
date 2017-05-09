package com.kliksembuh.ks;

import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.LoginFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.cast.framework.SessionManager;

import java.util.HashMap;

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
    private String firstName;
    private String lastName;
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

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // New session manager
        session = new SessionManagement(getApplicationContext());

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity if he is not
         * logged in
         * */
        session.checkLogin();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Bundle b = getIntent().getExtras();
//        userID = "93c9e1c3-2c89-40b2-a06f-fae904a64488";
        if(b != null) {
            userID = b.getString("userID");
            firstName = b.getString("firstName");
            lastName = b.getString("lastName");
            facilityID = b.getString("facilityID");
            facilityName = b.getString("facilityName");
            locationID = b.getString("SubDistrictCD");
            locationName = b.getString("SubDistrictDescription");
            tab = b.getString("tab");
            if(tab!=null){
                positionTab = Integer.parseInt(tab);
            }
        }
        else
        {
            // get user data from session
            HashMap<String, String> user = session.getUserDetails();

            firstName = user.get(SessionManagement.KEY_FNAME);
            lastName = user.get(SessionManagement.KEY_LNAME);
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
        View navHeader = navigationView.getHeaderView(0);
        TextView tvFirstName = (TextView) navHeader.findViewById(R.id.tvProfile_FName);
        tvFirstName.setText(firstName);
        TextView tvLastName = (TextView) navHeader.findViewById(R.id.tvProfile_LName);
        tvLastName.setText(lastName);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                userID = data.getStringExtra("userID");
                locationID = data.getStringExtra("subDistrict");
                locationName = data.getStringExtra("SubDistrictDescription");
                facilityID = data.getStringExtra("facilityID");
                facilityName = data.getStringExtra("facilityName");
            }
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Log Out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        session.logoutUser();
                        Toast.makeText(getApplicationContext(),"Anda telah berhasil logout.", Toast.LENGTH_SHORT).show();
                        //alert.showAlertDialog(HomeActivity.this, "Logout Berhasil", "Anda telah berhasil logout. Status: " + session.isLoggedIn(), true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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
            startActivityForResult(home, 1);
            //fragment = new SettingActivity();
        }
        else if (i == R.id.nav_search){
            Intent home = new Intent(this, HomeActivity.class);
            startActivityForResult(home, 1);
        }
        else if (i == R.id.nav_favorite){
            Intent favorite = new Intent(this, FavoriteActivity.class);
            startActivityForResult(favorite, 1);

        }
        else if (i == R.id.nav_histori_janji){
            Intent appointment = new Intent(this, MyAppointmentActivity.class);
            Bundle b  = new Bundle();
            b.putString("userID",userID);
            appointment.putExtras(b);
            startActivityForResult(appointment, 1);

        }
        else if (i == R.id.nav_pengingat_minum){
            Intent home = new Intent(this, SettingActivity.class);
            startActivityForResult(home, 1);
        }
        else if (i == R.id.nav_vital_sign){
            Intent home = new Intent(this, SettingActivity.class);
            startActivityForResult(home, 1);
        }
        else if (i == R.id.nav_hubungi_kami){
            Intent home = new Intent(this, ContactUsActivity.class);
            startActivityForResult(home, 1);
        }
        else if (i == R.id.nav_keluar){
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("Exit me", true);
//            startActivity(intent);
//            finish();
            onBackPressed();
//            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//            homeIntent.addCategory( Intent.CATEGORY_HOME );
//            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(homeIntent);

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
