package com.kliksembuh.ks.library;

import android.content.Context;
import android.graphics.Color;
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
        //TextView tvDate = (TextView)newHview.findViewById(R.id.tvDibuatTanggal);
        TextView tvHistoryRumahSakit = (TextView)newHview.findViewById(R.id.tvHistoryRumahSakit);
        ImageView imgDview = (ImageView)newHview.findViewById(R.id.ivDocPicHistory);
        TextView tvDrname = (TextView)newHview.findViewById(R.id.tv_Drname_History);
        TextView tvSpecialty = (TextView)newHview.findViewById(R.id.tv_SpecialtyHistory);
        TextView tvAppointSchedule = (TextView) newHview.findViewById(R.id.tvBookSchedule);
        TextView tvStatusHistory = (TextView)newHview.findViewById(R.id.tvStatusHistory);
        TextView tvWaktuBerobat = (TextView)newHview.findViewById(R.id.tvWaktuBerobat);
        TextView tvTimeStart = (TextView)newHview.findViewById(R.id.tvTimeStart);
        TextView tvTimeEnd = (TextView)newHview.findViewById(R.id.tvTimeEnd);

        String appointment = mHistory.get(position).getNoAppointment();
        String createdDate = mHistory.get(position).getTanggal();

        tvAppointment.setText("# " + appointment + " / " + createdDate );
//        tvDate.setText(mHistory.get(position).getTanggal());
        tvHistoryRumahSakit.setText(mHistory.get(position).getRumahSakit());
        imgDview.setImageDrawable(mHistory.get(position).getImgHistoryDoc());
        tvDrname.setText(mHistory.get(position).getNamaDokter());
        tvAppointSchedule.setText(mHistory.get(position).getAppointmentDate());
        tvSpecialty.setText(mHistory.get(position).getSpecialtyDoc())   ;
        tvStatusHistory.setText(mHistory.get(position).getStatusHistory());
        String convColor = tvStatusHistory.getText().toString();
        if(convColor.equals("Menunggu Konfirmasi")){
//            tvStatusHistory.setBackgroundResource(R.color.red);}
            tvStatusHistory.setTextColor(Color.parseColor("#d50000"));}
        if(convColor.equals("Dikonfirmasi")){
            tvStatusHistory.setTextColor(Color.parseColor("#eb3812"));}
        if(convColor.equals("Telah diulas")){
            tvStatusHistory.setTextColor(Color.parseColor("#FF9800"));}
        if(convColor.equals("Selesai")){
            tvStatusHistory.setTextColor(Color.parseColor("#2e7d32"));}
        tvWaktuBerobat.setText(mHistory.get(position).getWaktuBerobat());
        tvTimeStart.setText(mHistory.get(position).getTimeStart());
        tvTimeEnd.setText(mHistory.get(position).getTimeEnd());
        newHview.setTag(mHistory.get(position).getIdHistoryUpComing());

        return newHview;
    }
}
