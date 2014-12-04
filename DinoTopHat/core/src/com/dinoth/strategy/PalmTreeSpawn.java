package com.dinoth.strategy;

public class PalmTreeSpawn {
	
	private long spawned;
	private int lane;
	
	public PalmTreeSpawn(long spawned, int lane){
		this.spawned = spawned;
		this.lane = lane;
	}
	
	public boolean canSpawnTree(long now, int lane){
		if(this.lane != lane)
			return true;
		if(now - spawned > 2000)
			return true;
		return false;
	}
	
	public boolean canSpawnDino(long now, int lane){
		if(this.lane != lane){
			if(Math.abs(this.lane - lane)>1)
				return true;
			return now-spawned > 1000;
		}
		if(now-spawned > 3000)
			return true;
		return false;
	}
	
	public boolean readyToDie(long now){
		return (now-spawned > 5000);
	}
}
