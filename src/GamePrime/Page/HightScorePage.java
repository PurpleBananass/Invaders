package GamePrime.Page;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import EnginePrime.FileManager;
import EnginePrime.GManager;
import EnginePrime.GameManager;
import EnginePrime.SoundManager;
import GamePrime.DatabaseAPI;
import GamePrime.ETC.Score;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.FontMetrics;

public class HightScorePage implements GManager {
    public static final int MaxNum = 10;
    GameManager gm = GameManager.getInstance();
    JSONObject res = gm.GlobalData.get("Resource");
    List<Score> scoreList[] = new List[] { new ArrayList<>(), new ArrayList<>() };
    private SoundManager.PlayProp menuSoundProp;

    public void Initialize() {
        JSONObject BGM = (JSONObject) res.get("BGM");
        JSONObject SFX = (JSONObject) res.get("SFX");
        menuSoundProp = gm.Sm.new PlayProp(
                "Sound" + File.separator + "SFX" + File.separator + (String) SFX.get("MenuSelect"), null);
        SoundManager.PlayProp BgmProp = gm.Sm.new PlayProp(
                "Sound" + File.separator + "BGM" + File.separator + (String) BGM.get("HighScorePage"), "BGM");
        BgmProp.count = -1;
        gm.Sm.StopClip("BGM", 1);
        gm.Sm.playSound(BgmProp);
        FileManager fm = new FileManager();



        JSONArray scores = DatabaseAPI.GetRank("");
        List<Score> scoreList = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            scoreList.add(Score.toScore((JSONObject) scores.get(i)));
        }
        JSONObject PlayData = (JSONObject) GameManager.getInstance().GlobalData.get("LocalData").get("PlayData");
        int point = ((Number) PlayData.get("Point")).intValue();
        scoreList.add(new Score((String) GameManager.getInstance().GlobalData.get("LocalData").get("Player"), point));
        Collections.sort(scoreList, Collections.reverseOrder());
        if (scoreList.size() > 10) {
            scoreList.remove(scoreList.size() - 1);
        }
        int rankIndex = 1;
        scores = new JSONArray();
        for (Score s : scoreList) {
            s.rank = rankIndex;
            rankIndex++;
        }
    };

    public void Exit() {
        gm.Sm.StopClip("BGM");
    };

    public void PreUpdate() {
        Draw();
    };

    public void PreRender() {
    };

    public void LateRender() {
    };

    public void LateUpdate() {
        if (gm.Im.isKeyDown(KeyEvent.VK_ESCAPE)) {
            gm.Sm.playSound(menuSoundProp);
            gm.SetInstance(new MenuPage());
        }
    };

    private void Draw() {
        String highScoreString = "High Scores";
        String instructionsString = "Press Space to return";
        String gameMode_1 = "1P_Mode";
        String gameMode_2 = "2P_Mode";
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        FontMetrics fontmatrix = gm.Rm.SetFont("Big");
        grpahics.setColor(Color.GREEN);
        grpahics.drawString(highScoreString, gm.frame.getWidth() / 2 - fontmatrix.stringWidth(highScoreString) / 2,
                gm.frame.getHeight() / 8);
        fontmatrix = gm.Rm.SetFont("Regular");
        grpahics.setColor(Color.GRAY);
        grpahics.drawString(instructionsString,
                gm.frame.getWidth() / 2 - fontmatrix.stringWidth(instructionsString) / 2, gm.frame.getHeight() / 5);
        grpahics.drawString(gameMode_1, gm.frame.getWidth() * 3 / 13 - fontmatrix.stringWidth(gameMode_1) / 2,
                gm.frame.getHeight() * 4 / 15);
        grpahics.drawString(gameMode_2, gm.frame.getWidth() * 10 / 13 - fontmatrix.stringWidth(gameMode_2) / 2,
                gm.frame.getHeight() * 4 / 15);
        grpahics.setColor(Color.WHITE);
        String scoreString = "";
        for (int i = 0; i < scoreList.length; i++) {
            for (int j = 0; j < scoreList[i].size(); j++) {
                Score score = scoreList[i].get(j);
                scoreString = String.format("%s        %04d", score.name, score.value);
                grpahics.drawString(scoreString,
                        gm.frame.getWidth() * (3 + i * 7) / 13 - fontmatrix.stringWidth(scoreString) / 2,
                        gm.frame.getHeight() / 4 + fontmatrix.getHeight() * (j + 1) * 2);
            }
        }
    }
}
