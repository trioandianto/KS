package com.kliksembuh.ks;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


public class SettingActivity extends AppCompatActivity{
    private SettingActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String userID;
    private int positionTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
            positionTab = b.getInt("tab");

        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.containerSetting);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(positionTab);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabSetting);
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
                    SettingProfileActivity settingProfileActivity= new SettingProfileActivity();
                    try{
                        settingProfileActivity.setUserID(userID);
                    }
                    catch (Exception ex){

                    }finally {

                    }
                    return settingProfileActivity;
                case 1:
                    SettingVitalSignActivity settingVitalSignActivity= new SettingVitalSignActivity();
                    try{
                        settingVitalSignActivity.setUserID(userID);
                    }
                    catch (Exception ex){

                    }finally {

                    }
                    return settingVitalSignActivity;
                case 2:
                    SettingReminderActivity settingReminderActivity = new SettingReminderActivity();
                    return settingReminderActivity;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Atur Profil";
                case 1:
                    return "Atur Vital Sign";
                case 2:
                    return "Atur Pengingat";
            }
            return null;
        }
    }
}
