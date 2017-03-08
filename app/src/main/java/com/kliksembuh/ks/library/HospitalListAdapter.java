package com.kliksembuh.ks.library;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kliksembuh.ks.R;
import com.kliksembuh.ks.models.Hospital;

import java.util.List;

/**
 * Created by Trio Andianto on 2/1/2017.
 */

public class HospitalListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Hospital> mHospitalList;

    // Constructor
    public HospitalListAdapter(Context mContext, List<Hospital> mHospitalList) {
        this.mContext = mContext;
        this.mHospitalList = mHospitalList;
    }

    @Override
    public int getCount() {
        return mHospitalList.size();
    }

    @Override
    public Object getItem(int position) {
        return mHospitalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = View.inflate(mContext, R.layout.hospital_list, null);
        ImageView imgView = (ImageView)newView.findViewById(R.id.iv_hospital_pic);
        TextView tvName = (TextView)newView.findViewById(R.id.tv_name);
        TextView tvAddress = (TextView)newView.findViewById(R.id.tv_address);
//        Button btnBpjs = (Button)newView.findViewById(R.id.btn_bpjs);
//        btnBpjs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext.getApplicationContext(), "Clicked hospital id = " + view.getTag(), Toast.LENGTH_SHORT).show();
//            }
//        });
        // Set text for TextView
        imgView.setImageResource(mHospitalList.get(position).getHospital_pic_id());
        tvName.setText(mHospitalList.get(position).getName());
        tvAddress.setText(mHospitalList.get(position).getAddress());


        // Save hospital id to tag
        newView.setTag(mHospitalList.get(position).getId());

        return newView;
    }
}