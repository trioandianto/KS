package com.kliksembuh.ks;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kliksembuh.ks.library.Intromanager;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Intromanager intromanager;
    private ViewPagerAdapter viewPagerAdapter;
    private TextView[] dots;
    Button next,skip;
    private LinearLayout dotsLayout;
    private int[] layouts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intromanager = new Intromanager(this);
        if(!intromanager.Check()){
            intromanager.setFirst(false);
            Intent i= new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            finish();

        }
        if(Build.VERSION.SDK_INT>=25){

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);

//        if( getIntent().getBooleanExtra("Exit me", false)){
//            finish();
//            return; // add this to prevent from doing unnecessary stuffs
//        }

        viewPager =(ViewPager)findViewById(R.id.view_pager);
        dotsLayout=(LinearLayout)findViewById(R.id.layoutdots);
        skip =(Button)findViewById(R.id.btn_skip);
        next = (Button)findViewById(R.id.btn_next);
        layouts = new int[]{
                R.layout.activity_screen1,
                R.layout.activity_screen2,
                R.layout.activity_screen3,
                R.layout.activity_screen4};
        addBottomDots(0);
        changeStatusBarColor();
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewListener);
//        skip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(i);
//                finish();
//            }
//        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(+1);
                if(current<layouts.length){
                    viewPager.setCurrentItem(current);
                }
                else{
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });


    }
    private static long back_pressed;

    @Override
    public void onBackPressed() {
//
//        if (back_pressed + 2000 > System.currentTimeMillis()) {
//            super.onBackPressed();
//        } else {
//            Toast.makeText(this, "Press once again to exit!", Toast.LENGTH_SHORT).show();
//            back_pressed = System.currentTimeMillis();
//        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void addBottomDots(int position){
        dots = new TextView[layouts.length];
        int[] colorActive = getResources().getIntArray(R.array.dot_active);
        int[] colorInactive = getResources().getIntArray(R.array.dot_inactive);
        dotsLayout.removeAllViews();
        for (int i =0;i<dots.length;i++) {
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive[position]);
            dotsLayout.addView(dots[i]);
        }
        if(dots.length>0){
            dots[position].setTextColor(colorActive[position]);
        }
    }
    private int getItem(int i){
        return viewPager.getCurrentItem() + 1;
    }

ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        addBottomDots(position);
        if(position==layouts.length-1){
            next.setText("SIGN IN");
            skip.setVisibility(View.GONE);
        }
        else{
            next.setText("NEXT");
            skip.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
};
    private void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    public class ViewPagerAdapter extends PagerAdapter{

        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, int position){
            layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position],container,false);
            container.addView(view);
            return view;

        }
        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View)object;
            container.removeView(view);
        }
    }
    public void sendNotification(View view) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);

        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.mipmap.ic_launcher_new);
        mBuilder.setContentTitle("Klik Sembuh");
        mBuilder.setContentText("Selamat anda telah bergabung dengan kami.");

        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());
        startActivity(intent);
        finish();

    }
}
