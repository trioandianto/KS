package com.kliksembuh.ks.library;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kliksembuh.ks.R;
import com.kliksembuh.ks.models.Spesialization;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Trio Andianto on 4/6/2017.
 */

public class SearchSpesializationAdapter<T> extends BaseAdapter {
    private Context mContext;
    private List<Spesialization> mSpesialization;
    private ArrayList<Spesialization> mOriginalValues;

    public SearchSpesializationAdapter(Context context, List<Spesialization> spesializations){
        this.mContext = context;
        this.mSpesialization = spesializations;
        this.mOriginalValues = new ArrayList<Spesialization>();
        this.mOriginalValues.addAll(mSpesialization);

    }

    @Override
    public int getCount() {
        return mSpesialization.size();
    }

    @Override
    public Object getItem(int position) {
        return mSpesialization.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = View.inflate(mContext, R.layout.list_item, null);
        TextView tvName = (TextView)newView.findViewById(R.id.name);
        tvName.setText(mSpesialization.get(position).getName());
        newView.setTag(mSpesialization.get(position).getId());
        return newView;
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mSpesialization.clear();
        if (charText.length() == 0) {
            mSpesialization.addAll(mOriginalValues);
        } else {
            for (Spesialization wp : mOriginalValues) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mSpesialization.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
