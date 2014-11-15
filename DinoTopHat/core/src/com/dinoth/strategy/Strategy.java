package com.dinoth.strategy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.dinoth.logic.Entity;

public abstract class Strategy {

	protected Texture smallDino;
	protected Texture bigDino;
	protected Texture tree;
	
	protected int dinoCounter;
	
	public abstract  Entity spawnDino();
	
	public abstract float getSpawnDelay(float spawnDelay);
	
	public abstract int getSpeedIncrease(int speedIncrease);
	
	public void dispose() {
		smallDino.dispose();
		bigDino.dispose();
		tree.dispose();
	}
	
	public void recreate(){
		dinoCounter=0;
		smallDino = new Texture(Gdx.files.internal("PlayLogic/smalldinosheet.png"));
		bigDino = new Texture(Gdx.files.internal("PlayLogic/fatdinosheet.png"));
		tree = new Texture(Gdx.files.internal("PlayLogic/tree.png"));
	}
}
