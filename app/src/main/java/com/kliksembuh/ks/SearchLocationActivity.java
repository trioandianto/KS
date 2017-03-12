package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.kliksembuh.ks.library.HttpHandler;
import com.kliksembuh.ks.library.SearchAdapter;
import com.kliksembuh.ks.models.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu((menu));
        SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setQueryHint("Search...");
        menu.add("Search")
                .setIcon(R.drawable.places_ic_search)
                .setActionView(searchView)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_search, menu);
//        MenuItem menuItem = menu.findItem(R.id.menusearch);
//
//        SearchView searchView = (SearchView) menuItem.getActionView();
//
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
        //final ViewGroup parent = (ViewGroup)
        return true;
    }

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
    private class GetContacts extends AsyncTask<Void, Void, Void> {
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
        protected Void doInBackground(Void... args0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            try{
                JSONObject obj = new JSONObject(loadJSONFromAsset());

                    // Getting JSON Array node
                    JSONArray contacts = obj.getJSONArray("location");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);


                        String name = c.getString("name");
                        String code = c.getString("code");
//                        da =new ArrayList<>();
                        mLocation.add(new Location(name,code));
//                        da.add( name );
//                        da.add( code );


                        // Phone node is JSON Object
//                    JSONObject phone = c.getJSONObject("phone");
//                    String mobile = phone.getString("mobile");
//                    String home = phone.getString("home");
//                    String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("code", code);
                        contact.put("name", name);

                    // adding contact to contact list
                    formList.add(contact);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
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
