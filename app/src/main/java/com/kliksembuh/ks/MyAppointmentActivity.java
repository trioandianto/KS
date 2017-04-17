package com.kliksembuh.ks;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.containermyappointe);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsmyappoint);
        tabLayout.setupWithViewPager(mViewPager);
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
                    return myAppointmentUpComingActivity;
                case 1:
                    MyAppointmentConfirmedActivity myAppointmentConfirmedActivity = new MyAppointmentConfirmedActivity();
                    return myAppointmentConfirmedActivity;
                case 2:
                    MyAppointmentSuccessActivity myAppointmentSuccessActivity = new MyAppointmentSuccessActivity();
                    return myAppointmentSuccessActivity;
                case 3:
                    MyAppointmentHistoryActivity myAppointmentHistoryActivity = new MyAppointmentHistoryActivity();
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
                    return "Pending";
                case 1:
                    return "Confirmed";
                case 2:
                    return "Success";
                case 3:
                    return "Semua Trx";
            }
            return null;
        }
    }
}
