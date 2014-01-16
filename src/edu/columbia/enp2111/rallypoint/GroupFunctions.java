package edu.columbia.enp2111.rallypoint;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;

/**
 * This class is used to create and get groups of people sharing taxis stored
 * in the database.
 * @author Emily Pakulski
 */

public class GroupFunctions
{
    private JSONParser jsonParser;
    
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
        // Building parameters. Mapping to constants
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(Constants.KEY_TAG, Constants.CREATE_GROUP_TAG));
        params.add(new BasicNameValuePair(Constants.KEY_DATETIME, datetime));
        params.add(new BasicNameValuePair(Constants.KEY_DESTINATION, destination));
        params.add(new BasicNameValuePair(Constants.KEY_OWNER_UID, "52cf4405795782.50588600")); // TODO
        JSONObject json = jsonParser.getJSONFromUrl(Constants.API_URL, params);
        return json;
    }
    
    /**
     * Returns a selection of groups depending on the params passed.
     * @param search_params can be: all - all the groups, 
     * @return
     */
    public JSONObject getAllGroups()
    {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair(Constants.KEY_TAG, Constants.GET_GROUPS_TAG));
    	params.add(new BasicNameValuePair("search_params", "all"));
    	JSONObject json = jsonParser.getJSONFromUrl(Constants.API_URL, params);
    	return json;
    }
}