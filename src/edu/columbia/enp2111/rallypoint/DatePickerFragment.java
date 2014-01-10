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
 * From here: http://developer.android.com/guide/topics/ui/controls/pickers.html
 */

//TODO restrict selectable dates to up to 60 days in advance?

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
	private TextView departureDateView;
	private int day;
	private int month;
	private int year;
	
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
		DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, todaysYear, todaysMonth, 
				todaysDay);
		dialog.setTitle(R.string.button_date);
		return dialog;
	}
	
	public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,
			int selectedDay)
	{
		Calendar selectedDate = Calendar.getInstance();
		selectedDate.set(selectedYear, selectedMonth, selectedDay);
		String date = (selectedDate.get(Calendar.MONTH) + 1) + "-" 
				+ selectedDate.get(Calendar.DAY_OF_MONTH) + "-" + selectedDate.get(Calendar.YEAR);
		departureDateView.setText(date);
	}
	
	public int getDay() 
	{
		return day;		
	}
	
	public int getMonth()
	{
		return month;
	}
	
	public int getYear()
	{
		return year;
	}
}