package com.kliksembuh.ks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.kliksembuh.ks.library.HospitalListAdapter;
import com.kliksembuh.ks.models.Hospital;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Trio Andianto on 1/24/2017.
 */
public class FavoriteInstansiActivity extends Fragment {

    private ListView lvHospital;
    private HospitalListAdapter hAdapter;
    private List<Hospital> mHospitalList;

    RatingBar rb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_favorite_instansi, container, false);
        lvHospital = (ListView)rootView.findViewById(R.id.listview_hospital_favorite);
        mHospitalList = new ArrayList<>();
        mHospitalList.add(new Hospital(1, R.drawable.rs_pmi_bogor , "RS PMI Jakarta", "Jalan Rumah Sakit I, Bogor Tengah, Kota Bogor, Jawa Barat 16129"));
        mHospitalList.add(new Hospital(2, R.drawable.rs_cibinong_bogor, "RSUD Cibinong Bogor", "Jalan KSR Dadi Kusmayadi No. 27, Tengah, Cibinong, Bogor, Jawa Barat 16914"));
        mHospitalList.add(new Hospital(3, R.drawable.rs_medika_dramaga, "RS Medika Darmaga", "Jl. Raya Dramaga KM. 7.3, Bogor Barat, Margajaya, Bogor Barat, Kota Bogor, Jawa Barat 16680"));
        mHospitalList.add(new Hospital(4, R.drawable.rs_bogor_medical_centre, "RS Bogor Medical Centre", "Jl. Pajajaran Indah V No. 97, Baranangsiang, Bogor Timur, Kota Bogor, Jawa Barat 16143"));

        hAdapter = new HospitalListAdapter(getApplicationContext(), mHospitalList);
        lvHospital.setAdapter(hAdapter);

        lvHospital.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something
                // Ex. display msg with hospital id from view.getTag
                Toast.makeText(getApplicationContext(), "Clicked hospital id = " + view.getTag(), Toast.LENGTH_SHORT).show();
            }

        });

        return rootView;
    }
}
