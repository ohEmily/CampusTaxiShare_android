package edu.columbia.enp2111.rallypoint;

// handling login event
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Logging in the user activity. Connects to database.
 * @author Emily Pakulski
 * @author Ravi Tamada, androidhive.info for the database connection stuff
 */

public class LoginActivity extends ActionBarActivity
{
	private String email;
	private String password;
	private ActionBar actionBar;

	// JSON response node names
    public static String KEY_SUCCESS = "success";
	
    TextView loginErrorMsg;
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		actionBar = getActionBar();
		actionBar.show();
		
		// set text link to switch to registration page
		TextView toRegistration = (TextView) this.findViewById(R.id.link_to_registration);
		toRegistration.setOnClickListener(new OnClickListener() 
		{
	        @Override
	        public void onClick(View v)
	        {
	        	startActivity(new Intent(LoginActivity.this, 
	        			RegistrationActivity.class));
	        }
	    });
	}

	/** Called when login button is pressed. */
	public void submitLoginFields(View view) 
	{
		final EditText emailField = (EditText) findViewById(R.id.emailEditTextLogin);
		final EditText passwordField = (EditText) findViewById(R.id.passEditTextLogin);
		loginErrorMsg = (TextView) findViewById(R.id.login_error);
		
		email = emailField.getText().toString();
		password = passwordField.getText().toString();
		
		 new LoginAndNetworkTask().execute(email, password);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	/** 
	 * AsyncTask to connect to database in order to check login information and
	 * set user and network values in SQLite Database.
	 */
	private class LoginAndNetworkTask extends AsyncTask<String, Void, String>
	{   
		private String errorSource;
		
		protected String doInBackground(String ... params)
	    {
	    	errorSource = null; // by default, there is no error
			String network = "default";
			
	    	// get user information by connecting to database
			UserFunctions userFunction = new UserFunctions();
	    	if (params.length != 2)
	    		errorSource = "Username or password missing.";
	        JSONObject jsonUserObject = userFunction.loginUser(params[0], params[1]);

	        // parse data gotten from database
	        try 
	    	{
	    		if (jsonUserObject != null && jsonUserObject.getString(KEY_SUCCESS) != null)
	    		{
	    			String res = jsonUserObject.getString(KEY_SUCCESS);
	    			if (Integer.parseInt(res) == 1)
	    			{
	    				// user successfully logged in
		                // Store user details in SQLite Database
	    			    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		                JSONObject jsonUser = jsonUserObject.getJSONObject("user");

		                userFunction.logoutUser(getApplicationContext());
		                db.addUser(jsonUser.getString(DatabaseHandler.KEY_NAME), 
		                		jsonUser.getString(DatabaseHandler.KEY_EMAIL), 
		                		jsonUser.getString(DatabaseHandler.KEY_NETWORK),
				                jsonUser.getString(DatabaseHandler.KEY_UNIQUE_ID), 
				                jsonUser.getString(DatabaseHandler.KEY_CREATED_AT));                 
		            
		                // set network value to set up the network SQLite table
		                network = jsonUser.getString(DatabaseHandler.KEY_NETWORK);
	    			}
	    			else // Error in login
	    			{
//	    				errorSource = R.string.error_message_login;
	    			}
	    		}
	    	} 
		    catch (JSONException e)
		    {
//		        loginErrorMsg.setText(R.string.error_message_server);
		    	e.printStackTrace();
		    }
	        
	        /** network table */
	        NetworkFunctions networkFunction = new NetworkFunctions();
	        JSONObject jsonNetworkObject = networkFunction.getUserNetwork(network);
	        
	        try
	    	{
	    		if (jsonNetworkObject != null && jsonNetworkObject.getString(KEY_SUCCESS) != null)
	    		{
	    			String res = jsonNetworkObject.getString(KEY_SUCCESS);
	    			if (Integer.parseInt(res) == 1)
	    			{
	    				// network data successfully gotten
	    				DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		                JSONObject jsonNetwork = jsonNetworkObject.getJSONObject("network");

		                networkFunction.clearNetwork(getApplicationContext());
		               
		                db.addNetwork(
		                		jsonNetwork.getString(DatabaseHandler.KEY_DOMAIN_STRING),
		                		jsonNetwork.getString(DatabaseHandler.KEY_NETWORK_NAME),
		                		jsonNetwork.getString(DatabaseHandler.KEY_CAMPUS_PLACES),
				                jsonNetwork.getString(DatabaseHandler.KEY_NON_CAMPUS_PLACES)
				                );
	    			}
	    			else
	    			{
	    				// Error - this network doesn't exist
//	    				loginErrorMsg.setText(R.string.error_message_login);
	    			}
	    		}
	    	} 
		    catch (JSONException e)
		    {
//		        loginErrorMsg.setText(R.string.error_message_server);
		    	e.printStackTrace();
		    }
	    	return errorSource;
		}
	    
	    protected void onPostExecute(String error)
	    {

            if (error != null)
            {
            	// TODO
            	// set ERROR TEXT
            }
			
            // Launch Dashboard Screen
            Intent dashboard = new Intent(getApplicationContext(), 
            		DashboardActivity.class);

            // Close all activities before launching Dashboard
            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(dashboard);
            
            finish(); // Close login screen
            
	    }
	}
}
