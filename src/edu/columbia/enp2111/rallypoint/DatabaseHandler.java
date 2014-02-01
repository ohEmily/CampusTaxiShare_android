package edu.columbia.enp2111.rallypoint;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
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

    /** Note: constants must be changed server-side on the database as well as
     * in the Android Application Project. */
    /* Login table specific constants */
    private static final String TABLE_LOGIN = "login";
    
    // Login table column names
    public static final String KEY_UNIQUE_ID = "unique_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NETWORK = "network";
    public static final String KEY_UID = "uid";
    public static final String KEY_CREATED_AT = "created_at"; 
    
    /* Network table specific constants */
    private static final String TABLE_NETWORK = "network";
    // column names
    public static final String KEY_DOMAIN_STRING = "domain_string";
    public static final String KEY_NETWORK_NAME = "network_name";
    public static final String KEY_CAMPUS_PLACES = "campus_places";
    public static final String KEY_NON_CAMPUS_PLACES = "non_campus_places";
    
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
                + KEY_UNIQUE_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_NETWORK + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ");";
        db.execSQL(CREATE_LOGIN_TABLE);
        
        String CREATE_NETWORK_TABLE = "CREATE TABLE " + TABLE_NETWORK + "("
                + KEY_UNIQUE_ID + " INTEGER PRIMARY KEY,"
                + KEY_DOMAIN_STRING + " TEXT,"
                + KEY_NETWORK_NAME + " TEXT,"
                + KEY_CAMPUS_PLACES + " TEXT,"
                + KEY_NON_CAMPUS_PLACES + " TEXT" + ");";
        db.execSQL(CREATE_NETWORK_TABLE);
    }

    /**
     * Store the user's network's details in the database (called when user
     * 
     * @param domainString
     * @param networkName
     * @param campusPlaces
     * @param nonCampusPlaces
     */
    public void addNetwork(String domainString, String networkName, 
    		String campusPlaces, String nonCampusPlaces)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	// creating row containing network data
    	ContentValues values = new ContentValues();
    	values.put(KEY_DOMAIN_STRING, domainString);
    	values.put(KEY_NETWORK_NAME, networkName);
    	values.put(KEY_CAMPUS_PLACES, campusPlaces);
    	values.put(KEY_NON_CAMPUS_PLACES, nonCampusPlaces);
    	
    	db.insert(TABLE_NETWORK, null, values); // insert row
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
            network.put(KEY_CAMPUS_PLACES, cursor.getString(3)); 
            network.put(KEY_NON_CAMPUS_PLACES, cursor.getString(4));
        }
         
        cursor.close();
        db.close();
        return network;
    }
    
    /**
     * Storing user details in database (called when user logs in).
     * */
    public void addUser(String name, String email, String network, String uid,
    		String created_at)
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
            user.put(KEY_NAME, cursor.getString(1));
            user.put(KEY_EMAIL, cursor.getString(2));
            user.put(KEY_NETWORK, cursor.getString(3)); 
            user.put(KEY_UID, cursor.getString(4));
            user.put(KEY_CREATED_AT, cursor.getString(5));
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
    
    public void resetLoginTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOGIN, null, null); // Delete all rows
        db.close();
    }
    
    public void resetNetworkTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NETWORK, null, null); // Delete all rows
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