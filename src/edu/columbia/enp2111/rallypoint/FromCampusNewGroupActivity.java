package edu.columbia.enp2111.rallypoint;

import android.os.Bundle;
import android.view.View;

public class FromCampusNewGroupActivity extends NewGroupActivity
{	
	/**
	 * Sets the direction -- either to or from campus -- for the new cab
	 * share group.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		super.direction = KEY_FROM_CAMPUS;
	}
	
	/**
	 * Called when the "choose destination" button is pressed; opens the dialog to 
	 * choose a time without changing activity. 
	 */
	public void showDestinationPickerDialog(View v)
	{
		NetworkFunctions networkFunction = new NetworkFunctions();
		final String[] destinationOptions = networkFunction.getNonCampusPlaces(getApplicationContext());
		
		showLocationPickerDialog(v, destinationOptions);
	}
	
	/**
	 * Called when the "choose starting location" button is pressed; opens the dialog to 
	 * choose a destination without changing activity. 
	 */
	public void showStartLocationPickerDialog(View v)
	{
		NetworkFunctions networkFunction = new NetworkFunctions();
		final String[] destinationOptions = networkFunction.getCampusPlaces(getApplicationContext());
		
		showLocationPickerDialog(v, destinationOptions);
	}
}
