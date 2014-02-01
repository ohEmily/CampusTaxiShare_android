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

// TODO restrict selectable dates to today and later
// TODO restrict selectable dates to up to 60 days in advance?

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
	private TextView departureDateView;
	private String numberDate;
	private String humanReadableDate;
	
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
		dialog.setTitle(R.string.dialog_title_date);
		return dialog;
	}
	
	/**
	 * Converts date in "MM-DD-YYYY" format to "MONTH DD, YYYY" (e.g. from
	 * "01-15-2014" to "January 15, 2014").
	 * @param original time, with the hours out of 24
	 * @return new time, with HH always <= 12 and AM or PM designated
	 */
	public static String convertToHumanDate(int originalYear, int originalMonth, int originalDay)
	{
		String prettyDate = ""; // prettyDate is return value
		// turn month into full word (e.g. "03" to "March")
		if (originalMonth == 0)
			prettyDate += "January";
		else if (originalMonth == 1)
			prettyDate += "February";
		else if (originalMonth == 2)
			prettyDate += "March";
		else if (originalMonth == 3)
			prettyDate += "April";
		else if (originalMonth == 4)
			prettyDate += "May";
		else if (originalMonth == 5)
			prettyDate += "June";
		else if (originalMonth == 6)
			prettyDate += "July";
		else if (originalMonth == 7)
			prettyDate += "August";
		else if (originalMonth == 8)
			prettyDate += "September";
		else if (originalMonth == 9)
			prettyDate += "October";
		else if (originalMonth == 10)
			prettyDate += "November";
		else if (originalMonth == 11)
			prettyDate += "December";
		prettyDate += " " + originalDay + ", " + originalYear;
		return prettyDate;
	}
	
	/** Set the  */
	public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,
			int selectedDay)
	{
		// set the number date, MM-DD-YY
		Calendar selectedDate = Calendar.getInstance();
		selectedDate.set(selectedYear, selectedMonth, selectedDay);
		String month = makeTwoDigitLongString((selectedDate.get(Calendar.MONTH) + 1));
		String dayOfMonth = makeTwoDigitLongString(selectedDate.get(Calendar.DAY_OF_MONTH));
		numberDate = month + "-" + dayOfMonth + "-" + selectedDate.get(Calendar.YEAR);
		// make the date visible to the user by setting the TextView contents
		humanReadableDate = convertToHumanDate(selectedYear, selectedMonth, selectedDay);
		departureDateView.setText(humanReadableDate);
	}
	
	/** Formats numbers to a String that is always two digits long by adding
	 * a 0 if the number if less than 10. */
	private String makeTwoDigitLongString(int dayOrMonth)
	{
		if (dayOrMonth < 10)
			return "0" + Integer.toString(dayOrMonth);
		return Integer.toString(dayOrMonth);
	}
	
	/** Return the date in datetime format, i.e. MM-DD-YYYY. */
	public String getDate()
	{
		return numberDate;
	}
}