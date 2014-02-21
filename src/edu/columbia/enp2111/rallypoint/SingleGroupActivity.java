package edu.columbia.enp2111.rallypoint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Takes care of the functionality of a single group activity, shown when a
 *  group is selected from the Search Activity's list of available groups.
 * @author Emily Pakulski
 *
 */

public class SingleGroupActivity extends Activity
{
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_group);
        
        // getting intent data
        Intent previousActivity = getIntent();
        // Get JSON values from previous intent
        String destination = previousActivity.getStringExtra(GroupFunctions.KEY_DESTINATION);
        String datetime = previousActivity.getStringExtra(GroupFunctions.KEY_DATETIME);
        // Displaying all values on the screen
        TextView labelDestination = (TextView) findViewById(R.id.name_label);
        TextView labelDatetime = (TextView) findViewById(R.id.email_label);
        
        labelDestination.setText(destination);
        labelDatetime.setText(datetime);
    }
	
	public void joinGroup(View view)
	{
		
	}
}
