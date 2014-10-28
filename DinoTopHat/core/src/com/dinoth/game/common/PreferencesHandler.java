package com.dinoth.game.common;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferencesHandler {
	
	private static String[] keys = new String[]{"1st","2nd","3rd","4th","5th"};
	
	public static void postHighScore(int score){
		Preferences prefs = Gdx.app.getPreferences("My Preferences");
		int insert = 0;
		for(String key: keys){
			if(score < Integer.parseInt(prefs.getString(key, "0")))
				insert++;
			else
				break;
		}
		String temp = null;
		String next = ""+score;
		for(int i = insert; i < keys.length; i++){
			temp = prefs.getString(keys[i], "0");
			prefs.putString(keys[i], next);
			next = temp;
		}
		prefs.flush();
	}
	
	public static int[] getHighScores(){
		Preferences prefs = (Preferences) Gdx.app.getPreferences("My Preferences");
		int[] result = new int[5];
		for(int i = 0; i < keys.length; i++){
			result[i] = Integer.parseInt(prefs.getString(keys[i], "0"));
		}
		return result;
	}
}
