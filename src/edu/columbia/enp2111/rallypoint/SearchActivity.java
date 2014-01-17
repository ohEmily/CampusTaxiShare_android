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
	private static String url = "http://10.0.2.2/contacts";

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
				String name = ((TextView) view.findViewById(R.id.name))
						.getText().toString();
				String cost = ((TextView) view.findViewById(R.id.email))
						.getText().toString();

				// Starting single contact activity
				Intent in = new Intent(getApplicationContext(),
						SingleContactActivity.class);
				in.putExtra(TAG_DESTINATION, name);
				in.putExtra(TAG_DATETIME, cost);
				startActivity(in);
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
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			
			// adding params: telling JSON what type of request it'll be
			List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair(Constants.KEY_TAG, Constants.GET_GROUPS_TAG));
			
			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET, params);
			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr); // TODO, why does this work?
					
					Log.v("Testing", "line 111");
					// Getting JSON Array node
					all_groups = jsonObj.getJSONArray(TAG_GROUPS);
					Log.v("Testing", "line 114");
					// looping through All Contacts
					for (int i = 0; i < all_groups.length(); i++)
					{
						JSONObject aGroup = all_groups.getJSONObject(i);
						
						String name = aGroup.getString(TAG_DESTINATION);
						String email = aGroup.getString(TAG_DATETIME);

						// One HashMap per group of taxi sharers
						HashMap<String, String> group_object = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						group_object.put(TAG_DESTINATION, name);
						group_object.put(TAG_DATETIME, email);

						// adding contact to contact list
						groupList.add(group_object);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
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
					R.layout.list_item, new String[] { TAG_DESTINATION, TAG_DESTINATION }, new int[] { R.id.name,
							R.id.email });
			setListAdapter(adapter);
		}

	}

}