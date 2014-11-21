package com.dinoth.game.android;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.input.GestureDetector;
import com.dinoth.game.ActionResolver;
import com.dinoth.game.DinoTopHat;
import com.dinoth.logic.AndroidControls;
import com.dinoth.logic.SimpleDirectionGestureDetector;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.GoogleAnalytics;

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
		initialize(new DinoTopHat(Acont, this), config);
		Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {

			@Override
			public void onUp() {

			}

			@Override
			public void onRight() {

			}

			@Override
			public void onLeft() {

			}

			@Override
			public void onDown() {

			}
			}));
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
