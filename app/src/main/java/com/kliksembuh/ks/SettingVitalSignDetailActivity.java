package com.kliksembuh.ks;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class SettingVitalSignDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_vital_sign_detail);

        Toolbar newToolbar = (Toolbar)findViewById(R.id.toolbarvitalsigndetail);
        setSupportActionBar(newToolbar);
        newToolbar.setTitle("Pengaturan Vital Sign");
        setSupportActionBar(newToolbar);
        getWindow().setStatusBarColor(ContextCompat.getColor(SettingVitalSignDetailActivity.this, R.color.colorPrimaryDark));

    }
}
