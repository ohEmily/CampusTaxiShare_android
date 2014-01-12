package edu.columbia.enp2111.rallypoint;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class WhenWhereActivity extends FragmentActivity
{
	/* has to extend FragmentActivity:
	 * stackoverflow.com/questions/13121432/the-method-is-getsupportfragmentmanager-is-unsuported */

	// TODO limit how many people?
	private DialogFragment dateFragment;
	private DialogFragment timeFragment;
	
	private String taxiDate;
	private String taxiTime;

	private static String KEY_SUCCESS = "success";
	
	private UserFunctions userFunctions;
	
	AlertDialog levelDialog;
	static final int DATE_DIALOG_ID = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        /**
         * When/Where Screen for the application
         * */
        // Check login status in database
        userFunctions = new UserFunctions();
        if (userFunctions.isUserLoggedIn(getApplicationContext()))
        {
        	// user already logged in show databoard
            setContentView(R.layout.activity_when_where);
            TextView linkLogout = (TextView) findViewById(R.id.link_to_logout);
             
            linkLogout.setOnClickListener(new View.OnClickListener() {
                 
                public void onClick(View view)
                {
                    userFunctions.logoutUser(getApplicationContext());
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                    finish(); // Closing dashboard screen
                }
            });   
        }
        else
        {
            // user is not logged in show login screen
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            finish(); // Closing dashboard screen
        }   
	}
	
	/**
	 * @author: http://learnandroideasily.blogspot.com/2013/01/adding-radio-buttons-in-dialog.html
	 */
	public void showDestinationPickerDialog(View v)
	{		
		// Strings to Show In Dialog with Radio Buttons
		final CharSequence[] options = {"John F. Kennedy International Airport", "LaGuardia Airport","Newark Liberty International Airport"};
		
		// Creating and Building the Dialog 
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.destination); 
		builder.setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int item)
			{   
				TextView meetingPoint = (TextView) findViewById(R.id.myDestination);
				meetingPoint.setText(options[item]);
				levelDialog.dismiss();    
			}
		});
		levelDialog = builder.create();
		levelDialog.show();
	}
	
	public void showTimePickerDialog(View v)
	{
		TextView timeView = (TextView) findViewById(R.id.myDepartureTime);
		timeFragment = new TimePickerFragment(timeView);
	    timeFragment.show(getFragmentManager(), "timePicker");
	    // getFragmentManager() requires at least API level 11. See:
	    // http://logs.nslu2-linux.org/livelogs/android-dev/android-dev.20130819.txt
	}
	
	public void showDatePickerDialog(View v)
	{
		TextView dateView = (TextView) findViewById(R.id.myDepartureDate);
		dateFragment = new DatePickerFragment(dateView);
	    dateFragment.show(getFragmentManager(), "datePicker");
	}

	public void onSubmit(View v)
	{
		this.taxiDate = ((DatePickerFragment) dateFragment).getDate();
		this.taxiTime = ((TimePickerFragment) timeFragment).getTime();
		Log.v("Testing", "The time is " + taxiTime);
		Log.v("Testing", "The date is " + taxiDate);
		// Put all the values from the TextViews into the database
		
//		new MyAsyncTask().execute(taxiMonth, taxiYear, taxiDay, taxiHour, taxiMinute);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.when_where, menu);
		return true;
	}
	
	private class MyAsyncTask extends AsyncTask<String, Void, JSONObject>
	{   
	    // pass date and time as strings
		// http://stackoverflow.com/questions/12120433/php-mysql-insert-date-format
		protected JSONObject doInBackground(String ... params)
	    {
	    	GroupFunctions groupFunction = new GroupFunctions();
	    	if (params.length != 2)
	    		return null;
	        JSONObject json = groupFunction.createGroup(params[0], params[1]);
	        return json;
	    }
	   
	    protected void onPostExecute(JSONObject json)
	    {
	    	try
	    	{
	    		if (json != null && json.getString(KEY_SUCCESS) != null)
	    		{
	    			Log.v("Testing", "c");
	    			String res = json.getString(KEY_SUCCESS);
	    			if(Integer.parseInt(res) == 1)
	    			{
	    				// user successfully logged in
		                // Store user details in SQLite Database
		                UserDatabaseHandler db = new UserDatabaseHandler(getApplicationContext());
		                JSONObject json_user = json.getJSONObject("user");
	                 
		                // Clear all previous data in database
		                UserFunctions userFunction = new UserFunctions();
		                userFunction.logoutUser(getApplicationContext());
//		                db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json_user.getString(KEY_NETWORK),
//		                		json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));                 
//		                 
		                // Launch Dashboard Screen
		                Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
		                 
		                // Close all views before launching Dashboard
		                dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(dashboard);
		                 
		                finish(); // Close create screen
	    			}
	    			else
	    			{
	    				// Error in login
	    				Log.v("Testing", "Error in connecting to DB in WhenWhere");
//	    				loginErrorMsg.setText(R.string.error_message_login);
	    			}
	    		}
	    	} 
		    catch (JSONException e)
		    {
		        Log.v("Testing", "Server error. Please try again later.");
		    	e.printStackTrace();
		    }
		}
	}
}