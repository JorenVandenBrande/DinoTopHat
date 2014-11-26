package com.dinoth.game;

public interface ActionResolver {
	public void setTrackerScreenName(String path);
	
	public boolean getSignedInGPGS();
	public void loginGPGS();
	public void submitScoreGPGS(int score);
	public void unlockAchievementGPGS(String achievementId);
	public void getLeaderboardGPGS();
	public void getAchievementsGPGS();
}
