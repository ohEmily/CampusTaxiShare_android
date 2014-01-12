package edu.columbia.enp2111.rallypoint;

import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * Creates a dialog that allows the user to select a date, with the default 
 * date being today.
 * From here: http://developer.android.com/guide/topics/ui/controls/pickers.html
 */

// TODO restrict selectable dates to up to 60 days in advance?

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
	private TextView departureDateView;
	private String date;
	
	public DatePickerFragment() {}
	
	public DatePickerFragment(TextView departureDate)
	{
		this.departureDateView = departureDate;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int todaysYear = c.get(Calendar.YEAR);
		int todaysMonth = c.get(Calendar.MONTH);
		int todaysDay = c.get(Calendar.DAY_OF_MONTH);

//		// Create a new instance of DatePickerDialog and return it
		DatePickerDialog dialog = new DatePickerDialog(getActivity(), 
				this, todaysYear, todaysMonth, todaysDay);
		dialog.setTitle(R.string.button_date);
		return dialog;
	}
	
	public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,
			int selectedDay)
	{
		// format the date to MM-DD-YY
		Calendar selectedDate = Calendar.getInstance();
		selectedDate.set(selectedYear, selectedMonth, selectedDay);
		String month = makeTwoDigitLongString((selectedDate.get(Calendar.MONTH) + 1));
		String dayOfMonth = makeTwoDigitLongString(selectedDate.get(Calendar.DAY_OF_MONTH));
		date = month + "-" + dayOfMonth + "-" + selectedDate.get(Calendar.YEAR);
		
		// make the date visible to the user by setting the TextView contents
		departureDateView.setText(date);
	}
	
	/** Formats numbers to a String that is always two digits long by adding
	 * a 0 if the number if less than 10. */
	private String makeTwoDigitLongString(int dayOrMonth)
	{
		if (dayOrMonth < 10)
			return "0" + Integer.toString(dayOrMonth);
		return Integer.toString(dayOrMonth);
	}
	
	/** Return the date in MM-DD-YY format. */
	public String getDate()
	{
		return date;
	}
}