package com.stent.garagedooropener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class GdoMainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "com.stent.gdo.MESSAGE";
	public final static String GDO_HOSTNAME = "192.168.1.252";   // Raspberry PI #2
	public final static String GDO_PORT = "7654";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdo_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_gdo_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
    	Intent intent = new Intent(this, GdoSettingsActivity.class);
    	startActivity(intent);
        return true;
    }
    
    
    public void sendLeftDoorMessage(View view) {
    	// Do something in response to button press
    	Intent intent = new Intent(this, DoorUpDown.class);
    	String message = "Left";
    	intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);
    	    }
    
    public void sendRightDoorMessage(View view) {
    	// Do something in response to button press
    	Intent intent = new Intent(this, DoorUpDown.class);
    	String message = "Right";
    	intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);
    	    }
}
