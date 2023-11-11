package GamePrime.Page;

import EnginePrime.GManager;
import EnginePrime.GameManager;
import EnginePrime.FileManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import EnginePrime.SoundManager;
import GamePrime.ETC.Score;
import GamePrime.Ship.EnemyController;
import java.awt.event.KeyEvent;
import java.io.File;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.FontMetrics;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
public class EndPage implements GManager{
    int SelectIndex;
    GameManager gm = GameManager.getInstance();
    JSONObject PlayData;
    SoundManager.PlayProp menuSoundProp;
    public void Initialize(){
        SelectIndex = 0;
        PlayData =  (JSONObject)gm.GlobalData.get("LocalData").get("PlayData");
        menuSoundProp = gm.Sm.new PlayProp(
                "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_MenuClick.wav", null);

        if(!(boolean)PlayData.get("LevelClear")){
           SoundManager.PlayProp GameOverSoundProp = gm.Sm.new PlayProp(
                "res" + File.separator + "Sound" + File.separator + "BGM" + File.separator + "B_gameover.wav", "BGM");
            GameOverSoundProp.count = -1;
            gm.Sm.stopClip("BGM",1);
            gm.Sm.playSound(GameOverSoundProp);
            gm.Sm.playSound( gm.Sm.new PlayProp(
                "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_gameover.wav",null));
        }else{
             gm.Sm.playSound(gm.Sm.new PlayProp(
                "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_LevelClear.wav", null));
        }


    };

    public void PreUpdate(){

        if (gm.Im.isKeyDown(KeyEvent.VK_UP)) {
            SelectIndex = SelectIndex == 1 ? 0: 1;
        }
        if ( gm.Im.isKeyDown(KeyEvent.VK_DOWN)) {
            SelectIndex = SelectIndex == 0 ? 1: 0;
        }
        if (gm.Im.isKeyDown(KeyEvent.VK_SPACE)){
            int level = ((Number) PlayData.get("Level")).intValue();
            if(level < EnemyController.MaxStageCount && SelectIndex == 0 &&(boolean)PlayData.get("LevelClear")){
                PlayData.put("Level",level+1);
                gm.SetInstance(new GamePage());
                return;
            }
            SavePlayData();
            gm.SetInstance(new MenuPage());
        }
    };

    public static void SavePlayData(){
        FileManager fm = new FileManager();
        JSONObject SaveData = fm.LoadJsonObject("DataBase");
        String name = (String) GameManager.getInstance().GlobalData.get("LocalData").get("Player");
        JSONObject Userdata = (JSONObject) SaveData.get(name); 
        if (Userdata == null) {
            Userdata = new JSONObject();
            SaveData.put(name, Userdata);
        }
        JSONObject PlayData = (JSONObject) GameManager.getInstance().GlobalData.get("LocalData").get("PlayData");
        float point = ((Number) PlayData.get("Point")).floatValue();
        point = point/100;
        if(point>0){
            int money = ((Number)Userdata.get("Money")).intValue()+(int)point;
            GameManager.getInstance().GlobalData.get("LocalData").put("Money",money);
            Userdata.put("Money", money);
        }
        Userdata.put("StoreItem",GameManager.getInstance().GlobalData.get("LocalData").get("StoreItem"));
        AchievementPage.checkAchievements();
        Userdata.put("Achievement", (JSONObject)GameManager.getInstance().GlobalData.get("LocalData").get("Achievement"));
        fm.SaveString("DataBase", SaveData.toJSONString(), true);
        if (((Number) GameManager.getInstance().GlobalData.get("LocalData").get("PlayMode")).intValue()==1){
            SaveScore("Scores_2p");
        }else{
            SaveScore("Scores_1p");
        }
    }
    static void SaveScore(String scoreAttr) {
        List<Score> scoreList = new ArrayList<>();
        FileManager fm = new FileManager();
        JSONObject database = fm.LoadJsonObject("DataBase");
        JSONObject Scoreobj = (JSONObject) database.get("Scores");
        JSONArray scores = (JSONArray)Scoreobj.get(scoreAttr);

        for (int i = 0; i < scores.size(); i++) {
            scoreList.add(Score.toScore((JSONObject)scores.get(i)));
        }

        JSONObject PlayData = (JSONObject) GameManager.getInstance().GlobalData.get("LocalData").get("PlayData");
        int point = ((Number) PlayData.get("Point")).intValue();
        scoreList.add(new Score((String) GameManager.getInstance().GlobalData.get("LocalData").get("Player"), point));
        Collections.sort(scoreList, Collections.reverseOrder());
        if (scoreList.size() > 10) {
            scoreList.remove(scoreList.size() - 1);
        }
        scores = new JSONArray();
        Scoreobj.put(scoreAttr,scores);
        for (Score s : scoreList) {
            scores.add(s.toJSON());
        }
        fm.SaveString("DataBase", database.toJSONString(), true);
    };
    public void LateUpdate(){

    };

    public void PreRender(){
    };


	public void drawCenteredRegularString(final String string,final int height) {

        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        FontMetrics fontmatrix = gm.Rm.SetFont("Regular");
		grpahics.drawString(string, gm.frame.getWidth() / 2
				- fontmatrix.stringWidth(string) / 2, height);
	}

	public void drawCenteredBigString(final String string,final int height) {

        Graphics grpahics = gm.Rm.GetCurrentGraphic();
		grpahics.setColor(Color.GREEN);
        FontMetrics fontmatrix = gm.Rm.SetFont("Big");
		grpahics.drawString(string, gm.frame.getWidth() / 2
				- fontmatrix.stringWidth(string) / 2, height);
	}

	public void drawClear() {
		String titleString ;
        if((boolean)PlayData.get("LevelClear")){
            titleString = "LEVEL  " + ((Number) PlayData.get("Level")).intValue() + "  Clear";
        }else{
            titleString = "Game Over..";
        }
        
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        FontMetrics fontmatrix = gm.Rm.SetFont("Big");
		grpahics.setColor(Color.GREEN);
		drawCenteredBigString(titleString, gm.frame.getHeight() / 3 +  fontmatrix.getHeight() * 2);

		String continueString = "Continue";
		String exitString = "Exit";
		if (SelectIndex == 0){
			grpahics.setColor(Color.GREEN);
        }else{
			grpahics.setColor(Color.WHITE);
        }
        int level = ((Number) PlayData.get("Level")).intValue();
        if(level < 3 && (boolean)PlayData.get("LevelClear")){
            drawCenteredRegularString(continueString, gm.frame.getHeight() / 4 * 3);
        }
		if (SelectIndex == 1 || !(boolean)PlayData.get("LevelClear")|| level== EnemyController.MaxStageCount ){
			grpahics.setColor(Color.GREEN);
        }else{
			grpahics.setColor(Color.WHITE);
    	}   
        drawCenteredRegularString(exitString,gm.frame.getHeight() / 4 * 3 + fontmatrix.getHeight() * 2);
    }

    public void Exit(){};
	public void drawHorizontalLine(final int positionY, Color color) {
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
		grpahics.setColor(color);
		grpahics.drawLine(0, positionY, gm.frame.getWidth(), positionY);
		grpahics.drawLine(0, positionY + 1, gm.frame.getWidth(),positionY + 1);
	}

    void drawLives(int life){
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        grpahics.setColor(Color.WHITE);
        FontMetrics fontmatrix = gm.Rm.SetFont("Regular");
		grpahics.drawString(Integer.toString(life), 20, 25);
    }

    void DrawScore(){
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        grpahics.setColor(Color.WHITE);
        FontMetrics fontmatrix = gm.Rm.SetFont("Regular");
        String scoreString = String.format("%04d",((Number) PlayData.get("Point")).intValue());
		grpahics.drawString(scoreString, gm.frame.getWidth() - 60, 25);
    }

    public void LateRender(){
        DrawScore();
        drawLives(((Number) PlayData.get("Life")).intValue());
        int PlayMode = ((Number) gm.GlobalData.get("LocalData").get("PlayMode")).intValue();
        if (PlayMode == 1){
            drawLives(((Number) PlayData.get("Life")).intValue());
        }
        drawHorizontalLine( 40 - 1, Color.GREEN);
        drawClear();  
    };
}
