package com.dinoth.game.Screens;

import java.util.Iterator;

import com.dinoth.game.DinoTopHat;
import com.dinoth.game.Entity;
import com.dinoth.game.common.DinoMusic;
import com.dinoth.game.common.LocalIOHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Play implements Screen{

	private Texture userDino;
	private Texture smallDino;
	private Texture bigDino;
	private Texture tree;
	private Texture backGround;
	private Texture muteImage;
	private Texture mutedIm;
	private Texture deathScreen;
	private Texture replayScreen;
	private Texture tutScreen;
	//private Texture hitBox;
	
	private boolean showTut = true;
	private float speedMult = 1;
	private float spawnDelay = 1000000000;
	
	private Entity player;
	
	private Array<Entity> dinos;
	private Array<Entity> enemies;
	private int[][] laneCoordinates;
	private int lane;
	private long lastDinoTime;
	private int score = 0;
	
	private BitmapFont font;
	private CharSequence scoreStr;
	
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private DinoTopHat dinoGame;
	
	private boolean isDeath;

	public Play(DinoTopHat dinoGame) {
		this.dinoGame=dinoGame;
		batch=dinoGame.getBatch();
		camera=dinoGame.getCamera();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(backGround, 0, 0, 960, 540);
		if(DinoMusic.isPlaying())
			batch.draw(muteImage,10,460,70,70);
		else
			batch.draw(mutedIm,10,460,70,70);
		for(Entity dino:dinos){
			batch.draw(dino.getSprite(), dino.getImageX(), dino.getImageY(), dino.getImageWidth(), dino.getImageHeight());
			//batch.draw(hitBox, dino.getHitBox().x, dino.getHitBox().y, dino.getHitBox().width, dino.getHitBox().height);
		}
		for(Entity enemy:enemies){
			batch.draw(enemy.getSprite(), enemy.getImageX(), enemy.getImageY(), enemy.getImageWidth(), enemy.getImageHeight());
			//batch.draw(hitBox, enemy.getHitBox().x, enemy.getHitBox().y, enemy.getHitBox().width, enemy.getHitBox().height);
		}
		batch.draw(player.getSprite(), player.getImageX(), player.getImageY(), player.getImageWidth(), player.getImageHeight());
		//batch.draw(hitBox, player.getHitBox().x, player.getHitBox().y, player.getHitBox().width, player.getHitBox().height);
		scoreStr = "Score: "+score;
		font.setScale((float)0.7);
		font.draw(batch, scoreStr, 390, 510);
		
		if(isDeath) this.drawDeathScreen();
		if(showTut){
			batch.draw(tutScreen, 0, 0, 960, 540);
			if(Gdx.input.justTouched()){
				showTut = false;
			}
		}
		batch.end();
		if(!isDeath){
			if(!showTut)
				this.keepPlaying();
		}else{
			this.showDeathScreen();
		}
		
	}

	
	private void keepPlaying() {
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

	private void showDeathScreen() {
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
	
	private void drawDeathScreen() {
		batch.draw(deathScreen,250,70,460,400);
		font.setScale((float)0.65);
		font.draw(batch, "You are dead!", 310, 440);
		font.setScale((float)0.5);
		font.draw(batch, "score: "+ score, 320, 370);
		font.draw(batch, "Highest score: "+ LocalIOHandler.getHighScores()[0], 320, 320);
		batch.draw(replayScreen,395,100,170,160);
	}


	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		userDino = new Texture(Gdx.files.internal("PlayScreen/playerdino.png"));
		smallDino = new Texture(Gdx.files.internal("PlayScreen/smalldino.png"));
		bigDino = new Texture(Gdx.files.internal("PlayScreen/fatdino.png"));
		backGround = new Texture(Gdx.files.internal("PlayScreen/background.png"));
		tree = new Texture(Gdx.files.internal("PlayScreen/tree.png"));
		tutScreen = new Texture(Gdx.files.internal("PlayScreen/tutscreen1.png"));
		dinos = new Array<Entity>();
		enemies = new Array<Entity>();
		laneCoordinates = new int[][]{{50,30},{62, 110},{74, 190},{86, 270}};
		lane = 0;
		Rectangle userDinoRect = new Rectangle();
		userDinoRect.width = 40;
		userDinoRect.height = 50;
		userDinoRect.x = laneCoordinates[lane][0];
		userDinoRect.y = laneCoordinates[lane][1];
		player = new Entity(userDino, userDinoRect, 83, 110, -30, 0, 0);
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		muteImage=new Texture(Gdx.files.internal("MainMenu/dinomute.png"));
		mutedIm = new Texture(Gdx.files.internal("MainMenu/muted.png"));
		score=0;
		isDeath=false;
		//hitBox = new Texture(Gdx.files.internal("Playscreen/hitbox.png"));
		deathScreen = new Texture(Gdx.files.internal("MainMenu/deathscreen.png"));
		replayScreen= new Texture(Gdx.files.internal("MainMenu/dinoreplay.png"));
		lastDinoTime = TimeUtils.nanoTime();
	}
	
	private void spawnDino(){
		Rectangle dino = new Rectangle();
		dino.x = 1000;
		int dinoLane = MathUtils.random(0,3);
		dino.y = laneCoordinates[dinoLane][1];
		int type = MathUtils.random(0,100);
		if(type <=50){
			dino.width = 30;
			dino.height = 50;
			dinos.add(new Entity(smallDino, dino, 41, 70, -6,0,400));
		}
		else{
			int enemyType = MathUtils.random(0,100); 
			if(enemyType > 70){
				dino.width = 20;
				dino.height = 50;
				enemies.add(new Entity(tree, dino, 80, 70, -30, 0, 380));
			}
			else{
				dino.width = 50;
				dino.height = 50;
				enemies.add(new Entity(bigDino, dino, 124, 130, -15, 0,400));
			}
		}
		
		lastDinoTime = TimeUtils.nanoTime();
	}

	@Override
	public void hide() {
		this.dispose();
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		userDino.dispose();
		backGround.dispose();
		smallDino.dispose();
		bigDino.dispose();
		tree.dispose();
		backGround.dispose();
		muteImage.dispose();
		mutedIm.dispose();
		deathScreen.dispose();
		replayScreen.dispose();
		tutScreen.dispose();
		
	}

}
