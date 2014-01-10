package edu.columbia.enp2111.rallypoint;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

/**
 * Dashboard. Connects to database.
 * @author Emily Pakulski
 * @author Ravi Tamada, androidhive.info for the database connection stuff
 */

public class DashboardActivity extends Activity
{
    private UserFunctions userFunctions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);		
        /**
         * Dashboard Screen for the application
         * */
        // Check login status in database
        userFunctions = new UserFunctions();
        if (userFunctions.isUserLoggedIn(getApplicationContext())) // user already logged in show databoard
        {
            setContentView(R.layout.activity_dashboard);
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
