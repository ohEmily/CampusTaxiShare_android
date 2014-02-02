package edu.columbia.enp2111.rallypoint;

import java.util.Calendar;

/**
 * Handles date and time values, as well as parsing datetime values from
 * the database. Also allows translating one form of date or time to
 * the other.
 * @author Emily Pakulski
 */

public class DateTime
{
	private String numberDate;
	private String militaryTime;
	
	public DateTime() {}

	public void parseDateTime(String datetime)
	{
		String stringDate = datetime.substring(0, 10).trim(); 
		String stringTime = datetime.substring(10, datetime.length()).trim();
	}
	
	/** Parses number date of form MM/DD/YYYY */
	public static String parseDate(String dateString)
	{
		String[] dateArray = dateString.split("(?!\")\\p{Punct}");
		int originalMonth = Integer.parseInt(dateArray[0]);
		int originalDay = Integer.parseInt(dateArray[1]);
		int originalYear = Integer.parseInt(dateArray[2]);
		
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
	
	/** Parse time in form HH:MM to form HH:MM AM/PM */
	public static String parseTime(String timeString)
	{
		String[] timeArray = timeString.split("(?!\")\\p{Punct}");
		int militaryHour = Integer.parseInt(timeArray[0]);
		int oneDigitMinutes = Integer.parseInt(timeArray[1]);
		
		return parseTime(militaryHour, oneDigitMinutes);
	}
	
	public static String parseTime(int militaryHour, int oneDigitMinutes)
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