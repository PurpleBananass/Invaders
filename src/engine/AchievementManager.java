package engine;

import java.util.HashMap;
import java.util.Map;

public class AchievementManager {
  
    /** Singleton instance of the class. */
    private static AchievementManager instance;
  
    // Map to store achievements and their completion status
    private Map<String, Boolean> achievements;

    private AchievementManager() {
        achievements = new HashMap<>();
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
     * Add a new achievement to the manager.
     *
     * @param achievementName The name of the achievement.
     */
    public void addAchievement(String achievementName) {
        achievements.put(achievementName, false);
    }

    /**
     * Mark an achievement as achieved.
     *
     * @param achievementName The name of the achievement to mark as achieved.
     */
    public void markAchievementAsAchieved(String achievementName) {
        if (achievements.containsKey(achievementName)) {
            achievements.put(achievementName, true);
        }
    }

    /**
     * Check if an achievement has been achieved.
     *
     * @param achievementName The name of the achievement to check.
     * @return true if the achievement is achieved, false otherwise.
     */
    public boolean isAchievementAchieved(String achievementName) {
        if (achievements.containsKey(achievementName)) {
            return achievements.get(achievementName);
        }
        return false;
    }
}
