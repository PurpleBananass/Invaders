package GamePrime.Page;

import EnginePrime.Entity;
import EnginePrime.EventSystem;
import EnginePrime.FileManager;
import EnginePrime.GManager;
import EnginePrime.GameManager;
import GamePrime.Define.ItemDefine;
import GamePrime.Define.KeyDefine;
import GamePrime.ETC.Image;
import GamePrime.Ship.Bomb;
import GamePrime.Ship.Bullet;
import GamePrime.Ship.EnemyController;
import GamePrime.Ship.Item;
import GamePrime.Ship.Player;
import GamePrime.UI.PrepareUI;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.KeyEvent;
import java.io.File;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.FontMetrics;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class GamePage implements GManager {
    GameManager gm = GameManager.getInstance();
    JSONObject res = gm.GlobalData.get("Resource");
    public JSONObject PlayData;
    public int PlayMode;
    public boolean HardMode;
    public Map<String, Image> ImgRes = new HashMap<>();
    EnemyController enemycontrol;
    public Player player1;
    Player player2;

    public void Initialize() {
        gm.Sm.StopAll();
        PlayMode = ((Number) gm.GlobalData.get("LocalData").get("PlayMode")).intValue();
        HardMode = (boolean) gm.GlobalData.get("LocalData").get("HardMode");
        PlayData = (JSONObject) gm.GlobalData.get("LocalData").get("PlayData");
        FileManager fm = new FileManager();
        JSONObject Entity =  (JSONObject)res.get("Entity");
        JSONObject ShipType1 = (JSONObject)Entity.get("ShipType1");
        JSONObject ShipType2 = (JSONObject)Entity.get("ShipType2");
        JSONObject EnemyType1 = (JSONObject)Entity.get("EnemyType1");
        JSONObject EnemyType2 = (JSONObject)Entity.get("EnemyType2");
        ImgRes.put("Bullet", new Image(fm.GetImage("Img" + File.separator + Entity.get("Bullet"))));
        ImgRes.put("Item", new Image(fm.GetImage("Img" + File.separator + Entity.get("Item"))));
        ImgRes.put("ShipType1.Idle", new Image(fm.GetImage("Img" + File.separator + ShipType1.get("Idle"))));
        ImgRes.put("ShipType1.Destroyed", new Image(fm.GetImage("Img" + File.separator + ShipType1.get("Destroyed"))));
        ImgRes.put("ShipType2.Idle", new Image(fm.GetImage("Img" + File.separator + ShipType2.get("Idle"))));
        ImgRes.put("ShipType2.Destroyed", new Image(fm.GetImage("Img" + File.separator + ShipType2.get("Destroyed"))));
        ImgRes.put("EnemyType1.Idle", new Image(fm.GetImage("Img" + File.separator + EnemyType1.get("Idle"))));
        ImgRes.put("EnemyType1.Destroyed", new Image(fm.GetImage("Img" + File.separator + EnemyType1.get("Destroyed"))));
        ImgRes.put("EnemyType2.Idle", new Image(fm.GetImage("Img" + File.separator + EnemyType2.get("Idle"))));
        ImgRes.put("EnemyType2.Destroyed", new Image(fm.GetImage("Img" + File.separator + EnemyType2.get("Destroyed"))));
        PlayData.put("ScreenIndex", -1);
        PlayData.put("LevelClear", false);
        PlaySetting();
        EntityInitialize();
    };

    public void PlaySetting() {
        if (((Number) PlayData.get("Level")).intValue() == 1) {
            PlayData.put("Life", 3);
            PlayData.put("Life2", 3);
            PlayData.put("Level", 1);
            PlayData.put("ImgHeight", 70);
            PlayData.put("Point", 0);
            PlayData.put("MoveSpeed", 200);
            PlayData.put("ShotDelay", 1.5f);
            PlayData.put("ShotSpeed", 400.0f);
            PlayData.put("ShotCount", 0);
            PlayData.put("ShotCount2", 0);
            PlayData.put("KillCount", 0);
            PlayData.put("KillCount2", 0);

            JSONObject ItemData = (JSONObject) gm.GlobalData.get("LocalData").get("StoreItem");
            if (ItemData != null) {
                if ((boolean) ItemData.get("BonusLife")) {
                    ItemData.put("BonusLife", false);
                    PlayData.put("Life", ((Number) PlayData.get("Life")).intValue() + 1);
                    PlayData.put("Life2", ((Number) PlayData.get("Life2")).intValue() + 1);
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
            }
        }
        if (HardMode) {
            PlayData.put("Magazine1", 5);
            PlayData.put("Bullet1", 10);
            PlayData.put("Magazine2", 5);
            PlayData.put("Bullet2", 10);
        }
        JSONArray Item = new JSONArray();
        PlayData.put("ActiveItem", Item);
        Item = new JSONArray();
        PlayData.put("ActiveItem2", Item);
    }

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

        for (Entity ItemEntity : EventSystem.FindTagEntities("Item")) {
            Item item = ItemEntity.GetComponent(Item.class);
            if (item.pos.getY() > gm.frame.getHeight()) {
                EventSystem.Destroy(item.Obj);
            }
            player1.CheckCollsion(item);
            if (PlayMode == 1) {
                player2.CheckCollsion(item);
            }
        }

        for (Entity bulletEntity : EventSystem.FindTagEntities("PBullet")) {
            Bullet bullet = bulletEntity.GetComponent(Bullet.class);
            if (bullet == null) {
                bullet = bulletEntity.GetComponent(Bomb.class);
            }
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
            gm.SetInstance(new EndPage());
        } else if (((Number) PlayData.get("ScreenIndex")).intValue() != -1) {
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
                    EventSystem.DestroyAll();
                    EndPage.SavePlayData();
                    gm.SetInstance(new MenuPage());
                }
            }
        }
    }

    public void LateUpdate() {
        int life = ((Number) PlayData.get("Life")).intValue();
        int life2 = ((Number) PlayData.get("Life2")).intValue();
        if (PlayMode == 1) {
            if (life == 0 && life2 == 0) {
                PlayData.put("ScreenIndex", 3);
            }
        } else {
            if (life == 0) {
                PlayData.put("ScreenIndex", 3);
            }
        }
    };

    private void drawHorizontalLine(int y) {
        Graphics graphics = gm.Rm.GetCurrentGraphic();
        graphics.drawLine(0, y, gm.frame.getWidth(), y);
        graphics.drawLine(0, y + 1, gm.frame.getWidth(), y + 1);
    }

    private void DrawMenual() {
        Graphics graphics = gm.Rm.GetCurrentGraphic();
        int rectWidth = gm.frame.getWidth();
        int rectHeight = gm.frame.getHeight() / 6;
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, gm.frame.getHeight() / 2 - gm.frame.getHeight() / 12 - 90,
                rectWidth, rectHeight + 180);
        graphics.setColor(Color.CYAN);
        drawManualMenu();
        drawHorizontalLine(gm.frame.getHeight() / 2 - gm.frame.getHeight() / 12 - 90);
        drawHorizontalLine(gm.frame.getHeight() / 2 - gm.frame.getHeight() / 12 - 50);
        drawHorizontalLine(gm.frame.getHeight() / 2 + gm.frame.getHeight() / 12 + 90);
    }

    private void drawManualMenu() {
        Graphics graphics = gm.Rm.GetCurrentGraphic();
        FontMetrics fontmatrix = gm.Rm.SetFont("Regular");
        JSONArray key1 = (JSONArray) gm.GlobalData.get("Setting").get("KeySetting_1p");
        JSONArray key2 = (JSONArray) gm.GlobalData.get("Setting").get("KeySetting_2p");

        graphics.drawString("Play manual", gm.frame.getWidth() / 2
                - fontmatrix.stringWidth("Play manual") / 2, gm.frame.getHeight() / 2 - 105);
        graphics.drawString("Player1", gm.frame.getWidth() / 2 - 140, gm.frame.getHeight() / 2 - 60);
        graphics.drawString("Player2", gm.frame.getWidth() / 2 + 65, gm.frame.getHeight() / 2 - 60);

        graphics.setColor(Color.WHITE);
        int y = gm.frame.getHeight() / 2 - 30;
        int x1 = gm.frame.getWidth() / 2 - 150; // player1_manual
        int x2 = gm.frame.getWidth() / 2 - 50; // player1_setting
        int x3 = gm.frame.getWidth() / 2 + 50; // player2
        int x4 = gm.frame.getWidth() / 2 + 150; // player2_setting
        for (int i = 0; i < KeyDefine.KeyFunc.length; i++) {
            graphics.drawString(KeyDefine.KeyFunc[i], x1 - fontmatrix.stringWidth(KeyDefine.KeyFunc[i]) / 2,
                    y + 20 * i);
            String key = KeyEvent.getKeyText(((Number) key1.get(i)).intValue());
            graphics.drawString(key, x2 - fontmatrix.stringWidth(key) / 2, y + 20 * i);
            graphics.drawString(KeyDefine.KeyFunc[i], x3 - fontmatrix.stringWidth(KeyDefine.KeyFunc[i]) / 2,
                    y + 20 * i);
            key = KeyEvent.getKeyText(((Number) key2.get(i)).intValue());
            graphics.drawString(key, x4 - fontmatrix.stringWidth(key) / 2, y + 20 * i);
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
            drawPause();
        }
    }

    public void PreRender() {
    };

    public void LateRender() {
        Draw();
        drawScore();
        drawItems();

        drawAmmo();
        drawLives();
        drawHorizontalLine(40 - 1, Color.GREEN);
        drawHorizontalLine(gm.frame.getHeight() - 1, Color.GREEN); // separation line for bottom hud
    };

    void drawAmmo() {
        Graphics graphics = gm.Rm.GetCurrentGraphic();
        graphics.setColor(Color.WHITE);
        FontMetrics fontmatrix = gm.Rm.SetFont("Regular");
        if (HardMode) {
            int bullet = ((Number) PlayData.get("Bullet1")).intValue();
            int magazine = ((Number) PlayData.get("Magazine1")).intValue();
            graphics.drawString("Ammo : ", 200, 0);
            graphics.drawString(bullet + "/" + magazine, 300, fontmatrix.getHeight());

            if (PlayMode == 1) {
                bullet = ((Number) PlayData.get("Bullet2")).intValue();
                magazine = ((Number) PlayData.get("Magazine2")).intValue();
                graphics.drawString("Ammo : ",
                        gm.frame.getWidth() / 4 - fontmatrix.stringWidth(bullet + "/" + magazine + "Ammo : "), 0);
                graphics.drawString(bullet + "/" + magazine, gm.frame.getWidth() / 4, fontmatrix.getHeight());
            }

        }
    }

    void drawItems() {
        Graphics graphics = gm.Rm.GetCurrentGraphic();
        graphics.setColor(Color.WHITE);
        JSONArray item = (JSONArray) PlayData.get("ActiveItem");
        graphics.drawString(Integer.toString(item.size()), 205, gm.frame.getHeight() + 25);
        for (int i = 0; i < item.size(); i++) {
            int index = ((Number) item.get(i)).intValue();
            if (ItemDefine.ActiveItem[index] == "Ghost") {
                graphics.setColor(Color.CYAN);
            } else if (ItemDefine.ActiveItem[index] == "Auxiliary") {
                graphics.setColor(Color.green);
            } else if (ItemDefine.ActiveItem[index] == "Bomb") {
                graphics.setColor(Color.red);
            } else if (ItemDefine.ActiveItem[index] == "SpeedUp") {
                graphics.setColor(Color.YELLOW);
            }
            graphics.drawRect(100 + 35 * i, gm.frame.getHeight() - 10, 5, 5);
        }

        item = (JSONArray) PlayData.get("ActiveItem2");
        graphics.drawString(Integer.toString(item.size()), 205, gm.frame.getHeight() + 25);
        for (int i = 0; i < item.size(); i++) {
            int index = ((Number) item.get(i)).intValue();
            if (ItemDefine.ActiveItem[index] == "Ghost") {
                graphics.setColor(Color.CYAN);
            } else if (ItemDefine.ActiveItem[index] == "Auxiliary") {
                graphics.setColor(Color.green);
            } else if (ItemDefine.ActiveItem[index] == "Bomb") {
                graphics.setColor(Color.red);
            } else if (ItemDefine.ActiveItem[index] == "SpeedUp") {
                graphics.setColor(Color.YELLOW);
            }
            graphics.drawRect(gm.frame.getWidth() / 2 + 100 + 35 * i, gm.frame.getHeight() - 10, 5, 5);
        }
    }

    void drawPause() {
        Graphics graphics = gm.Rm.GetCurrentGraphic();
        int rectWidth = gm.frame.getWidth();
        int rectHeight = gm.frame.getHeight() / 6;
        String informationString = "Press Esc resume or space quit";
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, gm.frame.getHeight() / 2 - gm.frame.getHeight() / 12 - 40, rectWidth, rectHeight + 40);
        graphics.setColor(Color.YELLOW);
        FontMetrics fontmatrix = gm.Rm.SetFont("Regular");
        graphics.drawString("Pause", gm.frame.getWidth() / 2 - fontmatrix.stringWidth("Pause") / 2, 
            gm.frame.getHeight() / 2 - 80);
        graphics.drawString("Quit", gm.frame.getWidth() / 2 - fontmatrix.stringWidth("Quit") / 2,
            gm.frame.getHeight() / 2 - 10);
        graphics.setColor(Color.GRAY);
        graphics.drawString(informationString, gm.frame.getWidth() / 2
            - fontmatrix.stringWidth(informationString) / 2, gm.frame.getHeight() / 2 + fontmatrix.getHeight() / 10 + 20);
        drawHorizontalLine(gm.frame.getHeight() / 2 - gm.frame.getHeight() / 12 - 40);
        drawHorizontalLine(gm.frame.getHeight() / 2 - gm.frame.getHeight() / 12);
        drawHorizontalLine(gm.frame.getHeight() / 2 + gm.frame.getHeight() / 12);
    }

    public void drawHorizontalLine(final int positionY, Color color) {
        Graphics graphics = gm.Rm.GetCurrentGraphic();
        graphics.setColor(color);
        graphics.drawLine(0, positionY, gm.frame.getWidth(), positionY);
        graphics.drawLine(0, positionY + 1, gm.frame.getWidth(), positionY + 1);
    }

    void drawLives() {
        Graphics graphics = gm.Rm.GetCurrentGraphic();
        graphics.setColor(Color.WHITE);
        FontMetrics fontmatrix = gm.Rm.SetFont("Regular");
        graphics.drawString(Integer.toString(((Number) PlayData.get("Life")).intValue()), 20, 25);
        if (PlayMode == 1) {
            graphics.drawString(Integer.toString(((Number) PlayData.get("Life2")).intValue()), 50, 25);
        }
    }

    public void Exit() {
    };

    void drawScore() {
        Graphics graphics = gm.Rm.GetCurrentGraphic();
        graphics.setColor(Color.WHITE);
        FontMetrics fontmatrix = gm.Rm.SetFont("Regular");
        String scoreString = String.format("%04d", ((Number) PlayData.get("Point")).intValue());
        graphics.drawString(scoreString, gm.frame.getWidth() - 60, 25);
    }
}
