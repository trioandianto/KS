package com.kliksembuh.ks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.kliksembuh.ks.library.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by Trio Andianto on 1/24/2017.
 */
public class FavoritePelayananActivity extends Fragment {
    private String TAG = FavoritePelayananActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON

    ArrayList<HashMap<String, String>> contactList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_favorite_pelayanan, container, false);

        contactList = new ArrayList<>();
        lv = (ListView) rootView.findViewById(R.id.list);

        new GetContacts().execute();

        return rootView;
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {
        private Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
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
                JSONArray contacts = obj.getJSONArray("contacts");

                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);

                    String id = c.getString("id");
                    String name = c.getString("name");
                    String email = c.getString("email");
                    String address = c.getString("address");
                    String gender = c.getString("gender");

                    // Phone node is JSON Object
                    JSONObject phone = c.getJSONObject("phone");
                    String mobile = phone.getString("mobile");
                    String home = phone.getString("home");
                    String office = phone.getString("office");

                    // tmp hash map for single contact
                    HashMap<String, String> contact = new HashMap<>();

                    // adding each child node to HashMap key => value
                    contact.put("id", id);
                    contact.put("name", name);
                    contact.put("email", email);
                    contact.put("mobile", mobile);

                    // adding contact to contact list
                    contactList.add(contact);
                }
            } catch (final JSONException e) {
               // e.printStackTrace();
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), contactList,
                    R.layout.list_item, new String[]{"name", "email",
                    "mobile"}, new int[]{R.id.name,
                    R.id.email, R.id.mobile});

            lv.setAdapter(adapter);
        }
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("contact.json");
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
}
