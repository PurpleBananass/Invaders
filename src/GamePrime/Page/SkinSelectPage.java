package GamePrime.Page;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import EnginePrime.FileManager;
import EnginePrime.GManager;
import EnginePrime.GameManager;
import EnginePrime.SoundManager;
import GamePrime.Define.ShipDefine;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class SkinSelectPage implements GManager {
    GameManager gm = GameManager.getInstance();
    JSONObject res = gm.GlobalData.get("Resource");
    int SelectIndex1;
    int SelectIndex2;
    private Map<String, BufferedImage> img = new HashMap<>();
    SoundManager.PlayProp menuSoundProp;
    String imgString[] = { "ShipType1", "ShipType2" };

    public void Initialize() {
        SelectIndex1 = 0;
        SelectIndex2 = 0;
        if (gm.GlobalData.get("LocalData").get("P1_Ship") != null) {
            String prevShip1 = (String) gm.GlobalData.get("LocalData").get("P1_Ship");
            String prevShip2 = (String) gm.GlobalData.get("LocalData").get("P2_Ship");
            for (int i = 0; i < ShipDefine.Ship.length; i++) {
                if (prevShip1 == ShipDefine.Ship[i]) {
                    SelectIndex1 = i;
                }
                if (prevShip2 == ShipDefine.Ship[i]) {
                    SelectIndex2 = i;
                }
            }
        }
        FileManager fm = new FileManager();
        img.put("Reimu&Marisa", fm.GetImage(File.separator + "Img" + File.separator + "Reimu&Marisa.png"));
        JSONObject Entity = (JSONObject) res.get("Entity");
        JSONObject ShipType1 = (JSONObject) Entity.get("ShipType1");
        JSONObject ShipType2 = (JSONObject) Entity.get("ShipType2");
        img.put("ShipType1", fm.GetImage("Img" + File.separator + (String) ShipType1.get("Idle")));
        img.put("ShipType2", fm.GetImage("Img" + File.separator + (String) ShipType2.get("Idle")));
        JSONObject SFX = (JSONObject) res.get("SFX");
        menuSoundProp = gm.Sm.new PlayProp(
                "Sound" + File.separator + "SFX" + File.separator + (String) SFX.get("MenuSelect"), null);
    };

    public void PreUpdate() {
        if (gm.Im.isKeyDown(KeyEvent.VK_UP)) {
            SelectIndex1 = SelectIndex1 == 0 ? ShipDefine.Ship.length - 1 : SelectIndex1 - 1;
        }
        if (gm.Im.isKeyDown(KeyEvent.VK_DOWN)) {
            SelectIndex1 = SelectIndex1 == ShipDefine.Ship.length - 1 ? 0 : SelectIndex1 + 1;
        }
        if (gm.Im.isKeyDown(KeyEvent.VK_W)) {
            SelectIndex2 = SelectIndex2 == 0 ? ShipDefine.Ship.length - 1 : SelectIndex2 - 1;
        }
        if (gm.Im.isKeyDown(KeyEvent.VK_S)) {
            SelectIndex2 = SelectIndex2 == ShipDefine.Ship.length - 1 ? 0 : SelectIndex2 + 1;
        }
        if (gm.Im.isKeyDown(KeyEvent.VK_SPACE)) {
            gm.Sm.playSound(menuSoundProp);
            gm.GlobalData.get("LocalData").put("P1_Ship", ShipDefine.Ship[SelectIndex1]);
            gm.GlobalData.get("LocalData").put("P2_Ship", ShipDefine.Ship[SelectIndex2]);
            JSONObject PlayData = (JSONObject) gm.GlobalData.get("LocalData").get("PlayData");
            if (PlayData == null) {
                PlayData = new JSONObject();
                gm.GlobalData.get("LocalData").put("PlayData", PlayData);
            }
            PlayData.put("Level", 1);
            gm.SetInstance(new GamePage());
        }
    };

    public void Exit() {
    };

    public void PreRender() {
    };

    public void LateRender() {
    };

    public void LateUpdate() {
        Draw();
        if (gm.Im.isKeyDown(KeyEvent.VK_ESCAPE)) {
            gm.SetInstance(new SelectPage());
        }
    }

    void Draw() {
        String SkinString = "Select Your Ship Design!";
        String skin1p = "1P";
        String skin2p = "2P";
        int height = 100;
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        FontMetrics fontmatrix = gm.Rm.SetFont("Big");
        BufferedImage bg = img.get("Reimu&Marisa");
        int bgwidth = bg.getWidth() * gm.frame.getHeight() / bg.getHeight();
        int bgheight = gm.frame.getHeight();
        Graphics2D graphics2D = (Graphics2D) grpahics;
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        graphics2D.drawImage(bg, gm.frame.getWidth() / 2 - bgwidth / 2, gm.frame.getHeight() / 2 - bgheight / 2,
                bgwidth, bgheight, null);
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        int PlayMode = ((Number) gm.GlobalData.get("LocalData").get("PlayMode")).intValue();
        if (PlayMode == 1) {
            grpahics.setColor(Color.white);
            grpahics.drawLine(gm.frame.getWidth() / 2, gm.frame.getHeight() / 5, gm.frame.getWidth() / 2,
                    gm.frame.getHeight() * 4 / 5);
            grpahics.setColor(Color.WHITE);
            grpahics.drawString(skin1p, gm.frame.getWidth() / 4 - fontmatrix.stringWidth(skin1p) / 2 - 1,
                    gm.frame.getHeight() / 5);
            grpahics.drawString(skin2p, 3 * gm.frame.getWidth() / 4 - fontmatrix.stringWidth(skin1p) / 2 - 2,
                    gm.frame.getHeight() / 5);
            for (int i = 0; i < imgString.length; i++) {
                if (SelectIndex1 == i) {
                    grpahics.setColor(Color.GREEN);
                } else {
                    grpahics.setColor(Color.WHITE);
                }
                BufferedImage curimg = img.get(imgString[i]);
                int x = gm.frame.getWidth() / 4 - 13;
                int y = gm.frame.getHeight() / 4 + height * i;
                float width = curimg.getWidth() / (float) curimg.getHeight() * height;
                grpahics.drawImage(curimg, Math.round(x - width / 2), y, Math.round(width), height, null);
                grpahics.drawRect(x - height / 2, y, height, height);
                if (SelectIndex2 == i) {
                    grpahics.setColor(Color.GREEN);
                } else {
                    grpahics.setColor(Color.WHITE);
                }
                x = gm.frame.getWidth() * 3 / 4 - 13;
                grpahics.drawImage(curimg, Math.round(x - width / 2), y, Math.round(width), height, null);
                grpahics.drawRect(x - height / 2, y, height, height);
            }
        } else {
            grpahics.setColor(Color.WHITE);
            grpahics.drawString(skin1p, gm.frame.getWidth() / 2 - fontmatrix.stringWidth(skin1p) / 2 - 1,
                    gm.frame.getHeight() / 5);
            for (int i = 0; i < imgString.length; i++) {
                if (SelectIndex1 == i) {
                    grpahics.setColor(Color.GREEN);
                } else {
                    grpahics.setColor(Color.WHITE);
                }
                BufferedImage curimg = img.get(imgString[i]);
                int x = gm.frame.getWidth() / 2 - 13;
                int y = gm.frame.getHeight() / 4 + height * i;
                float width = curimg.getWidth() / (float) curimg.getHeight() * height;
                grpahics.drawImage(curimg, Math.round(x - width / 2), y, Math.round(width), height, null);
                grpahics.drawRect(x - height / 2, y, height, height);
            }
        }
        grpahics.setColor(Color.GREEN);
        grpahics.drawString(SkinString, gm.frame.getWidth() / 2 - fontmatrix.stringWidth(SkinString) / 2,
                gm.frame.getHeight() / 8);
    }
}
