package com.kliksembuh.ks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class ContactUsActivity extends AppCompatActivity {

    private TextView tvInputNoAppointment, tvInputFeedback;
    private Button btnSubmitFeedback, btnHubungiKami;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        tvInputNoAppointment = (TextView)findViewById(R.id.tv_InputFeedbackNoAppointment);
        tvInputFeedback = (TextView)findViewById(R.id.tv_InputFeedbacl);
        btnSubmitFeedback = (Button)findViewById(R.id.btnSubmitFeedback);
        btnHubungiKami = (Button)findViewById(R.id.btnHubungiKami);
    }
}
