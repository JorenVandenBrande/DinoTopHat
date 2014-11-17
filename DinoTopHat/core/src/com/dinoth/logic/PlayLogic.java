package com.dinoth.logic;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.dinoth.game.common.DinoMusic;
import com.dinoth.game.common.PreferencesHandler;
import com.dinoth.strategy.EasyStrategy;


public class PlayLogic {

	private Texture userDino;
	private Texture chicky;
	
	Sprite chicklettSprite;
	
	private float fadingFactor=0.0f;
	private float placement;
	
	private OrthographicCamera camera;
	
	private Entity player;
	private EasyStrategy easyStrat;
	
	private Array<Entity> allDinos;
	public static final int[][] laneCoordinates = new int[][]{{50,30},{62, 110},{74, 190},{86, 270}};
	private int lane;
	private long lastDinoTime;
	private int score = 0;
	private int baseMultiplier;
//	private int multiplier;
//	private int n;
	
	private int speedIncrease= 20;
	private float spawnDelay = 1000000000;

	private boolean isDeath;
	//private int streak;
	
	
	private Sound death;
	private Sound eat;
	
	private IControls controls;
	
	public PlayLogic(OrthographicCamera camera, IControls controls){
		this.controls = controls;
		this.camera = camera;
		this.easyStrat = new EasyStrategy();
	}
	

	public void keepPlaying() {
		if(Gdx.input.justTouched()){
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			if(touchPos.x<70 && touchPos.y>460){
				DinoMusic.pause();
			}
//			else if(touchPos.x < 480) lane = Math.min(3, lane+1);
//			else lane = Math.max(0, lane-1);
			else{
				lane = Math.max(0, Math.min(3, lane + controls.clickEvent(touchPos.x, touchPos.y)));
			}
		}
		if(Gdx.input.isKeyJustPressed(Keys.RIGHT) || Gdx.input.isKeyJustPressed(Keys.DOWN)) lane = Math.max(0, lane-1);
		if(Gdx.input.isKeyJustPressed(Keys.LEFT) || Gdx.input.isKeyJustPressed(Keys.UP)) lane = Math.min(3, lane+1);
		player.updateHitBoxCoords(laneCoordinates[lane][0], laneCoordinates[lane][1]);
		player.setLane(lane);
		if(TimeUtils.nanoTime() - lastDinoTime > spawnDelay) {
			spawnDino();
			speedIncrease=easyStrat.getSpeedIncrease(speedIncrease);
			spawnDelay = easyStrat.getSpawnDelay(spawnDelay);
		}
		Iterator<Entity> iter = allDinos.iterator();
		while(iter.hasNext()){
			Entity dino = iter.next();
			dino.updateHitBoxCoords(dino.getHitBox().x-(dino.getBaseSpeed()+speedIncrease)*Gdx.graphics.getDeltaTime(), dino.getHitBox().y);
			if(dino.getHitBox().x + 100 < 0 && dino.isGood()){
				
				
				iter.remove();
			}
			if(dino.getHitBox().overlaps(player.getHitBox())&& dino.isGood()){
				if(DinoMusic.isPlaying())
					eat.setVolume(eat.play(), 1);
				score+=1*baseMultiplier;
				fadingFactor=1.0f;
				placement=0;
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
				//LocalIOHandler.postHighScore(score);
				PreferencesHandler.postHighScore(score);
				isDeath=true;
				fadingFactor=0.0f;
				placement=0;
				break;
			}
		}
		
	}
	
	public int showDeathScreen() {
		if(Gdx.input.justTouched()){
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			if(touchPos.x>490 &&touchPos.x<660 && touchPos.y>100 && touchPos.y<260){
				isDeath=false;
				score=0;
				lane=0;
				speedIncrease=20;
				player.updateHitBoxCoords(laneCoordinates[lane][0], laneCoordinates[lane][1]);
				player.setLane(0);
				allDinos = new Array<Entity>();
				baseMultiplier=1;
				easyStrat.recreate();
			}else if(touchPos.x>290 &&touchPos.x<460 && touchPos.y>100 && touchPos.y<260){
				return 1;
			}
		}return 0;
	}
	
	private void spawnDino(){
		
		this.addEntity(easyStrat.spawnDino());
		lastDinoTime = TimeUtils.nanoTime();
		if(baseMultiplier!=easyStrat.getBaseMultiplier()){
			baseMultiplier=easyStrat.getBaseMultiplier();
		}
		
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
		userDino = new Texture(Gdx.files.internal("PlayLogic/playerdinosheet.png"));
		easyStrat.recreate();
		allDinos=new Array<Entity>();
		lane = 0;
		baseMultiplier=1;
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
		chicky = new Texture(Gdx.files.internal("PlayLogic/chicklett_as.png"));
		chicklettSprite = new Sprite(chicky);
	}


	public boolean isDeath() {
		return isDeath;
	}


	public void dispose() {
		userDino.dispose();
		easyStrat.dispose();
		
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
		return this.baseMultiplier;
	}
	
	public float getMultiplierProgress(){
		return ((easyStrat.getDinoCounter()%75)/75.0f);
	}
	
	public Sprite getChicky(float delta){
		chicklettSprite.setColor(1, 1, 1, fadingFactor);
		
		chicklettSprite.setBounds(laneCoordinates[lane][0]+player.getImageWidth()/6, laneCoordinates[lane][1]+player.getImageHeight(), 30, 30);
		if(fadingFactor>0){
			placement = placement + 30*delta;
			fadingFactor=Math.max(0, fadingFactor-delta);
			chicklettSprite.setBounds(laneCoordinates[lane][0]+player.getImageWidth()/6, laneCoordinates[lane][1]+player.getImageHeight()+ placement, 30, 30);
		}
		chicklettSprite.setColor(1, 1, 1, fadingFactor);
		
		return chicklettSprite;
	}
}
