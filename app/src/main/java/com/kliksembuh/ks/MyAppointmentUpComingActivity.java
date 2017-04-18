package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.kliksembuh.ks.library.HistoryAdapter;
import com.kliksembuh.ks.models.HistoryUpComing;

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

/**
 * Created by Trio Andianto on 1/24/2017.
 */
public class MyAppointmentUpComingActivity extends Fragment {

    private String userID;
    private String [] idHistoryUpComing;
    private String [] namaDokter;
    private Drawable imgHistoryDoc [];
    private String [] namaRumahSakit;
    private String tanggalDibuat;
    // To Do (Ucu)
    // Change data type string to int on noAppointment
    // Change string to date & time date on jadwalBerobat
    private String [] noAppointment;
    private String specialtyDoc;
    private String statusHistory;
    private String waktuBerobat;
    private String jamBerobat;

    private ListView lvUpcoming;
    private HistoryAdapter histAdapter;
    private List<HistoryUpComing> historyUpComingList;
    private Context globalContext = null;
    private int imageId;
    private Drawable drawableDoctor[];
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_my_appointment_upcoming, container, false);
        globalContext = this.getActivity();

//        int[] imgIdHosp = {R.drawable.dr_ilma_suraya, R.drawable.dr_indah_kusuma};

//        Button btnkonfirmasi = (Button) rootView.findViewById(R.id.btnkonfirmasi);
//        btnkonfirmasi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myIntent = new Intent(view.getContext(), AppointmentDetailActivity.class);
//                startActivityForResult(myIntent, 0);
//            }

////        });
        lvUpcoming = (ListView)rootView.findViewById(R.id.lvHistoryUpComing);
        historyUpComingList = new ArrayList<>();
        new HistoryAppoinmentAsync().execute();

//        imageId = imgIdHosp[1];
//        Drawable drawHistDoc = getResources().getDrawable(imageId);
//
//        historyUpComingList.add(new HistoryUpComing(23, "dr. Trio", drawHistDoc,"RS. Trimitra Bogor", "17-Apr-2017", "Bogor Utara, Jawa Barat","412321", "Dokter Umum", "Completed", "Pagi", "08.00-11.00"));
//        historyUpComingList.add(new HistoryUpComing(24, "dr. Indah", drawHistDoc,"RS. Medika Dramaga", "18-Apr-2017", "Bogor Utara, Jawa Barat","412321", "Dokter Umum", "Menunggu Konfirmasi", "Pagi", "08.00-11.00"));
//        histAdapter = new HistoryAdapter(globalContext.getApplicationContext(), historyUpComingList);
//        lvUpcoming.setAdapter((histAdapter));
//
//        //new HistoryAppoinment(userID).execute();
        return rootView;
    }
    public class HistoryAppoinmentAsync extends AsyncTask<String, Void, String> {
        // private String mUserID;
//        HistoryAppoinmentAsync(String userID) {
//            mUserID = userID;
//        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://basajans/KlikSembuhAPI/api/Transactions/GetHistoryAppointment?UserID=6fede7ca-1fa5-4934-94c7-8c95f3d78233");
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
                        drawableDoctor = new Drawable[jsonArray.length()];
                        String imageDoc;
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            imageDoc = jsonObject.getString("ImgUrl");
                            drawableDoctor[i] = LoadImageFromWebOperations(imageDoc);
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
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (success!="") {
                try {
                    JSONArray jsonArray = new JSONArray(success);

                    idHistoryUpComing = new String[jsonArray.length()];
                    namaDokter = new String[jsonArray.length()];
                    noAppointment = new String[jsonArray.length()];
                    namaRumahSakit = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++){
//                        historyUpComingList.add(new HistoryUpComing(23, "dr. Trio", drawHistDoc,"RS. Trimitra Bogor", "17-Apr-2017", "Bogor Utara, Jawa Barat","412321", "Dokter Umum", "Completed", "Pagi", "08.00-11.00"));
//                        historyUpComingList.add(new HistoryUpComing(24, "dr. Indah", drawHistDoc,"RS. Medika Dramaga", "18-Apr-2017", "Bogor Utara, Jawa Barat","412321", "Dokter Umum", "Menunggu Konfirmasi", "Pagi", "08.00-11.00"));
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String trxIDHistory = jsonObject.getString("TransactionID");
                        String doctorName = jsonObject.getString("MedicalPersonName");
                        String hospitalName =  jsonObject.getString("InstitutionName");
                        String createdOn = jsonObject.getString("ScheduleDate");
                        String trxNoAppointment = jsonObject.getString("TransactionID");
                        String specialtyDoc = jsonObject.getString("FacilityDesc");
                        String statusDesc = jsonObject.getString("StatusDesc");
                        String shiftDesc = jsonObject.getString("ShiftDesc");
                        String scheduleDate = jsonObject.getString("ScheduleDate");

                        idHistoryUpComing[i] = trxIDHistory;
                        namaDokter[i] = doctorName;
                        noAppointment [i] = trxNoAppointment;
                        namaRumahSakit [i] = hospitalName;

                        if (drawableDoctor.length <= 0){
                            historyUpComingList.add(new HistoryUpComing(Integer.parseInt(trxIDHistory), doctorName, null, hospitalName, createdOn, trxNoAppointment, specialtyDoc, statusDesc, shiftDesc, scheduleDate));
                        }
                        else{
                            historyUpComingList.add(new HistoryUpComing(Integer.parseInt(trxIDHistory), doctorName, drawableDoctor[i], hospitalName, createdOn, trxNoAppointment, specialtyDoc, statusDesc, shiftDesc, scheduleDate));
                        }
                        histAdapter = new HistoryAdapter(globalContext.getApplicationContext(), historyUpComingList);
                        lvUpcoming.setAdapter(histAdapter);

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

}
