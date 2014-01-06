package edu.columbia.enp2111.rallypoint;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

// handling login event
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Logging in the user activity. Connects to database.
 * @author Emily Pakulski
 * @author Ravi Tamada, androidhive.info for the database connection stuff
 */

public class LoginActivity extends Activity {
	private String email;
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		final TextView toRegistration = (TextView) this.findViewById(R.id.link_to_registration);
		toRegistration.setOnClickListener(new OnClickListener() 
		{
	        @Override
	        public void onClick(View v)
	        {
	        	LoginActivity.this.startActivity(new Intent(LoginActivity.this, 
	        			RegistrationActivity.class));
	        }
	    });
	}

	public void submitLoginFields(View view) 
	{
		Log.v("Testing", "submitLoginFields method");
		final EditText emailField = (EditText) findViewById(R.id.emailEditTextLogin);
		final EditText passwordField = (EditText) findViewById(R.id.passEditTextLogin);
		final TextView loginErrorMsg = (TextView) findViewById(R.id.login_error);
		
		email = emailField.getText().toString();
		password = passwordField.getText().toString();
        UserFunctions userFunction = new UserFunctions();
        JSONObject json = userFunction.loginUser(email, password);
        
		Log.v("Testing", "Login email: " + email);
		Log.v("Testing", "Login Password: " + password);
		
        try // check for login response
        {
            if (json.getString(KEY_SUCCESS) != null) 
            {
                loginErrorMsg.setText("");
                String res = json.getString(KEY_SUCCESS);
                if(Integer.parseInt(res) == 1)
                {
                    // user successfully logged in
                    // Store user details in SQLite Database
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    JSONObject json_user = json.getJSONObject("user");
                     
                    // Clear all previous data in database
                    userFunction.logoutUser(getApplicationContext());
                    db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), 
                    		json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));                       
                     
                    // Launch dashboard screen
                    Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
                     
                    // Close all views before launching dashboard
                    dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(dashboard);

                    finish(); // Close login screen to return to dashboard
                }
                else // Error in login
                {
                    loginErrorMsg.setText("Incorrect email/password.");
                }
            }
        }
        catch (JSONException e)
        {
        	e.printStackTrace();
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
}
