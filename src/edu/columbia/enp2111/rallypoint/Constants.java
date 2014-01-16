package edu.columbia.enp2111.rallypoint;

/**
 * File used to store all the constants used in the project.
 * 
 * @author Emily Pakulski
 */

public class Constants
{
	public static final String API_URL = "http://10.0.2.2/taxi_project/api/";
	
	/*
	 * NOTE: If making changes to these constants, changes must be made on
	 * the API_URL's index.php file, too.
	 */
	/* tags for what index.php should do: */
    public static final String LOGIN_USER_TAG = "login";
    public static final String REGISTER_USER_TAG = "register";
    public static final String CREATE_GROUP_TAG = "create_group";
    public static final String GET_GROUPS_TAG = "get_groups";
    
    /* keys for value mapping: */
    public static final String KEY_TAG = "tag";
    
    // Group-related keys
    public static final String KEY_DATETIME = "datetime";
    public static final String KEY_DESTINATION = "destination";
    public static final String KEY_OWNER_UID = "owner_uid";
    public static final String KEY_MEMBER1_UID = "member_1";
    public static final String KEY_MEMBER2_UID = "member_2";
    public static final String KEY_MEMBER3_UID = "member_3";
    public static final String KEY_GROUP_CREATED_AT = "created_at";
}