package com.dinoth.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class PalmTree extends Entity{

	public PalmTree(Texture tex,Rectangle rect){
		super(tex,rect, 80, 70, -30, 0, 380);
		this.updateHitBoxDim(20, 50);
		this.isGood=false;
	}
}
