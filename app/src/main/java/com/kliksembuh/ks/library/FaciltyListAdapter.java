package com.kliksembuh.ks.library;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kliksembuh.ks.R;
import com.kliksembuh.ks.models.Facilty;

import java.util.List;

/**
 * Created by Trio Andianto on 5/24/2017.
 */

public class FaciltyListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Facilty> mFaciltyList;

    public FaciltyListAdapter(Context mContext, List<Facilty> mFaciltyList) {
        this.mContext = mContext;
        this.mFaciltyList = mFaciltyList;
    }

    @Override
    public int getCount() {
        return mFaciltyList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFaciltyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newInsView = View.inflate(mContext, R.layout.list_item, null);

        TextView tvNamaFacilty = (TextView) newInsView.findViewById(R.id.name);
        tvNamaFacilty.setText(mFaciltyList.get(position).getNama());
        newInsView.setTag(mFaciltyList.get(position).getId());

        return newInsView;

    }
}
