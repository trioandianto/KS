package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;

import com.kliksembuh.ks.library.HospitalListAdapter;
import com.kliksembuh.ks.library.HttpHandler;
import com.kliksembuh.ks.models.Hospital;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class HospitalList extends AppCompatActivity {

    private String JSON_STRING;
    private ListView lvHospital;
    private HospitalListAdapter hAdapter;
    private List<Hospital> mHospitalList;
    private Button btnpeta;
    private ProgressDialog pDialog;
    private String [] rumahSakitID;
    private String [] nameRumahSakit;
    private String subDistrict;
    private String spesialisasi;
    private Drawable drawableHospital[];

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
            subDistrict = b.getString("subDistrict");
            spesialisasi = b.getString("spesialisasi");
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
        //new GetContacts().execute();
        new HospitalListAsync(subDistrict,spesialisasi).execute();
        // Add sample data
        // We can get data by DB, or web service
        //mHospitalList.add(new Hospital(3, R.drawable.rs_pmi_bogor , "RS PMI Bogor", "Kota Bogor, Jawa Barat 16129"));
//        mHospitalList.add(new Hospital(4, R.drawable.rs_pmi_bogor, "RSUD Cibinong Bogor", "Kota Bogor, Jawa Barat 16914"));
//        mHospitalList.add(new Hospital(5, R.drawable.rs_pmi_bogor, "RS Medika Darmaga", "Kota Bogor, Jawa Barat 16680"));
//        mHospitalList.add(new Hospital(3, R.drawable.rs_pmi_bogor, "RS Bogor Medical Centre", "Kota Bogor, Jawa Barat 16143"));

        lvHospital.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent myIntent = new Intent(getApplicationContext(),TestScroolView.class);
                Bundle b = new Bundle();
                b.putString("institution", rumahSakitID[position]);
                b.putString("tittle",nameRumahSakit[position]);//Your id
                //.putExtra("userID",userID);
                myIntent.putExtras(b);
                //.putExtra("userID",userID);
                startActivityForResult(myIntent, 0);


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
                rumahSakitID = new String[contacts.length()];
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);


                    String name = c.getString("name");
                    nameRumahSakit[i]=name;
                    String id = c.getString("id");
                    rumahSakitID[i]=id;
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
    public class HospitalListAsync extends AsyncTask<String, Void, String> {
        private String mSubdistrict;
        private String mSpesialisai;
        HospitalListAsync(String subDistrict, String spesialisasi) {
            mSubdistrict = subDistrict;
            mSpesialisai = spesialisasi;
        }
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
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.basajans.com:8868/BS.HealthCare.Application/api/Institutions/SearchInstitutionFromAfterLogin?subDistrict="+mSubdistrict+"&facility="+mSpesialisai);
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setRequestProperty("Content-Type", "application/json");
                    urlc.connect();
                    int responseCode=urlc.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        BufferedReader in=new BufferedReader(
                                new InputStreamReader(
                                        urlc.getInputStream()));
                        StringBuffer sb = new StringBuffer("");
                        String line="";
                        while((line = in.readLine()) != null) {
                            sb.append(line);
                            break;
                        }
                        in.close();



                        JSONArray jsonArray = new JSONArray(sb.toString());
                        drawableHospital = new Drawable[jsonArray.length()];
                        String image;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            image = jsonObject.getString("ImgUrl");
                            drawableHospital[i] = LoadImageFromWebOperations(image);

                        }
                        // Drawable image1 = LoadImageFromWebOperations(image);


                        return sb.toString();
                    }
                    else {
                        return "";

                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    return "";
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "";
                }catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }else {
                return "";
            }
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while(itr.hasNext()){

                String key= itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
        @Override
        protected void onPostExecute(final String success) {
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (success!="") {
                try {
                    JSONArray jsonArray = new JSONArray(success);
                    rumahSakitID =new String[jsonArray.length()];
                    nameRumahSakit = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("InstitutionID");
                        String name = jsonObject.getString("InstitutionName");

                        rumahSakitID[i] = id;
                        nameRumahSakit[i]=name;
                        // Drawable image1 = LoadImageFromWebOperations(image);
                        String alamat = jsonObject.getString("InstitutionAddress");
                        if(drawableHospital.length <= 0){
                            mHospitalList.add(new Hospital(Integer.parseInt(id), null, name, alamat));
                        }else{
                            mHospitalList.add(new Hospital(Integer.parseInt(id), drawableHospital[i], name, alamat));
                        }

                        hAdapter = new HospitalListAdapter(getApplicationContext(), mHospitalList);

                        lvHospital.setAdapter(hAdapter);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                //:TODO
            }
        }
        @Override
        protected void onCancelled() {

        }
    }
}
