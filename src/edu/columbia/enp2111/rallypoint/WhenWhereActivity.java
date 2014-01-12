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
	
	private TextView meetingPoint;
	private TextView timeView;
	private TextView dateView;
	
	private String taxiDate;
	private String taxiTime;
	private String destination;
	
    private static final String KEY_DATE_TIME = "date_time";
    private static final String KEY_DESTINATION = "destination";
    private static final String KEY_OWNER_UID = "owner_user";
//    private static final String KEY_MEMBER1_UID = "member_1";
//    private static final String KEY_MEMBER2_UID = "member_2";
//    private static final String KEY_MEMBER3_UID = "member_3";
    private static final String KEY_GROUP_CREATED_AT = "group_created_at";

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
				meetingPoint = (TextView) findViewById(R.id.myDestination);
				meetingPoint.setText(options[item]);
				levelDialog.dismiss();    
			}
		});
		levelDialog = builder.create();
		levelDialog.show();
	}
	
	public void showTimePickerDialog(View v)
	{
		timeView = (TextView) findViewById(R.id.myDepartureTime);
		timeFragment = new TimePickerFragment(timeView);
	    timeFragment.show(getFragmentManager(), "timePicker");
	    // getFragmentManager() requires at least API level 11. See:
	    // http://logs.nslu2-linux.org/livelogs/android-dev/android-dev.20130819.txt
	}
	
	public void showDatePickerDialog(View v)
	{
		dateView = (TextView) findViewById(R.id.myDepartureDate);
		dateFragment = new DatePickerFragment(dateView);
	    dateFragment.show(getFragmentManager(), "datePicker");
	}

	public void onSubmit(View v)
	{
		// TODO: double check that none of the values are null!
		if (timeView.getText() != null && dateView.getText() != null && meetingPoint.getText() != null)
		{
			this.taxiDate = ((DatePickerFragment) dateFragment).getDate();
			this.taxiTime = ((TimePickerFragment) timeFragment).getTime();
			this.destination = ((TextView) findViewById(R.id.myDestination)).getText().toString();
			Log.v("Testing", "The time is " + taxiTime);
			Log.v("Testing", "The date is " + taxiDate);
			Log.v("Testing", "The destination is " + destination);
			// Put all the values from the TextViews into the database
			new MyAsyncTask().execute(taxiDate, taxiTime, destination);
		}
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
			// parameters: String date, String time, String destination
	    	GroupFunctions groupFunction = new GroupFunctions();
	        JSONObject json = groupFunction.createGroup(params[0], params[1], params[2]);
	        return json;
	    }
	   
	    protected void onPostExecute(JSONObject json)
	    {
	    	try
	    	{
	    		if (json != null && json.getString(KEY_SUCCESS) != null)
	    		{
	    			String res = json.getString(KEY_SUCCESS);
	    			if(Integer.parseInt(res) == 1)
	    			{
	    				// only allow creating new groups if logged in!!
	    				UserDatabaseHandler db_user = new UserDatabaseHandler(getApplicationContext());
		                if (db_user.getRowCount() > 0) // user is logged in
		                {
			                Log.v("Testing", "User is logged in.");
		                	GroupDatabaseHandler db_group = new GroupDatabaseHandler(getApplicationContext());
			                JSONObject json_group = json.getJSONObject("group"); // TODO
			                
			                db_group.addGroup(json_group.getString(KEY_DATE_TIME), json_group.getString(KEY_DESTINATION), 
			                		json_group.getString(KEY_OWNER_UID), json_group.getString(KEY_GROUP_CREATED_AT));
			                
		                }
	                 
		                // Launch Dashboard Screen
		                Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
		                 
		                // Close all views before launching Dashboard
		                dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(dashboard);
		                finish(); // Return to dashboard
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