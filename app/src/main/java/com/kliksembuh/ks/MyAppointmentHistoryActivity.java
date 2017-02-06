package com.kliksembuh.ks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Trio Andianto on 1/24/2017.
 */
public class MyAppointmentHistoryActivity extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_my_appointment_history, container, false);

        return rootView;
    }
}
