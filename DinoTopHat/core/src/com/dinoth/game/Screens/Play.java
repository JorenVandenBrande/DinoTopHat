package com.dinoth.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.dinoth.game.DinoTopHat;
import com.dinoth.game.common.DinoMusic;
import com.dinoth.game.common.PreferencesHandler;
import com.dinoth.logic.Entity;
import com.dinoth.logic.PlayLogic;

public class Play implements Screen{

	Sprite sprite;
	
	
	private Texture backGround;
	private Texture muteImage;
	private Texture mutedIm;
	private Texture deathScreen;
	private Texture replayScreen;
	private Texture tutScreen;
	private Texture tutScreen2;
	private Texture controlSelect;
	private Texture hitBox;
	private Texture progressBorder;
	private Texture progressFilling;
	private Texture chicklett;
	private Texture menuButton;
	
	
	private boolean showTut1 = true;
	private boolean showTut2 = false;
	
	private BitmapFont font;
	private CharSequence scoreStr;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private DinoTopHat dinoGame;
	private PlayLogic playGame;
	
	private float stateTime = 0f;

	public Play(DinoTopHat dinoGame) {
		this.dinoGame=dinoGame;
		batch=dinoGame.getBatch();
		camera=dinoGame.getCamera();
		playGame = new PlayLogic(camera, dinoGame.controls, dinoGame);
	}

	@Override
	public void render(float delta) {
		stateTime += Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(0, 0, 0.8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.setColor(1, 1, 1, 1f);
		sprite.setBounds(0, 0, 960, 540);
		sprite.draw(batch);
		//batch.draw(backGround, 0, 0, 960, 540);
		if(DinoMusic.isPlaying())
			batch.draw(muteImage,10,460,70,70);
		else
			batch.draw(mutedIm,10,460,70,70);
		
		for(Entity ent:playGame.makeSpriteList()){
			batch.draw(ent.getFrame(stateTime), ent.getImageX(), ent.getImageY(), ent.getImageWidth(), ent.getImageHeight());
			//draw hitboxes for debugging
			//batch.draw(hitBox, ent.getHitBox().x, ent.getHitBox().y, ent.getHitBox().width, ent.getHitBox().height);
		}
		playGame.getChicky(delta).draw(batch);
		batch.draw(chicklett,150,460,70,70);
		scoreStr = " "+playGame.getScore();
		font.setScale((float)0.7);
		font.draw(batch, scoreStr, 225, 510);
		font.draw(batch, "multiplier: "+ playGame.getMultiplier() , 500, 510 );
		batch.draw(progressBorder, 500, 420, 347,44);
		batch.draw(progressFilling, 504, 420, playGame.getMultiplierProgress()*339, 44);
		if(playGame.isDeath()) this.drawDeathScreen();
		if(dinoGame.controls.showControlSelect()){
			batch.draw(controlSelect, 0, 0, 960, 540);
			if(Gdx.input.justTouched()){
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);
				tutScreen = dinoGame.controls.selectMode(touchPos.x, touchPos.y);
			}
		}
		else if(showTut1){
			batch.draw(tutScreen, 0, 0, 960, 540);
			if(Gdx.input.justTouched()){
				showTut1 = false;
				showTut2 = true;
			}
		}else if(showTut2){
			batch.draw(tutScreen2, 0, 0, 960, 540);
			if(Gdx.input.justTouched()){
				showTut2 = false;
			}
		}
		batch.end();
		if(!playGame.isDeath()){
			if(!showTut1 && !showTut2)
				playGame.keepPlaying();
		}else{
			if(playGame.showDeathScreen()==1){
				dinoGame.setScreen(dinoGame.getMainMenu());
				dinoGame.controls.resetControlSelect();
				showTut1 = true;
			}
		}
		
	}
	
	private void drawDeathScreen() {
		batch.draw(deathScreen,250,70,460,400);
		font.setScale((float)0.65);
		font.draw(batch, "You are dead!", 310, 440);
		font.setScale((float)0.5);
		font.draw(batch, "score: "+ playGame.getScore(), 320, 370);
		//font.draw(batch, "Highest score: "+ LocalIOHandler.getHighScores()[0], 320, 320);
		font.draw(batch, "Highest score: "+	PreferencesHandler.getHighScores()[0], 320, 320);
		batch.draw(replayScreen,490,100,170,160);
		batch.draw(menuButton,290,100,170,160);
	}


	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		
		backGround = new Texture(Gdx.files.internal("PlayScreen/background.png"));
		tutScreen = new Texture(Gdx.files.internal("PlayScreen/tutscreenpc.png"));
		tutScreen2 = new Texture(Gdx.files.internal("PlayScreen/tutscreenpart2.png"));
		controlSelect = new Texture(Gdx.files.internal("PlayScreen/controlselect2best.png"));
		//controlSelect = new Texture(Gdx.files.internal("PlayScreen/controlselect.png"));
		playGame.create();
		
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		muteImage=new Texture(Gdx.files.internal("PlayScreen/dinomute.png"));
		mutedIm = new Texture(Gdx.files.internal("PlayScreen/muted.png"));
		
		//hitBox = new Texture(Gdx.files.internal("Playscreen/hitbox.png"));
		deathScreen = new Texture(Gdx.files.internal("PlayScreen/scorebackground.png"));
		replayScreen= new Texture(Gdx.files.internal("PlayScreen/dinoreplay.png"));
		chicklett = new Texture(Gdx.files.internal("PlayLogic/chicklett_as.png"));
		menuButton = new Texture(Gdx.files.internal("PlayScreen/dinomenu.png"));
		progressBorder = new Texture(Gdx.files.internal("PlayScreen/progressbarborder.png"));
		progressFilling = new Texture(Gdx.files.internal("PlayScreen/progressbarfilling.png"));
		sprite=new Sprite(backGround);
		
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
		//hitBox.dispose();
		deathScreen.dispose();
		replayScreen.dispose();
		tutScreen.dispose();
		controlSelect.dispose();
		playGame.dispose();
		chicklett.dispose();
		tutScreen2.dispose();
		progressBorder.dispose();
		progressFilling.dispose();
	}

}
