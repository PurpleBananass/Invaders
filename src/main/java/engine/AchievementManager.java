package engine;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

public class AchievementManager {

	/** Singleton instance of the class. */
	private static AchievementManager instance;
	private int LuckyScore = 770; // '7' means the lucky number
	private int UnluckyScore = 440; // '4' means the unlucky number
	private int AceScore = 1110; // '1' means the ace in one card

	private static final Logger LOGGER = Logger.getLogger(Core.class
			.getSimpleName());

	public enum Achievement {
		ADVENTURE_START,
		SHARP_SHOOTER,
		DEADLY_ACCURACY,
		LUCKY_GUY,
		UNLUCKY_GUY,
		GAME_ACE,
		SOUL_MATES,
		AVIOPHOBIA,
		BUDDY_FXXKER,
		PAT_AND_MAT
	}

	// Map to store achievements and their completion status
	static private Map<Achievement, Boolean> achievements;

	private AchievementManager() {
		achievements = new EnumMap<>(Achievement.class);

		for (Achievement achievement : Achievement.values()) {
			achievements.put(achievement, false);
		}
	}

	public Map<Achievement, Boolean> getAchievements() {
		return achievements;
	}

	/**
	 * @return Shared instance of AchievementManager.
	 */
	public static AchievementManager getInstance() {
		if (instance == null)
			instance = new AchievementManager();
		return instance;
	}

	/**
	 * Mark an achievement as achieved.
	 *
	 * @param achievementName The name of the achievement to mark as achieved.
	 */
	public void markAchievementAsAchieved(Achievement achievementName) {
		if (isAchievementAchieved(achievementName)) {
			return;
		}

		if (achievements.containsKey(achievementName)) {
			achievements.put(achievementName, true);
		}

		LOGGER.info(achievementName + " achievement achieved!");
		SoundManager.playSound("SFX/S_Achievement","S_achievement",false,false);
		SoundManager.stopSound("S_achievement",2f);
	}

	/**
	 * Check if an achievement has been achieved.
	 *
	 * @param achievementName The name of the achievement to check.
	 * @return true if the achievement is achieved, false otherwise.
	 */
	public static boolean isAchievementAchieved(Achievement achievementName) {
		if (achievements.containsKey(achievementName)) {
			return achievements.get(achievementName);
		}
		return false;
	}

	/**
	 * Check if the requirments for achievements about accuracy and life
	 */
	public void checkAchievements(GameState gameState) {
		int level = gameState.getLevel();
		int shot = gameState.getBulletsShot1() + gameState.getBulletsShot2();
		int life_1 = gameState.getLivesRemaining1p();
		int life_2 = gameState.getLivesRemaining2p();
		int gamemode = gameState.getMode();
		int shipsDestroyed = gameState.getShipsDestroyed() + gameState.getShipsDestroyed2();
		double accuracy = ((double) shipsDestroyed / (double) shot) * 100;

		// if the player play with good accuracy
		if (shot > 0 && accuracy >= 90.0 && level >= 3) {
			markAchievementAsAchieved(Achievement.SHARP_SHOOTER);
		}

		// Check if the players recorded perfect accuracy, if the player want to clear
		// level 1, he has to shot 20 times at least
		if (shot >= 20 && accuracy == 100) {
			markAchievementAsAchieved(Achievement.DEADLY_ACCURACY);
		}

		// Check if the player didn't hit enemy's ships in 1p mode
		if (gamemode == 1 && shot > 0 && accuracy == 0) {
			markAchievementAsAchieved(Achievement.AVIOPHOBIA);
		}

		// Check if two players didn't hit enemy's ships
		if (gamemode == 2 && shot > 0 && accuracy == 0) {
			markAchievementAsAchieved(Achievement.PAT_AND_MAT);
		}

		// Check if two players clear level or gameover with same lives
		if (gamemode == 2 && life_1 == life_2) {
			markAchievementAsAchieved(Achievement.SOUL_MATES);
		}

		// Check one player has max life but the partner doesn't have
		if (gamemode == 2 && (life_1 - life_2 == 3 || life_2 - life_1 == 3)) {
			markAchievementAsAchieved(Achievement.BUDDY_FXXKER);
		}

	}

	/*
	 * Check if the score equals to the achievement
	 * 
	 * @param score the score of the player
	 */
	public void checkScore(int score) {
		if (score == LuckyScore) {
			markAchievementAsAchieved(Achievement.LUCKY_GUY);
		}

		else if (score == UnluckyScore) {
			markAchievementAsAchieved(Achievement.UNLUCKY_GUY);
		}

		else if (score == AceScore) {
			markAchievementAsAchieved(Achievement.GAME_ACE);
		}
	}
}
