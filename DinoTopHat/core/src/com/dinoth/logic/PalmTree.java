package com.dinoth.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class PalmTree extends Entity{

	public PalmTree(Texture tex,Rectangle rect){
		super(tex,rect, 80, 70, -20, 0, 380, 1, 1);
		this.updateHitBoxDim(35, 50);
		this.isGood=false;
	}
}
