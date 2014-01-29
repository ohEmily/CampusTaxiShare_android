package edu.columbia.enp2111.rallypoint;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
/**
 * Database handler that deals with the temporary login table, which allows
 * the app to keep track of whether a user is logged in, and who that user is.
 * 
 * Explanation of implementation:
 * The SQLite table in this class starts empty. When a user logs in, the
 * user's information (name, email, network, etc.) are pushed onto that row.
 * When the application is running, data about the user can be 
 * 
 * @author Emily Pakulski, modified Tamada's code
 * @author Ravi Tamada, original code from tutorial on AndroidHive.info
 */

public class DatabaseHandler extends SQLiteOpenHelper
{
    // All Static variables
    private static final int DATABASE_VERSION = 1;
 
    private static final String DATABASE_NAME = "taxi_app";

    /* Login table specific constants */
    private static final String TABLE_LOGIN = "login";
    // Login table column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NETWORK = "network";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at"; 
    
    /* Network table specific constants */
    private static final String TABLE_NETWORK = "network";
    // column names
    public static final String KEY_DOMAIN_STRING = "domain_string";
    public static final String KEY_NETWORK_NAME = "network_name";
    public static final String KEY_DEFAULT_MEETING_POINT = "default_meeting_point";
    public static final String KEY_DESTINATION_LIST = "destination_list";
    
    /** Constructor. */
    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    /**
     * Creating tables containing login data and logged in user's network's
     * data.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_NETWORK + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ");";
        db.execSQL(CREATE_LOGIN_TABLE);
        
        String CREATE_NETWORK_TABLE = "CREATE TABLE " + TABLE_NETWORK + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DOMAIN_STRING + " TEXT,"
                + KEY_NETWORK_NAME + " TEXT,"
                + KEY_DEFAULT_MEETING_POINT + " TEXT,"
                + KEY_DESTINATION_LIST + " TEXT" + ");";
        db.execSQL(CREATE_NETWORK_TABLE);
    }

    /**
     * Store the user's network's details in the database (called when user
     * 
     * @param domainString
     * @param networkName
     * @param meetingPoint
     * @param destinationList
     */
    public void addNetwork(String domainString, String networkName, 
    		String meetingPoint, String destinationList)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	// creating row containing network data
    	ContentValues values = new ContentValues();
    	values.put(KEY_DOMAIN_STRING, domainString);
    	values.put(KEY_NETWORK_NAME, networkName);
    	values.put(KEY_DEFAULT_MEETING_POINT, meetingPoint);
    	values.put(KEY_DESTINATION_LIST, destinationList);
    	
    	Log.v("Testing", "AddNetwork in DBHandler: " + domainString);
    	
    	db.insert(TABLE_NETWORK, null, values);
    	db.close();
    }
    
    /**
     * Getting the logged in user's network's data from database.
     * @return HashMap with user's name, email, network, uid & created_at
     * */
    public HashMap<String, String> getNetworkDetails()
    {
    	HashMap<String,String> network = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_NETWORK;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst(); // Move to first row (only one row in table)
        // fill hashmap "network"
        if(cursor.getCount() > 0)
        {
            network.put(KEY_DOMAIN_STRING, cursor.getString(1));
            network.put(KEY_NETWORK_NAME, cursor.getString(2));
            network.put(KEY_DEFAULT_MEETING_POINT, cursor.getString(3)); 
            network.put(KEY_DESTINATION_LIST, cursor.getString(4));
        }
        if (network.get(KEY_NETWORK_NAME) == null)
        	Log.v("Testing", "DBHandler: Network_name is null");
//        Log.v("Testing", (String) network.get(KEY_NETWORK_NAME));
        
        cursor.close();
        db.close();
        return network;
    }
    
    /**
     * Storing user details in database (called when user logs in).
     * */
    public void addUser(String name, String email, String network, String uid, String created_at)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // creating row containing user data
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); 
        values.put(KEY_EMAIL, email);
        values.put(KEY_NETWORK, network); 
        values.put(KEY_UID, uid);
        values.put(KEY_CREATED_AT, created_at);
        
        db.insert(TABLE_LOGIN, null, values); // insert row
        db.close(); // Closing database connection
    }
     
    /**
     * Getting the logged in user's data from database.
     * @return HashMap with user's name, email, network, uid & created_at
     * */
    public HashMap<String, String> getUserDetails()
    {
    	HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst(); // Move to first row (only one row in table)
        // fill hashmap "user"
        if(cursor.getCount() > 0)
        {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("network", cursor.getString(3)); 
            user.put("uid", cursor.getString(4));
            user.put("created_at", cursor.getString(5));
        }
        cursor.close();
        db.close();
        return user;
    }
 
    /**
     * Used for getting user login status; user is logged in when there are
     * rows in the table.
     * */
    public int getLoginRowCount()
    {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        return rowCount;
    }
     
    /**
     * Reset table by deleting the contents.
     * */
    public void resetTables()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOGIN, null, null); // Delete all rows
        db.delete(TABLE_NETWORK, null, null);
        db.close();
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NETWORK);
        // Create tables again
        onCreate(db);
    }
}