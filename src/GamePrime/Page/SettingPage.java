package GamePrime.Page;

import java.awt.event.KeyEvent;
import java.io.File;

import EnginePrime.GManager;
import EnginePrime.GameManager;
import EnginePrime.SoundManager;
import GamePrime.KeyDefine;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.FontMetrics;
import org.json.simple.JSONObject;


public class SettingPage implements GManager {

    
    GameManager gm = GameManager.getInstance();
    private SoundManager.PlayProp menuSoundProp;

    public void Initialize() {
        menuSoundProp = gm.Sm.new PlayProp(
                "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_MenuClick.wav", null);
    };

    public void PreUpdate() {
       
        Draw();
    }

    public void LateUpdate() {
        // if (gm.Im.isKeyDown(KeyEvent.VK_ESCAPE) && !selected) {
        //     gm.Sm.playSound(menuSoundProp);
        //     SaveSetting();
        //     gm.SetInstance(new MenuPage());
        // }
    };

    private void SaveSetting(){
        // JSONObject setting = new JSONObject();
        // setting.put("Volume", gm.Sm.GetMasterVolume());
        // setting.put("Mute", gm.Sm.IsMute());
        // setting.put("KeySetting", Keysetting);
        // FileManager fm = new FileManager();
        // fm.SaveString("settings", setting.toJSONString());
    }

    private void Draw() {
        drawSetting();
        drawSettingDetail();
    }

    private void drawSettingDetail() {
        Graphics graphic = gm.Rm.GetCurrentGraphic();
        FontMetrics matrix = gm.Rm.SetFont("Regular");
        if (itemCode == 0 || itemCode == 1) {
            if (itemCode == 0 && selected)
                graphic.setColor(Color.GREEN);
            else
                graphic.setColor(Color.WHITE);

            graphic.drawRect(gm.frame.getWidth() / 2, gm.frame.getHeight() / 4 + matrix.getHeight() / 8 * 12,
                    gm.frame.getWidth() / 4, matrix.getHeight());
            graphic.fillRect(gm.frame.getWidth() / 2, gm.frame.getHeight() / 4 + matrix.getHeight() / 8 * 12,
                    (int) (gm.frame.getWidth() / 4 * gm.Sm.GetMasterVolume() / SoundManager.maximum),
                    matrix.getHeight());
            graphic.drawString(Float.toString(gm.Sm.GetMasterVolume()), gm.frame.getWidth() / 4 * 3
                    + matrix.stringWidth("A") * 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 2);
            if (itemCode == 1 && selected)
                graphic.setColor(Color.GREEN);
            else
                graphic.setColor(Color.WHITE);

            if (gm.Sm.IsMute()) {

                graphic.drawString("ON", gm.frame.getWidth() / 10 * 7
                        - matrix.stringWidth("ON") / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 4);
            } else {
                graphic.drawString("OFF", gm.frame.getWidth() / 10 * 7
                        - matrix.stringWidth("OFF") / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 4);
            }
        }
        if (itemCode == 2) {

            DrawKeyString("LEFT", "RIGHT", "ATTACK", "BURST 1", "BURST 2", "RELOAD", "BOOSTER", "ITEM", 6);
            DrawKeyString(KeyDefine.keyList.get(0), KeyDefine.keyList.get(1), KeyDefine.keyList.get(2), KeyDefine.keyList.get(3), KeyDefine.keyList.get(4), KeyDefine.keyList.get(5), KeyDefine.keyList.get(6), KeyDefine.keyList.get(7)
            , 8);
            if (selected) {
                drawGreenKeyString(keyNum);
            }

        }

        if (itemCode == 3) {
            DrawKeyString("LEFT", "RIGHT", "ATTACK", "BURST 1", "BURST 2", "RELOAD", "BOOSTER", "ITEM", 6);
            DrawKeyString(KeyDefine.keyList.get(8), KeyDefine.keyList.get(9), KeyDefine.keyList.get(10), KeyDefine.keyList.get(11), KeyDefine.keyList.get(12), KeyDefine.keyList.get(13), KeyDefine.keyList.get(14), KeyDefine.keyList.get(15)
            , 8);
            if (selected) {
                drawGreenKeyString(keyNum);
            }
        }
    }

    private void drawGreenKeyString(int keyNum) {
        Graphics graphic = gm.Rm.GetCurrentGraphic();
        FontMetrics matrix = gm.Rm.SetFont("Regular");
        if (keyNum == 0) {
            graphic.setColor(Color.GREEN);
            graphic.drawString("LEFT", gm.frame.getWidth() / 10 * 6
                    - matrix.stringWidth("LEFT") / 2, gm.frame.getHeight() / 4 + matrix.getHeight());
        }
        if (keyNum == 1) {
            graphic.setColor(Color.GREEN);
            graphic.drawString("RIGHT", gm.frame.getWidth() / 10 * 6
                    - matrix.stringWidth("RIGHT") / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 3);
        }
        if (keyNum == 2) {
            graphic.setColor(Color.GREEN);
            graphic.drawString("ATTACK", gm.frame.getWidth() / 10 * 6
                    - matrix.stringWidth("ATTACK") / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 5);
        }
        if (keyNum == 3) {
            graphic.setColor(Color.GREEN);
            graphic.drawString("BURST 1", gm.frame.getWidth() / 10 * 6
                    - matrix.stringWidth("BURST 1") / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 7);
        }
        if (keyNum == 4) {
            graphic.setColor(Color.GREEN);
            graphic.drawString("BURST 2", gm.frame.getWidth() / 10 * 6
                    - matrix.stringWidth("BURST 2") / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 9);
        }
        if (keyNum == 5) {
            graphic.setColor(Color.GREEN);
            graphic.drawString("RELOAD", gm.frame.getWidth() / 10 * 6
                    - matrix.stringWidth("RELOAD") / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 11);
        }
        if (keyNum == 6) {
            graphic.setColor(Color.GREEN);
            graphic.drawString("BOOSTER", gm.frame.getWidth() / 10 * 6
                    - matrix.stringWidth("BOOSTER") / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 13);
        }
        if (keyNum == 7) {
            graphic.setColor(Color.GREEN);
            graphic.drawString("ITEM", gm.frame.getWidth() / 10 * 6
                    - matrix.stringWidth("ITEM") / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 15);
        }
    }

    private void DrawKeyString(String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8,
            int num) {
        Graphics graphic = gm.Rm.GetCurrentGraphic();
        FontMetrics matrix = gm.Rm.SetFont("Regular");
        graphic.setColor(Color.WHITE);
        graphic.drawString(s1, gm.frame.getWidth() / 10 * num
                - matrix.stringWidth(s1) / 2, gm.frame.getHeight() / 4 + matrix.getHeight());
        graphic.drawString(s2, gm.frame.getWidth() / 10 * num
                - matrix.stringWidth(s2) / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 3);
        graphic.drawString(s3, gm.frame.getWidth() / 10 * num
                - matrix.stringWidth(s3) / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 5);
        graphic.drawString(s4, gm.frame.getWidth() / 10 * num
                - matrix.stringWidth(s4) / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 7);
        graphic.drawString(s5, gm.frame.getWidth() / 10 * num
                - matrix.stringWidth(s5) / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 9);
        graphic.drawString(s6, gm.frame.getWidth() / 10 * num
                - matrix.stringWidth(s6) / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 11);
        graphic.drawString(s7, gm.frame.getWidth() / 10 * num
                - matrix.stringWidth(s7) / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 13);
        graphic.drawString(s8, gm.frame.getWidth() / 10 * num
                - matrix.stringWidth(s8) / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 15);
    }

    public void drawSetting() {

        String settingString = "Setting";
        String instructionsString1 = "Move with UP, DOWN / Select with SPACE";
        String instructionsString2 = "Press ESC to return";

        String volumeString = "Volume";
        String bgmString = "BGM";
        String keysString1 = "1P Keys";
        String keysString2 = "2P Keys";

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

        if (itemCode == 0) {
            graphic.setColor(Color.GREEN);
            if (selected) {
                matrix = gm.Rm.SetFont("Regular");
                graphic.drawString("*", gm.frame.getWidth() / 5
                        - matrix.stringWidth(volumeString) / 2 - 16, gm.frame.getHeight() / 4 + matrix.getHeight() * 2);
            }
        } else
            graphic.setColor(Color.WHITE);

        graphic.drawString(volumeString, gm.frame.getWidth() / 5
                - matrix.stringWidth(volumeString) / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 2);

        if (itemCode == 1) {
            graphic.setColor(Color.GREEN);
            if (selected) {
                graphic.drawString("*", gm.frame.getWidth() / 5
                        - matrix.stringWidth(bgmString) / 2 - 16, gm.frame.getHeight() / 4 + matrix.getHeight() * 4);
            }
        } else{
            graphic.setColor(Color.WHITE);

            graphic.drawString(bgmString, gm.frame.getWidth() / 5
                - matrix.stringWidth(bgmString) / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 4);
        }
        if (itemCode == 2) {
            graphic.setColor(Color.GREEN);
            if (selected) {

                graphic.drawString("*", gm.frame.getWidth() / 5
                        - matrix.stringWidth(keysString1) / 2 - 16, gm.frame.getHeight() / 4 + matrix.getHeight() * 6);
            }
        } else
            graphic.setColor(Color.WHITE);
        graphic.drawString(keysString1, gm.frame.getWidth() / 5
                - matrix.stringWidth(keysString1) / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 6);

        if (itemCode == 3) {
            graphic.setColor(Color.GREEN);
            if (selected) {
                graphic.drawString("*", gm.frame.getWidth() / 5
                        - matrix.stringWidth(keysString2) / 2 - 16, gm.frame.getHeight() / 4 + matrix.getHeight() * 8);
            }
        } else {
            graphic.setColor(Color.WHITE);
            graphic.drawString(keysString2, gm.frame.getWidth() / 5
                    - matrix.stringWidth(keysString2) / 2, gm.frame.getHeight() / 4 + matrix.getHeight() * 8);
        }

        graphic.setColor(Color.GREEN);
        graphic.drawLine(gm.frame.getWidth() / 5 * 2 - 1, gm.frame.getHeight() / 4,
                gm.frame.getWidth() / 5 * 2 - 1, gm.frame.getHeight() / 10 * 9);
        graphic.drawLine(gm.frame.getWidth() / 5 * 2, gm.frame.getHeight() / 4,
                gm.frame.getWidth() / 5 * 2, gm.frame.getHeight() / 10 * 9);
    }
}