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

/**
 * This class manages the activity wherein the user selects the destination
 * and the departure date and time in order to create a new group. 
 * The class also handles connecting to the database and pushing the user-
 * provided values onto the corresponding table.
 * 
 * @author Emily Pakulski
 *
 */

/* has to extend FragmentActivity because:
 * stackoverflow.com/questions/13121432/the-method-is-getsupportfragmentmanager-is-unsuported */
public class NewGroupActivity extends FragmentActivity
{
	public static final String KEY_CONFIRMATION_MESSAGE = "confirmation_message"; 
	private static String KEY_SUCCESS = "success";
	
	// dialogs
	private AlertDialog destinationDialog;
	private DialogFragment dateFragment;
	private DialogFragment timeFragment;
	
	// TextViews
	private TextView meetingPoint;
	private TextView timeView;
	private TextView dateView;
	
	// functionality
	private UserFunctions userFunctions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        userFunctions = new UserFunctions();
		
        // Check login status in database
        if (userFunctions.isUserLoggedIn(getApplicationContext()))
//        if (1 == 1)
        {
        	// user already logged in show databoard
            setContentView(R.layout.activity_when_where);
            TextView linkLogout = (TextView) findViewById(R.id.link_to_logout);
            
            linkLogout.setOnClickListener(new View.OnClickListener()
            {     
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
	 * Called when the "choose time" button is pressed; opens the dialog to 
	 * choose a time without changing activity. 
	 */
	public void showDestinationPickerDialog(View v)
	{		
		// Strings to Show In Dialog with Radio Buttons
		final CharSequence[] options = {"John F. Kennedy International Airport", "LaGuardia Airport","Newark Liberty International Airport"};
		
//		NetworkFunctions networkFunction = new NetworkFunctions();
//		final String[] destinationOptions = networkFunction.getDestinations(getApplicationContext());
		
		// Creating and Building the Dialog 
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.destination); 
		builder.setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int item)
			{   
				meetingPoint = (TextView) findViewById(R.id.myDestination);
				meetingPoint.setText(options[item]);
				destinationDialog.dismiss();    
			}
		});
		destinationDialog = builder.create();
		destinationDialog.show();
	}
	
	/** Called when the "choose time" button is pressed; opens the dialog to 
	 * choose a time without changing activity. */
	public void showTimePickerDialog(View v)
	{
		timeView = (TextView) findViewById(R.id.myDepartureTime);
		timeFragment = new TimePickerFragment(timeView);
	    timeFragment.show(getFragmentManager(), "timePicker");
	    // getFragmentManager() requires at least API level 11. See:
	    // http://logs.nslu2-linux.org/livelogs/android-dev/android-dev.20130819.txt
	}
	
	/** Called when the "choose date" button is pressed; opens the dialog to 
	 * choose a date without changing activity. */
	public void showDatePickerDialog(View v)
	{
		dateView = (TextView) findViewById(R.id.myDepartureDate);
		dateFragment = new DatePickerFragment(dateView);
	    dateFragment.show(getFragmentManager(), "datePicker");
	}

	/**
	 * Called when the "submit" button is pressed. Pushes the user's selection
	 * onto the database.
	 */
	public void onSubmit(View v)
	{
		// TODO: double check that none of the values are null!
		if (timeView.getText() != "" || dateView.getText() != "" || meetingPoint.getText() != "")
		{
			String taxiDate = ((DatePickerFragment) dateFragment).getDate();
			String taxiTime = ((TimePickerFragment) timeFragment).getTime();
			String destination = ((TextView) findViewById(R.id.myDestination)).getText().toString();
			String userID = userFunctions.getID(getApplicationContext());
			Log.v("Testing", "The time is " + taxiTime);
			Log.v("Testing", "The date is " + taxiDate);
			Log.v("Testing", "The destination is " + destination);
			Log.v("Testing", userID);
			String taxiDateTime = taxiDate + " " + taxiTime;
			// Put all the values from the TextViews into the database
			new CreateGroupTask().execute(taxiDateTime, destination, userID);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.when_where, menu);
		return true;
	}
	
	private class CreateGroupTask extends AsyncTask<String, Void, JSONObject>
	{   
	    // pass date and time as strings
		// http://stackoverflow.com/questions/12120433/php-mysql-insert-date-format
		protected JSONObject doInBackground(String ... params)
	    {
			// parameters: String datetime, String destination
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
	    				 if (userFunctions.isUserLoggedIn(getApplicationContext()))
	    		        {      
			                // Launch Dashboard Screen
			                Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
			                 
			                // Close all views before launching Dashboard
			                dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			                dashboard.putExtra(KEY_CONFIRMATION_MESSAGE, getString(R.string.confirmation_message));
			                startActivity(dashboard);
			                // TODO put extra so that a message is shown to confirm group making
			                finish(); // Return to dashboard
	    		        }
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