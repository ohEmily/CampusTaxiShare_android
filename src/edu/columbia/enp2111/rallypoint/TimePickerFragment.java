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
	
	private int hour;
	private int minute;
	
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

	public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour)
	{
		this.hour = hourOfDay;
		this.minute = minuteOfHour;
		String hourString = Integer.toString(hourOfDay);
		if (hourOfDay > 12)
			hourString = Integer.toString(hourOfDay - 12);
		String minuteString = Integer.toString(minuteOfHour);
		if (minuteOfHour < 10)
			minuteString = 0 + Integer.toString(minuteOfHour); 
		String AMorPM = "AM";
		if (hourOfDay >= 12)
			AMorPM = "PM";
		String time = (hourString + ":" + minuteString + " " + AMorPM);
		departureTimeView.setText(time);
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