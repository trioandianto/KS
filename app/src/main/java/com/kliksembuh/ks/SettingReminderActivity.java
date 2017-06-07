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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.kliksembuh.ks.library.ReminderListAdapter;
import com.kliksembuh.ks.models.Reminder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class SettingReminderActivity extends Fragment implements View.OnClickListener, ListView.OnItemClickListener{
    private ListView lvReminder;
    private ReminderListAdapter rAdapter;
    private List<Reminder> mReminderList;
    private ProgressDialog pDialog;
    private Button btnAddReminder;
    private Context globalContext = null;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    private String userID;

    private Drawable image = getResources().getDrawable(R.drawable.paracetamol);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View newView = inflater.inflate(R.layout.activity_reminder_list, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        globalContext = this.getActivity();
        lvReminder = (ListView) newView.findViewById(R.id.listview_reminder);
        lvReminder.setOnItemClickListener(this);
        btnAddReminder = (Button) newView.findViewById(R.id.btn_addReminder);
        btnAddReminder.setOnClickListener(this);
        mReminderList = new ArrayList<>();
        new ReminderListAsync(userID).execute();

        return newView;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i==R.id.btn_addReminder){
            Intent addRmdrIntent = new Intent(getActivity(), SettingReminderDetailActivity.class);
            Bundle b = new Bundle();
            b.putString("userID", userID);
            addRmdrIntent.putExtras(b);
            startActivityForResult(addRmdrIntent, 1);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object object = parent.getAdapter().getItem(position);
        Reminder reminder = (Reminder) object;

        int rmdrID = reminder.getRmdr_id();
        String rmdrTitle = reminder.getRmdrTitle();
        String rmdrType = reminder.getRmdrType();
        String rmdrDosage = reminder.getRmdrDosage();
        String rdmrDays = reminder.getRmdrDays();
        String rmdrTimings = reminder.getRmdrTimings();
        String rmdDuration = reminder.getRmdrDuration();


        Intent myIntent = new Intent(getActivity(),SettingReminderDetailActivity.class);
        Bundle b = new Bundle();
        b.putString("userID",userID);
        myIntent.putExtras(b);
        startActivityForResult(myIntent, 1);

    }
    public class ReminderListAsync extends AsyncTask<String, Void, String> {
        private String rmdUserID;

        public ReminderListAsync(String rmdUserID) {
            this.rmdUserID = rmdUserID;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getContext());
            pDialog.setProgress(R.drawable.pic_loading);
            pDialog.setMessage("Mohon Menunggu...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if ( netInfo != null && netInfo.isConnected()) {
                try
                {
                    URL url = new URL("http://cloud.abyor.com:11080/KlikSembuhAPI/api/MedicationReminders/GetMedicationReminderById?userID="+rmdUserID);
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setRequestProperty("Content-Type", "application/json");
                    urlc.connect();
                    int responseCode = urlc.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK){
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                        urlc.getInputStream()));
                        StringBuffer sb = new StringBuffer("");
                        String line = "";
                        while ((line = in.readLine()) != null){
                            sb.append(line);
                            break;
                        }
                        in.close();
                        return sb.toString();
                    }
                    else {
                        return "";
                    }
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                    return "";
                } catch (IOException ie) {
                    ie.printStackTrace();
                    return "";
                }
                catch (Exception e){
                    e.printStackTrace();
                    return "";
                }
            }
            else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != ""){
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int rmdrID = jsonObject.getInt("MedicationReminderID");
                        String rmdrTitle = jsonObject.getString("Title");
                        String rmdrType = jsonObject.getString("ReminderTypeID");
                        String rmdrDosage = jsonObject.getString("Dosage");
                        String rdmrDays = jsonObject.getString("Days");
                        String rmdrTimings = jsonObject.getString("Timings");
                        String rmdDuration = jsonObject.getString("Duration");
                        mReminderList.add(new Reminder(rmdrID, rmdrTitle,rmdrType,rmdrDosage,rmdrTimings,rdmrDays,rmdDuration,image));
                    }
                    rAdapter = new ReminderListAdapter(globalContext.getApplicationContext(),mReminderList);
                    lvReminder.setAdapter(rAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
