package com.kliksembuh.ks;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class FacilityPromotionActivity extends Fragment implements ListView.OnItemClickListener {

    private Context globalContext = null;
    private String userID;
    private String transaksiID;
    public String getUserID() { return  userID; }

    public void setUserID(String userID){ this.userID = userID; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_facility_promotion, container, false);
        globalContext = this.getActivity();
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
