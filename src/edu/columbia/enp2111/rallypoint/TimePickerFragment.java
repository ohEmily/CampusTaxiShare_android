package edu.columbia.enp2111.rallypoint;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * From here: http://developer.android.com/guide/topics/ui/controls/pickers.html
 */

@SuppressLint("ValidFragment")
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener 
{	
	private TextView departureTimeView;
	
	private static final int DEFAULT_HOUR = 12;
	private static final int DEFAULT_MINUTE = 0;
	
	private String hour;
	private String minute;
	
	// TODO: restrict time picker to 15 minute intervals
//	private static final int TIME_PICKER_INTERVAL = 15;
	// TODO set time picker title
	
	public TimePickerFragment() {}
	
	public TimePickerFragment(TextView timeView)
	{
		this.departureTimeView = timeView;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, DEFAULT_HOUR, 
				DEFAULT_MINUTE,	DateFormat.is24HourFormat(getActivity()));
	}

	/** Sets the values for time on the relevant TextView. */
	public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour)
	{
		hour = Integer.toString(hourOfDay);
		// sets hourString to non-military time (e.g. 2 o clock instead of 14 o clock)
		String hourString = hour;
		String AMorPM = "AM";
		if (hourOfDay > 12)
			hourString = Integer.toString(hourOfDay - 12);
		if (hourOfDay >= 12)
			AMorPM = "PM";
		minute = Integer.toString(minuteOfHour);
		if (minuteOfHour < 10)
			minute = 0 + minute; 
		String time = (hourString + ":" + minute + " " + AMorPM);
		departureTimeView.setText(time);
	}
	
	/** Returns the time in HH:SS format. */
	public String getTime()
	{
		return hour + ":" + minute;
	}
}