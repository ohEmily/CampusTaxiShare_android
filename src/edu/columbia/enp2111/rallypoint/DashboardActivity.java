package edu.columbia.enp2111.rallypoint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

/**
 * Dashboard prompting user to search for groups or create a new one. If user
 * is not logged in, dashboard is not displayed and user is redirected to
 * to the login page.
 * 
 * @author Emily Pakulski
 * @author Ravi Tamada, androidhive.info for the database connection stuff
 */

public class DashboardActivity extends Activity
{
    private UserFunctions userFunctions;
	
    /** Sets login message, network, and logout button. */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);	
		userFunctions = new UserFunctions(); 
		
        // Check login status in database
        if (userFunctions.isUserLoggedIn(getApplicationContext()))
        {
        	// user already logged in, so show databoard
        	setContentView(R.layout.activity_dashboard);
            
        	// show login message with user's name
    		TextView welcomeMessage = (TextView) findViewById(R.id.welcome_message);
            String nameOfUser = userFunctions.getName(getApplicationContext());
    		if (nameOfUser != null)
    			welcomeMessage.setText("Hi " + nameOfUser + "! Your network is: ");
    		
    		// show network name (e.g. "Columbia University", not 'columbia')
    		TextView networkName = (TextView) findViewById(R.id.campus_network_name);
    		String nameOfNetwork = userFunctions.getNetworkName(getApplicationContext());
    		if (nameOfNetwork != null)
    			networkName.setText(nameOfNetwork);
        	
    		Log.v("Testing", "e");
            // set listener for the logout link in the layout footer
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
        else // i.e. if user is not loggged in
        {
            // show login screen
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            finish(); // Closing dashboard screen
        }       
    }
	
	/** Called when the relevant button is pressed. */
	public void createFromSchoolGroup(View v)
	{
    	DashboardActivity.this.startActivity(new Intent(DashboardActivity.this, 
    			WhenWhereActivity.class));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dashboard, menu);
		return true;
	}
}
