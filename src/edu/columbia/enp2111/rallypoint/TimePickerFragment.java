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
	
	// default times when you open the TimePicker dialog
	private static final int DEFAULT_HOUR = 12;
	private static final int DEFAULT_MINUTE = 0;
	
	private String militaryHour;
	private String militaryMinute;
	
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

	/**
	 * Converts time in a 24 hour format to "HH:MM AM/PM".
	 * @param militaryHour, hours out of 24
	 * @param oneDigitMinutes, minutes are only 1 digit if less than 10
	 * @return new time, with hours always <= 12 and AM or PM designated
	 */
	public static String convertToPrettyTime(int militaryHour, int oneDigitMinutes)
	{
		// set hour to non-military time
		String stringHour = Integer.toString(militaryHour);
		String AMorPM = "AM";
		if (militaryHour > 12)
			stringHour = Integer.toString(militaryHour - 12);
		if (militaryHour >= 12)
			AMorPM = "PM";
		// make sure minutes are always 2 digits (eg. not 12:1 PM, but 12:01 PM)
		String stringMinute  = Integer.toString(oneDigitMinutes);
		if (oneDigitMinutes < 10)
			stringMinute = 0 + stringMinute; // string addition
		return (stringHour + ":" + stringMinute + " " + AMorPM);
	}
	
	
	/** Sets the values for time on the relevant TextView. */
	public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour)
	{
		// set the 24-hour, military time
		this.militaryHour = Integer.toString(hourOfDay);
		this.militaryMinute = Integer.toString(minuteOfHour);
		if (minuteOfHour < 10)
			this.militaryMinute = 0 + this.militaryMinute;
		// set full 'pretty' time, i.e. 2:45 PM
		departureTimeView.setText(convertToPrettyTime(hourOfDay, minuteOfHour));
	}
	
	/** Returns the time in HH:SS format. */
	public String getTime()
	{
		return militaryHour + ":" + militaryMinute;
	}
}