package com.dinoth.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class BigDino extends Entity{

	public BigDino(Texture tex,Rectangle rect) {
		super(tex, tex, rect, 124, 130, -35, 0,300, 1, 8);
		this.updateHitBoxDim(60, 50);
		this.isGood=false;
	}

}
