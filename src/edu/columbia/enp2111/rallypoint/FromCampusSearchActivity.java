package edu.columbia.enp2111.rallypoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FromCampusSearchActivity extends SearchActivity 
{
	Button fromCampusButton;

	@Override
	public void setDirection()
	{
		direction = KEY_FROM_CAMPUS;
	}
	
	@Override
	public void setNewGroupButton()
	{
		fromCampusButton = (Button) getWindow().getDecorView().findViewById(R.id.createNewGroupButton);
		fromCampusButton.setText(getString(R.string.new_from_campus_group));
		
		fromCampusButton.setOnClickListener(new View.OnClickListener()
		{
		    @Override
		    public void onClick(View v)
		    {
		    	FromCampusSearchActivity.this.startActivity(new Intent(FromCampusSearchActivity.this, 
		    			FromCampusNewGroupActivity.class));
		    }
		});
	}

}
