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
    public void PreRender(){};
    
    public void LateRender(){};
    private final int MaxPage = 5;
    private final int MinPage = 0;
    
    int PageIndex;
    
    private SoundManager.PlayProp menuSoundProp;
    private SoundManager.PlayProp MainBgmProp;


    public void Initialize(){
        PageIndex = MinPage;
        menuSoundProp = gm.Sm.new PlayProp(
                "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_MenuClick.wav", null);
        MainBgmProp = gm.Sm.new PlayProp(
                "res" + File.separator + "Sound" + File.separator + "BGM" + File.separator + "B_Main_a.wav", null);
        MainBgmProp.count = -1;
        

        JSONObject setting = gm.GlobalData.get("Setting");

        float volume = ((Number)setting.get("Volume")).floatValue();
        boolean ismute = (boolean)setting.get("IsMute");
        gm.Sm.setMasterVolume((float)volume);
        gm.Sm.SetMute(ismute);
        gm.Sm.playSound(MainBgmProp);
    };

    public void PreUpdate(){

        if (gm.Im.isKeyDown(KeyEvent.VK_DOWN)){
            PageIndex++;
            if(PageIndex > MaxPage){
                PageIndex = MinPage;
            }


        }
        if (gm.Im.isKeyDown(KeyEvent.VK_UP)){
            PageIndex--;
            if(PageIndex < MinPage){
                PageIndex = MaxPage;
            }
        }

        Draw();
    };

    public void LateUpdate(){
        if (gm.Im.isKeyDown(KeyEvent.VK_SPACE)) {
            gm.Sm.playSound(menuSoundProp);
            
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
                    gm.SetInstance(new AchievementPage());
                    break;
                case 5:
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
		String achievementString = "Achievements";
		String exitString = "exit";

        Graphics graphic = gm.Rm.GetCurrentGraphic();
        graphic.setColor(Color.WHITE);
        FontMetrics matrix = gm.Rm.SetFont("Regular");

        List<Runnable> lambdaFunctions = new ArrayList<>();
        lambdaFunctions.add(() -> graphic.drawString(playString, gm.frame.getWidth() / 2
                - matrix.stringWidth(playString) / 2, gm.frame.getHeight()* 2/3 - matrix.getHeight() * 5));
        lambdaFunctions.add(() -> graphic.drawString(highScoresString, gm.frame.getWidth() / 2
                - matrix.stringWidth(highScoresString) / 2, gm.frame.getHeight()* 2/3 - matrix.getHeight() * 3));
        lambdaFunctions.add(() -> graphic.drawString(shopString, gm.frame.getWidth() / 2
                - matrix.stringWidth(shopString) / 2, gm.frame.getHeight()* 2/3 - matrix.getHeight()));
        lambdaFunctions.add(() -> graphic.drawString(settingString, gm.frame.getWidth() / 2
                - matrix.stringWidth(settingString) / 2, gm.frame.getHeight()* 2/3 + matrix.getHeight()));
        lambdaFunctions.add(() ->graphic.drawString(achievementString, gm.frame.getWidth() / 2
                - matrix.stringWidth(achievementString) / 2, gm.frame.getHeight()* 2/3 + matrix.getHeight()*3));
        lambdaFunctions.add(() ->graphic.drawString(exitString, gm.frame.getWidth() / 2
                - matrix.stringWidth(exitString) / 2, gm.frame.getHeight()* 2/3 + matrix.getHeight()*5));

        for (int i =0; i<lambdaFunctions.size();i++){

            if(i == PageIndex){
                graphic.setColor(Color.WHITE);
            }
            else{
                graphic.setColor(Color.GREEN);
            }
            lambdaFunctions.get(i).run();
        }
    }
    private void DrawTitleString(){
        String titleString = "Invaders";
        String instructionsString = "select with w+s / arrows, confirm with space";

        Graphics graphic = gm.Rm.GetCurrentGraphic();

        graphic.setColor(Color.GREEN);
        FontMetrics matrix = gm.Rm.SetFont("Big");
        graphic.drawString(titleString, gm.frame.getWidth() / 2
                - matrix.stringWidth(titleString) / 2, gm.frame.getHeight() / 3 - matrix.getHeight() * 2);

        graphic.setColor(Color.GRAY);
        matrix = gm.Rm.SetFont("Regular");
        graphic.drawString(instructionsString, gm.frame.getWidth() / 2
                - matrix.stringWidth(instructionsString) / 2, gm.frame.getHeight() / 2 - matrix.getHeight() * 7/2);
    }
}
