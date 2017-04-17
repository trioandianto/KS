package com.kliksembuh.ks.library;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.kliksembuh.ks.R;
import com.kliksembuh.ks.models.Patient;

import java.util.List;

/**
 * Created by Ucu Nurul Ulum on 07/04/2017.
 */

public class PatientListAdapter <I> extends BaseAdapter implements Filterable {
    private Context mContext;
    private List<Patient> mPatientList;

    // Constructor
    public PatientListAdapter(Context mContext, List<Patient> mPatientList) {
        this.mContext = mContext;
        this.mPatientList = mPatientList;
    }

    @Override
    public int getCount() {
        return mPatientList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPatientList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Layout view for List of Doctor
        View newPatView = View.inflate(mContext, R.layout.list_patientprof, null);

        // getView for Patient List
        TextView tvPatMame = (TextView)newPatView.findViewById(R.id.tvListPatientProfName);
        TextView tvPatStatus = (TextView)newPatView.findViewById(R.id.tvListPatientProfStatus);

        // Set text and image for View of Doctor
        tvPatMame.setText(mPatientList.get(position).getFirstName());
        tvPatStatus.setText(mPatientList.get(position).getPatStatus());

        // Save hospital id to tag
        newPatView.setTag(mPatientList.get(position).getPat_id());

        return newPatView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
