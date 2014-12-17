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
	private Sprite dayAir;
	private Sprite dayRange;
	private Sprite dayMount;
	private Sprite dayRun;
	
	private Texture backGroundAirDay;
	private Texture backGroundRangeDay;
	private Texture backGroundMountDay;
	private Texture backGroundRunDay;
	private Texture backGroundFieldDay;
	private Texture backGroundAirNight;
	private Texture backGroundRangeNight;
	private Texture backGroundMountNight;
	private Texture backGroundRunNight;
	private Texture lanes;
	private Texture muteImage;
	private Texture mutedIm;
	private Texture deathScreen;
	private Texture replayScreen;
	private Texture tutScreen;
	private Texture tutScreen2;
	private Texture controlSelect;
	//private Texture hitBox;
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
//		sprite.setColor(1, 1, 1, 1f);
//		sprite.setBounds(0, 0, 960, 540);
//		sprite.draw(batch);
		
		this.animateBackground();
		if(DinoMusic.isPlaying())
			batch.draw(muteImage,10,460,70,70);
		else
			batch.draw(mutedIm,10,460,70,70);
		
		for(Entity ent:playGame.makeSpriteList()){
			batch.draw(ent.getFrame(stateTime, playGame.isDeath()), ent.getImageX(), ent.getImageY(), ent.getImageWidth(), ent.getImageHeight());
			//draw hitboxes for debugging
			//batch.draw(hitBox, ent.getHitBox().x, ent.getHitBox().y, ent.getHitBox().width, ent.getHitBox().height);
		}
		playGame.getChicky(delta).draw(batch);
		batch.draw(chicklett,
				150-Math.round(70*(0.125f*Math.sin(2*Math.PI*(1-Math.min(1,playGame.shaking)))))/2,
				460-Math.round(70*(0.125f*Math.cos(2*Math.PI*(1-Math.min(1,playGame.shaking)))))/2, 
				Math.round(70*(0.125f*Math.sin(2*Math.PI*(1-Math.min(1,playGame.shaking)))+1f)),
				Math.round(70*(0.125f*Math.cos(2*Math.PI*(1-Math.min(1,playGame.shaking)))+0.875f)));
		
		scoreStr = " "+playGame.getScore();
		font.setScale((float)0.7);
		font.draw(batch, scoreStr, 225, 510);
		font.draw(batch, "multiplier: "+ playGame.getMultiplier() , 500, 510 );
		batch.draw(progressBorder, 500, 420, 347,44);
		batch.draw(progressFilling, 504, 420, playGame.getMultiplierProgress()*324, 44);
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
	float xMountain=0;
	float xMountainRange=0;
	float xRunningField = 0;
	float xFrontField = 0;
	private void animateBackground() {
		dayAir.setColor(1, 1, 1, getDayTime());
		dayMount.setColor(1, 1, 1, getDayTime());
		dayRange.setColor(1, 1, 1, getDayTime());
		dayRun.setColor(1, 1, 1, getDayTime());
		
		/////Air
		batch.draw(backGroundAirNight, 0, 0, 960, 540);
		dayAir.setBounds(0, 0, 960, 540);
		dayAir.draw(batch);
		
		/////Mountain Range animation
		float x1 = -xMountainRange;
		if(xMountainRange>=3840){
			xMountainRange = 3840-xMountainRange;
			x1 = xMountainRange;
		}else if(xMountainRange>= 1920){
			x1 = 1920 + (1920-xMountainRange);
		}else{
			x1 = -xMountainRange;
		}
		batch.draw(backGroundRangeNight, x1, 0, 1920, 540);
		dayRange.setBounds(x1, 0, 1920, 540);
		dayRange.draw(batch);
		
		float x2 = 1920-xMountainRange;
		batch.draw(backGroundRangeNight, x2, 0, 1920, 540);
		dayRange.setBounds(x2, 0, 1920, 540);
		dayRange.draw(batch);
		
		xMountainRange+=30*Gdx.graphics.getDeltaTime();
		
		/////Mountain animation
		x1 = -xMountain;
		if(xMountain>=3840){
			xMountain = 3840-xMountain;
			x1 = xMountain;
		}else if(xMountain>= 1920){
			x1 = 1920 + (1920-xMountain);
		}else{
			x1 = -xMountain;
		}
		batch.draw(backGroundMountNight, x1, 0, 1920, 540);
		dayMount.setBounds(x1, 0, 1920, 540);
		dayMount.draw(batch);
		
		x2 = 1920-xMountain;
		
		batch.draw(backGroundMountNight, x2, 0, 1920, 540);
		dayMount.setBounds(x2, 0, 1920, 540);
		dayMount.draw(batch);
		
		xMountain+=50*Gdx.graphics.getDeltaTime();
		
		/////Running field animation
		
		x1 = -xRunningField;
		if(xRunningField>=3840){
			xRunningField = 3840-xRunningField;
			x1 = xRunningField;
		}else if(xRunningField>= 1920){
			x1 = 1920 + (1920-xRunningField);
		}else{
			x1 = -xRunningField;
		}
		batch.draw(backGroundRunNight, x1, 0, 1920, 540);
		dayRun.setBounds(x1, 0, 1920, 540);
		dayRun.draw(batch);
		
		x2 = 1920-xRunningField;
		
		batch.draw(backGroundRunNight, x2-1, 0, 1922, 540);
		dayRun.setBounds(x2-1, 0, 1922, 540);
		dayRun.draw(batch);
		
		xRunningField+=200*Gdx.graphics.getDeltaTime();
		
		
		
		/////Front field animation
		x1 = -xFrontField;
		if(xFrontField>=3840){
			xFrontField = 3840-xFrontField;
			x1 = xFrontField;
		}else if(xFrontField>= 1920){
			x1 = 1920 + (1920-xFrontField);
		}else{
			x1 = -xFrontField;
		}
		batch.draw(backGroundFieldDay, x1, 0, 1920, 540);
		
		x2 = 1920-xFrontField;
		
		batch.draw(backGroundFieldDay, x2, 0, 1920, 540);
		
		
		xFrontField+=250*Gdx.graphics.getDeltaTime();
		
		/////lanes
		batch.draw(lanes, 0,0);
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
		
		backGroundAirDay = new Texture(Gdx.files.internal("backgroundplayscreen/air.png"));
		backGroundRangeDay = new Texture(Gdx.files.internal("backgroundplayscreen/mountainrange.png"));
		backGroundMountDay= new Texture(Gdx.files.internal("backgroundplayscreen/mountain.png"));
		backGroundRunDay = new Texture(Gdx.files.internal("backgroundplayscreen/runfield.png"));
		backGroundFieldDay = new Texture(Gdx.files.internal("backgroundplayscreen/field.png"));
		backGroundAirNight = new Texture(Gdx.files.internal("backgroundplayscreen/nightair.png"));
		backGroundRangeNight = new Texture(Gdx.files.internal("backgroundplayscreen/nightmountainrange.png"));
		backGroundMountNight = new Texture(Gdx.files.internal("backgroundplayscreen/nightmountain.png"));
		backGroundRunNight = new Texture(Gdx.files.internal("backgroundplayscreen/nightrunfield.png"));
		lanes = new Texture(Gdx.files.internal("backgroundplayscreen/lanes.png"));
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
		//sprite=new Sprite(backGround);
		
		dayAir = new Sprite(backGroundAirDay);
		dayMount = new Sprite(backGroundMountDay);
		dayRange = new Sprite(backGroundRangeDay);
		dayRun = new Sprite(backGroundRunDay);
		
	}
	
	private float getDayTime(){
		return (float)((Math.cos(playGame.getDayProgress()*2*Math.PI)+1)/2);
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
		//backGround.dispose();
		//backGround.dispose();
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
		backGroundAirDay.dispose();
		backGroundRangeDay.dispose();
		backGroundMountDay.dispose();
		backGroundRunDay.dispose();
		backGroundFieldDay.dispose();
		backGroundAirNight.dispose();
		backGroundMountNight.dispose();
		backGroundRangeNight.dispose();
		backGroundRunNight.dispose();
		lanes.dispose();
	}

}
