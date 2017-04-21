package com.kliksembuh.ks.library;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kliksembuh.ks.R;
import com.kliksembuh.ks.models.PraktekDokter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trio Andianto on 4/19/2017.
 */

public class PraktekDokterAdapter extends BaseAdapter {
    private Context mContext;
    private List<PraktekDokter> mPraktek;
    private ArrayList<PraktekDokter> mOriginalValues;

    @Override
    public int getCount() {
        return mPraktek.size();
    }

    @Override
    public Object getItem(int position) {
        return mPraktek.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = View.inflate(mContext, R.layout.list_praktek_dokter_rumah_sakit, null);
        TextView tvName = (TextView)newView.findViewById(R.id.tvPraktekDokterRumahSakit);
        tvName.setText(mPraktek.get(position).getNamaRumahSakit());
        newView.setTag(mPraktek.get(position).getId());
        return newView;
    }
}
