package com.kliksembuh.ks.library;

import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;
/**
 * Created by Trio Andianto on 2/8/2017.
 */
public class UserFunctions {
    private JSONParser jsonParser;

    //URL of the PHP API WORKING!!!!!!
    private static String loginURL = "http://192.168.1.29/UserAPI/api/users/login";
    private static String registerURL = "http://unps.comli.com/learn2crack_login_api/index.php";
    private static String forpassURL = "http://unps.comli.com/learn2crack_login_api/index.php";
    private static String chgpassURL = "http://unps.comli.com/learn2crack_login_api/index.php";

    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";

    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL,params);
        return json;
    }

    /**
     * Function to change password
     **/

    public JSONObject chgPass(String newpas, String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", chgpass_tag));

        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(chgpassURL,params);
        return json;
    }

    /**
     * Function to reset the password
     **/

    public JSONObject forPass(String forgotpassword){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
        JSONObject json = jsonParser.getJSONFromUrl(forpassURL,params);
        return json;
    }

    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }
}
