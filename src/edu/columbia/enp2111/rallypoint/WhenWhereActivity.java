package edu.columbia.enp2111.rallypoint;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class WhenWhereActivity extends FragmentActivity
{
	/* has to extend FragmentActivity:
	 * stackoverflow.com/questions/13121432/the-method-is-getsupportfragmentmanager-is-unsuported */

	// TODO limit how many people?
	
//	private int mYear;
//	private int mMonth;
//	private int mDay;

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
		final CharSequence[] options = {"JFK", "LaGuardia","Newark"};
		
		// Creating and Building the Dialog 
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.prompt_destination); 
		builder.setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item)
			{            
				// TODO register the item
				levelDialog.dismiss();    
			}
		});
		levelDialog = builder.create();
		levelDialog.show();
	}
	
	public void showTimePickerDialog(View v)
	{
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getFragmentManager(), "timePicker");
	    // getFragmentManager() requires at least API level 11. See:
	    // http://logs.nslu2-linux.org/livelogs/android-dev/android-dev.20130819.txt
	}
	
	public void showDatePickerDialog(View v)
	{
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.when_where, menu);
		return true;
	}
}