package com.dinoth.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.dinoth.logic.SimpleDirectionGestureDetector.DirectionListener;

public class AndroidControls implements IControls,DirectionListener{
	
	private boolean showControlSelect = true;
	
	private String[] paths = new String[]{"PlayScreen/tutscreen1v2.png", "PlayScreen/tutscreen2v2.png", "PlayScreen/tutscreen3v2.png"};
	
	private int mode = 0;
	
	@Override
	public Texture selectMode(float x, float y){
		
		if(x > 20 && x < 315 && y <270){
			showControlSelect = false;
			mode = 0;
			return new Texture(Gdx.files.internal(paths[mode]));
			
		}
		if(x > 325 && x < 620 && y <270){
			mode = 1;
			showControlSelect = false;
			return new Texture(Gdx.files.internal(paths[mode]));
		}
		if(x > 630 && x < 925 && y<270){
			mode = 2;
			showControlSelect = false;
			return new Texture(Gdx.files.internal(paths[mode]));
		}if(x > 20 && x < 315 && y >270){
			showControlSelect = false;
			mode = 3;
			return new Texture(Gdx.files.internal(paths[mode]));
			
		}if(x > 325 && x < 620 && y >270){
			showControlSelect = false;
			mode = 4;
			return new Texture(Gdx.files.internal(paths[mode]));
			
		}if(x > 630 && x < 925 && y >270){
			showControlSelect = false;
			mode = 5;
			return new Texture(Gdx.files.internal(paths[mode]));
			
		}
		return null;
	}
	
	@Override
	public boolean showControlSelect(){
		return showControlSelect;
	}
	
	@Override
	public void resetControlSelect(){
		showControlSelect = true;
	}
	
	@Override
	public int clickEvent(float x, float y){
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

	@Override
	public void onLeft() {
		// TODO Auto-generated method stub
		if(mode==3){
			mode =2;
		}
	}

	@Override
	public void onRight() {
		// TODO Auto-generated method stub
		if(mode==3){
			mode=1;
		}
	}

	@Override
	public void onUp() {
		// TODO Auto-generated method stub
		if(mode==1){
			mode = 3;
		}
	}

	@Override
	public void onDown() {
		// TODO Auto-generated method stub
		
	}

	
}
