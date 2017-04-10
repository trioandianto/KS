package com.kliksembuh.ks;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SettingActivity extends Fragment{
    private SettingActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting);
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//
//        mViewPager = (ViewPager) findViewById(R.id.containerSetting);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabSetting);
//        tabLayout.setupWithViewPager(mViewPager);
//
//    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.activity_setting, container, false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = (ViewPager) rootView.findViewById(R.id.containerSetting);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabSetting);
        tabLayout.setupWithViewPager(mViewPager);
        return rootView;

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
                    return settingProfileActivity;
                case 1:
                    SettingVitalSignActivity settingVitalSignActivity= new SettingVitalSignActivity();
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
                    return "ATUR PROFIL";
                case 1:
                    return "ATUR VITAL SIGN";
                case 2:
                    return "ATUR PENGINGAT";
            }
            return null;
        }
    }
}
