package com.dinoth.game.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class LocalIOHandler {
	
	public static void postHighScore(int score){
		String fileName = "highscores.txt";
		String s = readFromFile(fileName);
		if(s == null || s.equals("")){
			writeToFile("1: " + score+ ";", fileName, false);
			return;
		}
		String[] scores = s.split(";");
		int[] highscores = new int[Math.min(5, scores.length+1)];
		for(int i = 0; i < scores.length; i++){
			highscores[i] = Integer.parseInt(scores[i].split(" ")[1]);
		}
		for(int i = 0; i < highscores.length; i++){
			if(score > highscores[i]){
				int temp = highscores[i];
				highscores[i] = score;
				score = temp;
			}
		}
		String res = "";
		for(int i = 1; i <= Math.min(5, highscores.length); i++){
			res+="" + i + ": " + highscores[i-1] + ";";
		}
		writeToFile(res, fileName, false);
	}
	
	public static int[] getHighScores(){
		int[] highscores = new int[5];
		String fileName = "highscores.txt";
		String s = readFromFile(fileName);
		if(s == null || s.equals("")){
			return highscores;
		}
		String[] scores = s.split(";");
		for(int i = 0; i < scores.length; i++){
			highscores[i] = Integer.parseInt(scores[i].split(" ")[1]);
		}
		return highscores;
	}
	
	public static void writeToFile(String s, String fileName, boolean append){
		try{
			FileHandle handle =  Gdx.files.local("data/"+fileName);
			handle.writeString(s, append);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String readFromFile(String fileName){
		try{
			FileHandle handle = Gdx.files.local("data/"+fileName);
			return handle.readString();
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}