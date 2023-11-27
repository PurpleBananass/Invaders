package GamePrime.Page;

import EnginePrime.GManager;
import EnginePrime.GameManager;
import EnginePrime.SoundManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.FontMetrics;
import org.json.simple.JSONObject;

public class MenuPage implements GManager {
    GameManager gm = GameManager.getInstance();
    JSONObject res = gm.GlobalData.get("Resource");
    private final int MaxPage = 6;
    private final int MinPage = 0;
    int PageIndex;
    private SoundManager.PlayProp menuSoundProp;
    private SoundManager.PlayProp MainBgmProp;

    public void Exit() {
    };

    public void Initialize() {
        PageIndex = MinPage;
        JSONObject BGM = (JSONObject) res.get("BGM");
        JSONObject SFX = (JSONObject) res.get("SFX");
        menuSoundProp = gm.Sm.new PlayProp(
                "Sound" + File.separator + "SFX" + File.separator + (String) SFX.get("MenuSelect"), null);
        MainBgmProp = gm.Sm.new PlayProp(
                "Sound" + File.separator + "BGM" + File.separator + (String) BGM.get("MenuPage"), "BGM");
        MainBgmProp.count = -1;
        JSONObject setting = gm.GlobalData.get("Setting");
        float volume = ((Number) setting.get("Volume")).floatValue();
        boolean ismute = (boolean) setting.get("IsMute");
        gm.Sm.setMasterVolume((float) volume);
        gm.Sm.SetMute(ismute);
        gm.Sm.playSound(MainBgmProp);
    };

    public void PreUpdate() {
        if (gm.Im.isKeyDown(KeyEvent.VK_DOWN)) {
            PageIndex++;
            if (PageIndex > MaxPage) {
                PageIndex = MinPage;
            }
            gm.Sm.playSound(menuSoundProp);
        }
        if (gm.Im.isKeyDown(KeyEvent.VK_UP)) {
            PageIndex--;
            if (PageIndex < MinPage) {
                PageIndex = MaxPage;
            }
            gm.Sm.playSound(menuSoundProp);
        }
        Draw();
    };

    public void LateUpdate() {
        if (gm.Im.isKeyDown(KeyEvent.VK_SPACE)) {
            switch (PageIndex) {
            case 0:
                gm.SetInstance(new SelectPage());
                break;
            case 1:
                gm.SetInstance(new HightScorePage());
                break;
            case 2:
                gm.SetInstance(new StorePage());
                break;
            case 3:
                gm.SetInstance(new SettingPage());
                break;
            case 4:
                gm.SetInstance(new HelpPage());
                break;
            case 5:
                gm.SetInstance(new AchievementPage());
                break;
            case 6:
                gm.running = false;
                break;
            default:
                break;
            }
        }
    };

    private void Draw() {
        DrawTitleString();
        String playString = "Play";
        String highScoresString = "High scores";
        String shopString = "Shop";
        String settingString = "Setting";
        String helpString = "Help";
        String achievementString = "Achievements";
        String exitString = "exit";
        Graphics graphic = gm.Rm.GetCurrentGraphic();
        graphic.setColor(Color.WHITE);
        FontMetrics matrix = gm.Rm.SetFont("Regular");
        List<Runnable> lambdaFunctions = new ArrayList<>();
        lambdaFunctions
                .add(() -> graphic.drawString(playString, gm.frame.getWidth() / 2 - matrix.stringWidth(playString) / 2,
                        gm.frame.getHeight() * 2 / 3 - matrix.getHeight() * 6));
        lambdaFunctions.add(() -> graphic.drawString(highScoresString,
                gm.frame.getWidth() / 2 - matrix.stringWidth(highScoresString) / 2,
                gm.frame.getHeight() * 2 / 3 - matrix.getHeight() * 4));
        lambdaFunctions
                .add(() -> graphic.drawString(shopString, gm.frame.getWidth() / 2 - matrix.stringWidth(shopString) / 2,
                        gm.frame.getHeight() * 2 / 3 - matrix.getHeight() * 2));
        lambdaFunctions.add(
                () -> graphic.drawString(settingString, gm.frame.getWidth() / 2 - matrix.stringWidth(settingString) / 2,
                        gm.frame.getHeight() * 2 / 3 + matrix.getHeight() * 0));
        lambdaFunctions
                .add(() -> graphic.drawString(helpString, gm.frame.getWidth() / 2 - matrix.stringWidth(helpString) / 2,
                        gm.frame.getHeight() * 2 / 3 + matrix.getHeight() * 2));
        lambdaFunctions.add(() -> graphic.drawString(achievementString,
                gm.frame.getWidth() / 2 - matrix.stringWidth(achievementString) / 2,
                gm.frame.getHeight() * 2 / 3 + matrix.getHeight() * 4));
        lambdaFunctions
                .add(() -> graphic.drawString(exitString, gm.frame.getWidth() / 2 - matrix.stringWidth(exitString) / 2,
                        gm.frame.getHeight() * 2 / 3 + matrix.getHeight() * 6));
        for (int i = 0; i < lambdaFunctions.size(); i++) {
            if (i == PageIndex) {
                graphic.setColor(Color.WHITE);
            } else {
                graphic.setColor(Color.GREEN);
            }
            lambdaFunctions.get(i).run();
        }
    }

    private void DrawTitleString() {
        String titleString = "Invaders";
        String instructionsString = "select with w+s / arrows, confirm with space";
        Graphics graphic = gm.Rm.GetCurrentGraphic();
        graphic.setColor(Color.GREEN);
        FontMetrics matrix = gm.Rm.SetFont("Big");
        graphic.drawString(titleString, gm.frame.getWidth() / 2 - matrix.stringWidth(titleString) / 2,
                gm.frame.getHeight() / 3 - matrix.getHeight() * 2);
        graphic.setColor(Color.GRAY);
        matrix = gm.Rm.SetFont("Regular");
        graphic.drawString(instructionsString, gm.frame.getWidth() / 2 - matrix.stringWidth(instructionsString) / 2,
                gm.frame.getHeight() / 2 - matrix.getHeight() * 7 / 2);
    }

    public void PreRender() {
    };

    public void LateRender() {
    };
}
