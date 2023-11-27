package GamePrime.Page;

import EnginePrime.FileManager;
import EnginePrime.GManager;
import EnginePrime.GameManager;
import EnginePrime.SoundManager;
import GamePrime.Define.AchievDefine;
import GamePrime.Define.ItemDefine;
import GamePrime.Define.KeyDefine;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.awt.FontMetrics;

public class LoginPage implements GManager {
    public void PreRender() {
    };

    public void LateRender() {
    };

    GameManager gm = GameManager.getInstance();
    JSONObject res = gm.GlobalData.get("Resource");
    private char[] name;
    private int index;
    private final int FIRST_CHAR = 65;
    private final int LAST_CHAR = 90;
    private SoundManager.PlayProp menuSoundProp;

    public void Initialize() {
        name = "AAA".toCharArray();
        index = 0;
        JSONObject SFX = (JSONObject) res.get("SFX");
        String menu = (String) SFX.get("MenuSelect");
        menuSoundProp = gm.Sm.new PlayProp("Sound" + File.separator + "SFX" + File.separator + menu, null);
    }

    public void PreUpdate() {
        if (gm.Im.isKeyDown(KeyEvent.VK_RIGHT)) {
            index = (index == 2 ? 0 : index + 1);
            gm.Sm.playSound(menuSoundProp);
        }
        if (gm.Im.isKeyDown(KeyEvent.VK_LEFT)) {
            index = (index == 0 ? 2 : index - 1);
            gm.Sm.playSound(menuSoundProp);
        }
        if (gm.Im.isKeyDown(KeyEvent.VK_UP)) {
            name[index] = (char) (name[index] == LAST_CHAR ? FIRST_CHAR : name[index] + 1);
            gm.Sm.playSound(menuSoundProp);
        }
        if (gm.Im.isKeyDown(KeyEvent.VK_DOWN)) {
            name[index] = (char) (name[index] == FIRST_CHAR ? LAST_CHAR : name[index] - 1);
            gm.Sm.playSound(menuSoundProp);
        }
        Draw();
    }

    public void LateUpdate() {
        if (gm.Im.isKeyDown(KeyEvent.VK_SPACE)) {
            gm.GlobalData.get("LocalData").put("Player", new String(name));
            LoadSetting();
            gm.SetInstance(new MenuPage());
        }
    }

    private JSONObject GenUserData() {
        JSONObject data = new JSONObject();
        JSONObject Setting = new JSONObject();
        Setting.put("Volume", 0);
        Setting.put("IsMute", false);
        JSONArray KeySetting_1p = new JSONArray();
        JSONArray KeySetting_2p = new JSONArray();
        for (int i = 0; i < KeyDefine.KeyFunc.length; i++) {
            KeySetting_1p.add(i);
            KeySetting_2p.add(i + 8);
        }
        Setting.put("KeySetting_1p", KeySetting_1p);
        Setting.put("KeySetting_2p", KeySetting_2p);
        JSONObject item = new JSONObject();
        for (ItemDefine.IntPair pair : ItemDefine.StoreItem) {
            item.put(pair.name, false);
        }
        data.put("Setting", Setting);
        data.put("StoreItem", item);
        data.put("Money", 0);
        JSONObject achieve = new JSONObject();
        for (String name : AchievDefine.Achieve) {
            achieve.put(name, false);
        }
        data.put("Achievement", achieve);
        return data;
    }

    private void LoadSetting() {
        FileManager fm = new FileManager();
        JSONObject database = fm.LoadJsonObject("DataBase");
        JSONObject scores = (JSONObject) database.get("Scores");
        if (scores == null) {
            scores = new JSONObject();
            scores.put("Scores_1p", new JSONArray());
            scores.put("Scores_2p", new JSONArray());
            database.put("Scores", scores);
            fm.SaveString("DataBase", database.toJSONString(), true);
        }
        JSONObject UserData = (JSONObject) database.get(new String(name));
        if (UserData == null) {
            UserData = GenUserData();
            database.put(new String(name), UserData);
            fm.SaveString("DataBase", database.toJSONString(), true);
        }
        gm.GlobalData.get("LocalData").put("Money", ((Number) UserData.get("Money")).intValue());
        gm.GlobalData.get("LocalData").put("StoreItem", UserData.get("StoreItem"));
        gm.GlobalData.get("LocalData").put("Achievement", UserData.get("Achievement"));
        gm.GlobalData.put("Setting", (JSONObject) UserData.get("Setting"));
    }

    private void Draw() {
        String s = "Username:";
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        grpahics.setColor(Color.WHITE);
        FontMetrics fontmatrix = gm.Rm.SetFont("Regular");
        grpahics.drawString(s, gm.frame.getWidth() / 2 - fontmatrix.stringWidth(s) / 2,
                gm.frame.getHeight() / 4 + fontmatrix.getHeight() * 3);
        // 3 letters name.
        int positionX = gm.frame.getWidth() / 2 - (fontmatrix.getWidths()[name[0]] + fontmatrix.getWidths()[name[1]]
                + fontmatrix.getWidths()[name[2]] + fontmatrix.getWidths()[' ']) / 2;
        for (int i = 0; i < 3; i++) {
            if (i == index)
                grpahics.setColor(Color.GREEN);
            else
                grpahics.setColor(Color.WHITE);
            positionX += fontmatrix.getWidths()[name[i]] / 2;
            positionX = i == 0 ? positionX
                    : positionX + (fontmatrix.getWidths()[name[i - 1]] + fontmatrix.getWidths()[' ']) / 2;
            grpahics.drawString(Character.toString(name[i]), positionX,
                    gm.frame.getHeight() / 4 + fontmatrix.getHeight() * 6);
        }
    }

    public void Exit() {
    };
}
