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
	/* tags for what index.php should do: 
	 * NOTE: If making changes to these constants, changes must be made on
	 * the API_URL's index.php file, too.
	 */
	public static final String TAG_CREATE_GROUP = "create_group";
    public static final String TAG_GET_GROUPS = "get_groups";
    
    // keys for passing values
    public static final String KEY_GROUP_ID = "group_id";
    public static final String KEY_OWNER_EMAIL = "owner_email";
    public static final String KEY_NETWORK = "network";
    public static final String KEY_START_LOCATION = "start_location";
    public static final String KEY_DESTINATION = "end_location";
    public static final String KEY_DATETIME = "departure_date_time";
    public static final String KEY_DIRECTION = "direction";
    public static final String KEY_MEMBER1_UID = "member_1";
    public static final String KEY_MEMBER2_UID = "member_2";
    public static final String KEY_MEMBER3_UID = "member_3";
    public static final String KEY_CREATED_AT = "created_at";
	public static final String KEY_UPDATED_AT = "updated_at";
    
	public static final String KEY_TO_CAMPUS_GROUPS = "to_campus";
	public static final String KEY_FROM_CAMPUS_GROUPS = "from_campus";

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
    public JSONObject createGroup(String ownerEmail, String network, 
    		String datetime, String startLocation, String destination, 
    		String direction)
    {
    	// Building parameters. Mapping to constants
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(JSONParser.KEY_TAG, TAG_CREATE_GROUP));
        params.add(new BasicNameValuePair(KEY_OWNER_EMAIL, ownerEmail));
        params.add(new BasicNameValuePair(KEY_NETWORK, network));
        params.add(new BasicNameValuePair(KEY_START_LOCATION, startLocation));
        params.add(new BasicNameValuePair(KEY_DATETIME, datetime));
        params.add(new BasicNameValuePair(KEY_DESTINATION, destination));
        params.add(new BasicNameValuePair(KEY_DIRECTION, direction));
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
    	params.add(new BasicNameValuePair(JSONParser.KEY_TAG, TAG_GET_GROUPS));
    	params.add(new BasicNameValuePair("search_params", "all"));
    	JSONObject json = jsonParser.getJSONFromUrl(JSONParser.API_URL, params);
    	return json;
    }
}