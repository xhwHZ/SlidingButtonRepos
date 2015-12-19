package com.xhw.togglebutton;

import com.xhw.togglebutton.view.ToggleButton;
import com.xhw.togglebutton.view.ToggleButton.OnToggleStateChangeListener;
import com.xhw.togglebutton.view.ToggleButton.ToggleState;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ToggleButton toggleBtn=(ToggleButton) this.findViewById(R.id.toggleBtn);
		toggleBtn.setSlideBgResource(R.drawable.slide_button_background);
		toggleBtn.setSwitchBgResource(R.drawable.switch_background);
		toggleBtn.setToggleState(ToggleState.Close);
		toggleBtn.setOnToggleStateChangeListener(new OnToggleStateChangeListener() {
			
			@Override
			public void onToggleStateChange(ToggleState state)
			{
				String stateStr=state==ToggleState.Open?"开关打开":"开关关闭";
				Toast.makeText(MainActivity.this, stateStr, 0).show();
			}
		});
		
	}

	
}
