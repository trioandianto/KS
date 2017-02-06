package com.kliksembuh.ks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.List;

/**
 * Created by Trio Andianto on 1/24/2017.
 */
public class FavoriteDokterActivity extends Fragment {
    private String JSON_STRING = "http://api.androidhive.info/contacts/";
    private ListView lvHospital;
    private HospitalListAdapter hAdapter;
    private List<Hospital> mHospitalList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {


        View rootView = inflater.inflate(R.layout.activity_favorite_dokter, container, false);
        lvHospital = (ListView)rootView.findViewById(R.id.listview_hospital_favorite);

        return rootView;
    }

}
