package com.dinoth.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class PlayerDino extends Entity{

	public PlayerDino(Texture tex,Rectangle rect){
		super(tex, rect, 83, 110, -30, 0, 0);
		this.updateHitBoxCoords(40, 50);
	}
}
