package edu.columbia.enp2111.rallypoint;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/**
 * This class is used to create and get groups of people sharing taxis stored
 * in the database.
 * @author Emily Pakulski
 */

public class GroupFunctions
{
    public static final String CREATE_GROUP_TAG = "create_group";
    public static final String GET_GROUPS_TAG = "get_groups";
	
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
    public JSONObject createGroup(String datetime, String destination, String userID)
    {
        // Building parameters. Mapping to constants
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(Constants.KEY_TAG, CREATE_GROUP_TAG));
        params.add(new BasicNameValuePair(Constants.KEY_DATETIME, datetime));
        params.add(new BasicNameValuePair(Constants.KEY_DESTINATION, destination));
        params.add(new BasicNameValuePair(Constants.KEY_OWNER_UID, userID));
        JSONObject json = jsonParser.getJSONFromUrl(JSONParser.API_URL, params);
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
    	params.add(new BasicNameValuePair(Constants.KEY_TAG, GET_GROUPS_TAG));
    	params.add(new BasicNameValuePair("search_params", "all"));
    	JSONObject json = jsonParser.getJSONFromUrl(JSONParser.API_URL, params);
    	return json;
    }
}