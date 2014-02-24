package edu.columbia.enp2111.rallypoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class ToCampusSearchActivity extends SearchActivity 
{
	private ProgressDialog pDialog;
	Button toCampusButton;
	
//	@Override
//	public void setDirection()
//	{
//		direction = KEY_TO_CAMPUS;
//	}
	
	@Override
	public void setNewGroupButton()
	{
		View thisView = getWindow().getDecorView();
		toCampusButton = (Button)thisView.findViewById(R.id.createNewGroupButton);
		toCampusButton.setText(getString(R.string.new_to_campus_group));
		
		toCampusButton.setOnClickListener(new View.OnClickListener()
		{
		    @Override
		    public void onClick(View v)
		    {
		    	ToCampusSearchActivity.this.startActivity(new Intent(ToCampusSearchActivity.this, 
		    			ToCampusNewGroupActivity.class));
		    }
		});
	}
	
	public void getGroups()
	{
		new GetGroups().execute();
	}

	/**
	 * AsyncTask class to get json by making HTTP call.
	 * */
	private class GetGroups extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(ToCampusSearchActivity.this);
			pDialog.setMessage("Getting groups...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void ... arg0)
		{			
			// adding params: telling JSON what type of request it'll be
			List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair(JSONParser.KEY_TAG, "get_to_campus_groups"));
	        
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
					String startPoint = aGroup.getString(GroupFunctions.KEY_START_LOCATION);
					String ownerEmail = aGroup.getString(GroupFunctions.KEY_OWNER_EMAIL);
					setDateAndTime(datetime);
					
					Log.v("Testing", "Start point: " + startPoint);
					
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
					ToCampusSearchActivity.this, groupList,
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
