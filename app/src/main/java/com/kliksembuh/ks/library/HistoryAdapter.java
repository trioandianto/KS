package com.kliksembuh.ks.library;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kliksembuh.ks.R;
import com.kliksembuh.ks.models.HistoryUpComing;

import java.util.List;

/**
 * Created by Trio Andianto on 4/12/2017.
 */

public class HistoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<HistoryUpComing> mHistory;

    public HistoryAdapter (Context context, List<HistoryUpComing> history){
        this.mContext = context;
        this.mHistory = history;
    }
    @Override
    public int getCount() {
        return mHistory.size();
    }

    @Override
    public Object getItem(int position) {
        return mHistory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newHview = View.inflate(mContext, R.layout.list_item_upcoming, null);


        TextView tvAppointment = (TextView)newHview.findViewById(R.id.tvNoAppointmentHistory);
        TextView tvDate = (TextView)newHview.findViewById(R.id.tvDibuatTanggal);
        TextView tvHistoryRumahSakit = (TextView)newHview.findViewById(R.id.tvHistoryRumahSakit);
        ImageView imgDview = (ImageView)newHview.findViewById(R.id.ivDocPicHistory);
        TextView tvDrname = (TextView)newHview.findViewById(R.id.tv_Drname_History);
        TextView tvSpecialty = (TextView)newHview.findViewById(R.id.tv_SpecialtyHistory);
        TextView tvStatusHistory = (TextView)newHview.findViewById(R.id.tvStatusHistory);
        TextView tvWaktuBerobat = (TextView)newHview.findViewById(R.id.tvWaktuBerobat);
        TextView tvJamBerobat = (TextView)newHview.findViewById(R.id.tvJamBerobat);

        tvAppointment.setText(mHistory.get(position).getNoAppointment());
        tvDate.setText(mHistory.get(position).getTanggal());
        tvHistoryRumahSakit.setText(mHistory.get(position).getRumahSakit());
        imgDview.setImageDrawable(mHistory.get(position).getImgHistoryDoc());
        tvDrname.setText(mHistory.get(position).getNamaDokter());
        tvSpecialty.setText(mHistory.get(position).getSpecialtyDoc());
        tvStatusHistory.setText(mHistory.get(position).getStatusHistory());
        tvWaktuBerobat.setText(mHistory.get(position).getWaktuBerobat());
        tvJamBerobat.setText(mHistory.get(position).getJamBerobat());
        newHview.setTag(mHistory.get(position).getIdHistoryUpComing());

        return newHview;
    }
}
