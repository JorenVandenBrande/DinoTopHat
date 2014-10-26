package com.dinoth.game.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class DinoMusic {

	private static Music music;
	private static boolean isPlaying;
	
	public static void run(){
		music=Gdx.audio.newMusic(Gdx.files.internal("Music/music.mp3"));
		music.setLooping(true);
		music.play();
		isPlaying=true;
	}
	
	public static boolean isPlaying(){
		return isPlaying;
	}
	
	public static void pause(){
		if(isPlaying){
			music.pause();
			isPlaying=false;
		}else{
			music.play();
			isPlaying=true;
		}
	}
}
