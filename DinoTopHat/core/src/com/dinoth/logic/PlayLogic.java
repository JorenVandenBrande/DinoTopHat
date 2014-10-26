package com.dinoth.logic;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
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
	
	private Entity player;
	
	
	private Array<Entity> dinos;
	private Array<Entity> enemies;
	private int[][] laneCoordinates;
	private int lane;
	private long lastDinoTime;
	private int score = 0;
	
	private float speedMult = 1;
	private float spawnDelay = 1000000000;

	private boolean isDeath;
	
	public PlayLogic(){
		
	}
	

	public void keepPlaying(Camera camera) {
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
		if(TimeUtils.nanoTime() - lastDinoTime > spawnDelay) spawnDino();
		Iterator<Entity> iter = dinos.iterator();
		while(iter.hasNext()){
			Entity dino = iter.next();
			dino.updateHitBoxCoords(dino.getHitBox().x-dino.getBaseSpeed()*speedMult*Gdx.graphics.getDeltaTime(), dino.getHitBox().y);
			if(dino.getHitBox().x + 100 < 0){
				enemies = new Array<Entity>();
				dinos = new Array<Entity>();
				speedMult = 1;
				spawnDelay = 1000000000;
				LocalIOHandler.postHighScore(score);
				isDeath=true;
				break;		
			}
			if(dino.getHitBox().overlaps(player.getHitBox())){
				score++;
				if(score % 3 == 0){
					speedMult = (float) Math.min(3,speedMult + 0.1);
					spawnDelay = (float) Math.max(500000000, spawnDelay*0.9);
				}
				iter.remove();
			}
		}
		iter = enemies.iterator();
		while(iter.hasNext()){
			Entity enemy = iter.next();
			enemy.updateHitBoxCoords(enemy.getHitBox().x-enemy.getBaseSpeed()*speedMult*Gdx.graphics.getDeltaTime(), enemy.getHitBox().y);
			if(enemy.getHitBox().x + 100 < 0){
				score++;
				if(score % 3 == 0){
					speedMult = (float) Math.min(3,speedMult + 0.1);
					spawnDelay = (float) Math.max(500000000, spawnDelay*0.9);
				}
				iter.remove();		
			}
			if(enemy.getHitBox().overlaps(player.getHitBox())){
				enemies = new Array<Entity>();
				dinos = new Array<Entity>();
				speedMult = 1;
				spawnDelay = 1000000000;
				LocalIOHandler.postHighScore(score);
				isDeath=true;
				break;
			}
		}
		
	}
	
	public void showDeathScreen(OrthographicCamera camera) {
		if(Gdx.input.justTouched()){
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			if(touchPos.x>395 &&touchPos.x<565 && touchPos.y>100 && touchPos.y<260){
				isDeath=false;
				score=0;
				lane=0;
				speedMult = 1;
				player.updateHitBoxCoords(laneCoordinates[lane][0], laneCoordinates[lane][1]);
				dinos = new Array<Entity>();
				enemies = new Array<Entity>();
			}
		}
	}
	
	private void spawnDino(){
		Rectangle dino = new Rectangle();
		dino.x = 1000;
		int dinoLane = MathUtils.random(0,3);
		dino.y = laneCoordinates[dinoLane][1];
		int type = MathUtils.random(0,100);
		if(type <=50){
			dinos.add(new SmallDino(smallDino,dino));
		}
		else{
			int enemyType = MathUtils.random(0,100); 
			if(enemyType > 70){
				enemies.add(new PalmTree(tree,dino));
			}
			else{
				enemies.add(new BigDino(bigDino,dino));
			}
		}
		
		lastDinoTime = TimeUtils.nanoTime();
	}


	public int getScore() {
		return score;
	}


	public Array<Entity> getDinos() {
		return this.dinos;
	}
	
	public Array<Entity> getEnemies() {
		return this.enemies;
	}
	
	public Entity getPlayer() {
		return this.player;
	}


	public void create() {
		userDino = new Texture(Gdx.files.internal("PlayScreen/playerdino.png"));
		smallDino = new Texture(Gdx.files.internal("PlayScreen/smalldino.png"));
		bigDino = new Texture(Gdx.files.internal("PlayScreen/fatdino.png"));
		tree = new Texture(Gdx.files.internal("PlayScreen/tree.png"));
		dinos = new Array<Entity>();
		enemies = new Array<Entity>();
		laneCoordinates = new int[][]{{50,30},{62, 110},{74, 190},{86, 270}};
		lane = 0;
		Rectangle userDinoRect = new Rectangle();
		userDinoRect.x = laneCoordinates[lane][0];
		userDinoRect.y = laneCoordinates[lane][1];
		player = new PlayerDino(userDino,userDinoRect);
		score=0;
		isDeath=false;
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
}
