package com.kliksembuh.ks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Trio Andianto on 1/24/2017.
 */
public class MyAppointmentUpComingActivity extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_my_appointment_upcoming, container, false);

        Button btnkonfirmasi = (Button) rootView.findViewById(R.id.btnkonfirmasi);
        btnkonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), AppointmentDetailActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        return rootView;
    }
}
