package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.kliksembuh.ks.library.SearchAdapter;
import com.kliksembuh.ks.models.Location;

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
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SearchLocationActivity extends AppCompatActivity implements ListView.OnItemClickListener{
    private ArrayAdapter<String> listAdapter;
    private ListAdapter adapter;
    private AutoCompleteTextView location;
    private List<Location> mLocation;
    private SearchAdapter searchAdapter;
    private ProgressDialog pDialog;
    private String TAG = SearchLocationActivity.class.getSimpleName();
    ArrayList<HashMap<String, String>> formList;
    ArrayList<String> da;
    private SearchView searchView;
//    private MaterialSearchView searchView;

    private ListView listItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        getWindow().setStatusBarColor(Color.BLACK);

        //location = (AutoCompleteTextView)findViewById(R.id.tvlocation);
        mLocation = new ArrayList<>();
        formList = new ArrayList<>();
        listItem = (ListView) findViewById(R.id.listlocation);
//        location =(AutoCompleteTextView) searchView.findViewById(R.id.tvlocation);
//        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                listAdapter.getFilter().filter( newText );
//                return false;
//            }
//        } );

        // Getting JSON Array node

        new GetContacts().execute();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu((menu));
//        SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
//        searchView.setQueryHint("Search...");
//        menu.add("Search")
//                .setIcon(R.drawable.places_ic_search)
//                .setActionView(searchView)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
////        MenuInflater inflater = getMenuInflater();
////        inflater.inflate(R.menu.menu_search, menu);
////        MenuItem menuItem = menu.findItem(R.id.menusearch);
////
////        SearchView searchView = (SearchView) menuItem.getActionView();
////
////
////        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////            @Override
////            public boolean onQueryTextSubmit(String query) {
////                return false;
////            }
////
////            @Override
////            public boolean onQueryTextChange(String newText) {
////                searchAdapter.getFilter().filter(newText);
////                return false;
////            }
////        });
//        //final ViewGroup parent = (ViewGroup)
//        return true;
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    public static String AssetJSONFile (String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("location.json");
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
    private class GetContacts extends AsyncTask<String, Void, String> {
        private Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SearchLocationActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://basajans/KliksembuhAPI/api/SubDistricts/GetSubDistrict");
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
                }

            } else {
                return "";
            }
        }



        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(result!=""){
                try {
                    JSONArray jsonArray = new JSONArray(result);
//                    Iterator<String> keys = jsonObj.keys();
//                    jsonObj.keys();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String subDistrictCD = jsonObject1.getString("SubDistrictCD");
                        String subDistrictDescription = jsonObject1.getString("SubDistrictDescription");
                        mLocation.add(new Location(subDistrictDescription,subDistrictCD));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//            adapter = new SimpleAdapter(
//                    SearchLocationActivity.this,formList,
//                    R.layout.list_item, new String[]{"name", "code"}, new int[]{R.id.name,
//                    R.id.email});
            searchAdapter = new SearchAdapter(getApplicationContext(), mLocation);
//            listAdapter = new ArrayAdapter<String>(  SearchLocationActivity.this,
//                    android.R.layout.simple_list_item_1, da);
//            listItem.setAdapter( listAdapter );


            listItem.setAdapter(searchAdapter);
        }
    }
}
