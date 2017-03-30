package com.kliksembuh.ks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class BookingActivity extends AppCompatActivity {
    private TextView jadwal;
    private String mPersonalID;
    private int mYear;
    private String mWeek;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            mPersonalID = b.getString("userID");
        }

        jadwal =(TextView)findViewById(R.id.tvjadwal);
        mWeek = "1";

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);

        new JadwalDokterAsync(mPersonalID,mYear,mWeek).execute();

        jadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jadwal.setBackgroundColor(Color.LTGRAY);
                Intent myIntent = new Intent(view.getContext(), KonfirmasiJanjiActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });




        //  http://basajans/kliksembuhapi/api/Schedules/GetWeeklySchedule?personnelId=MD0001&year=2017&startweek=1


    }

//    private class GetContacts extends AsyncTask<Void, Void, Void> {
//        private Context context;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Showing progress dialog
//            pDialog = new ProgressDialog(HospitalList.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... args0) {
//            HttpHandler sh = new HttpHandler();
//
//            // Making a request to url and getting response
//            try{
//                JSONObject obj = new JSONObject(loadJSONFromAsset());
//
//                // Getting JSON Array node
//                JSONArray contacts = obj.getJSONArray("hospital");
//
//                // looping through All Contacts
//                for (int i = 0; i < contacts.length(); i++) {
//                    JSONObject c = contacts.getJSONObject(i);
//
//
//                    String name = c.getString("name");
//                    String id = c.getString("id");
//                    String image = c.getString("imgUrl1");
//                    Drawable image1 = LoadImageFromWebOperations(image);
//                    String alamat = c.getString("Alamat");
//
//
//
//
////                        da =new ArrayList<>();
////                        da.add( name );
////                        da.add( code );
//
//
//                    // Phone node is JSON Object
////                    JSONObject phone = c.getJSONObject("phone");
////                    String mobile = phone.getString("mobile");
////                    String home = phone.getString("home");
////                    String office = phone.getString("office");
//
//                    // tmp hash map for single contact
//                    HashMap<String, String> contact = new HashMap<>();
//                    jadwal.add(new Hospital(Integer.parseInt(id), image1, name, alamat));
//
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            if (pDialog.isShowing())
//                pDialog.dismiss();
//            hAdapter = new HospitalListAdapter(getApplicationContext(), mHospitalList);
////            hAdapter.getFilter().filter(searchView);
//
//            lvHospital.setAdapter(hAdapter);
//        }
//    }
    public class JadwalDokterAsync extends AsyncTask<String, Void, String> {
        private String mPersonilID;
        private int mYear;
        private String mWeek;
        JadwalDokterAsync(String personilID, int year, String week) {
            mPersonilID = personilID;
            mYear = year;
            mWeek=week;
        }


        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.basajans.com:8868/BS.HealthCare.Application/api/Schedules/GetWeeklySchedule?personnelId="+mPersonilID+"&year="+mYear+"&startweek="+mWeek);
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setRequestProperty("Content-Type","application/json");
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

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                        }

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

            if (success!="") {
            } else {
                //:TODO
            }
        }
        @Override
        protected void onCancelled() {

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
}
