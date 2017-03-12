package com.kliksembuh.ks.library;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.kliksembuh.ks.R;
import com.kliksembuh.ks.models.Doctor;

import java.util.List;

/**
 * Created by Ucu Nurul Ulum on 06/02/2017.
 */

public class DoctorListAdapter <T> extends BaseAdapter implements Filterable {
    private Context mContext;
    private List<Doctor> mDoctorList;

    // Constructor
    public DoctorListAdapter(Context mContext, List<Doctor> mDoctorList) {
        this.mContext = mContext;
        this.mDoctorList = mDoctorList;
    }

    @Override
    public int getCount() {
        return mDoctorList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDoctorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Layout view for List of Doctor
        View newDview = View.inflate(mContext, R.layout.doctor_list, null);

        // getView for Doctor List
        ImageView imgDview = (ImageView)newDview.findViewById(R.id.iv_doc_pic_list);
        TextView tvDrname = (TextView)newDview.findViewById(R.id.tv_list_drname);
        TextView tvDrspecialty = (TextView)newDview.findViewById(R.id.tv_specialty_list);

        // Set text and image for View of Doctor
        imgDview.setImageDrawable(mDoctorList.get(position).getDoc_pic_id());
        tvDrname.setText(mDoctorList.get(position).getNameDoc());
        tvDrspecialty.setText(mDoctorList.get(position).getSpecialty());

        // Save hospital id to tag
        newDview.setTag(mDoctorList.get(position).getDoc_id());

        return newDview;
    }
    @Override
    public Filter getFilter() {
        return null;
    }
}
