package edu.columbia.enp2111.rallypoint;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.app.ActionBar;

// handling login event
import org.json.JSONException;
import org.json.JSONObject;

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
    private static String KEY_SUCCESS = "success";
//    private static String KEY_ERROR = "error";
//    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_NETWORK = "network";
    private static String KEY_CREATED_AT = "created_at";
	
    TextView loginErrorMsg;
    
    DatabaseHandler db;
    
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
		
		 new LoginTask().execute(email, password);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	/** 
	 * AsyncTask to connect to database in order to check login information.
	 */
	private class LoginTask extends AsyncTask<String, Void, JSONObject>
	{   
	    protected JSONObject doInBackground(String ... params)
	    {
	    	UserFunctions userFunction = new UserFunctions();
	    	if (params.length != 2)
	    		return null;
	        JSONObject json = userFunction.loginUser(params[0], params[1]);
	        return json;
	    }
	   
	    protected void onPostExecute(JSONObject json)
	    {
	    	try
	    	{
	    		if (json != null && json.getString(KEY_SUCCESS) != null)
	    		{
	    			loginErrorMsg.setText("");
	    			String res = json.getString(KEY_SUCCESS);
	    			if (Integer.parseInt(res) == 1)
	    			{
	    				// user successfully logged in
		                // Store user details in SQLite Database
		                db = new DatabaseHandler(getApplicationContext());
		                JSONObject jsonUser = json.getJSONObject("user");

		                UserFunctions userFunction = new UserFunctions();
		                userFunction.logoutUser(getApplicationContext());
		                db.addUser(jsonUser.getString(KEY_NAME), 
		                		jsonUser.getString(KEY_EMAIL), 
		                		jsonUser.getString(KEY_NETWORK),
				                json.getString(KEY_UID), 
				                jsonUser.getString(KEY_CREATED_AT));                 

		                // set up the network table in the SQLite Database
		                new GetNetworkInfoTask().execute(jsonUser.getString(KEY_NETWORK));
		                
		                // Launch Dashboard Screen
		                Intent dashboard = new Intent(getApplicationContext(), 
		                		DashboardActivity.class);
 
		                // Close all activities before launching Dashboard
		                dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(dashboard);
 
		                finish(); // Close login screen
	    			}
	    			else // Error in login
	    			{
	    				loginErrorMsg.setText(R.string.error_message_login);
	    			}
	    		}
	    	} 
		    catch (JSONException e)
		    {
		        loginErrorMsg.setText(R.string.error_message_server);
		    	e.printStackTrace();
		    }
		}
	}
	
	/** 
	 * AsyncTask to build the user's network information in the SqLite database.
	 */
	private class GetNetworkInfoTask extends AsyncTask<String, Void, JSONObject>
	{		
//		private Context context;
//		
//        public GetNetworkInfoTask(Context context)
//        {
//            this.context = context;
//        }
//		
		protected JSONObject doInBackground(String ... params)
	    {
	    	NetworkFunctions networkFunction = new NetworkFunctions();
	    	if (params.length != 1)
	    		return null;
	        JSONObject json = networkFunction.getUserNetwork(params[0]);
	        return json;
	    }
	   
	    protected void onPostExecute(JSONObject json)
	    {
	    	try
	    	{
	    		if (json != null && json.getString(KEY_SUCCESS) != null)
	    		{
	    			String res = json.getString(KEY_SUCCESS);
	    			if (Integer.parseInt(res) == 1)
	    			{
	    				// network data successfully gotten
//		                db = new DatabaseHandler(getApplicationContext());
		                JSONObject jsonNetwork = json.getJSONObject("network");

		                NetworkFunctions networkFunction = new NetworkFunctions();
		                networkFunction.clearNetwork(getApplicationContext());
		                
		                db.addNetwork(
		                		jsonNetwork.getString(DatabaseHandler.KEY_DOMAIN_STRING),
		                		jsonNetwork.getString(DatabaseHandler.KEY_NETWORK_NAME), 
		                		jsonNetwork.getString(DatabaseHandler.KEY_DEFAULT_MEETING_POINT),
				                jsonNetwork.getString(DatabaseHandler.KEY_DESTINATION_LIST)
				                );
		                
		                Log.v("Testing", "Default meeting point: " + 
				                jsonNetwork.getString(DatabaseHandler.KEY_DEFAULT_MEETING_POINT));
		                Log.v("Testing", "Domain name: " + 
				                jsonNetwork.getString(DatabaseHandler.KEY_DOMAIN_STRING));
		                Log.v("Testing", "Default meeting point: " + 
				                jsonNetwork.getString(DatabaseHandler.KEY_DEFAULT_MEETING_POINT));
	    			}
	    			else
	    			{
	    				// Error - this network doesn't exist
	    				loginErrorMsg.setText(R.string.error_message_login);
	    			}
	    		}
	    	} 
		    catch (JSONException e)
		    {
		        loginErrorMsg.setText(R.string.error_message_server);
		    	e.printStackTrace();
		    }
		}
	}
}
