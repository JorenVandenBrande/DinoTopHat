package com.dinoth.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class PlayerDino extends Entity{

	public PlayerDino(Texture tex,Rectangle rect){
		super(tex, rect, 83, 110, -35, 0, 0, 1, 8);
		this.updateHitBoxDim(30, 50);
	}
}
