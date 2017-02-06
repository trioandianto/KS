package com.kliksembuh.ks;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView tvtermsandpolicy = (TextView)findViewById(R.id.termsandpol);
        tvtermsandpolicy.setText(Html.fromHtml("Dengan menekan tombol buat akun baru, anda sudah menyetujui " +
                "<a href='com.kliksembuh.ks.TermANdPolicyActivity://Phone'>Syarat & Ketentuan.</a>"));


        Button btnlogin = (Button) findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
        Button btncreate = (Button) findViewById(R.id.btncreate);
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), VerifikasiActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        TextView tvtermandpolicy = (TextView) findViewById(R.id.termsandpol);
        tvtermandpolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), TermAndPolicyActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }
}
