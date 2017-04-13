package com.kliksembuh.ks;

import android.content.Context;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
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
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    private String userID;
    private ListView lvUpcoming;
    private HistoryAdapter historyAdapter;
    private List<HistoryUpComing> historyUpComingList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_my_appointment_upcoming, container, false);

//        Button btnkonfirmasi = (Button) rootView.findViewById(R.id.btnkonfirmasi);
//        btnkonfirmasi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myIntent = new Intent(view.getContext(), AppointmentDetailActivity.class);
//                startActivityForResult(myIntent, 0);
//            }
//        });
        historyUpComingList = new ArrayList<>();
        lvUpcoming = (ListView)rootView.findViewById(R.id.lvHistoryUpComing);
        new HistoryAppoinment(userID).execute();

        return rootView;
    }
    public class HistoryAppoinment extends AsyncTask<String, Void, String> {
        private String mUserID;
        HistoryAppoinment(String userID) {
            mUserID = userID;
        }
        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://basajans/kliksembuhapi/api/DailyScheduleAttendees/GetAttendeesAppointment?userID=0b3fc6fe-8736-4900-906a-476cb4db1e90");
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

}
