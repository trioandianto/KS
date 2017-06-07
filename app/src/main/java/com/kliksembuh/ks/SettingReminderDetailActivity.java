package com.kliksembuh.ks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

public class SettingReminderDetailActivity extends AppCompatActivity {
    private Button btnSimpan;
    private Spinner spnType, spnDosage, spnTImings,spnDays, spnDuration;
    private String userID;
    private AutoCompleteTextView atTittle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_reminder_detail);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
        }

        atTittle = (AutoCompleteTextView)findViewById(R.id.tvEditTitleRemidner);
        btnSimpan = (Button)findViewById(R.id.btnSimpanReminder);
        spnType = (Spinner)findViewById(R.id.spnReminderType);
        spnDosage = (Spinner)findViewById(R.id.spnDosage);
        spnTImings = (Spinner)findViewById(R.id.spnTimings);
        spnDays = (Spinner)findViewById(R.id.spnDays);
        spnDuration = (Spinner)findViewById(R.id.spnDuration);
    }
}
