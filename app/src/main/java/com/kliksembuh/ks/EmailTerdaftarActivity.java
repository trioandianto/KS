package com.kliksembuh.ks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EmailTerdaftarActivity extends AppCompatActivity implements View.OnClickListener{
    private String email;
    private TextView tvEmailTerdaftar;
    private TextView tvEmailMasuk;
    private TextView tvLupaPassword;
    private ImageView ivLupaPassword;
    private ImageView ivEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_terdaftar);


        Bundle b = getIntent().getExtras();
        if(b != null) {
            email = b.getString("Email");
        }
        tvEmailMasuk = (TextView)findViewById(R.id.tvemailku);
        tvEmailMasuk.setOnClickListener(this);
        tvLupaPassword = (TextView)findViewById(R.id.tvLupaPassword);
        tvLupaPassword.setOnClickListener(this);
        tvEmailTerdaftar = (TextView)findViewById(R.id.tvEmailTerdaftar);
        tvEmailTerdaftar.setOnClickListener(this);
        ivLupaPassword = (ImageView)findViewById(R.id.ivLupaPassword);
        ivLupaPassword.setOnClickListener(this);
        ivEmail = (ImageView)findViewById(R.id.ivEmail);
        if(email != null && email!=""){
            tvEmailTerdaftar.setText("Akun " +email+" sudah terdaftar.");
            tvEmailMasuk.setText(email);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i==R.id.tvemailku || i==R.id.ivEmail){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            Bundle b = new Bundle();
            b.putString("Email", email);
            intent.putExtras(b);
            startActivityForResult(intent, 1);

        }
        else if (i==R.id.tvLupaPassword || i ==R.id.ivLupaPassword){
            Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
            Bundle b = new Bundle();
            b.putString("Email", email);
            intent.putExtras(b);
            startActivityForResult(intent, 1);

        }

    }
}
