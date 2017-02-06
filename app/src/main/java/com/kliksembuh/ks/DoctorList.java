package com.kliksembuh.ks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DoctorList extends AppCompatActivity {

    private ListView lvDoctor;
    private DoctorListAdapter dAdapter;
    private List<Doctor> mDoctorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);
        lvDoctor = (ListView)findViewById(R.id.listview_doctor);
        mDoctorList = new ArrayList<>();
        // Add sample data
        // We can get data by DB, or web service
        mDoctorList.add(new Doctor(1, R.drawable.dr_ilma_suraya , "dr. Ilma Suraya", "Dokter Umum"));
        mDoctorList.add(new Doctor(2, R.drawable.dr_indah_kusuma , "dr. Indah Kusuma", "Dokter Umum"));
        mDoctorList.add(new Doctor(3, R.drawable.dr_menda_suci, "dr. Suci Menda", "Dokter Umum"));
        mDoctorList.add(new Doctor(4, R.drawable.dr_tengku_nissa , "dr. Tengku Nissa", "Dokter Umum"));
        mDoctorList.add(new Doctor(5, R.drawable.dr_winda_meyrisa, "dr. Winda Meyrisa", "Dokter Umum"));

        // Test adapter
        dAdapter = new DoctorListAdapter(getApplicationContext(), mDoctorList);
        lvDoctor.setAdapter(dAdapter);

        lvDoctor.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something
                // Ex. display msg with hospital id from view.getTag
                Toast.makeText(getApplicationContext(), "Clicked doctor id = " + view.getTag(), Toast.LENGTH_SHORT).show();
            }

        });
    }
}
