package com.dinoth.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class PcControls implements IControls{

	@Override
	public int clickEvent(float x, float y) {
		if(x < 480)
			return 1;
		return -1;
	}

	@Override
	public void resetControlSelect(){
		//Nothing happens.
	}

	@Override
	public boolean showControlSelect() {
		return false;
	}

	@Override
	public Texture selectMode(float x, float y) {
		//Nothing happens.
		return new Texture(Gdx.files.internal("PlayScreen/tutscreenpc.png"));
	}

}
