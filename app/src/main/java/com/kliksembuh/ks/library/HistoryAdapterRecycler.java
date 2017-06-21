package com.kliksembuh.ks.library;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kliksembuh.ks.R;
import com.kliksembuh.ks.models.HistoryUpComing;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trio Andianto on 6/20/2017.
 */

public class HistoryAdapterRecycler extends RecyclerView.Adapter<HistoryAdapterRecycler.DoctorViewHolder>{
    private List<HistoryUpComing> mHistory;
    private Context context;
    private ArrayList<HistoryUpComing> mOriginalValues;

    public HistoryAdapterRecycler (Context context,List<HistoryUpComing> mHistory){
        this.mHistory = mHistory;
        this.context = context;
        this.mOriginalValues = new ArrayList<HistoryUpComing>();
        this.mOriginalValues.addAll(mHistory);
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView tvAppointment;

        TextView tvHistoryRumahSakit;
        ImageView imgDview;
        TextView tvDrname ;
        TextView tvSpecialty;
        TextView tvAppointSchedule;
        TextView tvStatusHistory;
        TextView tvWaktuBerobat;
        TextView tvTimeStart;
        TextView tvTimeEnd;

        public DoctorViewHolder(View newHview) {
            super(newHview);
            tvAppointment = (TextView)newHview.findViewById(R.id.tvNoAppointmentHistory);
            tvHistoryRumahSakit = (TextView)newHview.findViewById(R.id.tvHistoryRumahSakit);
            imgDview = (ImageView)newHview.findViewById(R.id.ivDocPicHistory);
            tvDrname = (TextView)newHview.findViewById(R.id.tv_Drname_History);
            tvSpecialty = (TextView)newHview.findViewById(R.id.tv_SpecialtyHistory);
            tvAppointSchedule = (TextView) newHview.findViewById(R.id.tvBookSchedule);
            tvStatusHistory = (TextView)newHview.findViewById(R.id.tvStatusHistory);
            tvWaktuBerobat = (TextView)newHview.findViewById(R.id.tvWaktuBerobat);
            tvTimeStart = (TextView)newHview.findViewById(R.id.tvTimeStart);
            tvTimeEnd = (TextView)newHview.findViewById(R.id.tvTimeEnd);
        }

    }

    @Override
    public DoctorViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {


        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.doctor_list, parent, false);
        final DoctorViewHolder viewHolder = new DoctorViewHolder(listItem);
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = viewHolder.getAdapterPosition();

            }
        });

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(DoctorViewHolder holder, int position) {
//            holder.imgDview.setImageDrawable(mDoctorList.get(position).getDoc_pic_id());
        String appointment = mHistory.get(position).getNoAppointment();
        String createdDate = mHistory.get(position).getTanggal();

        holder.tvAppointment.setText("# " + appointment + " / " + createdDate );
//        tvDate.setText(mHistory.get(position).getTanggal());
        holder.tvHistoryRumahSakit.setText(mHistory.get(position).getRumahSakit());

        Picasso.with(context).load(mHistory.get(position).getImgHistoryDoc()).into(holder.imgDview);
        holder.tvDrname.setText(mHistory.get(position).getNamaDokter());
        holder.tvAppointSchedule.setText(mHistory.get(position).getAppointmentDate());
        holder.tvSpecialty.setText(mHistory.get(position).getSpecialtyDoc())   ;
        holder.tvStatusHistory.setText(mHistory.get(position).getStatusHistory());
        String convColor = holder.tvStatusHistory.getText().toString();
        if(convColor.equals("Menunggu Konfirmasi")){
//            tvStatusHistory.setBackgroundResource(R.color.red);}
            holder.tvStatusHistory.setTextColor(Color.parseColor("#d50000"));}
        if(convColor.equals("Dikonfirmasi")){
            holder.tvStatusHistory.setTextColor(Color.parseColor("#eb3812"));}
        if(convColor.equals("Telah diulas")){
            holder.tvStatusHistory.setTextColor(Color.parseColor("#FF9800"));}
        if(convColor.equals("Selesai")){
            holder.tvStatusHistory.setTextColor(Color.parseColor("#2e7d32"));}
        holder.tvWaktuBerobat.setText(mHistory.get(position).getWaktuBerobat());
        holder.tvTimeStart.setText(mHistory.get(position).getTimeStart());
        holder.tvTimeEnd.setText(mHistory.get(position).getTimeEnd());

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mHistory.size();
    }
}
