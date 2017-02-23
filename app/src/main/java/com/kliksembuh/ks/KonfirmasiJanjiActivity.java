package com.kliksembuh.ks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class KonfirmasiJanjiActivity extends AppCompatActivity {
    private Button btnBuatJanji;
    private ImageView editjadwal;
    private ImageView editdokter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_janji);
        btnBuatJanji = (Button)findViewById(R.id.btnbuatjanji);
        editdokter = (ImageView)findViewById(R.id.iveditdokter);
        editjadwal = (ImageView)findViewById(R.id.iveditjadwal);
        editdokter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), TestScroolView.class);
                startActivityForResult(myIntent, 0);
            }
        });
        editjadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), BookingActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        btnBuatJanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), MyAppointmentActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }
}
