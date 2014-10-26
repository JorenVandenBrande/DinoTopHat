package com.dinoth.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Entity {
	private Texture sprite;
	private Rectangle hitBox;
	private float imageWidth, imageHeight, xOffset, yOffset; //image size and offset from hitbox
	private float baseSpeed;
	
	public Entity(Texture sprite, Rectangle hitBox, float imageWidth, float imageHeight, float xOffset, float yOffset, float baseSpeed){
		this.sprite = sprite;
		this.hitBox = hitBox;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.baseSpeed = baseSpeed;
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
	
	public Texture getSprite(){
		return this.sprite;
	}
	
	public Rectangle getHitBox(){
		return this.hitBox;
	}
}
