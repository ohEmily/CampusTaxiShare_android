package edu.columbia.enp2111.rallypoint;

/**
 * @author Ravi Tamada, from androidhive.info
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
 
public class UserFunctions {
     
    private JSONParser jsonParser;
     
    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String loginURL = "http://10.0.2.2/taxi_login_api/";
    private static String registerURL = "http://10.0.2.2/taxi_login_api/";
     
    private static String login_tag = "login";
    private static String register_tag = "register";
     
    // constructor
    public UserFunctions()
    {
    	jsonParser = new JSONParser();
    }
     
    /**
     * Method to make a login request.
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String email, String password)
    {
        // Building parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }
     
    /**
     * Method to make a registration request.
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject registerUser(String name, String email, String network, String password)
    {
        // Building parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("network", network));
        params.add(new BasicNameValuePair("password", password));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        return json; // return json
    }
     
    /**
     * Method to get login status. Returns true if logged in, false if not.
     * */
    public boolean isUserLoggedIn(Context context)
    {
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        if(count > 0) // user logged in
        {
        	return true;
        }
        return false;
    }
     
    /**
     * Method to logout user and reset database.
     * */
    public boolean logoutUser(Context context)
    {
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }
     
}