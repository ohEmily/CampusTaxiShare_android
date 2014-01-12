package edu.columbia.enp2111.rallypoint;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
/**
 * @author Emily Pakulski, modified Tamada's code to include 'network' param
 */

public class GroupDatabaseHandler extends SQLiteOpenHelper
{
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "group_db";
 
    // Login table name
    private static final String TABLE_GROUP = "groups";
 
    // Login Table Columns names
    private static final String KEY_DATE_TIME = "date_time";
    private static final String KEY_DESTINATION = "destination";
    private static final String KEY_OWNER_UID = "owner_user";
    private static final String KEY_MEMBER1_UID = "member_1";
    private static final String KEY_MEMBER2_UID = "member_2";
    private static final String KEY_MEMBER3_UID = "member_3";
    private static final String KEY_GROUP_CREATED_AT = "group_created_at";
    
    public GroupDatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_GROUP_TABLE = "CREATE TABLE " + TABLE_GROUP + "("
                + KEY_DATE_TIME + " INTEGER PRIMARY KEY,"
                + KEY_DESTINATION + " TEXT,"
                + KEY_OWNER_UID + " TEXT UNIQUE,"
                + KEY_MEMBER1_UID + " TEXT,"
                + KEY_MEMBER2_UID + " TEXT,"
                + KEY_MEMBER3_UID + " TEXT,"
                + KEY_GROUP_CREATED_AT + " TEXT" + ");";
        db.execSQL(CREATE_GROUP_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
        // Create tables again
        onCreate(db);
    }
 
    /**
     * Storing user details in database
     * */
    public void addGroup(String datetime, String destination, String owner_uid, String created_at)
    {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_DATE_TIME, datetime); // Date and time
        values.put(KEY_DESTINATION, destination); // Destination
        values.put(KEY_OWNER_UID, owner_uid); // owner's UID
        values.put(KEY_GROUP_CREATED_AT, created_at); // Created at
        // Inserting Row
        db.insert(TABLE_GROUP, null, values);
        db.close(); // Closing database connection
    }
     
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getGroupDetails()
    {
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_GROUP;
          
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
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
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount()
    {
        String countQuery = "SELECT  * FROM " + TABLE_GROUP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        return rowCount;
    }
     
    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROUP, null, null); // Delete all rows
        db.close();
    }
}