package com.kliksembuh.ks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.kliksembuh.ks.library.PatientListAdapter;
import com.kliksembuh.ks.models.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lvPatient;
    private List<Patient> mPatientList;
    private PatientListAdapter pAdapter;
    private Button btnAddPatient;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
        }
        lvPatient = (ListView) findViewById(R.id.listview_patientProfile);
        mPatientList = new ArrayList<>();

        // Add sample data
        // We can get data by DB, or web service
        mPatientList.add(new Patient(1, 10920312, "Anak", "Sofi", "Fitria", 120, true, 1991, 123048, "Jl. Arzimar 3. No. A11. Bogor"));
        mPatientList.add(new Patient(2, 10920313, "Ayah", "Trio", "Andianto", 121, true, 1975, 123049, "Jl. Arzimar 3. No. A11. Bogor"));
        mPatientList.add(new Patient(3, 10920314, "Pribadi", "Adi", "Panggayuh", 122, true, 1988, 123050, "Jl. Arzimar 3. No. A11. Bogor"));
//        mPatientList.add(new Patient(2, 10920313, "Ayah", "Trio", "Andianto", 6281234, 1, 28/01/71, 12983921, "Jl. Arzimar 3. No. A11. Bogor");
//        mPatientList.add(new Patient(3, 10920314, "Pribadi", "Adie", "Panggayuh", 62812345, 1, 28/01/81, 12983922, "Jl. Arzimar 3. No. A11. Bogor");

        // Test adapter
        pAdapter = new PatientListAdapter(getApplicationContext(), mPatientList);
        lvPatient.setAdapter(pAdapter);


    }

    @Override
    public void onClick(View v) {
        Intent i=new Intent();
        i.setClass(this,PatientFormActivity.class);
        Bundle b = new Bundle();
        b.putString("userID", userID);
        i.putExtras(b);
        startActivity(i);
    }
}
