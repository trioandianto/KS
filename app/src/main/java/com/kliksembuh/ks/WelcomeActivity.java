package com.kliksembuh.ks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    ViewFlipper viewFlipper;
    Button next;
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        viewFlipper = (ViewFlipper)findViewById(R.id.viewFliper);
        next = (Button)findViewById(R.id.btnnext);
        signup = (Button)findViewById(R.id.btnsignup);

        next.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == next){
            viewFlipper.showNext();
        }
        else if(v == signup){
            viewFlipper.showPrevious();
        }

    }
}
