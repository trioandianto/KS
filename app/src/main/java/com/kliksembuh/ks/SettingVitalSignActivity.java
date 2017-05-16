package com.kliksembuh.ks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingVitalSignActivity extends Fragment implements View.OnClickListener {
    private TextView tvVitalSign;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View newView = inflater.inflate(R.layout.activity_setting_vital_sign, container, false);
        tvVitalSign = (TextView)newView.findViewById(R.id.tvVitalSign);
        tvVitalSign.setOnClickListener(this);
        return newView;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i==R.id.tvVitalSign){
            Intent editVitalSign = new Intent(v.getContext(), SettingVitalSignDetailActivity.class);
            startActivity(editVitalSign);
        }

    }
}
