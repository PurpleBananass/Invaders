package engine;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class AchievementManager {
  
    /** Singleton instance of the class. */
    private static AchievementManager instance;

    private static final Logger LOGGER = Logger.getLogger(Core.class
			.getSimpleName());
  
    // Map to store achievements and their completion status
    static private Map<String, Boolean> achievements;

    private AchievementManager() {
        achievements = new HashMap<>();

        // Initialize predefined achievements
        achievements.put("adventure start", false);
        achievements.put("sharp shooter", false);
        achievements.put("perfect shooter", false);
        achievements.put("lucky guy", false);
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
    public static void markAchievementAsAchieved(String achievementName) {
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
     * Check if the requirments for sharp shooter are met
     */
    public void checkAchievements (GameState gameState) {
        int level = gameState.getLevel();
        int shot = gameState.getBulletsShot();
        
        double accuracy = ((double) gameState.getShipsDestroyed()/(double) gameState.getBulletsShot())*100;
        if (shot > 0 && accuracy >= 10 && level >= 3) {
            markAchievementAsAchieved("sharp shooter");
        }
    }
}
