package com.kliksembuh.ks.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kliksembuh.ks.R;

import java.util.List;


/**
 * Created by Trio Andianto on 1/24/2017.
 */
public class ListAdapter extends ArrayAdapter {
    private List<ListImage> listimages;
    private int[] imageView = {R.drawable.dokter,R.drawable.dokter};
    private String[] namadokter={"TRIO","UCU"};
    private String[] alamat={"Brebes","Bogor"};
    private int resource;
    LayoutInflater inflater;

    public ListAdapter(Context context, int resource, List<ListImage> objects) {
        super(context, resource, objects);
        listimages = objects;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(R.layout.activity_list_favorite_dokter, null);

        }

        ImageView images;
        TextView tvlistname;
        TextView tvalamat;
        RatingBar rbdokter;

        images =(ImageView)convertView.findViewById(R.id.image_favorite_dokter);
        tvlistname = (TextView)convertView.findViewById(R.id.tvnamainstansi);
        tvalamat = (TextView)convertView.findViewById(R.id.tvalamatdokter);
        rbdokter = (RatingBar)convertView.findViewById(R.id.rbdokter);


        tvlistname.setText(listimages.get(position).getListName());
        tvalamat.setText(listimages.get(position).getAlamat());
        rbdokter.setRating(listimages.get(position).getRating());

        return convertView;

    }



}
