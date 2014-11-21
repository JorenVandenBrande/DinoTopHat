package com.dinoth.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class AndroidControls implements IControls,GestureListener{
	
	private boolean showControlSelect = true;
	
	private String[] paths = new String[]{"PlayScreen/tutscreen1v2.png", "PlayScreen/tutscreen2v2.png", "PlayScreen/tutscreen3v2.png"};
	
	private int mode = 0;
	
	@Override
	public Texture selectMode(float x, float y){
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
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
