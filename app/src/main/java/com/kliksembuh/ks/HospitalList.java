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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

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
    private HospitalListAdapterNew hAdapter;
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
    private Drawable load;

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
        load = getResources().getDrawable(R.drawable.pic_loading_small);
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
                //overridePendingTransition( R.anim.from_middle, R.anim.to_middle);
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
                b.putString("facilityName", facilityName);
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
            
            pDialog.setProgress(R.drawable.pic_loading);
            pDialog.setMessage("Mohon Menunggu...");
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

                    if (jsonArray.length()==0){
                        TextView newTextView = (TextView)findViewById(R.id.tvhospitalList);
                        newTextView.setText("Tidak tersedia rumah sakit untuk daerah "+subDistricDescription+".");
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("InstitutionID");
                        String name = jsonObject.getString("InstitutionName");
                        nameRumahSakit[i] = name;
                        String image = jsonObject.getString("ImgUrl");
                        Drawable photo = LoadImageFromWebOperations(image);
                        Drawable img1=null;
                        Drawable img2=null;
                        Drawable img3=null;
                        String selengkapnya="";
                        String igd = "";
                        String bpjs = "";
                        String fct1 = "";
                        String fct2 ="";
                        String fct3 ="";
                        String semua = "";

                        rumahSakitID[i] = id;
                        // Drawable image1 = LoadImageFromWebOperations(image);
                        String addres = jsonObject.getString("InstitutionAddress");
                        alamat[i] = addres;

                        String phNumber = jsonObject.getString("InstitutionPhoneNbr");
                        //phoneNbr[i] = phNumber;
                        int cpblDesc ;
                        JSONArray jsonArray2 = jsonObject.getJSONArray("Capabilities");
                        for (int j = 0 ; j < jsonArray2.length() ; j++){
                            JSONObject objectInner = jsonArray2.getJSONObject(j);
                            cpblDesc = objectInner.getInt("CapabilitiesID");
                            if (cpblDesc==1 && cpblDesc > 0){
                                igd = objectInner.getString("CapabilitiesDesc");
                            }
                            else if(cpblDesc==2 && cpblDesc>0){
                                bpjs = objectInner.getString("CapabilitiesDesc");
                            }
                        }
                        JSONArray jsonArray1 = jsonObject.getJSONArray("Insurances");
                        for (int k = 0; k<jsonArray1.length();k++){
                            try {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(k);
                                String img = jsonObject1.getString("ImageUrl");
                                if(k==0){
                                    img1 = LoadImageFromWebOperations(img);
                                }
                                else if (k==1){
                                    img2 = LoadImageFromWebOperations(img);

                                }else if(k==2){
                                    img3 = LoadImageFromWebOperations(img);

                                }
                            }catch (Exception e){

                            }
                        }
                        JSONArray jsonArray3 = jsonObject.getJSONArray("HealthFacilities");
                        for (int l = 0; l<jsonArray3.length();l++){
                            try {
                                JSONObject jsonObject1 = jsonArray3.getJSONObject(l);
                                String s = jsonObject1.getString("Name");
                                if(l==0){
                                    fct1 = "-"+s;
                                }
                                else if (l==1){
                                    fct2 = "-"+s;
                                }else if(l==2){
                                    fct3 = "-"+s;
                                }
                            }catch (Exception e){

                            }
                        }


                        int asd = jsonObject.getInt("totalHealthFacilityNbr");
                        String total = jsonObject.getString("TotalLikeNbr");

                            semua ="Lihat Semua Spesialisasi ("+asd+")";

                        int arr = jsonObject.getInt("totalInsuranceNbr");

                            selengkapnya = "Lihat Semua Asuransi ("+arr+").";


                        mHospitalList.add(new Hospital(id, photo, name, addres, phNumber, null, image, img1, img2, img3, selengkapnya, igd,bpjs, fct1, fct2, fct3, semua, total));

                    }
                    finalListHospital = String.valueOf(mHospitalList.size());
                    TextView newTextView = (TextView)findViewById(R.id.tvhospitalList);
                    newTextView.setText("Menampilkan "+finalListHospital+" pencarian untuk "+facilityName+" di "+subDistricDescription+".");
                    hAdapter = new HospitalListAdapterNew(getApplicationContext(), mHospitalList);
                    lvHospital.setAdapter(hAdapter);
//                    for(Hospital currentHospital : mHospitalList){
//                        new ImageDrawable(currentHospital).execute();
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                //:TODO
                ImageView imgPicNotFound = (ImageView) findViewById(R.id.ivPicNotFound);
                imgPicNotFound.setImageResource(R.drawable.pic_notfound);
                TextView tvHosNotFound = (TextView)findViewById(R.id.tvHospitalNotFound);
                tvHosNotFound.setText("Oops hasil pencarian Anda tidak dapat ditemukan. Silahkan melakukan pencarian kembali dengan kata kunci lain.");
                TextView newTextView = (TextView)findViewById(R.id.tvhospitalList);
                TextView btnNewSearch = (TextView) findViewById(R.id.btnNewSearch);
                btnNewSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent homeIntent = new Intent(HospitalList.this, HomeActivity.class);
                        startActivityForResult(homeIntent, 1);
                    }
                });
                btnNewSearch.setBackgroundColor(getResources().getColor(R.color.buttonBlue));
                btnNewSearch.setText("Ganti Pencarian");
                newTextView.setVisibility(View.GONE);
                //newTextView.setText("Tidak tersedia rumah sakit untuk daerah "+subDistricDescription+".");
            }
        }
        @Override
        protected void onCancelled() {

        }
    }
    public class ImageDrawable extends AsyncTask<String, Void, Drawable>{

        Hospital hospital;
        public ImageDrawable(Hospital hospital){
            this.hospital  = hospital;
        }
        @Override
        protected Drawable doInBackground(String... params) {
            //return null;
            Drawable imageDrawable = LoadImageFromWebOperations(hospital.getStringImg());
            this.hospital.setHospital_pic_id(imageDrawable);

            return imageDrawable;
        }

//        @Override
//        protected void onPostExecute(Drawable drawable) {
//            super.onPostExecute(drawable);
//
//        }
    }
    public class HospitalListAdapterNew<T> extends BaseAdapter implements Filterable {
        private Context mContext;
        private List<Hospital> mHospitalList;
        private ArrayList<T> mOriginalValues;
        private ArrayFilter mFilter;
        private final Object mLock = new Object();
        private List<T> mObjects;

        // Constructor
        public HospitalListAdapterNew(Context mContext, List<Hospital> mHospitalList) {
            this.mContext = mContext;
            this.mHospitalList = mHospitalList;
        }

        @Override
        public int getCount() {
            return mHospitalList.size();
        }

        @Override
        public Object getItem(int position) {
            return mHospitalList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View newView = View.inflate(mContext, R.layout.hospital_list, null);
            ImageView imgView = (ImageView)newView.findViewById(R.id.iv_hospital_pic);
            TextView tvName = (TextView)newView.findViewById(R.id.tv_name);
            TextView tvAddress = (TextView)newView.findViewById(R.id.tv_address);
            TextView tvPhoneNbr = (TextView) newView.findViewById(R.id.tv_phone);
            ImageView image1 = (ImageView)newView.findViewById(R.id.iv_bpjs_ic);
            ImageView image2 = (ImageView)newView.findViewById(R.id.iv_axa_ic);
            ImageView image3 = (ImageView)newView.findViewById(R.id.iv_zurich_ic);
            // Text view for capabilities (on Backend there is no IGD, but BPJS)
            TextView tvIGD = (TextView) newView.findViewById(R.id.tv_IGD);
            TextView tv24hour = (TextView) newView.findViewById(R.id.tv_24hours);
            TextView tvLihatSelengkapnya = (TextView)newView.findViewById(R.id.tv_moreinfo);
            TextView like = (TextView)newView.findViewById(R.id.tv_favourite);
            TextView fcl1 = (TextView)newView.findViewById(R.id.tv_doctorGigi);
            TextView fcl2 = (TextView)newView.findViewById(R.id.tv_doctorKandungan);
            TextView fcl3 = (TextView)newView.findViewById(R.id.tv_doctorTHT);
            TextView semua = (TextView)newView.findViewById(R.id.tv_moreinfo2);
            semua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(HospitalList.this, FaciltyInstitutionActivity.class);
                    Bundle b = new Bundle();
                    b.putString("userID", userID);
                    b.putString("subDistrict",subDistrict);
                    b.putString("facilityID",spesialisasi);
                    b.putString("facilityName",facilityName);
                    b.putString("SubDistrictDescription",subDistricDescription);
                    b.putString("Name",nameRumahSakit[position]);
                    myIntent.putExtras(b);
                    startActivityForResult(myIntent, 1);
                }
            });
            tvLihatSelengkapnya.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(HospitalList.this, InsuranceListActivity.class);
                    Bundle b = new Bundle();
                    b.putString("userID", userID);
                    b.putString("subDistrict",subDistrict);
                    b.putString("facilityID",spesialisasi);
                    b.putString("facilityName",facilityName);
                    b.putString("SubDistrictDescription",subDistricDescription);
                    b.putString("Name",nameRumahSakit[position]);
                    myIntent.putExtras(b);
                    startActivityForResult(myIntent, 1);
                }
            });


            imgView.setImageDrawable(mHospitalList.get(position).getHospital_pic_id());
            tvName.setText(mHospitalList.get(position).getName());
            tvAddress.setText(mHospitalList.get(position).getAddress());
            tvPhoneNbr.setText(mHospitalList.get(position).getPhoneNbr());
            image1.setImageDrawable(mHospitalList.get(position).getIv_image1());
            image2.setImageDrawable(mHospitalList.get(position).getIv_image2());
            image3.setImageDrawable(mHospitalList.get(position).getIv_image3());
            tvLihatSelengkapnya.setText(mHospitalList.get(position).getMoreInfoInsurance());
            tvIGD.setText(mHospitalList.get(position).getIgd());
            tv24hour.setText(mHospitalList.get(position).getBpjs());
            like.setText(mHospitalList.get(position).getLike());
            fcl1.setText(mHospitalList.get(position).getFclt1());
            fcl2.setText(mHospitalList.get(position).getFclt2());
            fcl3.setText(mHospitalList.get(position).getFclt3());
            semua.setText(mHospitalList.get(position).getSemua());
            // Save hospital id to tag
            newView.setTag(mHospitalList.get(position).getId());

            return newView;
        }
        @Override
        public Filter getFilter() {
            if (mFilter == null) {
                mFilter = new ArrayFilter();
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
}
