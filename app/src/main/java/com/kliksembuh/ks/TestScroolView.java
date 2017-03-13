package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kliksembuh.ks.library.DoctorListAdapter;
import com.kliksembuh.ks.library.HttpHandler;
import com.kliksembuh.ks.library.ObservableScrollView;
import com.kliksembuh.ks.models.Doctor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class TestScroolView extends ActionBarActivity implements OnMapReadyCallback,AdapterView.OnItemSelectedListener {
    public static final String EXTRA_NAME = "cheese_name";
    private GoogleMap mMap;
    ViewPager viewPager;
    private int[] layouts;
    private ScrollView scrollView;
    private ObservableScrollView mScrollView;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private List<Doctor> mDokterList;
    private ViewPagerAdapter viewPagerAdapter;
    private CardView cardView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ListView lvDokter;
    private DoctorListAdapter dAdapter;
    private ProgressDialog pDialog;
    private Spinner spinner;
    private String rumahSakitID;
    private String toolbarTitle;

    private static final String[]paths = {"Dokter Umum", "Dokter Gigi", "Dokter Mata"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            String userID = b.getString("userID");
            rumahSakitID = userID;
            toolbarTitle = b.getString("tol");
        }

        setContentView(R.layout.activity_test_scrool_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(toolbarTitle);
        setSupportActionBar(toolbar);


        mDokterList = new ArrayList<>();
        lvDokter = (ListView)findViewById(R.id.lvDetailRumahSakit);
        //lvDokter.setNestedScrollingEnabled(true);
        spinner = (Spinner)findViewById(R.id.dplistdokter);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(TestScroolView.this,
                android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        layouts = new int[]{
                R.drawable.doctorlist1,
                R.drawable.doctorlist2,
                R.drawable.doctorlist3};
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager = (ViewPager)findViewById(R.id.backdrop);
        viewPager.setAdapter(viewPagerAdapter);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapdoctorlistcoba);
        mapFragment.getMapAsync(this);
//        cardView = (CardView)findViewById(R.id.cvdoktera);
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myIntent = new Intent(view.getContext(), BookingActivity.class);
//                startActivityForResult(myIntent, 0);
//            }
//        });
        //toolbar.addView(spinner);

        new GetContacts().execute();





    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng bogor = new LatLng(-6.595038, 106.816635);
        mMap.addMarker(new MarkerOptions().position(bogor).title("Bogor"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bogor,16.0f));
        mMap.setMapType(mMap.MAP_TYPE_TERRAIN);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p>
     * Impelmenters can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        Context context;
        public ViewPagerAdapter(Context context){
            this.context = context;
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position){

            View itemView = layoutInflater.inflate(R.layout.doctor_image_slide, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.doctorImageView);
            imageView.setImageResource(layouts[position]);

            container.addView(itemView);

            return itemView;

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
            container.removeView((LinearLayout) object);
        }
    }
    private class GetContacts extends AsyncTask<Void, Void, Void> {
        private Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(TestScroolView.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... args0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            try{
                JSONObject obj = new JSONObject(loadJSONFromAsset());

                // Getting JSON Array node
                JSONArray contacts = obj.getJSONArray("dokter");

                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);

                    if(c.getString("Rs").equals(rumahSakitID)){
                        String name = c.getString("name");
                        String id = c.getString("id");
                        String image = c.getString("imgUrl");
                        Drawable image1 = LoadImageFromWebOperations(image);
                        String alamat = c.getString("alamat");
                        mDokterList.add(new Doctor(Integer.parseInt(id), image1, name, alamat));
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            dAdapter = new DoctorListAdapter(getApplicationContext(), mDokterList);
//            hAdapter.getFilter().filter(searchView);

            lvDokter.setAdapter(dAdapter);
        }
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("dokter.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

}
