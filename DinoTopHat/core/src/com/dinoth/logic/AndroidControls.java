package com.dinoth.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Vector2;
import com.dinoth.logic.SimpleDirectionGestureDetector.DirectionListener;

public class AndroidControls extends GestureAdapter implements IControls,InputProcessor{
	
	private boolean showControlSelect = true;
	
	private String[] paths = new String[]{"PlayScreen/tutscreen1v2.png", "PlayScreen/tutscreen2v2.png", "PlayScreen/tutscreen3v2.png","PlayScreen/tutscreen1v2.png","PlayScreen/tutscreen1v2.png","PlayScreen/tutscreen1v2.png"};
	
	private int mode = 0;
	private PlayLogic logic;
	public void setLogic(PlayLogic logic){
		this.logic = logic;
	}
	
	@Override
	public Texture selectMode(float x, float y){
		
		if(x > 20 && x < 315 && y >220){
			showControlSelect = false;
			mode = 0;
			return new Texture(Gdx.files.internal(paths[mode]));
			
		}
		if(x > 325 && x < 620 && y >220){
			mode = 1;
			showControlSelect = false;
			return new Texture(Gdx.files.internal(paths[mode]));
		}
		if(x > 630 && x < 925 && y>220){
			mode = 2;
			showControlSelect = false;
			return new Texture(Gdx.files.internal(paths[mode]));
		}if(x > 20 && x < 315 && y <220){
			showControlSelect = false;
			mode = 3;
			return new Texture(Gdx.files.internal(paths[mode]));
			
		}if(x > 325 && x < 620 && y <220){
			showControlSelect = false;
			mode = 4;
			return new Texture(Gdx.files.internal(paths[mode]));
			
		}if(x > 630 && x < 925 && y <220){
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
	public int clickEvent(float x, float y, int yCoord){
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
		}if(mode == 5){
			if(y>yCoord)
				return 1;
			return -1;
		}
		return 0;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if( mode==3){
			logic.dragOccured(screenX,screenY,pointer);
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		if (mode == 4){
			logic.FlingOccured(velocityX, velocityY, button);
			return true;
		}
		return false;
	}
}
