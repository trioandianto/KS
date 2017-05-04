package com.kliksembuh.ks;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ucu Nurul Ulum on 17/02/2017.
 */

public class TimeSlotAdapter extends BaseAdapter {
    private Context mContext;
    private List<TimeSlot> mTimeList;

    // Constructor
    public TimeSlotAdapter(Context mContext, List<TimeSlot> mTimeList) {
        this.mContext = mContext;
        this.mTimeList = mTimeList;
    }

    @Override
    public int getCount() {
        return mTimeList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTimeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Layout view for List of Time
        View newTview = View.inflate(mContext, R.layout.timeslot_list, null);

        // getView for Time Slot List
        TextView tvTimeSlot = (TextView)newTview.findViewById(R.id.tv_timeSlot);

        // Set text / time for View of Time Schedule List
        tvTimeSlot.setText(mTimeList.get(position).getTime_slot());

        newTview.setTag(mTimeList.get(position).getTime_id());

        return newTview;
    }
}
