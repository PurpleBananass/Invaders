package GamePrime.Page;

import java.awt.event.KeyEvent;
import java.io.File;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import EnginePrime.FileManager;
import EnginePrime.GManager;
import EnginePrime.GameManager;
import EnginePrime.SoundManager;
import GamePrime.Define.KeyDefine;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.FontMetrics;

public class SettingPage implements GManager {
    public void PreRender(){};
    
    public void LateRender(){};
    GameManager gm = GameManager.getInstance();
    private SoundManager.PlayProp menuSoundProp;
    private int itemCode;
    private int keyNum;
    private boolean Itemselected;
    private boolean Keyselected;

    JSONArray keySettings[];

    public void Initialize() {
        itemCode = 0;
        keyNum = 0;
        Itemselected = false;
        Keyselected = false;

        JSONObject data = gm.GlobalData.get("Setting");
        keySettings = new JSONArray[] { (JSONArray) data.get("KeySetting_1p"), (JSONArray) data.get("KeySetting_2p") };
        menuSoundProp = gm.Sm.new PlayProp(
                "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_MenuClick.wav", null);
    };

    public void PreUpdate() {
        if (Keyselected) {
            int newkey = gm.Im.GetAnyKeyDown();
            if(newkey != - 1){
                int oldkey = ((Number)keySettings[itemCode-2].get(keyNum)).intValue();
                //중복 된다면 바꿔준다.
                for (int j = 0; j <2; j++){
                    for (int i =0; i <KeyDefine.KeyFunc.length; i++){
                        if(((Number)keySettings[j].get(i)).intValue()== newkey){
                            keySettings[j].set(i, oldkey);
                        }
                    }
                }
                keySettings[itemCode-2].set(keyNum, newkey);
                Keyselected = false;
            }
        } else if (Itemselected) {
            switch (itemCode) {
                case 0:
                    if (gm.Im.isKeyPressed(KeyEvent.VK_LEFT)) {
                        float volume = gm.Sm.GetMasterVolume();
                        volume = volume < SoundManager.minimum ? SoundManager.minimum
                                : volume - (SoundManager.maximum - SoundManager.minimum)
                                        * (float) gm.Et.GetElapsedSeconds();
                        gm.Sm.setMasterVolume(volume);
                    }
                    if (gm.Im.isKeyPressed(KeyEvent.VK_RIGHT)) {
                        float volume = gm.Sm.GetMasterVolume();
                        volume = volume > SoundManager.maximum ? SoundManager.minimum
                                : volume + (SoundManager.maximum - SoundManager.minimum)
                                        * (float) gm.Et.GetElapsedSeconds();
                        gm.Sm.setMasterVolume(volume);
                    }
                    if (gm.Im.isKeyDown(KeyEvent.VK_SPACE)) {
                        Itemselected = false;
                        gm.Sm.playSound(menuSoundProp);
                    }
                    break;
                case 1:
                    if (gm.Im.isKeyDown(KeyEvent.VK_LEFT)) {
                        gm.Sm.SetMute(!gm.Sm.IsMute());
                    }
                    if (gm.Im.isKeyDown(KeyEvent.VK_RIGHT)) {
                        gm.Sm.SetMute(!gm.Sm.IsMute());
                    }
                    if (gm.Im.isKeyDown(KeyEvent.VK_SPACE)) {
                        Itemselected = false;
                        gm.Sm.playSound(menuSoundProp);
                    }
                    break;
                case 2:
                    if (gm.Im.isKeyDown(KeyEvent.VK_LEFT)) {
                        Itemselected = false;
                    }
                    if (gm.Im.isKeyDown(KeyEvent.VK_DOWN)) {
                        keyNum = keyNum == keySettings[0].size()-1 ? 0 : keyNum + 1;
                    }
                    if (gm.Im.isKeyDown(KeyEvent.VK_UP)) {
                        keyNum = keyNum == 0 ? keySettings[0].size()-1 : keyNum - 1;
                    }
                    if (gm.Im.isKeyDown(KeyEvent.VK_SPACE)) {
                        Keyselected = true;
                        gm.Sm.playSound(menuSoundProp);
                    }
                    break;
                case 3:
                    if (gm.Im.isKeyDown(KeyEvent.VK_LEFT)) {
                        Itemselected = false;
                    }
                    if (gm.Im.isKeyDown(KeyEvent.VK_DOWN)) {
                        keyNum = keyNum == keySettings[0].size()-1 ? 0 : keyNum + 1;
                    }
                    if (gm.Im.isKeyDown(KeyEvent.VK_UP)) {
                        keyNum = keyNum == 0 ? keySettings[0].size()-1 : keyNum - 1;
                    }
                    if (gm.Im.isKeyDown(KeyEvent.VK_SPACE)) {
                        Keyselected = true;
                        gm.Sm.playSound(menuSoundProp);
                    }
                    break;
                default:
                    break;
            }
        } else {
            if (gm.Im.isKeyDown(KeyEvent.VK_DOWN)) {
                itemCode = itemCode == 3 ? 0 : itemCode + 1;
            }
            if (gm.Im.isKeyDown(KeyEvent.VK_UP)) {
                itemCode = itemCode == 0 ? 3 : itemCode - 1;
            }
            if (gm.Im.isKeyDown(KeyEvent.VK_SPACE)) {
                Itemselected = true;
                gm.Sm.playSound(menuSoundProp);
            }
        }
        Draw();
    }

    public void LateUpdate() {
        if (gm.Im.isKeyDown(KeyEvent.VK_ESCAPE) && !Itemselected) {
            gm.Sm.playSound(menuSoundProp);
            SaveSetting();
            gm.SetInstance(new MenuPage());
        }
    };

    private void SaveSetting() {

        JSONObject setting = gm.GlobalData.get("Setting");
        setting.put("Volume", gm.Sm.GetMasterVolume());
        setting.put("IsMute", gm.Sm.IsMute());
        FileManager fm = new FileManager();
        JSONObject SaveData = fm.LoadJsonObject("DataBase");
        String name = (String) gm.GlobalData.get("LocalData").get("Player");
        JSONObject Userdata = (JSONObject) SaveData.get(name);
        if (Userdata == null) {
            Userdata = new JSONObject();
            SaveData.put(name, Userdata);
        }
        Userdata.put("Setting", setting);
        fm.SaveString("DataBase", SaveData.toJSONString(), true);
    }

    private void Draw() {
        drawSetting();
        drawSettingDetail();
    }
    public void Exit(){};
    private void drawSettingDetail() {
        Graphics graphic = gm.Rm.GetCurrentGraphic();
        FontMetrics matrix = gm.Rm.SetFont("Regular");

        if (itemCode < 2) {
            if (itemCode == 0 && Itemselected)
                graphic.setColor(Color.GREEN);
            else
                graphic.setColor(Color.WHITE);

            graphic.drawRect(gm.frame.getWidth() / 2, gm.frame.getHeight() / 4 + matrix.getHeight() / 8 * 12,
                    gm.frame.getWidth() / 4, matrix.getHeight());
            graphic.fillRect(gm.frame.getWidth() / 2, gm.frame.getHeight() / 4 + matrix.getHeight() / 8 * 12,
                    (int) (gm.frame.getWidth() / 4 * gm.Sm.GetVolumePercent() / 100),
                    matrix.getHeight());
            graphic.drawString(Integer.toString((int) gm.Sm.GetVolumePercent()), gm.frame.getWidth() / 4 * 3
                    + matrix.stringWidth("A") * 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 2);

            if (itemCode == 1 && Itemselected)
                graphic.setColor(Color.GREEN);
            else
                graphic.setColor(Color.WHITE);

            if (gm.Sm.IsMute()) {
                graphic.drawString("OFF", gm.frame.getWidth() / 10 * 7
                        - matrix.stringWidth("OFF") / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 4);
            } else {
                graphic.drawString("ON", gm.frame.getWidth() / 10 * 7
                        - matrix.stringWidth("ON") / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 4);
            }
        }
        if (itemCode == 2 || itemCode == 3) {
            for (int i = 0; i < KeyDefine.KeyFunc.length; i++) {
                graphic.setColor(Color.WHITE);
                if (!(Keyselected && keyNum == i)) {
                    graphic.drawString(keySettings[itemCode - 2].get(i).toString(),
                            (int) (gm.frame.getWidth() * 8 / 10.0f)
                                    - matrix.stringWidth(keySettings[itemCode - 2].get(i).toString()),
                            gm.frame.getHeight() / 4 + matrix.getHeight() * (2 * i + 1));
                }
                if (keyNum == i && Itemselected) {
                    if (Itemselected) {
                        graphic.setColor(Color.GREEN);
                    }
                }

                graphic.drawString(KeyDefine.KeyFunc[i], (int) (gm.frame.getWidth() * 6 / 10.0f)
                        - matrix.stringWidth(KeyDefine.KeyFunc[i]),
                        gm.frame.getHeight() / 4 + matrix.getHeight() * (2 * i + 1));
            }
        }
    }

    public void drawSetting() {

        String settingString = "Setting";
        String instructionsString1 = "Move with UP, DOWN / Select with SPACE";
        String instructionsString2 = "Press ESC to return";

        Graphics graphic = gm.Rm.GetCurrentGraphic();
        FontMetrics matrix = gm.Rm.SetFont("Big");

        graphic.setColor(Color.GREEN);
        graphic.drawString(settingString, gm.frame.getWidth() / 2
                - matrix.stringWidth(settingString) / 2, gm.frame.getHeight() / 8);

        matrix = gm.Rm.SetFont("Regular");

        graphic.setColor(Color.GRAY);
        graphic.drawString(instructionsString1, gm.frame.getWidth() / 2
                - matrix.stringWidth(instructionsString1) / 2, gm.frame.getHeight() / 5 - matrix.getHeight() / 2);

        graphic.drawString(instructionsString2, gm.frame.getWidth() / 2
                - matrix.stringWidth(instructionsString2) / 2, gm.frame.getHeight() / 5 + matrix.getHeight() / 2);

        String[] string = { "Volume", "BGM", "1P Keys", "2P Keys" };

        for (int i = 0; i < 4; i++) {
            if (itemCode == i) {
                graphic.setColor(Color.GREEN);
                if (Itemselected) {
                    graphic.drawString("*", gm.frame.getWidth() / 5
                            - matrix.stringWidth(string[i]) / 2 - 16,
                            gm.frame.getHeight() / 4 + matrix.getHeight() * 2 * (i + 1));
                }
            } else {
                graphic.setColor(Color.WHITE);
            }
            graphic.drawString(string[i], gm.frame.getWidth() / 5
                    - matrix.stringWidth(string[i]) / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 2 * (i + 1));
        }
        graphic.setColor(Color.GREEN);
        graphic.drawLine(gm.frame.getWidth() / 5 * 2 - 1, gm.frame.getHeight() / 4,
                gm.frame.getWidth() / 5 * 2 - 1, gm.frame.getHeight() / 10 * 9);
        graphic.drawLine(gm.frame.getWidth() / 5 * 2, gm.frame.getHeight() / 4,
                gm.frame.getWidth() / 5 * 2, gm.frame.getHeight() / 10 * 9);
    }
}