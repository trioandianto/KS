package com.kliksembuh.ks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class TermAndPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_policy);
        Toolbar newToolbar = (Toolbar)findViewById(R.id.toolbarsyaratdanketentuan);
        setSupportActionBar(newToolbar);
        newToolbar.setTitle("Syarat & Ketentuan");
        setSupportActionBar(newToolbar);
    }
}
