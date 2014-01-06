package edu.columbia.enp2111.rallypoint;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

/**
 * From here: http://developer.android.com/guide/topics/ui/controls/pickers.html
 */

//TODO restrict selectable dates to up to 60 days in advance?

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
	private int year;
	private int month;
	private int day;
	
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
		dialog.setTitle(R.string.prompt_date);
		return dialog;
	}
	
	public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,
			int selectedDay)
	{
		this.year = selectedYear;
		this.month = selectedMonth;
		this.day = selectedDay;
	}
}