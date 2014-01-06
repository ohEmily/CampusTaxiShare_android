package edu.columbia.enp2111.rallypoint;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

/**
 * From here: http://developer.android.com/guide/topics/ui/controls/pickers.html
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener 
{	
	private int hour;
	private int minute;
	
	// TODO: restrict time picker to 15 minute intervals
//	private static final int TIME_PICKER_INTERVAL = 15;
	
	// TODO set time picker title
	
	private static final int DEFAULT_HOUR = 12;
	private static final int DEFAULT_MINUTE = 0;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, DEFAULT_HOUR, 
				DEFAULT_MINUTE,	DateFormat.is24HourFormat(getActivity()));
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour)
	{
		this.hour = hourOfDay;
		this.minute = minuteOfHour;
	}
	
	public int getHour()
	{
		return hour;
	}

	public int getMinute()
	{
		return minute;
	}
}