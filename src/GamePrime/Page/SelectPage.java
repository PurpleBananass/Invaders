package GamePrime.Page;

import EnginePrime.GManager;
import EnginePrime.GameManager;
import EnginePrime.SoundManager;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.awt.FontMetrics;

public class SelectPage implements GManager {

    GameManager gm = GameManager.getInstance();

    int PlayMode;
    boolean HardMode;

    private int SelectIndex;

    SoundManager.PlayProp menuSoundProp;

    public void Initialize() {
        PlayMode = 0;
        HardMode = false;
        if(gm.GlobalData.get("LocalData").get("HardMode") !=null ){
           PlayMode =((Number)gm.GlobalData.get("LocalData").get("PlayMode")).intValue();
           HardMode =(boolean) gm.GlobalData.get("LocalData").get("   ");
        }

        SelectIndex = 0;
        menuSoundProp = gm.Sm.new PlayProp(
                "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_MenuClick.wav", null);
    }

    public void PreUpdate() {
        if (gm.Im.isKeyDown(KeyEvent.VK_UP) || gm.Im.isKeyDown(KeyEvent.VK_DOWN)) {
            SelectIndex = SelectIndex == 1 ? 0 : 1;
        }
        switch (SelectIndex) {
            case 0:
                if (gm.Im.isKeyDown(KeyEvent.VK_RIGHT) || gm.Im.isKeyDown(KeyEvent.VK_LEFT)) {
                    PlayMode = PlayMode == 1 ? 0 : 1;
                }
                if (gm.Im.isKeyDown(KeyEvent.VK_SPACE)) {
                    SelectIndex++;
                    gm.Sm.playSound(menuSoundProp);
                }
                break;
            case 1:
                if (gm.Im.isKeyDown(KeyEvent.VK_RIGHT) || gm.Im.isKeyDown(KeyEvent.VK_LEFT)) {
                    HardMode = !HardMode;
                }
                if (gm.Im.isKeyDown(KeyEvent.VK_SPACE)) {
                    gm.Sm.playSound(menuSoundProp);
                    gm.GlobalData.get("LocalData").put("PlayMode", PlayMode);
                    gm.GlobalData.get("LocalData").put("HardMode", HardMode);
                    gm.SetInstance(new SkinSelectPage());
                }
                break;
            default:
                break;
        }
    };

    public void LateUpdate() {
        Draw();
        if (gm.Im.isKeyDown(KeyEvent.VK_ESCAPE)) {
            gm.SetInstance(new MenuPage());
        }

    };

    void Draw() {
        String selectString = "Select Mode";
        String instructionsString = "select with a+d / arrows, confirm with space";

        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        FontMetrics fontmatrix = gm.Rm.SetFont("Regular");

        grpahics.setColor(Color.GRAY);
        grpahics.drawString(instructionsString, gm.frame.getWidth() / 2
                - fontmatrix.stringWidth(instructionsString) / 2, gm.frame.getHeight() / 5);

        grpahics.setColor(Color.GREEN);
        fontmatrix = gm.Rm.SetFont("Big");

        grpahics.drawString(selectString, gm.frame.getWidth() / 2
                - fontmatrix.stringWidth(selectString) / 2, gm.frame.getHeight() / 8);

        if (SelectIndex == 0)
            grpahics.setColor(Color.GREEN);
        else
            grpahics.setColor(Color.WHITE);

        grpahics.drawString("Player", gm.frame.getWidth() / 5
                - fontmatrix.stringWidth("Player") / 2, gm.frame.getHeight() / 8 * 3);
        if (SelectIndex == 1)
            grpahics.setColor(Color.GREEN);
        else
            grpahics.setColor(Color.WHITE);
        grpahics.drawString("Hard Mode", gm.frame.getWidth() / 5
                - fontmatrix.stringWidth("Player") / 2, gm.frame.getHeight() / 8 * 5);

        if (PlayMode == 0)
            grpahics.setColor(Color.GREEN);
        else
            grpahics.setColor(Color.WHITE);


        grpahics.drawString("1P", gm.frame.getWidth() / 10 * 6
                - fontmatrix.stringWidth("1P") / 2,
                gm.frame.getHeight()
                        / 8 * 3 + fontmatrix.getHeight() * 2);

        if (PlayMode == 1)
            grpahics.setColor(Color.GREEN);
        else
            grpahics.setColor(Color.WHITE);
        grpahics.drawString("2P", gm.frame.getWidth() / 10 * 8
                - fontmatrix.stringWidth("2P") / 2,
                gm.frame.getHeight()
                        / 8 * 3 + fontmatrix.getHeight() * 2);

        grpahics.setColor(Color.WHITE);
        grpahics.drawString("ON", gm.frame.getWidth() / 10 * 6
                - fontmatrix.stringWidth("ON") / 2,
                gm.frame.getHeight()
                        / 8 * 5 + fontmatrix.getHeight() * 2);
        grpahics.drawString("OFF", gm.frame.getWidth() / 10 * 8
                - fontmatrix.stringWidth("OFF") / 2,
                gm.frame.getHeight()
                        / 8 * 5 + fontmatrix.getHeight() * 2);

        grpahics.setColor(Color.GREEN);
        if (HardMode)
            grpahics.drawString("ON", gm.frame.getWidth() / 10 * 6
                    - fontmatrix.stringWidth("ON") / 2,
                    gm.frame.getHeight()
                            / 8 * 5 + fontmatrix.getHeight() * 2);
        else
            grpahics.drawString("OFF", gm.frame.getWidth() / 10 * 8
                    - fontmatrix.stringWidth("OFF") / 2,
                    gm.frame.getHeight()
                            / 8 * 5 + fontmatrix.getHeight() * 2);

    }
}
