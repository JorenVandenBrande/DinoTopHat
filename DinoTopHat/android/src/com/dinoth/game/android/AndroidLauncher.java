package com.dinoth.game.android;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.input.GestureDetector;
import com.dinoth.game.ActionResolver;
import com.dinoth.game.DinoTopHat;
import com.dinoth.logic.AndroidControls;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.swarmconnect.Swarm;

public class AndroidLauncher extends AndroidApplication implements ActionResolver {
	
	private Tracker tracker, globalTracker;
	private DinoApplication myApplication;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		myApplication = (DinoApplication) getApplication();
		tracker = myApplication.getTracker(DinoApplication.TrackerName.APP_TRACKER);
		globalTracker = myApplication.getTracker(DinoApplication.TrackerName.GLOBAL_TRACKER);	
		AndroidControls Acont =new AndroidControls();
		initialize(new DinoTopHat(Acont, this, true), config);
		Gdx.input.setInputProcessor(new GestureDetector(Acont));
		// Add this method call
	    Swarm.setActive(this);
	}

	// Add everything below here too
	public void onResume() {
	    super.onResume();
	    Swarm.setActive(this);
	    
	    // Replace MY_APP_ID with your App ID from the Swarm Admin Panel
	    // Replace MY_APP_KEY with your string App Key from the Swarm Admin Panel
	    Swarm.init(this, 14415, "813cab32e79ff2e0a57714bf8d4305c4");
	}

	public void onPause() {
	    super.onPause();
	    Swarm.setInactive(this);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
		// ...
	}
 
	@Override
	public void onStop() {
		super.onStop();
		// ...
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}

	@Override
	public void setTrackerScreenName(String path) {
		// Set screen name.
		// Where path is a String representing the screen name.
		globalTracker.setScreenName(path);
		globalTracker.send(new HitBuilders.AppViewBuilder().build());
	}
}
