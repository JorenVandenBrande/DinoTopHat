package com.dinoth.strategy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.dinoth.logic.BigDino;
import com.dinoth.logic.Entity;
import com.dinoth.logic.PalmTree;
import com.dinoth.logic.PlayLogic;
import com.dinoth.logic.SmallDino;

public class EasyStrategy extends Strategy{
	
	public EasyStrategy(){
		this.recreate();
	}
	
	@Override
	public Entity spawnDino() {
		Rectangle dino = new Rectangle();
		dino.x = 1000;
		int dinoLane = MathUtils.random(0,3);
		dino.y = PlayLogic.laneCoordinates[dinoLane][1];
		int type = MathUtils.random(0,100);
		int randomness=70;
		Entity ent;
		if(dinoCounter>50)randomness=50;
		if(dinoCounter>100){
			randomness=30;
		}
		if(type <=randomness){
			ent = new SmallDino(smallDino,dino);
		}
		else{
			int enemyType = MathUtils.random(0,100); 
			if(enemyType > 70){
				ent = new PalmTree(tree,dino);
			}
			else{
				ent = new BigDino(bigDino,dino);
			}
		}
		ent.setLane(dinoLane);
		dinoCounter++;
		return ent;
	}
	
	public float getSpawnDelay(float spawnDelay) {
		if(dinoCounter%6==0){
			return (float) Math.max(200000000, spawnDelay-100000000);
		}
		return spawnDelay;
	}
	
	public int getSpeedIncrease(int speedIncrease) {
		if(dinoCounter%6==0 && speedIncrease<350)
			speedIncrease+=40;
		return speedIncrease;
	}

}
