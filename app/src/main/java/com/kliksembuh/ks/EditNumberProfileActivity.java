package com.kliksembuh.ks;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class EditNumberProfileActivity extends Dialog implements View.OnClickListener {
    private Activity editNumActivity;
    private Dialog dialogBox;
    private Button batal, ubahNomor;
    private TextView inputNomorBaru;
    private TextView Password;
    private TextView lblNomorTerdaftar;
    private TextView tv_NomorTerdaftar;
    private TextView linkforgotpassword_profile;

    public EditNumberProfileActivity(Activity a) {
        super(a);
        this.editNumActivity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit_number_profile);
        super.onCreate(savedInstanceState);
        lblNomorTerdaftar = (TextView)findViewById(R.id.lblNomorTerdaftar);
        tv_NomorTerdaftar= (TextView)findViewById(R.id.tv_NomorTerdaftar);
        inputNomorBaru = (TextView)findViewById(R.id.tv_InputNomorBaru);
        Password= (TextView)findViewById(R.id.tv_InputPasswordProfile);
        linkforgotpassword_profile= (TextView)findViewById(R.id.linkforgotpassword_profile);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_number_profile);
    }

    @Override
    public void onClick(View v) {

    }

//    public void showDialog(Activity activity, String msg){
//        final Dialog dialog = new Dialog(activity);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.activity_edit_number_profile);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//        lblNomorTerdaftar = (TextView)findViewById(R.id.lblNomorTerdaftar);
//        tv_NomorTerdaftar= (TextView)findViewById(R.id.tv_NomorTerdaftar);
//        inputNomorBaru = (TextView)findViewById(R.id.tv_InputNomorBaru);
//        Password= (TextView)findViewById(R.id.tv_InputPasswordProfile);
//        linkforgotpassword_profile= (TextView)findViewById(R.id.linkforgotpassword_profile);
//        Button dialogBtn_cancel = (Button) dialog.findViewById(R.id.btn_Batal);
//        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                    Toast.makeText(getApplicationContext(),"Cancel" ,Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
//
//        Button dialogBtn_okay = (Button) dialog.findViewById(R.id.btn_UbahNomor);
//        dialogBtn_okay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                    Toast.makeText(getApplicationContext(),"Okay" ,Toast.LENGTH_SHORT).show();
//                dialog.cancel();
//            }
//        });
//
//        dialog.show();
//    }
}
