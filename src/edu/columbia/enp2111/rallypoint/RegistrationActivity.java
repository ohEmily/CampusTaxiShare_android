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


//handling registration event
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
//    private static String KEY_ERROR = "error";
//    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_NETWORK = "network";
    private static String KEY_CREATED_AT = "created_at";

    // Activity page elements
    TextView registerErrorMsg;
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		Log.v("Testing", "opened registration activity");
		
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
	
	// sources: http://stackoverflow.com/questions/4531396/get-value-of-a-edit-text-field
	// http://developer.android.com/training/basics/firstapp/starting-activity.html
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
	        // testing
	        Log.v("Testing", "Email: " + email);
			Log.v("Testing", "Name: " + firstName);
			Log.v("Testing", "Network: " + network); 
			Log.v("Testing", "Password: " + password);
			new MyAsyncTask().execute(firstName, email, network, password);
		}
	}
	
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

//	from http://stackoverflow.com/questions/13136539/caused-by-android-os-networkonmainthreadexception
//	http://stackoverflow.com/questions/6343166/android-os-networkonmainthreadexception
//	http://pastebin.com/TbdAwS5g
	private class MyAsyncTask extends AsyncTask<String, Void, JSONObject> {
	       
        protected JSONObject doInBackground(String ... params)
        {
                UserFunctions userFunction = new UserFunctions();
                if (params.length != 4)
                        return null;
                JSONObject json = userFunction.registerUser(params[0], params[1], params[2], params[3]);
                return json;
        }
       
        protected void onPostExecute(JSONObject json)
        {
	        try 
	        {
	        	if (json != null && json.getString(KEY_SUCCESS) != null)
	            {
	                Log.v("Testing", "onPostExecute: json != null");
	        		registerErrorMsg.setText("");
	                String res = json.getString(KEY_SUCCESS);
	                if(Integer.parseInt(res) == 1) // user successfully registered
	                {
	                    // Store user details in SQLite Database
	                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
	                    JSONObject json_user = json.getJSONObject("user");
	                    // Clear all previous data in database
	                    UserFunctions userFunction = new UserFunctions();
	                    userFunction.logoutUser(getApplicationContext());
	                    db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json_user.getString(KEY_NETWORK), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));                  
	                    // Launch Dashboard Screen
	                    Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
	                    // Close all views before launching Dashboard
	                    dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                    startActivity(dashboard);
	                    finish(); // Close Registration Activity
	                }
	                else
	                {
	                    registerErrorMsg.setText(R.string.error_message_registration);
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
        }
	}
}
