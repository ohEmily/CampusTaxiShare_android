package edu.columbia.enp2111.rallypoint;

import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class RegistrationActivity extends Activity {
	private String email;
	private String firstName;
	private String network;
	private String password;

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
	
	// sources: http://stackoverflow.com/questions/4531396/get-value-of-a-edit-text-field
	// http://developer.android.com/training/basics/firstapp/starting-activity.html
	public void submitRegistrationFields(View view)
	{
		final EditText emailField = (EditText) findViewById(R.id.emailEditTextRegistration);
		final EditText firstNameField = (EditText) findViewById(R.id.nameEditTextRegistration);
		final EditText passField = (EditText) findViewById(R.id.passEditTextRegistration);
		
		setFirstName(firstNameField.getText().toString());
		setEmailAndNetwork(emailField.getText().toString());
		setPassword(passField.getText().toString());
		Log.v("Testing", "Email: " + email);
		Log.v("Testing", "Network: " + network);
		Log.v("Testing", "Name: " + firstName);
		Log.v("Testing", "Password: " + password);
		
		finish(); // pop this activity and go back to login screen
	}
	
	private void setFirstName(String name)
	{
		int length = name.length();
		if (!(length >= 2) || !(length <= 20));
				// TODO error
		this.firstName = name;
	}

	/**
	 * Returns false if the text does not have an "@" or a ".edu" or if the 
	 * email is not from one of the registered universities.
	 * @param text
	 */
	private void setEmailAndNetwork(String inputEmail)
	{
		String emailAddress = inputEmail.toLowerCase(Locale.getDefault());
		// check if actually an email address
		if (!emailAddress.contains("@") || !emailAddress.contains(".edu"));
			// TODO Error Handling ^ notice semicolon after if-statement
		// check if its an email from one of the registered schools
		String[] names = getResources().getStringArray(R.array.registered_colleges);
		for (String each : names) 
		{
			int i = emailAddress.indexOf(each);
		    if (i >= 0)
		    {
		    	network = each; // set the correct network
		    	email = emailAddress;
		    }
		}
		// TODO Error Handling
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
