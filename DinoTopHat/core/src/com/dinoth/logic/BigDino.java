package com.dinoth.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class BigDino extends Entity{

	public BigDino(Texture tex,Rectangle rect) {
		super(tex, rect, 124, 130, -35, 0,400);
		this.updateHitBoxDim(40, 50);
		this.isGood=false;
	}

}
