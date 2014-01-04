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

public class LoginActivity extends Activity {
	private String email;
	private String password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		final TextView toRegistration = (TextView) this.findViewById(R.id.link_to_registration);
		toRegistration.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	LoginActivity.this.startActivity(new Intent(LoginActivity.this, 
	        			RegistrationActivity.class));
	        }
	    });
	}

	public void submitRegistrationFields(View view) {
		final EditText emailField = (EditText) findViewById(R.id.emailEditTextLogin);
		final EditText passwordField = (EditText) findViewById(R.id.passEditTextLogin);
		
		email = emailField.getText().toString();
		password = passwordField.getText().toString();
		Log.v("Testing", "Login email: " + email);
		Log.v("Testing", "Login Password: " + password);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
