package edu.columbia.enp2111.rallypoint;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class AccountConfirmationActivity extends Activity {

//	public static final String EMAIL_ADDRESS = edu.columbia.enp2111.rallyPoint.email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_confirmation);
		
		// get email from previous activity
		// http://developer.android.com/training/basics/firstapp/starting-activity.html
		Intent intent = getIntent();
//		String message = intent.getStringExtra(HomeActivity.EMAIL_ADDRESS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account_confirmation, menu);
		return true;
	}

}
