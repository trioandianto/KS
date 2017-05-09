package com.kliksembuh.ks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingProfileActivity extends Fragment implements View.OnClickListener{

    private TextView editEmailkontak;
    private TextView editPasswordProf;
    private TextView editProfilPatient;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    private String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View newView = inflater.inflate(R.layout.activity_setting_profile, container, false);
        editEmailkontak = (TextView) newView.findViewById(R.id.tvEditEmailKontak);
        editEmailkontak.setOnClickListener(this);
        editProfilPatient = (TextView) newView.findViewById(R.id.tvEditProfilPatient);
        editProfilPatient.setOnClickListener(this);
        editPasswordProf = (TextView) newView.findViewById(R.id.tvEditPasswordProf);
        editPasswordProf.setOnClickListener(this);

        return newView;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.tvEditEmailKontak){
            Intent editEmailKontak = new Intent(v.getContext(), ProfileManagementActivity.class);
            startActivity(editEmailKontak);
        }
        if(i == R.id.tvEditProfilPatient){
            Intent editProfilPatient = new Intent(v.getContext(), PatientProfileActivity.class);
            Bundle b = new Bundle();
            b.putString("userID", userID);
            editProfilPatient.putExtras(b);
            startActivity(editProfilPatient);
        }
        if(i == R.id.tvEditPasswordProf){
            Intent editPasswordProf = new Intent (v.getContext(), EditPasswordProfileActivity.class);
            startActivity(editPasswordProf);
        }


    }
}
