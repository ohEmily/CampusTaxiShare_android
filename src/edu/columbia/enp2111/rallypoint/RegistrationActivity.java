package edu.columbia.enp2111.rallypoint;

import java.util.Locale;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
* Registering a new user. Connects to database.
* @author Emily Pakulski
* @author Ravi Tamada, androidhive.info for the database connection stuff
*/

public class RegistrationActivity extends Activity {
	private String email;
	private String firstName;
	private String password;
	private String network;
	
    // JSON Response node names
    private static String KEY_SUCCESS = "success";

    // Activity page elements
    TextView registerErrorMsg;
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		final TextView toLogin = (TextView) this.findViewById(R.id.link_to_login);
		toLogin.setOnClickListener(new OnClickListener()
		{
	        @Override
	        public void onClick(View v) 
	        {
	        	finish();// pop this activity off activity stack and go back to login screen
	        }
	    });
	}
	

	/**
	 * Called when the user submits all the fields; checks that .
	 * @param view
	 */
	public void submitRegistrationFields(View view)
	{
		final EditText emailField = (EditText) findViewById(R.id.emailEditTextRegistration);
		final EditText firstNameField = (EditText) findViewById(R.id.nameEditTextRegistration);
		final EditText passField = (EditText) findViewById(R.id.passEditTextRegistration);
		registerErrorMsg = (TextView) findViewById(R.id.registration_error);
		
		String tempName = firstNameField.getText().toString();
		String tempEmail = emailField.getText().toString();
		String tempPass = passField.getText().toString();
		
		if (setFirstName(tempName) && setEmailAndNetwork(tempEmail) && setPassword(tempPass))
		{
			new RegisterAndNetworkTask().execute(firstName, email, network, password);
		}
	}
	
	/**
	 * Sets the user's first name if the length is appropriate.
	 * @param the String the user included
	 */
	private boolean setFirstName(String name)
	{
		int length = name.length();
		if (!(length >= 2) || !(length <= 20))
		{
			registerErrorMsg.setText(R.string.error_message_name_length);
			return false;
		}
		this.firstName = name;
		return true;
	}

	/**
	 * Sets the email address and puts the user into a network based on the
	 * email address given.
	 * @param inputEmail the email address
	 */
	private boolean setEmailAndNetwork(String inputEmail)
	{
		String emailAddress = inputEmail.toLowerCase(Locale.getDefault());
		// check if actually an email address
		if (!emailAddress.contains("@") || !emailAddress.contains(".edu"))
		{
			registerErrorMsg.setText(R.string.error_message_invalid_edu_email);
			return false;
		}
		// check if its an email from one of the registered schools
		String[] names = getResources().getStringArray(R.array.registered_colleges);
		for (String each : names) 
		{
			int i = emailAddress.indexOf(each);
		    if (i >= 0)
		    {
		    	network = each; // set the correct network
		    	this.email = emailAddress;
		    	return true;
		    }
		}
		registerErrorMsg.setText(R.string.error_message_registered_schools);
		return false;
	}
	
	private boolean setPassword(String pass)
	{
		if (pass.length() >= 5 && pass.length() <= 25)
		{
			this.password = pass;
			return true;
		}
		registerErrorMsg.setText(R.string.error_message_password_length);
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}

	/** 
	 * Deals with registering the user and setting the new user's network.
	 * @author Emily Pakulski
	 *
	 */
	private class RegisterAndNetworkTask extends AsyncTask<String, Void, String> {
	       
        protected String doInBackground(String ... params)
        {
        	String errorSource = null; // by default, there is no error
        	
        	UserFunctions userFunction = new UserFunctions();
            if (params.length != 4)
                errorSource = "Please fill in all the fields.";
            JSONObject json = userFunction.registerUser(params[0], params[1], params[2], params[3]);

	        try 
	        {
	        	if (json != null && json.getString(KEY_SUCCESS) != null)
	            {
	                String res = json.getString(KEY_SUCCESS);
	                if(Integer.parseInt(res) == 1) // user successfully registered
	                {
	                    // Store user details in SQLite Database
	                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
	                    JSONObject json_user = json.getJSONObject("user");
	                    // Clear all previous data in database
	                    userFunction.logoutUser(getApplicationContext());
	                    
	                    db.addUser(json_user.getString(DatabaseHandler.KEY_NAME), 
	                    		json_user.getString(DatabaseHandler.KEY_EMAIL), 
	                    		json_user.getString(DatabaseHandler.KEY_NETWORK), 
	                    		json.getString(DatabaseHandler.KEY_UID), 
	                    		json_user.getString(DatabaseHandler.KEY_CREATED_AT));
	                }
	                else
	                {
//	                    registerErrorMsg.setText(R.string.error_message_registration);
	                }
	            }
	        	else
	        	{
	        		Log.v("Testing", "didn't enter if statement in registrationActivity onPostExecute");
	        	}
	        }
	        catch (JSONException e) 
	        {
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
        	registerErrorMsg.setText(error);
        	
        	// Launch Dashboard Screen
            Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
            // Close all views before launching Dashboard
            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(dashboard);
            finish(); // Close Registration Activity
        }
	}
}
