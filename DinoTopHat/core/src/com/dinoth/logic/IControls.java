package com.dinoth.logic;

import com.badlogic.gdx.graphics.Texture;

public interface IControls {
	
	public abstract int clickEvent(float x, float y);
	
	public abstract void resetControlSelect();
	
	public abstract boolean showControlSelect();
	
	public abstract Texture selectMode(float x, float y);

}
