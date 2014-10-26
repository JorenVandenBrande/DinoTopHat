package com.dinoth.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class BigDino extends Entity{

	public BigDino(Texture tex,Rectangle rect) {
		super(tex, rect, 124, 130, -15, 0,400);
		this.updateHitBoxCoords(50, 50);
	}

}
