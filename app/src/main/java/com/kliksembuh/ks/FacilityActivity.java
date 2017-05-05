package com.kliksembuh.ks;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FacilityActivity extends AppCompatActivity {
    private FacilityActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.containerFacility);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsFacility);
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter{
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    FacilityServiceActivity facilityServiceActivity= new FacilityServiceActivity();
                    facilityServiceActivity.setUserID(userID);
                    return facilityServiceActivity;
                case 1:
                    FacilityRatingActivity facilityRatingActivity= new FacilityRatingActivity();
                    facilityRatingActivity.setUserID(userID);
                    return facilityRatingActivity;
                case 2:
                    FacilityInsuranceActivity facilityInsuranceActivity= new FacilityInsuranceActivity();
                    facilityInsuranceActivity.setUserID(userID);
                    return facilityInsuranceActivity;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Layanan";
                case 1:
                    return "Penilaian";
                case 2:
                    return "Rekanan Asuransi";
            }
            return null;
        }
    }
}
