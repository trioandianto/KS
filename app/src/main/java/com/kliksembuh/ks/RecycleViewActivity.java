package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kliksembuh.ks.models.Doctor;
import com.squareup.picasso.Picasso;

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
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class RecycleViewActivity extends AppCompatActivity {
    private RecyclerView rvDokter;
    private RecycleAdapter dAdapter;
    private List<Doctor> listDoctor;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);

        rvDokter = (RecyclerView)findViewById(R.id.rvDoctor);
        listDoctor = new ArrayList<>();
        dAdapter = new RecycleAdapter(getApplicationContext(), listDoctor);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvDokter.setLayoutManager(mLayoutManager);
        rvDokter.setItemAnimator(new DefaultItemAnimator());
        rvDokter.setAdapter(dAdapter);
        new DokterListAsync().execute();
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
    public class DokterListAsync extends AsyncTask<String, Void, String> {

        DokterListAsync() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RecycleViewActivity.this);
            pDialog.setMessage("Mohon menunggu...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try{
                    URL url = new URL("http://cloud.abyor.com:11080/kliksembuhapi/api/MedicalPersonnel/SearchMedicalPersonnelBasedOnInstitutions?institution=1&facility=");
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
            List<String> list = new ArrayList<String>();
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (success!="") {

                try{
                    JSONArray jsonArray = new JSONArray(success);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("MedicalPersonnelID");
                        String personelCD = jsonObject.getString("MedicalPersonnelCD");
                        String image = jsonObject.getString("ImgUrl");
                        String tittle = "MSC, Sp. MK";
                        Drawable photo = LoadImageFromWebOperations(image);

                        String frontTitle = jsonObject.getString("FrontTitle");
                        String name = jsonObject.getString("Name");
                        // to do; change alamat to Doctor Specialty
                        String spesiality = jsonObject.getString("HealthFacilityDesc");

                        //List specialty doctor in Spinner
                        if(list.contains(spesiality)){
                            //TODO
                        }
                        else{
                            //TODO
                            list.add(spesiality);
                        }

                        JSONArray jsonArray1 = jsonObject.getJSONArray("Institute");
                        for (int j=0;j<jsonArray1.length();j++){

                            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);

//                            TextView tvNameHosp = (TextView)findViewById(R.id.tvHospitalName);
//                            tvNameHosp.setText(jsonObject1.getString("InstitutionName"));
                        }

                        listDoctor.add(new Doctor(id, image, frontTitle, name, spesiality, image,"Lihat Kualifikasi",tittle));



//                        new ImageDrawable(mDokterList).execute();
                    }
                    dAdapter = new RecycleAdapter(getApplicationContext(),listDoctor);
//                        dAdapter.filter(spesial);
                    rvDokter.setAdapter(dAdapter);
//                    for (Doctor currentDokter : mDokterList) {
//                        new ImageDrawable(currentDokter).execute();
//                    }


                }catch (JSONException e){
                    e.printStackTrace();
                }
                //mapFragment.getMapAsync(AppointmentDetailActivity.this);

            } else {
                //:TODO
            }
        }
        @Override
        protected void onCancelled() {

        }
    }

    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.DoctorViewHolder> {
        private List<Doctor> mDoctorList;
        private Context context;
        public RecycleAdapter (Context context,List<Doctor> mDoctorList){
            this.mDoctorList = mDoctorList;
            this.context = context;

        }

        public class DoctorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public ImageView imgDview ;
            public TextView tvFrontTitle;
            public TextView tvTittle;
            public TextView tvDrname;
            public TextView tvDrspecialty;
            public TextView btnKualiifikasi;

            public DoctorViewHolder(View newDview) {
                super(newDview);
                imgDview = (ImageView)newDview.findViewById(R.id.iv_doc_pic_list);
                tvFrontTitle = (TextView)newDview.findViewById(R.id.tv_FrontTitleDr);
                tvTittle = (TextView)newDview.findViewById(R.id.tv_specialty_list) ;
                tvDrname = (TextView)newDview.findViewById(R.id.tv_list_drname);
                tvDrspecialty = (TextView)newDview.findViewById(R.id.tv_tittle_list);
                btnKualiifikasi = (TextView) newDview.findViewById(R.id.btn_kualiifikasi);
            }
            @Override
            public void onClick(View v) {

            }
        }

        @Override
        public DoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.doctor_list, parent, false);
            DoctorViewHolder viewHolder = new DoctorViewHolder(listItem);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(DoctorViewHolder holder, int position) {
//            holder.imgDview.setImageDrawable(mDoctorList.get(position).getDoc_pic_id());

            Picasso.with(context).load(mDoctorList.get(position).getDoc_pic_id()).into(holder.imgDview);
            holder.tvFrontTitle.setText(mDoctorList.get(position).getFrontTtlDoc());
            holder.tvDrname.setText(mDoctorList.get(position).getNameDoc());
            holder.tvDrspecialty.setText(mDoctorList.get(position).getSpecialty());
            holder.btnKualiifikasi.setText(mDoctorList.get(position).getKualifikasi());
            holder.tvTittle.setText(mDoctorList.get(position).getTittle());

        }

        @Override
        public int getItemCount() {
            return mDoctorList.size();
        }

//        @Override
//        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//            super.onAttachedToRecyclerView(recyclerView);
//        }
    }
}
