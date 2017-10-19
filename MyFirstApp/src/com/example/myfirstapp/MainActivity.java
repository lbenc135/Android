package com.example.myfirstapp;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId())
    	{
    	case R.id.action_search:
    		
    		return true;
    	case R.id.action_settings:
    		
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    public void sendMessage (View view)
    {
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	EditText editText = (EditText) findViewById(R.id.edit_message);
    	String message = editText.getText().toString();
    	intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);
    }
    
    public void ViewMap (View view)
    {
    	Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
		Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
    	PackageManager packageManager = getPackageManager();
    	List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
    	if(activities.size()>0)
    		startActivity(mapIntent);
    	else
    	{
    		Intent intent = new Intent(this, DisplayMessageActivity.class);
    		String message = "Sorry, no app to view the map";
    		intent.putExtra(EXTRA_MESSAGE, message);
    		startActivity(intent);
    	}
    }
    
    public void ShareApp (View view)
    {
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setData(Uri.parse("market://details?id=com.licoforen.alientalk"));
    	Intent chooser = Intent.createChooser(intent, "Choose an app");
    	if (intent.resolveActivity(getPackageManager()) != null) {
    	    startActivity(chooser);
    	}
    }
    
}
