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
import com.dinoth.game.DinoTopHat;
import com.dinoth.game.common.DinoMusic;
import com.dinoth.game.common.PreferencesHandler;
import com.dinoth.strategy.AdvancedStrategy;
import com.dinoth.strategy.Strategy;

public class PlayLogic {

	private Texture userDino;
	private Texture userDinoDead;
	private Texture chicky;
	
	Sprite chicklettSprite;
	
	private float fadingFactor=0.0f;
	public float shaking=0.0f;
	private float placement;
	
	private OrthographicCamera camera;
	
	private Entity player;
	private Strategy strat;
	
	private Array<Entity> allDinos;
	public static final int[][] laneCoordinates = new int[][]{{50,30},{62, 110},{74, 190},{86, 270}};
	public int lane;
	private long lastDinoTime;
	private int score = 0;
	private int kills = 0;
	private boolean herbivoreAchBlocked = false;
	private boolean streakerAchBlocked = false;
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
	private DinoTopHat dinoGame;
	
	public PlayLogic(OrthographicCamera camera, IControls controls, DinoTopHat dinoGame){
		this.dinoGame = dinoGame;
		this.controls = controls;
		this.camera = camera;
		this.strat = new AdvancedStrategy();
		if(this.controls instanceof AndroidControls ){
			((AndroidControls) this.controls).setLogic(this);
		}
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
				lane = Math.max(0, Math.min(3, lane + controls.clickEvent(touchPos.x, touchPos.y,laneCoordinates[lane][1])));
			}
		}
		if(Gdx.input.isKeyJustPressed(Keys.RIGHT) || Gdx.input.isKeyJustPressed(Keys.DOWN)) lane = Math.max(0, lane-1);
		if(Gdx.input.isKeyJustPressed(Keys.LEFT) || Gdx.input.isKeyJustPressed(Keys.UP)) lane = Math.min(3, lane+1);
		player.updateHitBoxCoords(laneCoordinates[lane][0], laneCoordinates[lane][1]);
		player.setLane(lane);
		if(TimeUtils.nanoTime() - lastDinoTime > spawnDelay) {
			spawnDino();
			speedIncrease=strat.getSpeedIncrease(speedIncrease);
			spawnDelay = strat.getSpawnDelay(spawnDelay);
		}
		Iterator<Entity> iter = allDinos.iterator();
		while(iter.hasNext()){
			Entity dino = iter.next();
			if(dino instanceof PalmTree)
				dino.updateHitBoxCoords(dino.getHitBox().x-(dino.getBaseSpeed())*Gdx.graphics.getDeltaTime(), dino.getHitBox().y);
			else
				dino.updateHitBoxCoords(dino.getHitBox().x-(dino.getBaseSpeed()+speedIncrease)*Gdx.graphics.getDeltaTime(), dino.getHitBox().y);
			if(dino.getHitBox().x + 100 < 0 && dino.isGood()){
				if(score < 42){
					streakerAchBlocked = true;
				}
				iter.remove();
			}
			if(dino.getHitBox().overlaps(player.getHitBox())&& dino.isGood()){
				if(DinoMusic.isPlaying())
					eat.setVolume(eat.play(), 1);
				if(baseMultiplier < 3){
					herbivoreAchBlocked = true;
				}
				kills++;
				score+=1*baseMultiplier;
				fadingFactor=1.0f;
				shaking = 1.0f;
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
				PreferencesHandler.gamesPlayedIncr();
				PreferencesHandler.LifetimeKillsIncr(kills);
				PreferencesHandler.LifetimePointsIncr(score);
				dinoGame.nbGames++;
				dinoGame.ar.trackScore(score, dinoGame.nbGames);
				dinoGame.ar.trackGameOver(TimeUtils.timeSinceMillis(dinoGame.startTime), dinoGame.nbGames);
				dinoGame.startTime = TimeUtils.millis();
				if(dinoGame.isAndroid){
					dinoGame.ar.submitScoreGPGS(score);
					if(score == 0) //unlock The Sigert
						dinoGame.ar.unlockAchievementGPGS("CgkIidrglpgbEAIQAg");
					if(PreferencesHandler.getGamesPlayed()>=10)  //unlock Stay Casual
						dinoGame.ar.unlockAchievementGPGS("CgkIidrglpgbEAIQAw");
					if(PreferencesHandler.getGamesPlayed()>=100) //unlock The Addict
						dinoGame.ar.unlockAchievementGPGS("CgkIidrglpgbEAIQBA");
					if(PreferencesHandler.getLifetimePoints() >= 100000) //unlock Point Hoarder
						dinoGame.ar.unlockAchievementGPGS("CgkIidrglpgbEAIQBw");
					if(PreferencesHandler.getLifetimeKills() >= 2500)   //unlock Baby Killer
						dinoGame.ar.unlockAchievementGPGS("CgkIidrglpgbEAIQCA");
					if(baseMultiplier >=3 && !herbivoreAchBlocked) //unlock The Herbivore
						dinoGame.ar.unlockAchievementGPGS("CgkIidrglpgbEAIQBg");
					if(score >= 42 && !streakerAchBlocked)  //unlock The Streaker
						dinoGame.ar.unlockAchievementGPGS("CgkIidrglpgbEAIQBQ");
					if(score >= 10000) //unlock Like a DinoSir
						dinoGame.ar.unlockAchievementGPGS("CgkIidrglpgbEAIQCQ");
					
				}
				isDeath=true;
				fadingFactor=0.0f;
				shaking = 0.0f;
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
				kills = 0;
				herbivoreAchBlocked = false;
				streakerAchBlocked = false;
				lane=0;
				speedIncrease=20;
				player.updateHitBoxCoords(laneCoordinates[lane][0], laneCoordinates[lane][1]);
				player.setLane(0);
				allDinos = new Array<Entity>();
				baseMultiplier=1;
				strat.recreate();
			}else if(touchPos.x>290 &&touchPos.x<460 && touchPos.y>100 && touchPos.y<260){
				return 1;
			}
		}return 0;
	}
	
	private void spawnDino(){
		Entity ent = strat.spawnDino();
		if(ent != null)
			this.addEntity(ent);
		lastDinoTime = TimeUtils.nanoTime();
		if(baseMultiplier!=strat.getBaseMultiplier()){
			baseMultiplier=strat.getBaseMultiplier();
		}
		baseMultiplier=strat.getBaseMultiplier();
		
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
		userDinoDead = new Texture(Gdx.files.internal("PlayLogic/playerdinoghostsheet.png"));
		strat.recreate();
		allDinos=new Array<Entity>();
		lane = 0;
		baseMultiplier=1;
		Rectangle userDinoRect = new Rectangle();
		userDinoRect.x = laneCoordinates[lane][0];
		userDinoRect.y = laneCoordinates[lane][1];
		player = new PlayerDino(userDino, userDinoDead,userDinoRect);
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
		strat.dispose();
		userDinoDead.dispose();
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
		if(isDeath)
			return 0.0f;
		return strat.getProgress();
	}
	
	public Sprite getChicky(float delta){
		chicklettSprite.setColor(1, 1, 1, fadingFactor);
		
		chicklettSprite.setBounds(laneCoordinates[lane][0]+player.getImageWidth()/6, laneCoordinates[lane][1]+player.getImageHeight(), 30, 30);
		if(fadingFactor>0){
			placement = placement + 30*delta;
			fadingFactor=Math.max(0, fadingFactor-delta);
			shaking = Math.max(0, shaking-4*delta);
			chicklettSprite.setBounds(laneCoordinates[lane][0]+player.getImageWidth()/6, laneCoordinates[lane][1]+player.getImageHeight()+ placement, 30, 30);
		}
		chicklettSprite.setColor(1, 1, 1, fadingFactor);
		
		return chicklettSprite;
	}

	public int firstY=-1;
	
	public int startLane= 0;
	
	public void dragOccured(int screenX, int screenY, int pointer){
		if(isDeath) return;
		if(firstY == -1) return;
		int diff = firstY - screenY;
		lane = Math.max(0, Math.min(lane+(diff/90), 3));
		if(diff/90 != 0)
			firstY = screenY;
		
	}

	public void dragOccured2(int screenX, int screenY, int pointer) {
		//write code for dragging here;
		if(isDeath) return;
		if(firstY == -1) return;
		int diff = screenY - firstY;
		if(Math.abs(diff)<=75){
			lane = Math.max(0, Math.min(3, startLane ));
		}
		if(diff>75 && diff<=150){
			if(startLane-1 != lane){
				lane = Math.max(0, Math.min(3, startLane-1 ));
			}
			return;
		}
		if(diff>150 && diff<=225){
			if(startLane-2 != lane){
				lane = Math.max(0, Math.min(3, startLane-2 ));
			}
			return;
		}
		if(diff>225){
			if(startLane-3 != lane){
				lane = Math.max(0, Math.min(3, startLane-3 ));
			}
			return;
		}
		if(diff<-75 && diff>=-150){
			if(startLane+1 != lane){
				lane = Math.max(0, Math.min(3, startLane+1 ));
			}
		}
		if(diff<-150 && diff>=-225){
			if(startLane+2 != lane){
				lane = Math.max(0, Math.min(3, startLane+2 ));
			}
		}
		if(diff<225){
			if(startLane+3 != lane){
				lane = Math.max(0, Math.min(3, startLane+3 ));
			}
		}
		
		
	}


	public void FlingOccured(float velocityX, float velocityY, int button) {
		// write swipecode here;
		if(isDeath) return;
		if(Math.abs(velocityX)>Math.abs(velocityY)){
			if(velocityX>0){
					
			}else{
					
			}
		}else{
			if(velocityY>0){
				lane = Math.max(0, Math.min(3, lane -1));
			}else{                                  
				lane = Math.max(0, Math.min(3, lane +1 ));	
			}
		}
	}
}
