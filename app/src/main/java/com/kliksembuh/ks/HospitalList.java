package com.kliksembuh.ks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HospitalList extends Activity {

    private String JSON_STRING;
    private ListView lvHospital;
    private HospitalListAdapter hAdapter;
    private List<Hospital> mHospitalList;

    RatingBar rb;

//    protected void onPreExecute(){
//        json_url = "http://192.168.1.18/userapi/api/institutions/getinstitutions";
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hospital Screen Logic (created by Ucu on 24012017)
        setContentView(R.layout.activity_hospital_list);
        lvHospital = (ListView)findViewById(R.id.listview_hospital);
        mHospitalList = new ArrayList<>();
        // Add sample data
        // We can get data by DB, or web service
        mHospitalList.add(new Hospital(1, R.drawable.rs_pmi_bogor , "RS PMI Jakarta", "Jalan Rumah Sakit I, Kota Bogor, Jawa Barat 16129"));
        mHospitalList.add(new Hospital(2, R.drawable.rs_cibinong_bogor, "RSUD Cibinong Bogor", "Jalan KSR Dadi Kusmayadi No. 27, Bogor, Jawa Barat 16914"));
        mHospitalList.add(new Hospital(3, R.drawable.rs_medika_dramaga, "RS Medika Darmaga", "Jl. Raya Dramaga KM. 7.3, Bogor Barat, Kota Bogor, Jawa Barat 16680"));
        mHospitalList.add(new Hospital(4, R.drawable.rs_bogor_medical_centre, "RS Bogor Medical Centre", "Jl. Pajajaran Indah V No. 97, Kota Bogor, Jawa Barat 16143"));

        // Test adapter
        hAdapter = new HospitalListAdapter(getApplicationContext(), mHospitalList);
        lvHospital.setAdapter(hAdapter);

            lvHospital.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something
                // Ex. display msg with hospital id from view.getTag
                Toast.makeText(getApplicationContext(), "Clicked hospital id = " + view.getTag(), Toast.LENGTH_SHORT).show();

                // Go to another activity
                if (position == 0){
                    Intent myIntent = new Intent(view.getContext(),TestScroolView.class);
                    startActivityForResult(myIntent, 0);
                }
                else if (position == 1){
                    Intent myIntent = new Intent(view.getContext(),TestScroolView.class);
                    startActivityForResult(myIntent, 0);
                }
                else if (position == 2){
                    Intent myIntent = new Intent(HospitalList.this,TestScroolView.class);
                    startActivityForResult(myIntent, 0);
                }
                else {
                    Intent myIntent = new Intent(view.getContext(),TestScroolView.class);
                    startActivityForResult(myIntent, 0);
                }

            }

        });

        // Set Ratingbar
    }
}
