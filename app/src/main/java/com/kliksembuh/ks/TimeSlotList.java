package com.kliksembuh.ks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ucu Nurul Ulum on 17/02/2017.
 */

public class TimeSlotList extends Activity {

    private ListView lvTimeSlot;
    private TimeSlotAdapter tAdapter;
    private List<TimeSlot> mTimeSlotList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Time Screen Logic (created by Ucu on 17022017)
        setContentView(R.layout.activity_booking);
        // lvTimeSlot = (ListView)findViewById(R.id.listview_timeSlot);
        mTimeSlotList = new ArrayList<>();
        // Add sample data
        // We can get data by DB, or web service
        mTimeSlotList.add(new TimeSlot(1, "08:00 - 12:00"));
        mTimeSlotList.add(new TimeSlot(2, "13:00 - 16:00"));
        mTimeSlotList.add(new TimeSlot(3, "19:00 - 21:00"));

        // Test adapter
        tAdapter = new TimeSlotAdapter(getApplicationContext(), mTimeSlotList);
        lvTimeSlot.setAdapter(tAdapter);

        lvTimeSlot.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something
                // Ex. display msg with hospital id from view.getTag
                Toast.makeText(getApplicationContext(), "Clicked time id = " + view.getTag(), Toast.LENGTH_SHORT).show();

                // Go to another activity
//                if (position == 0){
//                    Intent myIntent = new Intent(view.getContext(),DoctorList.class);
//                    startActivityForResult(myIntent, 0);
//                }
//                if (position == 1){
//                    Intent myIntent = new Intent(view.getContext(),DoctorList.class);
//                    startActivityForResult(myIntent, 0);
//                }
//                if (position == 2){
//                    Intent myIntent = new Intent(HospitalList.this,DoctorList.class);
//                    startActivityForResult(myIntent, 0);
//                }
//                if (position == 3){
//                    Intent myIntent = new Intent(view.getContext(),DoctorList.class);
//                    startActivityForResult(myIntent, 2);
//                }

            }

        });
    }
}
