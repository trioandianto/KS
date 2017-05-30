package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.kliksembuh.ks.library.ReminderListAdapter;
import com.kliksembuh.ks.models.Reminder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ReminderListActivity extends AppCompatActivity {

    private ListView lvReminder;
    private ReminderListAdapter rAdapter;
    private List<Reminder> mReminderList;
    private ProgressDialog pDialog;
    private Button btnAddReminder;
    private String userID;
    private String rmdrID;
    private String rmdrTitle;
    private String rmdrType;
    private String rmdrDosage;
    private String rdmrDays;
    private String rmdrTimings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
        }

        lvReminder = (ListView) findViewById(R.id.listview_reminder);
        btnAddReminder = (Button) findViewById(R.id.btn_addReminder);
        btnAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addRmdrIntent = new Intent(ReminderListActivity.this, SettingReminderDetailActivity.class);
                Bundle b = new Bundle();
                b.putString("userID", userID);
                addRmdrIntent.putExtras(b);
                startActivityForResult(addRmdrIntent, 1);

            }
        });
        mReminderList = new ArrayList<>();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK){
                userID = data.getStringExtra("userID");
            }
        }
    }
    public Drawable LoadImageFromWebOperations (String url){
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable draw = Drawable.createFromStream(is, "src name");
            return draw;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public class ReminderListAsync extends AsyncTask<String, Void, String> {

        public ReminderListAsync() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ReminderListActivity.this);
            pDialog.setProgress(R.drawable.pic_loading);
            pDialog.setMessage("Mohon Menunggu...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if ( netInfo != null && netInfo.isConnected()) {
                try
                {
                    URL url = new URL("http://cloud.abyor.com:11080/kliksembuhapi/api/Institutions/SearchInstitutionFromAfterLogin?subDistrict=");
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
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
