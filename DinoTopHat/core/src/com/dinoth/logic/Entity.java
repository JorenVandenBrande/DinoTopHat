package com.dinoth.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Entity {
	private Texture sprite;
	private Rectangle hitBox;
	private float imageWidth, imageHeight, xOffset, yOffset; //image size and offset from hitbox
	private float baseSpeed;
	private int lane;
	protected boolean isGood;
	private Animation walkAnimation;
    private Texture walkSheet;
    private TextureRegion[] walkFrames;
    
    private final int FRAME_ROWS;
    private final int FRAME_COLS;
	
	public Entity(Texture sprite, Rectangle hitBox, float imageWidth, float imageHeight, float xOffset, float yOffset, float baseSpeed, int frameRows, int frameCols){
		this.hitBox = hitBox;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.baseSpeed = baseSpeed;
		this.FRAME_ROWS = frameRows;
		this.FRAME_COLS = frameCols;
		this.walkSheet = sprite;
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS, walkSheet.getHeight()/FRAME_ROWS);
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(0.1f, walkFrames);
	}
	
	public void updateHitBoxCoords(float x, float y){
		this.hitBox.x = x;
		this.hitBox.y = y;
	}
	
	public void updateHitBoxDim(float width, float height){
		this.hitBox.width = width;
		this.hitBox.height = height;
	}
	
	public float getImageWidth(){
		return this.imageWidth;
	}
	
	public float getImageHeight(){
		return this.imageHeight;
	}
	
	public float getImageX(){
		return this.hitBox.x+xOffset;
	}
	
	public float getImageY(){
		return this.hitBox.y+yOffset;
	}
	
	public float getBaseSpeed(){
		return this.baseSpeed;
	}
	
	public TextureRegion getFrame(float stateTime){
		return walkAnimation.getKeyFrame(stateTime, true);
	}
	
	public Rectangle getHitBox(){
		return this.hitBox;
	}

	public void setLane(int dinoLane) {
		this.lane=dinoLane;
	}

	public int getLane() {
		return lane;
	}

	public boolean isGood() {
		
		return this.isGood;
	}
}
