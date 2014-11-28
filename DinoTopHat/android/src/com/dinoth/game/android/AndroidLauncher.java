package com.dinoth.game.android;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.input.GestureDetector;
import com.dinoth.game.ActionResolver;
import com.dinoth.game.DinoTopHat;
import com.dinoth.logic.AndroidControls;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements;
import com.google.android.gms.games.achievement.Achievements.UpdateAchievementResult;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

public class AndroidLauncher extends AndroidApplication implements GameHelperListener, ActionResolver {
	
	private Tracker tracker, globalTracker;
	private DinoApplication myApplication;
	private GameHelper gameHelper;
	
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
		if (gameHelper == null) {
			gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
			gameHelper.enableDebugLog(true);
		}
		InputMultiplexer imp = new InputMultiplexer();
		imp.addProcessor(Acont);
		imp.addProcessor(new GestureDetector(Acont));
		Gdx.input.setInputProcessor(imp);
		// Add this method call
	    gameHelper.setup(this);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
		gameHelper.onStart(this);
	}
 
	@Override
	public void onStop() {
		super.onStop();
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
		gameHelper.onStop();
	}
	
	public void onActivityResult(int request, int response, Intent data){
		super.onActivityResult(request, response, data);
		gameHelper.onActivityResult(request, response, data);
	}

	@Override
	public void setTrackerScreenName(String path) {
		// Set screen name.
		// Where path is a String representing the screen name.
		globalTracker.setScreenName(path);
		globalTracker.send(new HitBuilders.AppViewBuilder().build());
	}

	@Override
	public boolean getSignedInGPGS() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable(){
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {
		}
	}

	@Override
	public void submitScoreGPGS(int score) {
		Games.Leaderboards.submitScore(gameHelper.getApiClient(), "CgkIidrglpgbEAIQAA", score);
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
		PendingResult<Achievements.LoadAchievementsResult> res = Games.Achievements.load(gameHelper.getApiClient(), false);
		AchievementBuffer buff = res.await().getAchievements();
		int achCount = 0;
		for(Achievement a : buff){
			if(a.getState() == 0){
				achCount++;
			}
		}
		if(achCount >= 8)
			Games.Achievements.unlock(gameHelper.getApiClient(), "CgkIidrglpgbEAIQCg");
	}

	@Override
	public void getLeaderboardGPGS() {
		if (gameHelper.isSignedIn()) {
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), "CgkIidrglpgbEAIQAA"), 100);
		}
		else if (!gameHelper.isConnecting()) {
			loginGPGS();
		}
	}

	@Override
	public void getAchievementsGPGS() {
		if (gameHelper.isSignedIn()) {
			Games.Achievements.reveal(gameHelper.getApiClient(), "CgkIidrglpgbEAIQAg");
			Games.Achievements.reveal(gameHelper.getApiClient(), "CgkIidrglpgbEAIQAw");
			Games.Achievements.reveal(gameHelper.getApiClient(), "CgkIidrglpgbEAIQBA");
			Games.Achievements.reveal(gameHelper.getApiClient(), "CgkIidrglpgbEAIQBQ");
			Games.Achievements.reveal(gameHelper.getApiClient(), "CgkIidrglpgbEAIQBg");
			Games.Achievements.reveal(gameHelper.getApiClient(), "CgkIidrglpgbEAIQBw");
			Games.Achievements.reveal(gameHelper.getApiClient(), "CgkIidrglpgbEAIQCA");
			Games.Achievements.reveal(gameHelper.getApiClient(), "CgkIidrglpgbEAIQCQ");
			Games.Achievements.reveal(gameHelper.getApiClient(), "CgkIidrglpgbEAIQCg");
			startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 101);
		}
		else if (!gameHelper.isConnecting()) {
			loginGPGS();
		}
	}

	@Override
	public void onSignInFailed() {
	}

	@Override
	public void onSignInSucceeded() {
	}

	@Override
	public void trackBeginSession(long millis) {
		globalTracker.send(new HitBuilders.EventBuilder()
         .setCategory("session")
         .setAction("begin")
         .setLabel(""+millis)
         .build());
	}

	@Override
	public void trackGameOver(long elapsed, int game) {
		globalTracker.send(new HitBuilders.EventBuilder()
        .setCategory("game")
        .setAction(""+game)
        .setLabel(""+elapsed)
        .build());
	}

	@Override
	public void trackScore(int score, int game) {
		globalTracker.send(new HitBuilders.EventBuilder()
        .setCategory("score")
        .setAction(""+game)
        .setLabel(""+score)
        .build());
	}
}
