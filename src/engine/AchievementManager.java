package engine;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class AchievementManager {
  
    /** Singleton instance of the class. */
    private static AchievementManager instance;
    private int LuckyScore = 770; //'7' means the lucky number
    private int UnluckyScore = 440; //'4' means the unlucky number
    private int AceScore = 1110; //'1' means the ace in one card

    private static final Logger LOGGER = Logger.getLogger(Core.class
			.getSimpleName());
  
    // Map to store achievements and their completion status
    static private Map<String, Boolean> achievements;

    private AchievementManager() {
        achievements = new HashMap<>();

        // Initialize predefined achievements
        achievements.put("adventure start", false);
        achievements.put("sharp shooter", false);
        achievements.put("deadly accuracy", false); 
        achievements.put("lucky guy", false);
        achievements.put("unlucky guy", false);
        achievements.put("game ace", false);
        achievements.put("soul mates", false);
        achievements.put("aviophobia", false);
        achievements.put("buddy fxxker", false);
        achievements.put("pat & mat", false);
    }

    public Map<String, Boolean> getAchievements() {
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
    public void markAchievementAsAchieved(String achievementName) {
        if (isAchievementAchieved(achievementName)) {
            return;
        }

        if (achievements.containsKey(achievementName)) {
            achievements.put(achievementName, true);
        }

        LOGGER.info(achievementName + " achievement achieved!");
    }

    /**
     * Check if an achievement has been achieved.
     *
     * @param achievementName The name of the achievement to check.
     * @return true if the achievement is achieved, false otherwise.
     */
    public static boolean isAchievementAchieved(String achievementName) {
        if (achievements.containsKey(achievementName)) {
            return achievements.get(achievementName);
        }
        return false;
    }

    /**
     * Check if the requirments for achievements about accuracy and life
     */
    public void checkAchievements (GameState gameState) {
        int level = gameState.getLevel();
        int shot = gameState.getBulletsShot1() + gameState.getBulletsShot2();
        int life_1 = gameState.getLivesRemaining1p();
        int life_2 = gameState.getLivesRemaining2p();
        int gamemode = gameState.getMode();
        double accuracy = ((double)gameState.getShipsDestroyed() / (double)shot) * 100;

        //if the player play with good accuracy 
        if (shot > 0 && accuracy >= 90.0 && level >= 3) {
            markAchievementAsAchieved("sharp shooter");
         }
        
         //Check if the players recorded perfect accuracy, if the player want to clear level 1, he has to shot 20 times at least
        if (shot >= 20 && accuracy == 100) { 
            markAchievementAsAchieved("deadly accuracy");
        }

        //Check if the player didn't hit enemy's ships in 1p mode
        if (gamemode == 1 && shot > 0 && accuracy == 0) {
            markAchievementAsAchieved("aviophobia");
        }

        //Check if two players didn't hit enemy's ships
        if (gamemode == 2 && shot > 0 && accuracy == 0) {
            markAchievementAsAchieved("pat & mat");
        }

        //Check if two players clear level or gameover with same lives 
        if (gamemode == 2 && life_1 == life_2) {
            markAchievementAsAchieved("soul mates");
        }

        //Check one player has max life but the partner doesn't have
        if (gamemode == 2 && (life_1 - life_2 == 3 || life_2 - life_1 == 3)) {
            markAchievementAsAchieved("buddy fxxker");
        }

    }
    /*
     * Check if the score equals to the achievement
     * 
     * @param score the score of the player
     */
    public void checkScore(int score) {
        if (score == LuckyScore) {
            markAchievementAsAchieved("lucky guy");
        }

        else if (score == UnluckyScore) {
            markAchievementAsAchieved("unlucky guy");
        }

        else if (score == AceScore) {
            markAchievementAsAchieved("game ace");
        }
    }
}
