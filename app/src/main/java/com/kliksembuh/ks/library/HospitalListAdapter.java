package com.kliksembuh.ks.library;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.kliksembuh.ks.R;
import com.kliksembuh.ks.models.Hospital;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trio Andianto on 2/1/2017.
 */

public class HospitalListAdapter<T> extends BaseAdapter implements Filterable {
    private Context mContext;
    private List<Hospital> mHospitalList;
    private ArrayList<T> mOriginalValues;
    private ArrayFilter mFilter;
    private final Object mLock = new Object();
    private List<T> mObjects;

    // Constructor
    public HospitalListAdapter(Context mContext, List<Hospital> mHospitalList) {
        this.mContext = mContext;
        this.mHospitalList = mHospitalList;
    }

    @Override
    public int getCount() {
        return mHospitalList.size();
    }

    @Override
    public Object getItem(int position) {
        return mHospitalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = View.inflate(mContext, R.layout.hospital_list, null);
        ImageView imgView = (ImageView)newView.findViewById(R.id.iv_hospital_pic);
        TextView tvName = (TextView)newView.findViewById(R.id.tv_name);
        TextView tvAddress = (TextView)newView.findViewById(R.id.tv_address);
        TextView tvPhoneNbr = (TextView) newView.findViewById(R.id.tv_phone);
        // Text view for capabilities (on Backend there is no IGD, but BPJS)
        TextView tvIGD = (TextView) newView.findViewById(R.id.tv_IGD);
        TextView tv24hour = (TextView) newView.findViewById(R.id.tv_24hours);


        imgView.setImageDrawable(mHospitalList.get(position).getHospital_pic_id());
        tvName.setText(mHospitalList.get(position).getName());
        tvAddress.setText(mHospitalList.get(position).getAddress());
        tvPhoneNbr.setText(mHospitalList.get(position).getPhoneNbr());
        String getCapabilitiesID = mHospitalList.get(position).getCapabilitiesDesc();

        if(getCapabilitiesID == String.valueOf(1)){
            tv24hour.setText("24");
        }
        else if (getCapabilitiesID == String.valueOf(2)){
            tvIGD.setText("BPJS");
        }




        // Save hospital id to tag
        newView.setTag(mHospitalList.get(position).getId());

        return newView;
    }
    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }
    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            final FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                final ArrayList<T> list;
                synchronized (mLock) {
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                final String prefixString = prefix.toString().toLowerCase();

                final ArrayList<T> values;
                synchronized (mLock) {
                    values = new ArrayList<>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<T> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final T value = values.get(i);
                    final String valueText = value.toString().toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");
                        for (String word : words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<T>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}