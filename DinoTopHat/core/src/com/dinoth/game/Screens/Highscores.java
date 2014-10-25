package com.dinoth.game.Screens;

import com.dinoth.game.DinoTopHat;
import com.dinoth.game.common.LocalIOHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Highscores implements Screen{

	private DinoTopHat dinoGame;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Texture backgroundImage;
	private Texture returnToMenu;
	
	private BitmapFont font;
	private CharSequence title="Highscores";
	private CharSequence first;
	private CharSequence second;
	private CharSequence third;
	private CharSequence fourth;
	private CharSequence fifth;

	private int[] scores;

	
	
	public Highscores(DinoTopHat dinoGame) {
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
		//font.setScale((float)1.5);
		font.setScale((float)0.6);
		batch.draw(backgroundImage,0,0,960,540);
		batch.draw(returnToMenu, 320, 90, 320, 450);
		font.draw(batch,title,350,510);
		font.draw(batch, first,410 , 430);
		font.draw(batch, second,410 , 370);
		font.draw(batch, third,410 , 310);
		font.draw(batch, fourth,410 , 250);
		font.draw(batch, fifth,410 , 190);
		
		batch.draw(returnToMenu,250,20,460,75);
		//font.setScale((float)1.5);
		font.setScale((float)0.6);
		font.draw(batch, "Return to menu",300 ,70);
		
		batch.end();
		
		if(Gdx.input.justTouched()){
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			if(touchPos.x > 250 && touchPos.x<710 && touchPos.y>20 && touchPos.y<95){
				dinoGame.setScreen(dinoGame.getMainMenu());
			}
		}
		
		
	}

	@Override
	public void resize(int width, int height) {
		
		
	}

	@Override
	public void show() {
		backgroundImage = new Texture(Gdx.files.internal("MainMenu/dinoback.png"));
		//font = new BitmapFont(Gdx.files.internal("fonts/comicsanswhite.fnt"));
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		scores=LocalIOHandler.getHighScores();
		first="1. "+scores[0];
		second="2. "+scores[1];
		third="3. "+scores[2];
		fourth="4. "+scores[3];
		fifth="5. "+scores[4];
		
		returnToMenu = new Texture(Gdx.files.internal("MainMenu/deathscreen.png"));
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
		returnToMenu.dispose();
	}

}
