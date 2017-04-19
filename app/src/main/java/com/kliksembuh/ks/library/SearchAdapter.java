package com.kliksembuh.ks.library;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kliksembuh.ks.R;
import com.kliksembuh.ks.models.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Trio Andianto on 3/10/2017.
 */

public class SearchAdapter<T> extends BaseAdapter{
    private Context mContext;
    private List<Location> mLocation;
    private ArrayList<Location> mOriginalValues;
    public SearchAdapter(Context mContext, List<Location> mLocation) {
        this.mContext = mContext;
        this.mLocation = mLocation;
        this.mOriginalValues = new ArrayList<Location>();
        this.mOriginalValues.addAll(mLocation);
    }

    @Override
    public int getCount() {
        return mLocation.size();
    }

    @Override
    public Object getItem(int position) {
        return mLocation.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View newView = View.inflate(mContext, R.layout.list_item, null);
        TextView tvName = (TextView)newView.findViewById(R.id.name);
        tvName.setText(mLocation.get(position).getName());
        newView.setTag(mLocation.get(position).getId());
        return newView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mLocation.clear();
        if (charText.length() == 0) {
            mLocation.addAll(mOriginalValues);
        } else {
            for (Location wp : mOriginalValues) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mLocation.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
