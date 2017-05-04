package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kliksembuh.ks.library.HospitalListAdapter;
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
    private String subDistricDescription;
    private String facilityName;
    private String spesialisasi;
    private Drawable drawableHospital[];
    private String userID;
    private String alamat[];
    private String finalListHospital;

    RatingBar rb;

//    protected void onPreExecute(){
//        json_url = "http://192.168.1.18/userapi/api/institutions/getinstitutions";
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hospital Screen Logic (created by Ucu on 24012017)
        setContentView(R.layout.activity_hospital_list);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
            subDistrict = b.getString("subDistrict");
            subDistricDescription = b.getString("SubDistrictDescription");
            spesialisasi = b.getString("facilityID");
            facilityName = b.getString("facilityName");
        }

        Toolbar newToolbar = (Toolbar)findViewById(R.id.toolbarHospitalList);
        setSupportActionBar(newToolbar);
        newToolbar.setTitle(subDistricDescription);
        setSupportActionBar(newToolbar);
        getWindow().setStatusBarColor(ContextCompat.getColor(HospitalList.this, R.color.colorPrimaryDark));

        lvHospital = (ListView)findViewById(R.id.listview_hospital);
        btnpeta = (Button)findViewById(R.id.btnpeta);
        btnpeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(HospitalList.this,ListMapActivity.class);
                Bundle b = new Bundle();
                b.putString("userID", userID);
                b.putString("subDistrict",subDistrict);
                b.putString("facilityID",spesialisasi);
                b.putString("facilityName",facilityName);
                b.putString("SubDistrictDescription",subDistricDescription);
                myIntent.putExtras(b);
                startActivityForResult(myIntent, 1);
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

                Object object = parent.getAdapter().getItem(position);
                Hospital hospital = (Hospital) object;
                String idHospital = hospital.getId();
                String nameHospital = hospital.getName();
                String alamat = hospital.getAddress();

                Intent myIntent = new Intent(getApplicationContext(),TestScroolView.class);
                Bundle b = new Bundle();
                b.putString("institution", idHospital);
                b.putString("tittle", nameHospital);//Your id
                b.putString("facilityID", spesialisasi);
                b.putString("userID",userID);
                b.putString("alamat", alamat);
                //.putExtra("userID",userID);
                myIntent.putExtras(b);
                //.putExtra("userID",userID);
                startActivityForResult(myIntent, 1);
            }
        });
        // Set Ratingbar
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                userID = data.getStringExtra("userID");
                subDistrict = data.getStringExtra("subDistrict");
                subDistricDescription = data.getStringExtra("SubDistrictDescription");
                spesialisasi = data.getStringExtra("facilityID");
                facilityName = data.getStringExtra("facilityName");
            }
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
                try {
                    URL url = new URL("http://cloud.abyor.com:11080/kliksembuhapi/api/Institutions/SearchInstitutionFromAfterLogin?subDistrict=" + mSubdistrict + "&facility=" + mSpesialisai);
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setRequestProperty("Content-Type", "application/json");
                    urlc.connect();
                    int responseCode = urlc.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                        urlc.getInputStream()));
                        StringBuffer sb = new StringBuffer("");
                        String line = "";
                        while ((line = in.readLine()) != null) {
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
                    } else {
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
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }
            else {
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
                    alamat = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("InstitutionID");
                        String name = jsonObject.getString("InstitutionName");
                        nameRumahSakit[i] = name;
                        String image = jsonObject.getString("ImgUrl");

                        rumahSakitID[i] = id;
                        // Drawable image1 = LoadImageFromWebOperations(image);
                        String addres = jsonObject.getString("InstitutionAddress");
                        alamat[i] = addres;

                        String phNumber = jsonObject.getString("InstitutionPhoneNbr");
                        //phoneNbr[i] = phNumber;
                        String cpblDesc = "";
                        JSONArray jsonArray2 = jsonObject.getJSONArray("Capabilities");
                        for (int j = 0 ; j < jsonArray2.length() ; j++){
                            JSONObject objectInner = jsonArray2.getJSONObject(j);
                            cpblDesc = objectInner.optString("CapabilitiesID");
                        }
                        if(drawableHospital.length <= 0 && cpblDesc == ""){
                            mHospitalList.add(new Hospital(id, null, name, addres, phNumber, null));
                        }
                        else
                        {
                            mHospitalList.add(new Hospital(id, drawableHospital[i], name, addres, phNumber, cpblDesc));
                        }



                        //String cpblDesc = jsonObject.getString("Capabilities");
                        // String getCapability= jsonObject.getJSONObject("Capabilities").getString("CapabilitiesID");
//                        if(jsonObject.has("Capabilities")){
//                            if(!jsonObject.isNull("Capabilities")){
//                                JSONObject objCapabilites = jsonObject.getJSONObject("Capabilities");
//                                JSONArray jsonArray2 = objCapabilites.getJSONArray("Capabilities");
//                                for (int j = 0 ; j < jsonArray2.length() ; j++){
//                                    JSONObject objectInner = jsonArray.getJSONObject(j);
//                                    String cpblDesc = objectInner.optString("CapabilitiesID");
//                                }
//                            }
//                        }
                        Drawable photo = LoadImageFromWebOperations(image);

                        finalListHospital = String.valueOf(mHospitalList.size());
                        TextView newTextView = (TextView)findViewById(R.id.tvhospitalList);
                        newTextView.setText("Menampilkan "+finalListHospital+" instansi kesehatan di "+subDistricDescription+" yang menyediakan Dokter "+facilityName+".");

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
