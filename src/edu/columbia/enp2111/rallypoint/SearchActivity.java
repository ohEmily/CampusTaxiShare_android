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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SearchActivity extends ListActivity
{
	private ProgressDialog pDialog;

	// URL to get contacts JSON
	private static String url = Constants.API_URL;

	// JSON Node names
	public static final String TAG_GROUPS = "groups";
	public static final String TAG_DESTINATION = "destination";
	public static final String TAG_DATETIME = "datetime";

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

		// Listview on item click listener
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String destination = ((TextView) view.findViewById(R.id.destination))
						.getText().toString();
				String datetime = ((TextView) view.findViewById(R.id.datetime))
						.getText().toString();

				// Starting single contact activity
				Intent singleContact = new Intent(getApplicationContext(),
						SingleGroupActivity.class);
				singleContact.putExtra(Constants.KEY_DESTINATION, destination);
				singleContact.putExtra(Constants.KEY_DATETIME, datetime);
				startActivity(singleContact);
			}
		});

		// Calling async task to get json
		new GetGroups().execute();
	}

	/**
	 * Async task class to get json by making HTTP call.
	 * */
	private class GetGroups extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(SearchActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void ... arg0)
		{			
			// adding params: telling JSON what type of request it'll be
			List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair(Constants.KEY_TAG, Constants.GET_GROUPS_TAG));
			
			// Creating a JSONParser
			JSONParser jp = new JSONParser();
			JSONObject jsonObj = jp.getJSONFromUrl(url, params);
			
			try
			{
				// Getting JSON Array node
				all_groups = jsonObj.getJSONArray("groups");
				
				// looping through All Contacts
				for (int i = 0; i < all_groups.length(); i++)
				{
					JSONObject aGroup = all_groups.getJSONObject(i);
					
					String destination = aGroup.getString(Constants.KEY_DESTINATION);
					Log.v("Testing", destination);
					String datetime = aGroup.getString(Constants.KEY_DATETIME);
					Log.v("Testing", datetime);
					// One HashMap per group of taxi sharers
					HashMap<String, String> group_object = new HashMap<String, String>();
						// adding each child node to HashMap key => value
					group_object.put(Constants.KEY_DESTINATION, destination);
					group_object.put(Constants.KEY_DATETIME, datetime);
					// adding contact to contact list
					groupList.add(group_object);
				}
			} catch (JSONException e) {
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
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new SimpleAdapter(
					SearchActivity.this, groupList,
					R.layout.list_item, 
					new String[] {Constants.KEY_DESTINATION, Constants.KEY_DATETIME}, 
					new int[] { R.id.destination, R.id.datetime});
			setListAdapter(adapter);
		}

	}

}