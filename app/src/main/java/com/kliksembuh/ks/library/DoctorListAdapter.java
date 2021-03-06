package com.kliksembuh.ks.library;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kliksembuh.ks.R;
import com.kliksembuh.ks.models.Doctor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Ucu Nurul Ulum on 06/02/2017.
 */

public class DoctorListAdapter <T> extends BaseAdapter {
    private Context mContext;
    private List<Doctor> mDoctorList;
    private ArrayList<Doctor> mOriginalValues;
    private Boolean isClick = false;
    private int countClick = 0;

    // Constructor
    public DoctorListAdapter(Context mContext, List<Doctor> mDoctorList) {
        this.mContext = mContext;
        this.mDoctorList = mDoctorList;
        this.mOriginalValues = new ArrayList<Doctor>();
        this.mOriginalValues.addAll(mDoctorList);
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
        TextView tvFrontTitle = (TextView)newDview.findViewById(R.id.tv_FrontTitleDr);
        TextView tvTittle = (TextView)newDview.findViewById(R.id.tv_specialty_list) ;
        TextView tvDrname = (TextView)newDview.findViewById(R.id.tv_list_drname);
        TextView tvDrspecialty = (TextView)newDview.findViewById(R.id.tv_tittle_list);
        TextView btnKualiifikasi = (TextView) newDview.findViewById(R.id.btn_kualiifikasi);

        final TextView tvFavDoc = (TextView) newDview.findViewById(R.id.tvFavDoc);
        final TextView tvValueOfDoc = (TextView) newDview.findViewById(R.id.tvValueOfFavDoc);
        tvFavDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClick){
                    tvFavDoc.setBackgroundResource(R.drawable.ic_favorite_24dp);
                    countClick++;
                    tvValueOfDoc.setText(String.valueOf(countClick));
                    isClick=true;
                }
                else{
                    tvFavDoc.setBackgroundResource(R.drawable.ic_favorite_border_black_14dp);
                    countClick--;
                    tvValueOfDoc.setText(String.valueOf(countClick));
                    isClick=false;
                }
            }
        });
        //TextView tvFavoriteDoc = (TextView) newDview.findViewById(R.id.tvFavouriteDoc);

        btnKualiifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Masih dalam pengembangan.", Toast.LENGTH_LONG).show();
            }
        });

        // Set text and image for View of Doctor
//        imgDview.setImageDrawable(mDoctorList.get(position).getDoc_pic_id());
        Picasso.with(mContext).load(mDoctorList.get(position).getDoc_pic_id()).resize(80, 90).into(imgDview);
        tvFrontTitle.setText(mDoctorList.get(position).getFrontTtlDoc());
        tvDrname.setText(mDoctorList.get(position).getNameDoc());
        tvDrspecialty.setText(mDoctorList.get(position).getSpecialty());
        btnKualiifikasi.setText(mDoctorList.get(position).getKualifikasi());
        tvTittle.setText(mDoctorList.get(position).getTittle());

        // Save hospital id to tag
        newDview.setTag(mDoctorList.get(position).getDoc_id());

        return newDview;
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
