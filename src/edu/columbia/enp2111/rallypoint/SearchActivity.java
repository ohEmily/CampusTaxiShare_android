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

public class SearchActivity extends ListActivity
{
	private ProgressDialog pDialog;
	
	// JSON Node names
	public static final String TAG_GROUPS = "groups";
	public static final String TAG_DESTINATION = "destination";
	public static final String TAG_DATETIME = "datetime";
	
	public static final String LIST_KEY_DESTINATION = "destinationText";
	public static final String LIST_KEY_DATE = "dateText";
	public static final String LIST_KEY_TIME = "timeText";
	
	String stringDate;
	String stringTime;
	
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
		
		// Listview on item click listener. Called when you click any of the
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

		// Calling async task to get json
		new GetGroups().execute();
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
	
	/** Called when the relevant button is pressed: creates a new taxi share
	 * group going from campus. */
	public void createNewGroup(View v)
	{
    	SearchActivity.this.startActivity(new Intent(SearchActivity.this, 
    			NewGroupActivity.class));
	}

	/**
	 * AsyncTask class to get json by making HTTP call.
	 * */
	private class GetGroups extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(SearchActivity.this);
			pDialog.setMessage("Getting groups...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void ... arg0)
		{			
			// adding params: telling JSON what type of request it'll be
			List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair(JSONParser.KEY_TAG, GroupFunctions.TAG_GET_GROUPS));
			
			// Creating a JSONParser
			JSONParser jp = new JSONParser();
			JSONObject jsonObj = jp.getJSONFromUrl(JSONParser.API_URL, params);
			
			try
			{
				// Getting JSON Array node
				all_groups = jsonObj.getJSONArray("groups");
				
				// looping through All Contacts
				for (int i = 0; i < all_groups.length(); i++)
				{
					JSONObject aGroup = all_groups.getJSONObject(i);
					
					String destination = aGroup.getString(GroupFunctions.KEY_DESTINATION);
					String datetime = aGroup.getString(GroupFunctions.KEY_DATETIME);
					String ownerEmail = aGroup.getString(GroupFunctions.KEY_OWNER_EMAIL);
					setDateAndTime(datetime);
					
					// One HashMap per group of taxi sharers
					HashMap<String, String> group_object = new HashMap<String, String>();
					// adding each child node to HashMap (key => value)
					group_object.put(LIST_KEY_DESTINATION, destination);
					group_object.put(LIST_KEY_DATE, stringDate);
					group_object.put(LIST_KEY_TIME, stringTime);
					group_object.put(GroupFunctions.KEY_OWNER_EMAIL, ownerEmail);
					// adding group to group list
					groupList.add(group_object);
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();

			/**
			 * Updating parsed JSON data into ListView.
			 * */
			ListAdapter adapter = new SimpleAdapter(
					SearchActivity.this, groupList,
					R.layout.list_item, 
					new String[] {LIST_KEY_DESTINATION, 
							LIST_KEY_DATE,
							LIST_KEY_TIME }, 
					new int[] { R.id.destination, 
							R.id.date,
							R.id.time });
			setListAdapter(adapter);	
		}
	}
}