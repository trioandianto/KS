package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;

import com.kliksembuh.ks.library.HospitalListAdapter;
import com.kliksembuh.ks.library.HttpHandler;
import com.kliksembuh.ks.models.Hospital;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HospitalList extends AppCompatActivity {

    private String JSON_STRING;
    private ListView lvHospital;
    private HospitalListAdapter hAdapter;
    private List<Hospital> mHospitalList;
    private Button btnpeta;
    private ProgressDialog pDialog;
    private ListAdapter adapter;
    ArrayList<HashMap<String, String>> formList;
    private SearchView searchView;
    private String rumahSakitID;
    private String [] nameRumahSakit;

    RatingBar rb;

//    protected void onPreExecute(){
//        json_url = "http://192.168.1.18/userapi/api/institutions/getinstitutions";
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hospital Screen Logic (created by Ucu on 24012017)
        setContentView(R.layout.activity_hospital_list);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            String userID = b.getString("userID");
        }

        lvHospital = (ListView)findViewById(R.id.listview_hospital);
        btnpeta = (Button)findViewById(R.id.btnpeta);
        btnpeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(HospitalList.this,ListMapActivity.class);
                startActivity(myIntent);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });
        mHospitalList = new ArrayList<>();
        new GetContacts().execute();
        // Add sample data
        // We can get data by DB, or web service
        //mHospitalList.add(new Hospital(3, R.drawable.rs_pmi_bogor , "RS PMI Bogor", "Kota Bogor, Jawa Barat 16129"));
//        mHospitalList.add(new Hospital(4, R.drawable.rs_pmi_bogor, "RSUD Cibinong Bogor", "Kota Bogor, Jawa Barat 16914"));
//        mHospitalList.add(new Hospital(5, R.drawable.rs_pmi_bogor, "RS Medika Darmaga", "Kota Bogor, Jawa Barat 16680"));
//        mHospitalList.add(new Hospital(3, R.drawable.rs_pmi_bogor, "RS Bogor Medical Centre", "Kota Bogor, Jawa Barat 16143"));



            lvHospital.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something
                // Ex. display msg with hospital id from view.getTag
                //Toast.makeText(getApplicationContext(), "Clicked hospital id = " + view.getTag(), Toast.LENGTH_SHORT).show();
//                Class<? extends Activity> activityToStart = null;
//                switch (position) {
//                case 0:
//                    activityToStart = TestScroolView.class;
//                }
//                Intent i = new Intent(getApplicationContext(), activityToStart);
//                startActivity(i);

                // Go to another activity
                if (position == 0){
                    Intent myIntent = new Intent(getApplicationContext(),TestScroolView.class);
                    Bundle b = new Bundle();
                    b.putString("userID", "1");
                    b.putString("tol","RS PMI BOGOR");//Your id
                    //.putExtra("userID",userID);
                    myIntent.putExtras(b);
                    //.putExtra("userID",userID);
                    startActivityForResult(myIntent, 0);
                }
                else if (position == 1){
                    Intent myIntent = new Intent(getApplicationContext(),TestScroolView.class);
                    //Your id
                    Bundle b = new Bundle();
                    //.putExtra("userID",userID);
                    b.putString("userID", "2");
                    b.putString("tol","RS CIBINGONG BOGOR");//Your id
                    //.putExtra("userID",userID);
                    myIntent.putExtras(b);
                    startActivityForResult(myIntent, 0);
                }
                else if (position == 2){
                    Intent myIntent = new Intent(getApplicationContext(),TestScroolView.class);
                    Bundle b = new Bundle();
                    //.putExtra("userID",userID);
                    b.putString("userID", "3");
                    b.putString("tol","RS BOGOR MEDICA CENTRE");//Your id
                    //.putExtra("userID",userID);
                    myIntent.putExtras(b);
                    startActivityForResult(myIntent, 0);
                }
                else {
                    Intent myIntent = new Intent(getApplicationContext(),TestScroolView.class);
                    Bundle b = new Bundle();
                    //.putExtra("userID",userID);
                    b.putString("userID", "4");
                    b.putString("tol","RS MEDICA DERMAGA");//Your id
                    //.putExtra("userID",userID);
                    myIntent.putExtras(b);
                    startActivityForResult(myIntent, 0);
                }

            }

        });

        // Set Ratingbar
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("hospital.json");
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
    private class GetContacts extends AsyncTask<Void, Void, Void> {
        private Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(HospitalList.this);
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
                JSONArray contacts = obj.getJSONArray("hospital");

                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);


                    String name = c.getString("name");
                    String id = c.getString("id");
                    String image = c.getString("imgUrl1");
                    Drawable image1 = LoadImageFromWebOperations(image);
                    String alamat = c.getString("Alamat");




//                        da =new ArrayList<>();
//                        da.add( name );
//                        da.add( code );


                    // Phone node is JSON Object
//                    JSONObject phone = c.getJSONObject("phone");
//                    String mobile = phone.getString("mobile");
//                    String home = phone.getString("home");
//                    String office = phone.getString("office");

                    // tmp hash map for single contact
                    HashMap<String, String> contact = new HashMap<>();
                    mHospitalList.add(new Hospital(Integer.parseInt(id), image1, name, alamat));


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
            hAdapter = new HospitalListAdapter(getApplicationContext(), mHospitalList);
//            hAdapter.getFilter().filter(searchView);

            lvHospital.setAdapter(hAdapter);
        }
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
