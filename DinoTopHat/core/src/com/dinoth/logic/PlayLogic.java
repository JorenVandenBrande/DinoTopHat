package com.dinoth.logic;

import java.util.Iterator;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.dinoth.game.common.DinoMusic;
import com.dinoth.game.common.LocalIOHandler;

public class PlayLogic {

	private Texture userDino;
	private Texture smallDino;
	private Texture bigDino;
	private Texture tree;
	
	private OrthographicCamera camera;
	
	private Entity player;
	
	private Array<Entity> allDinos;
	private int[][] laneCoordinates;
	private int lane;
	private long lastDinoTime;
	private int score = 0;
	private int multiplier;
	private int n;
	
	private int speedIncrease= 20;
	private float spawnDelay = 1000000000;

	private boolean isDeath;
	private int streak;
	private int dinoCounter;
	
	private Sound death;
	private Sound eat;
	
	public PlayLogic(OrthographicCamera camera){
		this.camera = camera;
	}
	

	public void keepPlaying() {
		if(Gdx.input.justTouched()){
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			if(touchPos.x<70 && touchPos.y>460){
				DinoMusic.pause();
			}else if(touchPos.x < 480) lane = Math.min(3, lane+1);
			else lane = Math.max(0, lane-1);
		}
		if(Gdx.input.isKeyJustPressed(Keys.RIGHT)) lane = Math.max(0, lane-1);
		if(Gdx.input.isKeyJustPressed(Keys.LEFT)) lane = Math.min(3, lane+1);
		player.updateHitBoxCoords(laneCoordinates[lane][0], laneCoordinates[lane][1]);
		player.setLane(lane);
		if(TimeUtils.nanoTime() - lastDinoTime > spawnDelay) {
			spawnDino();
			if(dinoCounter%7==0){
				if(speedIncrease<350)
					speedIncrease+=40;
				spawnDelay = (float) Math.max(100000000, spawnDelay*0.9);
			}
		}
		Iterator<Entity> iter = allDinos.iterator();
		while(iter.hasNext()){
			Entity dino = iter.next();
			dino.updateHitBoxCoords(dino.getHitBox().x-(dino.getBaseSpeed()+speedIncrease)*Gdx.graphics.getDeltaTime(), dino.getHitBox().y);
			if(dino.getHitBox().x + 100 < 0 && dino.isGood()){
				multiplier=1;
				streak=0;
				n=5;
				iter.remove();
			}
			if(dino.getHitBox().overlaps(player.getHitBox())&& dino.isGood()){
				if(DinoMusic.isPlaying())
					eat.setVolume(eat.play(), 1);
				score+=1*multiplier;
				if(streak == n){
					multiplier++;
					streak=0;
					n*=2;
					
					
				}
				streak++;
				iter.remove();
			}
			if(dino.getHitBox().x + 100 < 0 && !dino.isGood()){
				
				iter.remove();		
			}
			if(dino.getHitBox().overlaps(player.getHitBox()) && !dino.isGood()){
				if(DinoMusic.isPlaying())
					death.play();
				allDinos=new Array<Entity>();
				speedIncrease=20;
				spawnDelay = 1000000000;
				LocalIOHandler.postHighScore(score);
				isDeath=true;
				
				break;
			}
		}
		
	}
	
	public void showDeathScreen() {
		if(Gdx.input.justTouched()){
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			if(touchPos.x>395 &&touchPos.x<565 && touchPos.y>100 && touchPos.y<260){
				isDeath=false;
				score=0;
				lane=0;
				speedIncrease=20;
				dinoCounter=0;
				player.updateHitBoxCoords(laneCoordinates[lane][0], laneCoordinates[lane][1]);
				player.setLane(0);
				allDinos = new Array<Entity>();
				streak=0;
				multiplier=1;
				n=5;
			}
		}
	}
	
	private void spawnDino(){
		Rectangle dino = new Rectangle();
		dino.x = 1000;
		int dinoLane = MathUtils.random(0,3);
		dino.y = laneCoordinates[dinoLane][1];
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
		this.addEntity(ent);
		lastDinoTime = TimeUtils.nanoTime();
	}


	private void addEntity(Entity ent) {
		
			int count=0;
			for(Entity dino:allDinos){
				if(dino.getLane()>ent.getLane()) count++;
				else break;
			}
			allDinos.insert(count, ent);
		
	}


	public int getScore() {
		return score;
	}
	
	public Entity getPlayer() {
		return this.player;
	}


	public void create() {
		userDino = new Texture(Gdx.files.internal("PlayLogic/playerdino.png"));
		smallDino = new Texture(Gdx.files.internal("PlayLogic/smalldino.png"));
		bigDino = new Texture(Gdx.files.internal("PlayLogic/fatdino.png"));
		tree = new Texture(Gdx.files.internal("PlayLogic/tree.png"));
		allDinos=new Array<Entity>();
		laneCoordinates = new int[][]{{50,30},{62, 110},{74, 190},{86, 270}};
		lane = 0;
		n=5;
		streak=0;
		multiplier=1;
		dinoCounter=0;
		Rectangle userDinoRect = new Rectangle();
		userDinoRect.x = laneCoordinates[lane][0];
		userDinoRect.y = laneCoordinates[lane][1];
		player = new PlayerDino(userDino,userDinoRect);
		player.setLane(lane);
		score=0;
		isDeath=false;
		death=Gdx.audio.newSound(Gdx.files.internal("Sounds/boink.wav"));
		eat=Gdx.audio.newSound(Gdx.files.internal("Sounds/nom.wav"));
		lastDinoTime = TimeUtils.nanoTime();
		
	}


	public boolean isDeath() {
		return isDeath;
	}


	public void dispose() {
		userDino.dispose();
		smallDino.dispose();
		bigDino.dispose();
		tree.dispose();
		
	}


	public Array<Entity> makeSpriteList() {
		
		Array<Entity> returnList=new Array<Entity>(allDinos);
		if( returnList.size==0 || player.getLane()==3){
			returnList.insert(0,player);
			return returnList;
		}
		int count=0;
		for(Entity dino:returnList){
			if(dino.getLane()>player.getLane()) count++;
			else break;
		}
		returnList.insert(count, player);
		return returnList;
	
		
	}


	public int getMultiplier() {
		return this.multiplier;
	}
}
