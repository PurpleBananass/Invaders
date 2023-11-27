package GamePrime.Page;

import java.awt.event.KeyEvent;
import org.json.simple.JSONObject;
import java.io.File;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.FontMetrics;
import EnginePrime.GManager;
import EnginePrime.GameManager;
import EnginePrime.SoundManager;
import GamePrime.Define.AchievDefine;

public class AchievementPage implements GManager {

    GameManager gm = GameManager.getInstance();
    JSONObject res = gm.GlobalData.get("Resource");
    JSONObject PlayData;
    JSONObject Achievement;

    SoundManager.PlayProp menuSoundProp;

    public void PreRender(){};
    public void LateRender(){};
    public void Initialize() {
        JSONObject BGM = (JSONObject)res.get("BGM");
        JSONObject SFX = (JSONObject)res.get("SFX");
        menuSoundProp = gm.Sm.new PlayProp("Sound" + File.separator + "SFX" + File.separator + (String)SFX.get("MenuSelect"), null);
        SoundManager.PlayProp BgmProp = gm.Sm.new PlayProp("Sound" + File.separator + "BGM" + File.separator + (String)BGM.get("AchievePage"), "BGM");
        BgmProp.count = -1;
        gm.Sm.stopClip("BGM", 1);
        gm.Sm.playSound(BgmProp);

        Achievement = (JSONObject)gm.GlobalData.get("LocalData").get("Achievement");
        PlayData = (JSONObject) gm.GlobalData.get("LocalData").get("PlayData");
    };

    public void PreUpdate() {

    };

    public void LateUpdate() {
        Draw();
        if ((gm.Im.isKeyDown(KeyEvent.VK_ESCAPE) || gm.Im.isKeyDown(KeyEvent.VK_SPACE))) {
            gm.Sm.playSound(menuSoundProp);
            gm.SetInstance(new MenuPage());
        }
    };
    public void Exit(){};
    private void Draw() {

        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        FontMetrics fontmatrix = gm.Rm.SetFont("Regular");
        grpahics.setColor(Color.GREEN);

        int x = 20; // Fixed X-coordinate for achievement titles.
        int y = 50; // Fixed Y-coordinate for the initial position.

        for (String name : AchievDefine.Achieve) {

            grpahics.setColor(Color.YELLOW);
            grpahics.drawString(name, x, y);
            if ((boolean) Achievement.get(name)) {
                grpahics.setColor(Color.GREEN);
            } else {
                grpahics.setColor(Color.RED);
            }
            // Calculate the position to display achievementStatus (completed or incomplete)
            // on the right of achievementTitle.
            int titleWidth = fontmatrix.stringWidth(name);
            int statusX = x + titleWidth + 10; // You can adjust the spacing as needed.

            // Display whether the achievement is completed or not.
            String achievementStatus = (boolean) Achievement.get(name) ? "Completed" : "Incomplete";
            grpahics.drawString(achievementStatus, statusX, y);

            // You can add more information about the achievement if needed.
            // For example, you can display the progress or description.

            // Increase the Y-coordinate for the next achievement entry.
            y += fontmatrix.getHeight()+10;
        }
    }
    public static void checkAchievements() {
        GameManager gm = GameManager.getInstance();
        JSONObject Achievement = (JSONObject)gm.GlobalData.get("LocalData").get("Achievement");
        JSONObject PlayData = (JSONObject) gm.GlobalData.get("LocalData").get("PlayData");
		int level =((Number) PlayData.get("Level")).intValue();
		int life_1 = ((Number) PlayData.get("Life")).intValue();
		int life_2 = ((Number) PlayData.get("Life2")).intValue();
        int Score = ((Number) PlayData.get("Point")).intValue();
		int gamemode = ((Number) gm.GlobalData.get("LocalData").get("PlayMode")).intValue();
		int shot = ((Number) PlayData.get("ShotCount")).intValue() +  ((Number) PlayData.get("ShotCount2")).intValue() ;
		int shipsDestroyed =  ((Number) PlayData.get("KillCount")).intValue() + ((Number) PlayData.get("KillCount2")).intValue();
		double accuracy = ((double) shipsDestroyed / (double) shot) * 100;
        
        int LuckyScore = 770; // '7' means the lucky number
        int UnluckyScore = 440; // '4' means the unlucky number
        int AceScore = 1110; // '1' means the ace in one card
		// if the player play with good accuracy

        
            Achievement.put("ADVENTURE_START",true);

		if (shot > 0 && accuracy >= 90.0 && level >= 3) {
            
            Achievement.put("SHARP_SHOOTER",true);
		}

		// Check if the players recorded perfect accuracy, if the player want to clear
		// level 1, he has to shot 20 times at least
		if (shot >= 20 && accuracy == 100) {
            Achievement.put("DEADLY_ACCURACY",true);
		}

		// Check if the player didn't hit enemy's ships in 1p mode
		if (gamemode == 0 && shot > 0 && accuracy == 0) {
            Achievement.put("AVIOPHOBIA",true);
		}

		// Check if two players didn't hit enemy's ships
		if (gamemode == 1 && shot > 0 && accuracy == 0) {
            
            Achievement.put("PAT_AND_MAT",true);
		}

		// Check if two players clear level or gameover with same lives
		if (gamemode == 1 && life_1 == life_2) {
            Achievement.put("SOUL_MATES",true);
		}

		// Check one player has max life but the partner doesn't have
		if (gamemode == 1 && (life_1 - life_2 == 3 || life_2 - life_1 == 3)) {
            Achievement.put("BUDDY_FXXKER",true);
		}
		if (Score == LuckyScore) {
            
            Achievement.put("LUCKY_GUY",true);
		}
		else if (Score == UnluckyScore) {
            
            Achievement.put("UNLUCKY_GUY",true);
		}

		else if (Score == AceScore) {
            Achievement.put("GAME_ACE",true);
		}
	}
}
