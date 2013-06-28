package com.example.arithmeticaal;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;


public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
	}

    public void startPlayer1(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), Player1Activity.class); 
    	startActivity(intent);
    }

    public void startPlayer2(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), Player2Activity.class); 
    	startActivity(intent);
    }
    
    public void startSettings(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), SettingsActivity.class); 
    	startActivity(intent);
    }
    
    public void startAbout(View v)
    {
    	Intent intent = new Intent(getApplicationContext(), AboutActivity.class); 
    	startActivity(intent);
    }
}
