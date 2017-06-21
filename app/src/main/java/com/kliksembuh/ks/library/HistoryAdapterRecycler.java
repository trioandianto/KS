package com.kliksembuh.ks.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kliksembuh.ks.R;
import com.kliksembuh.ks.models.Doctor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Trio Andianto on 6/20/2017.
 */

public class HistoryAdapterRecycler extends RecyclerView.Adapter<HistoryAdapterRecycler.DoctorViewHolder>{
    private List<Doctor> mDoctorList;
    private Context context;
    private ArrayList<Doctor> mOriginalValues;

    public HistoryAdapterRecycler (Context context,List<Doctor> mDoctorList){
        this.mDoctorList = mDoctorList;
        this.context = context;
        this.mOriginalValues = new ArrayList<Doctor>();
        this.mOriginalValues.addAll(mDoctorList);
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgDview ;
        public TextView tvFrontTitle;
        public TextView tvTittle;
        public TextView tvDrname;
        public TextView tvDrspecialty;
        public TextView btnKualiifikasi;

        public DoctorViewHolder(View newDview) {
            super(newDview);
            imgDview = (ImageView)newDview.findViewById(R.id.iv_doc_pic_list);
            tvFrontTitle = (TextView)newDview.findViewById(R.id.tv_FrontTitleDr);
            tvTittle = (TextView)newDview.findViewById(R.id.tv_specialty_list) ;
            tvDrname = (TextView)newDview.findViewById(R.id.tv_list_drname);
            tvDrspecialty = (TextView)newDview.findViewById(R.id.tv_tittle_list);
            btnKualiifikasi = (TextView) newDview.findViewById(R.id.btn_kualiifikasi);
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

        Picasso.with(context).load(mDoctorList.get(position).getDoc_pic_id()).into(holder.imgDview);
        holder.tvFrontTitle.setText(mDoctorList.get(position).getFrontTtlDoc());
        holder.tvDrname.setText(mDoctorList.get(position).getNameDoc());
        holder.tvDrspecialty.setText(mDoctorList.get(position).getSpecialty());
        holder.btnKualiifikasi.setText(mDoctorList.get(position).getKualifikasi());
        holder.tvTittle.setText(mDoctorList.get(position).getTittle());

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDoctorList.size();
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mDoctorList.clear();
        if (charText.length() == 0) {
            mDoctorList.addAll(mOriginalValues);
        } else {
            for (Doctor wp : mOriginalValues) {
                if (wp.getSpecialty().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mDoctorList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
