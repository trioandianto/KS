package com.kliksembuh.ks.library;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kliksembuh.ks.R;
import com.kliksembuh.ks.models.JadwalDokter;

import java.util.List;


/**
 * Created by Trio Andianto on 3/29/2017.
 */

public class JadwalDokterAdapter extends BaseAdapter{

    private Context mContext;
    private List<JadwalDokter> mJadwalDokter;

    public JadwalDokterAdapter(Context context,List<JadwalDokter> jadwalDokters){
        mContext=context;
        mJadwalDokter = jadwalDokters;
    }
    @Override
    public int getCount() {
        return mJadwalDokter.size();
    }
    @Override
    public Object getItem(int position) {
        return mJadwalDokter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newDview = View.inflate(mContext, R.layout.list_jadwal_dokter, null);
        TextView jadwal = (TextView)newDview.findViewById(R.id.tvjadwaldokter);
        jadwal.setText(mJadwalDokter.get(position).getJadwalDokter());
        newDview.setTag(mJadwalDokter.get(position).getDetailProgramID());
        return newDview;
    }
}
