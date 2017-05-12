package com.kliksembuh.ks.library;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kliksembuh.ks.R;
import com.kliksembuh.ks.models.Insurance;

import java.util.List;

/**
 * Created by Ucu Nurul Ulum on 10/05/2017.
 */

public class InsuranceListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Insurance> mInsuranceList;

    public InsuranceListAdapter(Context mContext, List<Insurance> mInsuranceList) {
        this.mContext = mContext;
        this.mInsuranceList = mInsuranceList;
    }

    @Override
    public int getCount() {
        return mInsuranceList.size();
    }

    @Override
    public Object getItem(int position) {
        return mInsuranceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newInsView = View.inflate(mContext, R.layout.list_insurance, null);

        ImageView imgInsView = (ImageView) newInsView.findViewById(R.id.iv_InsPic);
        TextView tvInsName = (TextView) newInsView.findViewById(R.id.tv_InsName);

        imgInsView.setImageDrawable(mInsuranceList.get(position).getInstImgUrl());
        tvInsName.setText(mInsuranceList.get(position).getInstName());

        newInsView.setTag(mInsuranceList.get(position).getInstImgUrl());

        return newInsView;

    }
}
