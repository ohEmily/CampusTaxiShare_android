package edu.columbia.enp2111.rallypoint;

import java.util.Calendar;

/**
 * Handles date and time values, as well as parsing datetime values from
 * the database.
 * 
 * @author Emily Pakulski
 *
 */

public class DateTime
{
	private String numberDate;
	private String militaryTime;
	private String humanDate;
	private String humanTime;
	
	public DateTime() {}

	public void parseDate(String dateString)
	{
		
	}
	
	public void parseTime(String dateString)
	{
		
	}
	
	public String getNumberDate()
	{
		return numberDate;
	}

	public static String createNumberDate(int year, int month, int day)
	{
		Calendar selectedDate = Calendar.getInstance();
		selectedDate.set(year, month, day);
		String stringMonth = makeTwoDigitLongString((selectedDate.get(Calendar.MONTH) + 1));
		String dayOfMonth = makeTwoDigitLongString(selectedDate.get(Calendar.DAY_OF_MONTH));
		return stringMonth + "-" + dayOfMonth + "-" + selectedDate.get(Calendar.YEAR);
	}
	
	/** Formats numbers to a String that is always two digits long by adding
	 * a 0 if the number if less than 10. */
	private static String makeTwoDigitLongString(int dayOrMonth)
	{
		if (dayOrMonth < 10)
			return "0" + Integer.toString(dayOrMonth);
		return Integer.toString(dayOrMonth);
	}
	
//	public String createMilitaryTime(int hour, int minute)
//	{
//		
//	}
	
	public void setNumberDate(String date)
	{
		this.numberDate = date;
	}

	public String getTime()
	{
		return militaryTime;
	}

	public void setTime(String time)
	{
		this.militaryTime = time;
	}
	
	
}