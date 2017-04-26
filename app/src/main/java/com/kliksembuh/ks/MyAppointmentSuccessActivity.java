package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MyAppointmentSuccessActivity extends Fragment {
    private String [] idHistoryUpComing;
    private String [] namaDokter;
    private String [] namaRumahSakit;
    private String [] noAppointment;
    private ListView lvUpcoming;
    private HistoryAdapter histAdapter;
    private List<HistoryUpComing> historyUpComingList;
    private Context globalContext = null;
    private int imageId;
    private Drawable drawableDoctor[];
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.activity_my_appointment_success, container, false);
        globalContext = this.getActivity();

        lvUpcoming = (ListView)rootView.findViewById(R.id.lvHistorySuccess);
        historyUpComingList = new ArrayList<>();
        new HistoryAppoinmentSuccessAsync().execute();
        return rootView;

    };
    public class HistoryAppoinmentSuccessAsync extends AsyncTask<String, Void, String> {
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
                    URL url = new URL("http://192.168.1.6/KlikSembuhAPI/api/Transactions/GetHistoryAppointment?UserID=6fede7ca-1fa5-4934-94c7-8c95f3d78233");
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
                            imageDoc = jsonObject.getString("MedicalPersonnelImageUrl");
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

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String trxIDHistory = jsonObject.getString("TransactionID");
                        String doctorName = jsonObject.getString("MedicalPersonName");
                        String hospitalName =  jsonObject.getString("InstitutionName");
                        String createdOn = jsonObject.getString("ScheduleDate");
                        String trxNoAppointment = jsonObject.getString("TransactionID");
                        String specialtyDoc = jsonObject.getString("FacilityDesc");
                        String statusID = jsonObject.getString("Status");
                        String statusDesc = jsonObject.getString("StatusDesc");
                        String shiftDesc = jsonObject.getString("ShiftDesc");
                        String timeStart = jsonObject.getString("TimeStart");
                        String timeEnd = jsonObject.getString("TimeEnd");

                        try {
                            //yyyy-MM-dd'T'HH:mm:ss
                            SimpleDateFormat schdlDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date scheduleDate = schdlDateFormatter.parse(createdOn);
                            SimpleDateFormat newSchdlFormat = new SimpleDateFormat("dd-MMM-yyyy");
                            String schdlDatePars = newSchdlFormat.format(scheduleDate);

                            // Get date from string
                            SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
                            Date dateTimeStart = dateFormatter.parse(timeStart);
                            Date dateTimeEnd = dateFormatter.parse(timeEnd);

                            // Get time from date
                            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
                            String timeStartPars = timeFormatter.format(dateTimeStart);
                            String timeEndPars = timeFormatter.format(dateTimeEnd);

                            idHistoryUpComing[i] = trxIDHistory;
                            namaDokter[i] = doctorName;
                            noAppointment [i] = trxNoAppointment;
                            namaRumahSakit [i] = hospitalName;

                            if (drawableDoctor.length <= 0){
                                historyUpComingList.add(new HistoryUpComing(Integer.parseInt(trxIDHistory), doctorName, null, hospitalName, schdlDatePars, trxNoAppointment, specialtyDoc, statusDesc, shiftDesc, timeStartPars, timeEndPars));
                            }
                            else{
                                if(statusID == String.valueOf(4)){
                                    historyUpComingList.add(new HistoryUpComing(Integer.parseInt(trxIDHistory), doctorName, drawableDoctor[i], hospitalName, schdlDatePars, trxNoAppointment, specialtyDoc, statusDesc, shiftDesc, timeStartPars, timeEndPars));
                                }
                                histAdapter = new HistoryAdapter(globalContext.getApplicationContext(), historyUpComingList);
                                lvUpcoming.setAdapter(histAdapter);
                            }


                        }catch (ParseException e){
                            e.printStackTrace();
                        }

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

        public String getTime(String Vertrektijd){
            final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date dateObj;
            String newDateStr = null;
            try
            {
                dateObj = df.parse(Vertrektijd);
                SimpleDateFormat fd = new SimpleDateFormat("HH:mm");
                newDateStr = fd.format(dateObj);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return newDateStr;
        }
    }
}
