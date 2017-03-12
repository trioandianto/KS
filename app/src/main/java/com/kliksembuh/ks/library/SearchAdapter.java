package com.kliksembuh.ks.library;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.kliksembuh.ks.R;
import com.kliksembuh.ks.models.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trio Andianto on 3/10/2017.
 */

public class SearchAdapter<T> extends BaseAdapter implements Filterable {
    private Context mContext;
    private List<Location> mLocation;
    private ArrayList<T> mOriginalValues;
    private ArrayFilter mFilter;
    private final Object mLock = new Object();
    private List<T> mObjects;
    public SearchAdapter(Context mContext, List<Location> mLocation) {
        this.mContext = mContext;
        this.mLocation = mLocation;
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
        TextView tvCode = (TextView)newView.findViewById(R.id.email);

        tvName.setText(mLocation.get(position).getName());;
        tvCode.setText(mLocation.get(position).getId());

        newView.setTag(mLocation.get(position).getId());

        return newView;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new SearchAdapter.ArrayFilter();
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
