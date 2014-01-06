package edu.columbia.enp2111.rallypoint;

import java.util.Locale;

import android.os.Bundle;
import android.os.StrictMode;
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
	private String network;
	private String password;
	
    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		Log.v("Testing", "opened registration activity");
		
		
		// from http://stackoverflow.com/questions/13136539/caused-by-android-os-networkonmainthreadexception
		// TODO
		// http://stackoverflow.com/questions/6343166/android-os-networkonmainthreadexception
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
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
		Log.v("Testing", "clicked button to submit registration fields");
		final EditText emailField = (EditText) findViewById(R.id.emailEditTextRegistration);
		final EditText firstNameField = (EditText) findViewById(R.id.nameEditTextRegistration);
		final EditText passField = (EditText) findViewById(R.id.passEditTextRegistration);
		final TextView registerErrorMsg = (TextView) findViewById(R.id.registration_error);
		
		setFirstName(firstNameField.getText().toString());
		setEmailAndNetwork(emailField.getText().toString());
		setPassword(passField.getText().toString());
		
        // testing
        Log.v("Testing", "Email: " + email);
//		Log.v("Testing", "Network: " + network);
		Log.v("Testing", "Name: " + firstName);
		Log.v("Testing", "Password: " + password);
		
		Log.v("Testing", "Creating UserFuctions and json objects.");
		UserFunctions userFunction = new UserFunctions();
        JSONObject json = userFunction.registerUser(this.firstName, this.email, this.password);
		
        // check for login response
        try
        {
            if (json.getString(KEY_SUCCESS) != null)
            {
            	registerErrorMsg.setText("");
            	String res = json.getString(KEY_SUCCESS);
            	if (Integer.parseInt(res) == 1)
            	{
            	// user successfully registered
	                // Store user details in SQLite Database
	            	DatabaseHandler db = new DatabaseHandler(getApplicationContext());
	            	JSONObject json_user = json.getJSONObject("user");
	                     
	            	// Clear all previous data in database
	            	userFunction.logoutUser(getApplicationContext());
	                db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), 
	                		json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));                       
	                // Launch Dashboard Screen
	                Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
	                // Close all views before launching Dashboard
	                dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                startActivity(dashboard);
	
	                finish(); // close registration screen
                }
            	else
            	{
            		registerErrorMsg.setText("Error occured in registration"); // error in registration
            	}
            }
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
	
	private void setFirstName(String name)
	{
//		int length = name.length();
//		if (!(length >= 2) || !(length <= 20));
//				// TODO error
		this.firstName = name;
	}

	/**
	 * Sets the email address and puts the user into a network based on the
	 * email address given.
	 * @param inputEmail the email address
	 */
	private void setEmailAndNetwork(String inputEmail)
	{
		String emailAddress = inputEmail.toLowerCase(Locale.getDefault());
//		// check if actually an email address
//		if (!emailAddress.contains("@") || !emailAddress.contains(".edu"));
//			// TODO Error Handling ^ notice semicolon after if-statement
//		// check if its an email from one of the registered schools
//		String[] names = getResources().getStringArray(R.array.registered_colleges);
//		for (String each : names) 
//		{
//			int i = emailAddress.indexOf(each);
//		    if (i >= 0)
//		    {
//		    	network = each; // set the correct network
		    	this.email = emailAddress;
//		    }
//		}
//		// TODO Error Handling
	}
	
	private void setPassword(String pass)
	{
		this.password = pass; // TODO error checking
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
