package GamePrime.Page;

import EnginePrime.Core;
import EnginePrime.Entity;
import EnginePrime.EventSystem;
import EnginePrime.FileManager;
import EnginePrime.GManager;
import EnginePrime.GameManager;
import EnginePrime.SoundManager;
import GamePrime.Image;
import GamePrime.KeyDefine;
import GamePrime.PrepareUI;
import GamePrime.Ship.Bullet;
import GamePrime.Ship.EnemyController;
import GamePrime.Ship.Player;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.FontMetrics;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.awt.image.BufferedImage;

public class GamePage implements GManager {

    GameManager gm = GameManager.getInstance();
    int PlayMode;
    boolean HardMode;
    public void PreRender(){};
    public void LateRender(){
        Draw();
    };
    public Map<String, Image> ImgRes = new HashMap<>();

    public JSONObject PlayData;
    EnemyController enemycontrol;
    Player player1;
    Player player2;

    public void Initialize() {
        PlayMode = ((Number) gm.GlobalData.get("LocalData").get("PlayMode")).intValue();
        HardMode = (boolean) gm.GlobalData.get("LocalData").get("HardMode");
        JSONObject ItemData = (JSONObject) gm.GlobalData.get("LocalData").get("Item");
        PlayData = (JSONObject) gm.GlobalData.get("LocalData").get("PlayData");
        if (((Number) PlayData.get("Level")).intValue() == 1) {
            FileManager fm = new FileManager();
            ImgRes.put("Magic", new Image(fm.GetImage("res" + File.separator + "Img" + File.separator + "Magic.png")));
            ImgRes.put("Magic2",
                    new Image(fm.GetImage("res" + File.separator + "Img" + File.separator + "Magic2.png")));
            ImgRes.put("Reimu", new Image(fm.GetImage("res" + File.separator + "Img" + File.separator + "Reimu.png")));
            ImgRes.put("Marisa",
                    new Image(fm.GetImage("res" + File.separator + "Img" + File.separator + "Marisa.png")));
            ImgRes.put("VioletCloud",
                    new Image(fm.GetImage("res" + File.separator + "Img" + File.separator + "VioletCloud.png")));
            ImgRes.put("Flan_Fuck",
                    new Image(fm.GetImage("res" + File.separator + "Img" + File.separator + "Flan_Fuck.png")));
            ImgRes.put("Flandre",
                    new Image(fm.GetImage("res" + File.separator + "Img" + File.separator + "Flandre.png")));
            ImgRes.put("Cirno", new Image(fm.GetImage("res" + File.separator + "Img" + File.separator + "Cirno.png")));
            PlayData.put("Life", 3);
            PlayData.put("Level", 1);
            PlayData.put("ImgHeight", 70);
            PlayData.put("LevelClear",false);


        }
        PlayData.put("MoveSpeed", 100);
        PlayData.put("ShotDelay", 1.5f);
        PlayData.put("ShotSpeed", 400.0f);
        PlayData.put("ScreenIndex", -1);
        if ((boolean) ItemData.get("BonusLife")) {
            ItemData.put("BonusLife", false);
            PlayData.put("Life", ((Number) PlayData.get("Life")).intValue() + 1);
        }
        if ((boolean) ItemData.get("MoveSpeed")) {
            ItemData.put("MoveSpeed", false);
            PlayData.put("MoveSpeed", ((Number) PlayData.get("MoveSpeed")).intValue() + 200);
        }
        if ((boolean) ItemData.get("ShotSpeed")) {
            ItemData.put("ShotSpeed", false);
            PlayData.put("ShotDelay", ((Number) PlayData.get("ShotDelay")).floatValue() - 0.5f);
            PlayData.put("ShotSpeed", ((Number) PlayData.get("ShotSpeed")).floatValue() + 200.f);
        }
        EntityInitialize();
    };

    private void EntityInitialize() {
        EventSystem.DestroyAll();
        Entity player1Entity = EventSystem.Initiate("Player1");
        player1 = player1Entity.AddComponent(Player.class);
        if (PlayMode == 1) {
            Entity player2Entity = EventSystem.Initiate("Player2");
            player2 = player2Entity.AddComponent(Player.class);
        }
        Entity control = EventSystem.Initiate("EnemyController");
        enemycontrol = control.AddComponent(EnemyController.class);
    }

    private void ProcCollision() {
        for (Entity bulletEntity : EventSystem.FindTagEntities("PBullet")) {
            Bullet bullet = bulletEntity.GetComponent(Bullet.class);
            if (bullet.pos.getY() > gm.frame.getHeight() || bullet.pos.getY() < 0) {
                EventSystem.Destroy(bullet.Obj);
            }
            enemycontrol.CheckCollsion(bullet);
        }
        for (Entity bulletEntity : EventSystem.FindTagEntities("EBullet")) {
            Bullet bullet = bulletEntity.GetComponent(Bullet.class);
            if (bullet.pos.getY() > gm.frame.getHeight() || bullet.pos.getY() < 0) {
                EventSystem.Destroy(bullet.Obj);
            }
            player1.CheckCollsion(bullet);
            if (PlayMode == 1) {
                player2.CheckCollsion(bullet);
            }
        }
    }

    public void PreUpdate() {
        if (((Number) PlayData.get("ScreenIndex")).intValue() == 3) {
            EventSystem.DestroyAll();
            

        } else {
            if (((Number) PlayData.get("ScreenIndex")).intValue() == 0) {
                ProcCollision();

            }
            if (gm.Im.isKeyDown(KeyEvent.VK_CONTROL)) {
                if (((Number) PlayData.get("ScreenIndex")).intValue() == 1) {
                    PlayData.put("ScreenIndex", 0);
                } else {
                    PlayData.put("ScreenIndex", 1);
                }
            } else if (gm.Im.isKeyDown(KeyEvent.VK_ESCAPE)) {
                if (((Number) PlayData.get("ScreenIndex")).intValue() == 2) {
                    PlayData.put("ScreenIndex", 0);
                } else {
                    PlayData.put("ScreenIndex", 2);
                }
            } else if (gm.Im.isKeyDown(KeyEvent.VK_SPACE)) {
                if (((Number) PlayData.get("ScreenIndex")).intValue() == 2) {
                    PlayData.put("ScreenIndex", 3);
                }
            }
        }
    }

    private void Quit() {
        EventSystem.DestroyAll();
        // save Data
        gm.SetInstance(new MenuPage());
    }

    public void LateUpdate() {
        
    };

    


    private void drawHorizontalLine(int y) {
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        grpahics.drawLine(0, y, gm.frame.getWidth(), y);
        grpahics.drawLine(0, y + 1, gm.frame.getWidth(), y + 1);
    }

    private void DrawMenual() {

        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        int rectWidth = gm.frame.getWidth();
        int rectHeight = gm.frame.getHeight() / 6;
        grpahics.setColor(Color.BLACK);
        grpahics.fillRect(0, gm.frame.getHeight() / 2 - gm.frame.getHeight() / 12 - 90,
                rectWidth, rectHeight + 180);
        grpahics.setColor(Color.CYAN);
        drawManualMenu();
        drawHorizontalLine(gm.frame.getHeight() / 2 - gm.frame.getHeight() / 12 - 90);
        drawHorizontalLine(gm.frame.getHeight() / 2 - gm.frame.getHeight() / 12 - 50);
        drawHorizontalLine(gm.frame.getHeight() / 2 + gm.frame.getHeight() / 12 + 90);
    }

    private void drawManualMenu() {
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        FontMetrics fontmatrix = gm.Rm.SetFont("Regular");
        JSONArray key1 = (JSONArray) gm.GlobalData.get("Setting").get("KeySetting_1p");
        JSONArray key2 = (JSONArray) gm.GlobalData.get("Setting").get("KeySetting_2p");

        grpahics.drawString("Play manual", gm.frame.getWidth() / 2
                - fontmatrix.stringWidth("Play manual") / 2, gm.frame.getHeight() / 2 - 105);
        grpahics.drawString("Player1", gm.frame.getWidth() / 2 - 140, gm.frame.getHeight() / 2 - 60);
        grpahics.drawString("Player2", gm.frame.getWidth() / 2 + 65, gm.frame.getHeight() / 2 - 60);

        grpahics.setColor(Color.WHITE);
        int y = gm.frame.getHeight() / 2 - 30;
        int x1 = gm.frame.getWidth() / 2 - 150; // player1_manual
        int x2 = gm.frame.getWidth() / 2 - 50; // player1_setting
        int x3 = gm.frame.getWidth() / 2 + 50; // player2
        int x4 = gm.frame.getWidth() / 2 + 150; // player2_setting
        for (int i = 0; i < KeyDefine.KeyFunc.length; i++) {
            grpahics.drawString(KeyDefine.KeyFunc[i], x1 - fontmatrix.stringWidth(KeyDefine.KeyFunc[i]) / 2,
                    y + 20 * i);
            String key = KeyEvent.getKeyText(((Number) key1.get(i)).intValue());
            grpahics.drawString(key, x2 - fontmatrix.stringWidth(key) / 2, y + 20 * i);
            grpahics.drawString(KeyDefine.KeyFunc[i], x3 - fontmatrix.stringWidth(KeyDefine.KeyFunc[i]) / 2,
                    y + 20 * i);
            key = KeyEvent.getKeyText(((Number) key2.get(i)).intValue());
            grpahics.drawString(key, x4 - fontmatrix.stringWidth(key) / 2, y + 20 * i);
        }
    }

    private void Draw() {
        if (((Number) PlayData.get("ScreenIndex")).intValue() == -1) {
            Entity cooldown = EventSystem.FindEntity("CountDown");
            if (cooldown == null) {
                Entity countdown = EventSystem.Initiate("CountDown");
                countdown.AddComponent(PrepareUI.class);
            }
        } else if (((Number) PlayData.get("ScreenIndex")).intValue() == 1) {
            DrawMenual();
        } else if (((Number) PlayData.get("ScreenIndex")).intValue() == 2) {

            Graphics grpahics = gm.Rm.GetCurrentGraphic();
            int rectWidth = gm.frame.getWidth();
            int rectHeight = gm.frame.getHeight() / 6;
            grpahics.setColor(Color.BLACK);
            grpahics.fillRect(0, gm.frame.getHeight() / 2 - gm.frame.getHeight() / 12 - 40, rectWidth, rectHeight + 40);

            grpahics.setColor(Color.YELLOW);
            FontMetrics fontmatrix = gm.Rm.SetFont("Regular");
            grpahics.drawString("Quit", gm.frame.getWidth() / 2 - fontmatrix.stringWidth("Quit") / 2,
                    gm.frame.getHeight() / 2 - 10);

            drawHorizontalLine(gm.frame.getHeight() / 2 - gm.frame.getHeight() / 12 - 40);
            drawHorizontalLine(gm.frame.getHeight() / 2 - gm.frame.getHeight() / 12);
            drawHorizontalLine(gm.frame.getHeight() / 2 + gm.frame.getHeight() / 12);
        }
    }
}
