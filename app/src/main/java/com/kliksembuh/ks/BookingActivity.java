package com.kliksembuh.ks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BookingActivity extends AppCompatActivity {
    private TextView jadwal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        jadwal =(TextView)findViewById(R.id.tvjadwal);

        jadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), KonfirmasiJanjiActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });


    }
}
