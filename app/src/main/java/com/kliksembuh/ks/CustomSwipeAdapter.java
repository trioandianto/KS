package com.kliksembuh.ks;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by trioandianto on 12/21/2016.
 */
public class CustomSwipeAdapter extends PagerAdapter {

    private int[] image_resource = {R.layout.activity_home_screen1,
            R.layout.activity_home_screen2,
            R.layout.activity_home_screen3,
            R.layout.activity_home_screen4};
    private Context ctx;
    private LayoutInflater layoutInflater;

    public CustomSwipeAdapter(Context ctx){
        this.ctx = ctx;
    }
    @Override
    public int getCount() {
        return image_resource.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    int counter = 0;
    @Override
    public Object instantiateItem(ViewGroup container,int position){
        //super.instantiateItem(container, position);
        layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.swipe_layout,container,false);
        ImageView imageView = (ImageView)item_view.findViewById(R.id.image_view);

        if (position < 3) {
            imageView.setImageResource(image_resource[position]);
            container.addView(item_view);
            //System.out.println( ex.getMessage());
        }
        counter ++;

        return item_view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((LinearLayout)object);
    }
}
