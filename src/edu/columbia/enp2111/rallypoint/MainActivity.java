package edu.columbia.enp2111.rallypoint;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// learned this here:
	// http://developer.android.com/guide/topics/ui/controls/radiobutton.html
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_destination:
	            if (checked)
	                // select a destination
	            	// JFK, LaGuardia, Newark
	            break;
	        case R.id.radio_date_time:
	            if (checked)
	                // select a date and time
	            break;
	        case R.id.radio_rally_point:
	        	if (checked)
	        		// select a rally point
	        	break;
	    }
	}
}
