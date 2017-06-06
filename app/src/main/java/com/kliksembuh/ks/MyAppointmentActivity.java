package com.kliksembuh.ks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MyAppointmentActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
        }


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.containermyappointe);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsmyappoint);
        tabLayout.setupWithViewPager(mViewPager);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                userID = data.getStringExtra("userID");
            }
        }
    }
    public void onBackPressed() {
        Intent startMain = new Intent(getApplicationContext(),HomeActivity.class);
        Bundle b = new Bundle();
        b.putString("userID",userID);
        startMain.putExtras(b);
        startActivity(startMain);

    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    MyAppointmentUpComingActivity myAppointmentUpComingActivity= new MyAppointmentUpComingActivity();
                    try {
                        myAppointmentUpComingActivity.setUserID(userID);
                    }catch (Exception e){

                    }

                    return myAppointmentUpComingActivity;
                case 1:
                    MyAppointmentConfirmedActivity myAppointmentConfirmedActivity = new MyAppointmentConfirmedActivity();
                    myAppointmentConfirmedActivity.setUserID(userID);
                    return myAppointmentConfirmedActivity;
                case 2:
                    MyAppointmentSuccessActivity myAppointmentSuccessActivity = new MyAppointmentSuccessActivity();
                    myAppointmentSuccessActivity.setUserID(userID);
                    return myAppointmentSuccessActivity;
                case 3:
                    MyAppointmentHistoryActivity myAppointmentHistoryActivity = new MyAppointmentHistoryActivity();
                    myAppointmentHistoryActivity.setUserID(userID);
                    return myAppointmentHistoryActivity;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Menunggu Konfirmasi";
                case 1:
                    return "Dikonfirmasi";
                case 2:
                    return "Selesai";
                case 3:
                    return "Semua Transaksi";
            }
            return null;
        }
    }
}
