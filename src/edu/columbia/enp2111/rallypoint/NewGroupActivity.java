package edu.columbia.enp2111.rallypoint;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
public abstract class NewGroupActivity extends FragmentActivity
{
	public static final String KEY_CONFIRMATION_MESSAGE = "confirmation_message"; 
	private static String KEY_SUCCESS = "success";
	
	// direction values
	public static final String KEY_TO_CAMPUS = "t";
	public static final String KEY_FROM_CAMPUS = "f";
	
	// dialogs
	private AlertDialog destinationDialog;
	private DialogFragment dateFragment;
	private DialogFragment timeFragment;
	
	// TextViews
	private TextView startingLocationView;
	private TextView destinationView;
	private TextView timeView;
	private TextView dateView;
	
	// values
	protected String taxiDate;
	protected String taxiTime;
	protected String destination;
	protected String startLocation;
	protected String direction;
	
	// functionality
	private UserFunctions userFunctions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        userFunctions = new UserFunctions();
		
        // Check login status in database
        if (userFunctions.isUserLoggedIn(getApplicationContext()))
        {
        	// user already logged in show databoard
            setContentView(R.layout.activity_new_group);
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
	
	public void showLocationPickerDialog(View v, final String[] destinationOptions)
	{
		// Creating and Building the Dialog 
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_title_end_location); 
		builder.setSingleChoiceItems(destinationOptions, -1, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int item)
			{   
				startingLocationView = (TextView) findViewById(R.id.myDestination);
				startingLocationView.setText(destinationOptions[item]);
				destinationDialog.dismiss();    
			}
		});
		destinationDialog = builder.create();
		destinationDialog.show();
	}
	
	public abstract void showDestinationPickerDialog(View v);
	public abstract void showStartLocationPickerDialog(View v);
	
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
		if (timeView.getText() != "" || dateView.getText() != "" || destinationView.getText() != "")
		{
			this.taxiDate = ((DatePickerFragment) dateFragment).getDate();
			this.taxiTime = ((TimePickerFragment) timeFragment).getTime();
			this.startLocation = ((TextView) findViewById(R.id.myMeetingPoint)).getText().toString();
			this.destination = ((TextView) findViewById(R.id.myDestination)).getText().toString();
			String ownerEmail = userFunctions.getEmail(getApplicationContext());
			String network = userFunctions.getNetwork(getApplicationContext());
			Log.v("Testing", "The time is " + taxiTime);
			Log.v("Testing", "The date is " + taxiDate);
			Log.v("Testing", "The destination is " + destination);
			Log.v("Testing", "The owner's email is: " + ownerEmail);
			String taxiDateTime = taxiDate + " " + taxiTime;
			// Put all the values from the TextViews into the database
			new CreateGroupTask().execute(ownerEmail, network, taxiDateTime, startLocation, destination, direction);
			
			// add Toast (small popup window) notifying user that the activity was created
			CharSequence text = getString(R.string.confirmation_message);
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(getApplicationContext(), text, duration);
			toast.show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
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
			// parameters: String datetime, String destination, String ownerEmail
	    	GroupFunctions groupFunction = new GroupFunctions();
	        JSONObject json = groupFunction.createGroup(params[0], params[1], params[2], params[3], params[4], params[5]);
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