package com.kliksembuh.ks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kliksembuh.ks.models.VitalSign;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
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

public class SettingVitalSignDetailActivity extends AppCompatActivity {
    private EditText etTekananDarahs;
    private EditText etTekananDarahD;
    private EditText etSuhuTubuh;
    private EditText etDenyutNadi;
    private EditText etPernafasan;
    private Button btnsimpanvitalsign;
    private String userID;
    private int vitalSignID;
    private PostVitalSignTask postAuthTask = null;
    private DeleteVitalSignTask updateAuthTask = null;
    private ListView lvHistory;
    private HistoryAdapter historyAdapter;
    private List<VitalSign> lVitalSign;
    private TextView tvLastUpdateVtl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_vital_sign_detail);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            userID = b.getString("userID");
        }

        Toolbar newToolbar = (Toolbar)findViewById(R.id.toolbarvitalsigndetail);
        setSupportActionBar(newToolbar);
        newToolbar.setTitle("Pengaturan Vital Sign");
        setSupportActionBar(newToolbar);
        getWindow().setStatusBarColor(ContextCompat.getColor(SettingVitalSignDetailActivity.this, R.color.colorPrimaryDark));
        lVitalSign = new ArrayList<>();

        etTekananDarahs = (EditText)findViewById(R.id.tv_ValueOfTekDarah);
        etTekananDarahD = (EditText)findViewById(R.id.tv_ValueOfTekDarah2);
        etSuhuTubuh = (EditText)findViewById(R.id.tv_ValueOfSuhu);
        etDenyutNadi = (EditText)findViewById(R.id.tv_ValueOfDenyutNadi);
        etPernafasan = (EditText)findViewById(R.id.tv_ValueOfPernafasan);
        btnsimpanvitalsign = (Button)findViewById(R.id.btnsimpanvitalsign);
        tvLastUpdateVtl = (TextView)findViewById(R.id.tvLastUpdateVtl);
        lvHistory =(ListView)findViewById(R.id.lvHistoryVitalSign);
        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = parent.getAdapter().getItem(position);
                VitalSign vitalSign = (VitalSign) object;
                String tekananDarahS = vitalSign.getTekananDarahS();
                String tekananDarahD = vitalSign.getTekananDarahD();
                String suhuTubuh = vitalSign.getSuhuTubuh();
                String denyutNadi = vitalSign.getDenyutNadi();
                String pernafasan = vitalSign.getPernafasan();
                etTekananDarahs.setText(tekananDarahS);
                etTekananDarahD.setText(tekananDarahD);
                etSuhuTubuh.setText(suhuTubuh);
                etDenyutNadi.setText(denyutNadi);
                etPernafasan.setText(pernafasan);
            }
        });
        btnsimpanvitalsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptVitalSign();
            }
        });
        new GetVitalSignTask(userID).execute();
    }
    public void attemptVitalSign(){
        if (postAuthTask != null) {
            return;
        }
        etTekananDarahs.setError(null);
        etTekananDarahD.setError(null);
        etSuhuTubuh.setError(null);
        etDenyutNadi.setError(null);
        etPernafasan.setError(null);
        String tekananDarahD = etTekananDarahD.getText().toString();
        String tekananDarahS = etTekananDarahs.getText().toString();
        String suhuTubuh = etSuhuTubuh.getText().toString();
        String denyutNadi = etDenyutNadi.getText().toString();
        String pernafasan = etPernafasan.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(tekananDarahD)){
            etTekananDarahD.setError(getString(R.string.not_null));
            focusView = etTekananDarahD;
            cancel = true;

        }
        else if(TextUtils.isEmpty(tekananDarahS)){
            etTekananDarahs.setError(getString(R.string.not_null));
            focusView = etTekananDarahs;
            cancel = true;

        }
        else if(TextUtils.isEmpty(suhuTubuh)){
            etSuhuTubuh.setError(getString(R.string.not_null));
            focusView = etSuhuTubuh;
            cancel = true;

        }
        else if(TextUtils.isEmpty(denyutNadi)){
            etDenyutNadi.setError(getString(R.string.not_null));
            focusView = etDenyutNadi;
            cancel = true;

        }
        else if(TextUtils.isEmpty(pernafasan)){
            etPernafasan.setError(getString(R.string.not_null));
            focusView = etPernafasan;
            cancel = true;

        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else {
            postAuthTask = new PostVitalSignTask(tekananDarahD, tekananDarahS, suhuTubuh, denyutNadi, pernafasan,userID);
            postAuthTask.execute((String)null);
        }

    }
    private class GetVitalSignTask extends AsyncTask<String, Void, String> {

        private String uUserID;

        public GetVitalSignTask(String uUserID){
            this.uUserID = uUserID;
        }
        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/KlikSembuhAPI/api/VitalSigns/GetVitalSignByUserID/"+uUserID);
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

                        return sb.toString();

                    }
                    else {
//                        Toast.makeText(getApplicationContext(), "Gagal membuat janji.", Toast.LENGTH_LONG).show();
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
            }else {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s!=""){
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    lVitalSign = new ArrayList<>();
                    if (jsonArray.length()==0) {
                    }
                    else {
                        int a = 0;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String tekananDarahS = jsonObject.getString("BloodPressureSiastolic");
                            String tekananDarahD = jsonObject.getString("BloodPressureDiastolic");
                            String suhuTubuh = jsonObject.getString("BodyTemperature");
                            String denyutNadi = jsonObject.getString("Pulse");
                            String pernafasan = jsonObject.getString("Respiration");
                            int id = jsonObject.getInt("VitalSignID");
                            String date = jsonObject.getString("CreatedDateTime");
                            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            SimpleDateFormat timeFormatter = new SimpleDateFormat("dd-MM-yyyy");
                            SimpleDateFormat time = new SimpleDateFormat("HH:mm");
                            String detailDate="";
                            Date date1 = dateFormatter.parse(date);
                            String dateFormat = timeFormatter.format(date1);
                            String timeFormat = time.format(date1);
                            detailDate = dateFormat +"/ "+timeFormat;

                            if(a>0){

                            }
                            else {
                                tvLastUpdateVtl.setText("Last Update : "+detailDate);
                                etTekananDarahs.setText(tekananDarahS);
                                etTekananDarahD.setText(tekananDarahD);
                                etSuhuTubuh.setText(suhuTubuh);
                                etDenyutNadi.setText(denyutNadi);
                                etPernafasan.setText(pernafasan);
                                a=1;
                            }
                            lVitalSign.add(new VitalSign(id,tekananDarahS,tekananDarahD,denyutNadi,pernafasan,suhuTubuh, detailDate));
                        }
                        historyAdapter = new HistoryAdapter(getApplicationContext(), lVitalSign);
                        lvHistory.setAdapter(historyAdapter);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class DeleteVitalSignTask extends AsyncTask<String, Void, String>{
        int uVitalSignID;

        public DeleteVitalSignTask(int uVitalSignID){
            this.uVitalSignID = uVitalSignID;

        }
        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/KlikSembuhAPI/api/VitalSigns/DeleteVitalSign/"+uVitalSignID);
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    JSONObject jsonObject = new JSONObject();
                    urlc.setConnectTimeout(3000);
                    urlc.setRequestProperty("Content-Type","application/json");
                    urlc.setRequestMethod("DELETE");
                    urlc.setDoInput(true);
                    urlc.setDoOutput(true);
                    DataOutputStream os = new DataOutputStream(urlc.getOutputStream());
                    os.writeBytes(jsonObject.toString());
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
                        os.flush();
                        os.close();

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
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }else {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != "") {
                Toast.makeText(getApplicationContext(), "Data Berhasil di Hapus.", Toast.LENGTH_LONG).show();
                new GetVitalSignTask(userID).execute();

            }
            else{
                postAuthTask = null;
            }
        }
    }
    private class PostVitalSignTask extends AsyncTask<String, Void, String>{
        String tekananDarahD ;
        String tekananDarahS ;
        String suhuTubuh ;
        String denyutNadi;
        String pernafasan;
        String uUserID;

        public PostVitalSignTask(String tekananDarahD, String tekananDarahS, String suhuTubuh, String denyutNadi, String pernafasan, String  uUserID){
            this.tekananDarahD = tekananDarahD;
            this.tekananDarahS = tekananDarahS;
            this.suhuTubuh = suhuTubuh;
            this.denyutNadi = denyutNadi;
            this.pernafasan = pernafasan;
            this.uUserID = uUserID;

        }
        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/KlikSembuhAPI/api/VitalSigns/PostVitalSign");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("UserID", uUserID);
                    jsonObject.put("BloodPressureSiastolic", tekananDarahS);
                    jsonObject.put("BloodPressureDiastolic", tekananDarahD);
                    jsonObject.put("BodyTemperature", suhuTubuh);
                    jsonObject.put("Pulse", denyutNadi);
                    jsonObject.put("Respiration", pernafasan);
                    urlc.setConnectTimeout(3000);
                    urlc.setRequestProperty("Content-Type","application/json");
                    urlc.setRequestMethod("POST");
                    urlc.setDoInput(true);
                    urlc.setDoOutput(true);
                    DataOutputStream os = new DataOutputStream(urlc.getOutputStream());

                    os.writeBytes(jsonObject.toString());


                    int responseCode=urlc.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_CREATED) {

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
                        os.flush();
                        os.close();

                        return sb.toString();

                    }
                    else {
//                        Toast.makeText(getApplicationContext(), "Gagal membuat janji.", Toast.LENGTH_LONG).show();
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
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "";
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }else {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != "") {
                Toast.makeText(getApplicationContext(), "Data Berhasil di Simpan.", Toast.LENGTH_LONG).show();
                new GetVitalSignTask(userID).execute();

            }
            else{
                postAuthTask = null;
            }
        }
    }
    public class HistoryAdapter extends BaseAdapter{
        private Context mContext;
        private List<VitalSign> mVitalSign;
        public HistoryAdapter (Context context, List<VitalSign> vitalSigns){
            this.mContext = context;
            this.mVitalSign = vitalSigns;
        }

        @Override
        public int getCount() {
            return mVitalSign.size();
        }

        @Override
        public Object getItem(int position) {
            return mVitalSign.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View newView = View.inflate(mContext, R.layout.list_history_vs, null);
            TextView tvName = (TextView)newView.findViewById(R.id.tvDetail);
            TextView btnDelete = (TextView) newView.findViewById(R.id.tvDelete);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DeleteVitalSignTask(mVitalSign.get(position).getId()).execute();
                }
            });

            tvName.setText(mVitalSign.get(position).getDate());
            newView.setTag(mVitalSign.get(position).getId());

            return newView;
        }
    }
}
