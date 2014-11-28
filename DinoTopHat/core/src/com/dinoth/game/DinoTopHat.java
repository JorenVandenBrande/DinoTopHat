package com.dinoth.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.dinoth.game.Screens.MainMenu;
import com.dinoth.game.common.DinoMusic;
import com.dinoth.game.common.LocalIOHandler;
import com.dinoth.logic.IControls;

public class DinoTopHat extends Game {
	
	public static final String TITLE = "DinoTopHat";
	private OrthographicCamera camera;
	private SpriteBatch batch;
	public boolean isAndroid;
	public int nbGames = 0;
	public long startTime;
	
	public IControls controls;
	public ActionResolver ar;
	
	public DinoTopHat(IControls cont, ActionResolver ar, boolean isAndroid){
		this.isAndroid = isAndroid;
		this.controls = cont;
		this.ar = ar;
		this.startTime = TimeUtils.millis();
		ar.trackBeginSession(startTime);
		ar.setTrackerScreenName("com.dinoth.game.Screens.Play");
	}
	
	private MainMenu menu;
	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 960,540);
		batch= new SpriteBatch();
		menu= new MainMenu(this);
		DinoMusic.run();
		LocalIOHandler.writeToFile("", "highscores.txt", true);
		setScreen(menu);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void pause(){
		super.pause();
	}
	
	@Override
	public void dispose(){
		super.dispose();
		batch.dispose();
		
	}
	
	@Override
	public void resume(){
		super.resume();
	}
	
	@Override
	public void resize(int width, int height){
		
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public Screen getMainMenu() {
		return this.menu;
	}
	
	
	
}
