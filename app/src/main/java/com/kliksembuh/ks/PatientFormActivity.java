package com.kliksembuh.ks;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PatientFormActivity extends AppCompatActivity {
    private Spinner spnStatus, spnAsuransi;
    private RadioButton rbJenisKelaminL, rbJenisKelaminP;
    private TextView tvFname, tvLname, tvMobile, tvTanggalLahir, tvNoBPJSKes, tvAlamat, tvNamaKerabat, tvNoHPKerabat;
    private Button btnSimpan;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_form);
        spnStatus = (Spinner) findViewById(R.id.spnStatusSaya);
        spnAsuransi = (Spinner) findViewById(R.id.spnAsuransi);
        rbJenisKelaminL = (RadioButton) findViewById(R.id.radioBtnJenisL);
        rbJenisKelaminP = (RadioButton) findViewById(R.id.radioBtnJenisP);
        tvFname = (TextView)findViewById(R.id.tvFNameForm);
        tvLname = (TextView)findViewById(R.id.tvLNameForm);
        tvMobile= (TextView)findViewById(R.id.tvEditMobileForm);
        tvLname = (TextView)findViewById(R.id.tvLNameForm);
        tvMobile= (TextView)findViewById(R.id.tvEditMobileForm);
        tvTanggalLahir = (TextView)findViewById(R.id.tvTanggalLahirForm);
        tvNoBPJSKes= (TextView)findViewById(R.id.tvNoBPJSKesehatan);
        tvAlamat= (TextView)findViewById(R.id.tvAlamatForm);
        tvNamaKerabat = (TextView)findViewById(R.id.tvNamaKerabatForm);
        tvNoHPKerabat = (TextView)findViewById(R.id.tvNoHPKerabat);
        btnSimpan = (Button) findViewById(R.id.btnSimpanProfileForm);


    }
}
