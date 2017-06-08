package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MyAppointmentConfirmedActivity extends Fragment implements ListView.OnItemClickListener{

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
    private TextView tvStatusConfirmed;
    private String userID;
    private String transaksiID;
    public String getUserID() {
        return userID;
    }
    private ImageView imgPicNotFound;
    private TextView tvHosNotFound;
    private RelativeLayout rvImageBooking;

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.activity_my_appointment_confirmed, container, false);
        imgPicNotFound = (ImageView) rootView.findViewById(R.id.ivPicNotFoundsHConfirm);
        tvHosNotFound = (TextView)rootView.findViewById(R.id.tvScheduleNotFoundsHConfirm);
        rvImageBooking = (RelativeLayout)rootView.findViewById(R.id.rvImageHistoryConfirm);

        globalContext = this.getActivity();

        lvUpcoming = (ListView)rootView.findViewById(R.id.lvHistoryConfirmed);
        lvUpcoming.setOnItemClickListener(this);
        historyUpComingList = new ArrayList<>();
        new HistoryConfirmedAsync(userID).execute();
        return rootView;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                userID = data.getStringExtra("userID");
                transaksiID = data.getStringExtra("transaksiID");
                new HistoryConfirmedAsync(userID).execute();

            }
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object object = parent.getAdapter().getItem(position);
        HistoryUpComing historyUpComing = (HistoryUpComing) object;
        transaksiID = String.valueOf(historyUpComing.getIdHistoryUpComing());
        String status =historyUpComing.getStatusHistory();

        Intent myIntent = new Intent(getApplicationContext(),AppointmentDetailActivity.class);
        Bundle b = new Bundle();
        b.putString("userID",userID);
        b.putString("transaksiID", transaksiID);
        b.putString("status", status);
        //.putExtra("userID",userID);
        myIntent.putExtras(b);
        //.putExtra("userID",userID);
        startActivityForResult(myIntent, 1);
    }

    public class HistoryConfirmedAsync extends AsyncTask<String, Void, String> {
         private String mUserID;
        HistoryConfirmedAsync(String userID) {
            mUserID = userID;
        }
        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/KlikSembuhAPI/api/Transactions/GetHistoryAppointment?UserID="+userID);
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
        @Override
        protected void onPostExecute(final String success) {
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
                        String appointmentDate = jsonObject.getString("ScheduleDate");
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

                            // Appointment Schedule Parser
                            SimpleDateFormat appointDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date appointDate = appointDateFormatter.parse(appointmentDate);
                            SimpleDateFormat newAppointFormat = new SimpleDateFormat("dd-MMM-yyyy");
                            String appointDatePars = newAppointFormat.format(appointDate);

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
                                historyUpComingList.add(new HistoryUpComing(Integer.parseInt(trxIDHistory), doctorName, null, hospitalName, schdlDatePars, trxNoAppointment, specialtyDoc, appointDatePars, statusDesc, shiftDesc, timeStartPars, timeEndPars));
                            }
                            else{
                                if(statusID == String.valueOf(2)){
                                    historyUpComingList.add(new HistoryUpComing(Integer.parseInt(trxIDHistory), doctorName, drawableDoctor[i], hospitalName, schdlDatePars, trxNoAppointment, specialtyDoc, appointDatePars, statusDesc, shiftDesc, timeStartPars, timeEndPars));

                                }

                            }

                        }catch (ParseException e){
                            e.printStackTrace();
                        }
                        if(historyUpComingList.size()>0){
                            histAdapter = new HistoryAdapter(globalContext.getApplicationContext(), historyUpComingList);
                            lvUpcoming.setAdapter(histAdapter);

                            rvImageBooking.setVisibility(View.INVISIBLE);
                            imgPicNotFound.setImageDrawable(null);
                            tvHosNotFound.setText(null);
                        }else{
                            rvImageBooking.setVisibility(View.VISIBLE);
                            imgPicNotFound.setImageResource(R.drawable.pic_notfound);
                            tvHosNotFound.setText("Tidak ada History.");
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                //:TODO
                rvImageBooking.setVisibility(View.VISIBLE);
                imgPicNotFound.setImageResource(R.drawable.pic_notfound);
                tvHosNotFound.setText("Tidak ada History.");
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
