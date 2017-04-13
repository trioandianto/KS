package com.kliksembuh.ks.library;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kliksembuh.ks.R;
import com.kliksembuh.ks.models.HistoryUpComing;

import java.util.List;

/**
 * Created by Trio Andianto on 4/12/2017.
 */

public class HistoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<HistoryUpComing> mHistory;

    public HistoryAdapter (Context context, List<HistoryUpComing> history){
        this.mContext = context;
        this.mHistory = history;
    }
    @Override
    public int getCount() {
        return mHistory.size();
    }

    @Override
    public Object getItem(int position) {
        return mHistory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newHview = View.inflate(mContext, R.layout.list_item_upcoming, null);

        ImageView imgDview = (ImageView)newHview.findViewById(R.id.image_favorite_dokter);
        TextView tvDrname = (TextView)newHview.findViewById(R.id.tvnamainstansi);
        TextView tvalamat = (TextView)newHview.findViewById(R.id.tvalamathistorydokter);
        TextView tvDate = (TextView)newHview.findViewById(R.id.tvtanggalhistorydokter);

        imgDview.setImageDrawable(mHistory.get(position).getImage());
        tvDate.setText(mHistory.get(position).getTanggal());
        tvalamat.setText(mHistory.get(position).getAlamat());
        tvDrname.setText(mHistory.get(position).getNamaDokter());
        return newHview;
    }
}
