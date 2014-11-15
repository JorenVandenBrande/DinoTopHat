package com.dinoth.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class SmallDino extends Entity{

	public SmallDino(Texture tex,Rectangle rect){
		super(tex,rect, 41, 70, -6,0,400, 1, 8);
		this.updateHitBoxDim(30, 50);
		this.isGood=true;
	}
}
