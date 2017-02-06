package com.kliksembuh.ks;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FavoriteActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.containerfavorite);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsfavorite);
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
                    FavoriteInstansiActivity favoriteInstansiActivity= new FavoriteInstansiActivity();
                    return favoriteInstansiActivity;
                case 1:
                    FavoriteDokterActivity favoriteDokterActivity = new FavoriteDokterActivity();
                    return favoriteDokterActivity;
                case 2:
                    FavoritePelayananActivity favoritePelayananActivity = new FavoritePelayananActivity();
                    return favoritePelayananActivity;
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
                    return "INSTANSI";
                case 1:
                    return "DOKTER";
                case 2:
                    return "PELAYANAN";
            }
            return null;
        }
    }
}
