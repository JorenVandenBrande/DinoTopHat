package com.dinoth.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Controls {
	
	private static boolean showControlSelect = true;
	
	private static String[] paths = new String[]{"PlayScreen/tutscreen1v2.png", "PlayScreen/tutscreen2v2.png", "PlayScreen/tutscreen3v2.png"};
	
	private static int mode = 0;
	
	public static Texture selectMode(float x, float y){
		if(y > 365 || y < 175)
			return null;
		if(x > 20 && x < 315){
			showControlSelect = false;
			mode = 0;
			return new Texture(Gdx.files.internal(paths[mode]));
			
		}
		if(x > 325 && x < 620){
			mode = 1;
			showControlSelect = false;
			return new Texture(Gdx.files.internal(paths[mode]));
		}
		if(x > 630 && x < 925){
			mode = 2;
			showControlSelect = false;
			return new Texture(Gdx.files.internal(paths[mode]));
		}
		return null;
	}
	
	public static boolean showControlSelect(){
		return showControlSelect;
	}
	
	public static void resetControlSelect(){
		showControlSelect = true;
	}
	
	public static int clickEvent(float x, float y){
		if(mode == 0){
			if(x < 480)
				return 1;
			return -1;
		}
		if(mode == 1){
			if(y > 270)
				return 1;
			return -1;
		}
		if(mode == 2){
			if(x > 480)
				return 1;
			return -1;
		}
		return 0;
	}
	
}
