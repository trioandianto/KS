package com.kliksembuh.ks;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContactUsActivity extends AppCompatActivity {

    private TextView tvInputNoAppointment, tvInputFeedback;
    private Button btnSubmitFeedback, btnHubungiKami;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        tvInputNoAppointment = (TextView) findViewById(R.id.tv_InputFeedbackNoAppointment);
        tvInputFeedback = (TextView) findViewById(R.id.tv_InputFeedbacl);
        btnSubmitFeedback = (Button) findViewById(R.id.btnSubmitFeedback);
        btnHubungiKami = (Button) findViewById(R.id.btnHubungiKami);
        btnHubungiKami.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hubungiKami();
            }
        });
    }
    private void hubungiKami() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda akan menghubungi customer service kami: 087723302087, Lanjutkan?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:087723302087"));
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(callIntent);
                        finish();

                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
