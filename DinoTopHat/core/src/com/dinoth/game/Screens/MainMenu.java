package com.dinoth.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.dinoth.game.DinoTopHat;
import com.dinoth.game.common.DinoMusic;

public class MainMenu implements Screen{
	
	private Texture backgroundImage;
	private Texture playImage;
	private Texture highScoreImage;
	private Texture muteImage;
	private Texture mutedIm;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	private CharSequence str = "Dino Top Hat";
	
	private Play playScreen;
	private Highscores highscoresScreen;
	
	private DinoTopHat dinoGame;
	
	public MainMenu(DinoTopHat dinoGame) {
		//LocalIOHandler.writeToFile("", "highscores.txt", false); clear the highscores on reboot
		this.playScreen=new Play(dinoGame);
		this.highscoresScreen=new Highscores(dinoGame);
		this.dinoGame=dinoGame;
		this.camera=dinoGame.getCamera();
		this.batch=dinoGame.getBatch();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(backgroundImage, 0, 0,960, 540);
		//batch.draw(titleImage,300,400,200,50);
		font.draw(batch, str, 240, 520);
		batch.draw(highScoreImage, 285, 20,170,160);
		if(!DinoMusic.isPlaying())
			batch.draw(mutedIm,500,20,170,160);
		else
			batch.draw(muteImage,500,20,170,160);
		batch.draw(playImage,335,200,290,255);
		batch.end();
		
		
		if(Gdx.input.justTouched()){
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			if(touchPos.x > 335 && touchPos.x<625 && touchPos.y>200 && touchPos.y<455){
				dinoGame.setScreen(playScreen);
			}
			if(touchPos.x > 285 && touchPos.x<455 && touchPos.y>20 && touchPos.y<180){
				dinoGame.setScreen(highscoresScreen);
			}
			if(touchPos.x > 500 && touchPos.x<670 && touchPos.y>20 && touchPos.y<180){
				DinoMusic.pause();
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		
		
	}

	@Override
	public void show() {
		backgroundImage = new Texture(Gdx.files.internal("MainMenu/dinoback.png"));
		playImage=new Texture(Gdx.files.internal("MainMenu/dinoplay.png"));
		muteImage = new Texture(Gdx.files.internal("MainMenu/dinomute.png"));
		mutedIm = new Texture(Gdx.files.internal("MainMenu/muted.png"));
		highScoreImage=new Texture(Gdx.files.internal("MainMenu/dinohigh.png"));
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		
		//TODO: initialise other images and rectangles
		
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
		backgroundImage.dispose();
		playImage.dispose();
		highScoreImage.dispose();
		muteImage.dispose();
		mutedIm.dispose();
		
	}

}
