package com.kliksembuh.ks.library;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kliksembuh.ks.R;
import com.kliksembuh.ks.models.Reminder;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Ucu Nurul Ulum on 29/05/2017.
 */

public class ReminderListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Reminder> mReminder;

    public ReminderListAdapter(Context mContext, List<Reminder> mReminder) {
        this.mContext = mContext;
        this.mReminder = mReminder;
    }

    @Override
    public int getCount() {
        return mReminder.size();
    }

    @Override
    public Object getItem(int position) {
        return mReminder.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newRView = View.inflate(mContext, R.layout.list_reminder, null);
        TextView tvRmdrTitle = (TextView) newRView.findViewById(R.id.tv_rmdrTitle);
        TextView tvRmdrDosage = (TextView) newRView.findViewById(R.id.tv_rmdrDosage);
        TextView tvRmdrDaysTiming =(TextView) newRView.findViewById(R.id.tv_rmdrDays);

        String getTitle = mReminder.get(position).getRmdrTitle();
        String getDosage = mReminder.get(position).getRmdrDosage();
        String getDuration = mReminder.get(position).getRmdrDosage();
        String getType = mReminder.get(position).getRmdrType();
        String getDays = mReminder.get(position).getRmdrDays();
        String getTimings = mReminder.get(position).getRmdrTimings();

        tvRmdrTitle.setText(getTitle);
        tvRmdrDosage.setText(getDosage);
        tvRmdrDaysTiming.setText(getDays + " | " + getTimings);
        newRView.setTag(mReminder.get(position).getRmdr_id());

        return newRView;
    }
}
