package edu.columbia.enp2111.rallypoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * Shows all the groups available.
 * @author Emily Pakulski
 *
 */

public abstract class SearchActivity extends ListActivity
{
	private ProgressDialog pDialog;
	
	// JSON Node names
	public static final String TAG_GROUPS = "groups";
	public static final String TAG_DESTINATION = "destination";
	public static final String TAG_DATETIME = "datetime";
	public static final String KEY_DIRECTION = "direction";
	
	public static final String LIST_KEY_DESTINATION = "destinationText";
	public static final String LIST_KEY_DATE = "dateText";
	public static final String LIST_KEY_TIME = "timeText";
	
	String stringDate;
	String stringTime;
	
	// whether we're going to campus or from campus
//	protected String direction;
	
	// contacts JSONArray
	JSONArray all_groups = null;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> groupList;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		groupList = new ArrayList<HashMap<String, String>>();

		ListView lv = getListView();
		
		// ListView on item click listener. Called when you click any of the
		// groups displayed -- opens that group's activity
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, 
					int position, long id)
			{
				// getting values from selected ListItem
				String destination = ((TextView) view.findViewById(R.id.destination))
						.getText().toString();
				// separating out the values for date and the values for time
				String datetime = ((TextView) view.findViewById(R.id.date))
						.getText().toString();

				// Starting single group activity
				Intent singleGroup = new Intent(getApplicationContext(),
						SingleGroupActivity.class);
				singleGroup.putExtra(GroupFunctions.KEY_DESTINATION, destination);
				singleGroup.putExtra(GroupFunctions.KEY_DATETIME, datetime);
				startActivity(singleGroup);
			}
		});

		setNewGroupButton();
		
		// Calling async task to get json
		getGroups();
//		new GetGroups().execute();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.when_where, menu);
		return true;
	}
	
	/**
	 * Takes the combined datetime string (format: MM/DD/YYYY HH:SS AM) and
	 * splits up the date and time, setting the corresponding stringDate and
	 * stringTime instance variables.
	 * @param datetime in MM/DD/YYYY HH:SS AM format
	 */
	public void setDateAndTime(String datetime)
	{
		// 10 is the length of a date (MM-DD-YYYY ) so use substring(0,10)
		                            // 12345678910
		String dateSubstring = DateTime.parseDate(datetime.substring(0, 10).trim());
		stringDate = dateSubstring; 
		
		String timeSubstring = datetime.substring(10, datetime.length()).trim();
		stringTime = DateTime.parseTime(timeSubstring);
	}
	
	public abstract void setNewGroupButton();
	public abstract void getGroups();
}