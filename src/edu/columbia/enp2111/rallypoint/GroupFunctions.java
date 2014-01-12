package edu.columbia.enp2111.rallypoint;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;
 
public class GroupFunctions
{
    private JSONParser jsonParser;

    private static String URL = "http://10.0.2.2/taxi_login_api/";
    
    private static String create_tag = "create_group";

    /** Default constructor. */
    public GroupFunctions()
    {
    	jsonParser = new JSONParser();
    }
     
    /**
     * Method to make a create group request.
     * @param email
     * @param password
     * */
    public JSONObject createGroup(String datetime, String destination)
    {
        // Building parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", create_tag));
        params.add(new BasicNameValuePair("datetime", datetime));
        params.add(new BasicNameValuePair("destination", destination));
        params.add(new BasicNameValuePair("owner_uid", "52cf4405795782.50588600"));
        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
    }
}