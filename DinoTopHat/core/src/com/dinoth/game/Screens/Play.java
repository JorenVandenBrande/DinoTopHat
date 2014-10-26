package com.dinoth.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dinoth.game.DinoTopHat;
import com.dinoth.game.common.DinoMusic;
import com.dinoth.game.common.LocalIOHandler;
import com.dinoth.logic.Entity;
import com.dinoth.logic.PlayLogic;

public class Play implements Screen{

	
	private Texture backGround;
	private Texture muteImage;
	private Texture mutedIm;
	private Texture deathScreen;
	private Texture replayScreen;
	private Texture tutScreen;
	//private Texture hitBox;
	
	private boolean showTut = true;
	
	private BitmapFont font;
	private CharSequence scoreStr;
	
	Entity player;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private DinoTopHat dinoGame;
	private PlayLogic playGame;

	public Play(DinoTopHat dinoGame) {
		this.dinoGame=dinoGame;
		batch=dinoGame.getBatch();
		camera=dinoGame.getCamera();
		playGame = new PlayLogic();
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
		for(Entity dino:playGame.getDinos()){
			batch.draw(dino.getSprite(), dino.getImageX(), dino.getImageY(), dino.getImageWidth(), dino.getImageHeight());
			//batch.draw(hitBox, dino.getHitBox().x, dino.getHitBox().y, dino.getHitBox().width, dino.getHitBox().height);
		}
		for(Entity enemy:playGame.getEnemies()){
			batch.draw(enemy.getSprite(), enemy.getImageX(), enemy.getImageY(), enemy.getImageWidth(), enemy.getImageHeight());
			//batch.draw(hitBox, enemy.getHitBox().x, enemy.getHitBox().y, enemy.getHitBox().width, enemy.getHitBox().height);
		}
		player = playGame.getPlayer();
		batch.draw(player.getSprite(), player.getImageX(), player.getImageY(), player.getImageWidth(), player.getImageHeight());
		//batch.draw(hitBox, player.getHitBox().x, player.getHitBox().y, player.getHitBox().width, player.getHitBox().height);
		scoreStr = "Score: "+playGame.getScore();
		font.setScale((float)0.7);
		font.draw(batch, scoreStr, 390, 510);
		
		if(playGame.isDeath()) this.drawDeathScreen();
		if(showTut){
			batch.draw(tutScreen, 0, 0, 960, 540);
			if(Gdx.input.justTouched()){
				showTut = false;
			}
		}
		batch.end();
		if(!playGame.isDeath()){
			if(!showTut)
				playGame.keepPlaying(camera);
		}else{
			playGame.showDeathScreen(camera);
		}
		
	}
	
	private void drawDeathScreen() {
		batch.draw(deathScreen,250,70,460,400);
		font.setScale((float)0.65);
		font.draw(batch, "You are dead!", 310, 440);
		font.setScale((float)0.5);
		font.draw(batch, "score: "+ playGame.getScore(), 320, 370);
		font.draw(batch, "Highest score: "+ LocalIOHandler.getHighScores()[0], 320, 320);
		batch.draw(replayScreen,395,100,170,160);
	}


	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		
		backGround = new Texture(Gdx.files.internal("PlayScreen/background.png"));
		tutScreen = new Texture(Gdx.files.internal("PlayScreen/tutscreen1.png"));
		playGame.create();
		
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		muteImage=new Texture(Gdx.files.internal("MainMenu/dinomute.png"));
		mutedIm = new Texture(Gdx.files.internal("MainMenu/muted.png"));
		
		//hitBox = new Texture(Gdx.files.internal("Playscreen/hitbox.png"));
		deathScreen = new Texture(Gdx.files.internal("MainMenu/deathscreen.png"));
		replayScreen= new Texture(Gdx.files.internal("MainMenu/dinoreplay.png"));
		
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
		backGround.dispose();
		backGround.dispose();
		muteImage.dispose();
		mutedIm.dispose();
		deathScreen.dispose();
		replayScreen.dispose();
		tutScreen.dispose();
		
	}

}
