package edu.columbia.enp2111.rallypoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ToCampusSearchActivity extends SearchActivity 
{
	Button toCampusButton;
	
	@Override
	public void setDirection()
	{
		direction = KEY_TO_CAMPUS;
	}
	
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

}
