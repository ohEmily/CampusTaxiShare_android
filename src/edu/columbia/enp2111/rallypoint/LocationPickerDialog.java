package edu.columbia.enp2111.rallypoint;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

public class LocationPickerDialog {

	private AlertDialog destinationDialog;
	
	public LocationPickerDialog(View v, Context context, TextView aTextView)
	{
		final TextView textView = aTextView;
		
		// Strings to Show In Dialog with Radio Buttons
		final CharSequence[] options = {"John F. Kennedy International Airport", "LaGuardia Airport","Newark Liberty International Airport"};
		
//		NetworkFunctions networkFunction = new NetworkFunctions();
//		final String[] destinationOptions = networkFunction.getDestinations(getApplicationContext());
		
		// Creating and Building the Dialog 
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.destination); 
		builder.setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int item)
			{   
				textView.setText(options[item]);
				destinationDialog.dismiss();    
			}
		});
		destinationDialog = builder.create();
		destinationDialog.show();
	}
}
