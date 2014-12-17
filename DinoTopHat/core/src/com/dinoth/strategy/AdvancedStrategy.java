package com.dinoth.strategy;

import java.util.Iterator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.dinoth.logic.BigDino;
import com.dinoth.logic.Entity;
import com.dinoth.logic.PalmTree;
import com.dinoth.logic.PlayLogic;
import com.dinoth.logic.SmallDino;

public class AdvancedStrategy extends Strategy{
	
	private long prevTime;
	private Array<PalmTreeSpawn> trees;

	public AdvancedStrategy(){
		this.recreate();
		this.trees = new Array<PalmTreeSpawn>();
		this.prevTime = TimeUtils.nanoTime();
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
			Iterator<PalmTreeSpawn> iter = trees.iterator();
			while(iter.hasNext()){
				PalmTreeSpawn next = iter.next();
				if(next.readyToDie(TimeUtils.millis())){
					iter.remove();
					continue;
				}
				if(!next.canSpawnDino(TimeUtils.millis(), dinoLane)){
					return null;
				}
			}
			ent = new SmallDino(smallDino,dino);
		}
		else{
			int enemyType = MathUtils.random(0,100); 
			Iterator<PalmTreeSpawn> iter = trees.iterator();
			boolean treeRedFlag = false;
			boolean dinoRedFlag = false;
			while(iter.hasNext()){
				PalmTreeSpawn next = iter.next();
				if(next.readyToDie(TimeUtils.millis())){
					iter.remove();
					continue;
				}
				if(!next.canSpawnDino(TimeUtils.millis(), dinoLane)){
					dinoRedFlag = true;
					treeRedFlag = true;
					break;
				}
				else if(!next.canSpawnTree(TimeUtils.millis(), dinoLane)){
					treeRedFlag = true;
				}
			}
			if(enemyType > 90 && trees.size < 4 && !treeRedFlag){
				ent = new PalmTree(tree,dino);
				trees.add(new PalmTreeSpawn(TimeUtils.millis(), dinoLane));
			}
			else if(!dinoRedFlag){
				ent = new BigDino(bigDino,dino);
			}
			else
				return null;
		}
		ent.setLane(dinoLane);
		dinoCounter++;
		return ent;
	}
	
	public float getSpawnDelay(float spawnDelay) {
		
		if(dinoCounter%7==0){
			return (float) Math.max(350000000, spawnDelay-100000000);
		}
		return spawnDelay;
	}
	
	public int getSpeedIncrease(int speedIncrease) {
		if(dinoCounter%7==0 && speedIncrease<200)
			speedIncrease+=40;
		return speedIncrease;
	}

	public int getBaseMultiplier() {
		long prev = TimeUtils.timeSinceNanos(prevTime);
		
		long fap = 15000000000L;
		if(prev >= fap){
			multi++;
			prevTime = TimeUtils.nanoTime();
			day = !day;
		}
		return multi;
	}

	public float getProgress() {
		if(dinoCounter==0){
			prevTime = TimeUtils.nanoTime();
			return 0.0f;
		}
		return (TimeUtils.timeSinceNanos(prevTime))/15000000000.0f;
	}

}