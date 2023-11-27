package GamePrime.UI;

import org.json.simple.JSONObject;
import java.awt.Graphics;
import EnginePrime.Component;
import EnginePrime.EventSystem;
import EnginePrime.GameManager;
import java.io.File;
import EnginePrime.SoundManager;
import java.awt.Color;
import java.awt.FontMetrics;

public class PrepareUI extends Component {
    GameManager gm = GameManager.getInstance();
    JSONObject res = gm.GlobalData.get("Resource");
    double elapsedSeconds;
    int Seconds;
    JSONObject PlayData;
    SoundManager.PlayProp levelProp;
    SoundManager.PlayProp levelProp2;

    public void Start() {
        JSONObject SFX = (JSONObject) res.get("SFX");
        levelProp = gm.Sm.new PlayProp(
                "Sound" + File.separator + "SFX" + File.separator + (String) SFX.get("LevelStart"), null);
        levelProp2 = gm.Sm.new PlayProp(
                "Sound" + File.separator + "SFX" + File.separator + (String) SFX.get("LevelStart2"), null);
        elapsedSeconds = 6;
        Seconds = 4;
        PlayData = (JSONObject) gm.GlobalData.get("LocalData").get("PlayData");
        SetRenderPrior(2);
    }

    public void Update() {
        elapsedSeconds -= gm.Et.GetElapsedSeconds();
        if ((int) elapsedSeconds != Seconds && (int) elapsedSeconds <= 4) {
            if ((int) elapsedSeconds >= 1) {
                gm.Sm.playSound(levelProp);
            } else {
                gm.Sm.playSound(levelProp2);
            }
            Seconds = (int) elapsedSeconds;
        }
        if (elapsedSeconds < 0) {
            elapsedSeconds = 0;
            PlayData.put("ScreenIndex", 0);
            EventSystem.Destroy(this.Obj);
        }
    }

    public void Render() {
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        FontMetrics fontmatrix = gm.Rm.SetFont("Big");
        int rectWidth = gm.frame.getWidth();
        int rectHeight = gm.frame.getHeight() / 6;
        grpahics.setColor(Color.BLACK);
        grpahics.fillRect(0, gm.frame.getHeight() / 2 - rectHeight / 2, rectWidth, rectHeight);
        grpahics.setColor(Color.GREEN);
        float y = gm.frame.getHeight() * (1 / 2.0f - 1 / 12.0f);
        grpahics.drawLine(0, (int) y, gm.frame.getWidth(), (int) y);
        grpahics.drawLine(0, (int) y + 1, gm.frame.getWidth(), (int) y + 1);
        y = gm.frame.getHeight() * (1 / 2.0f + 1 / 12.0f);
        grpahics.drawLine(0, (int) y, gm.frame.getWidth(), (int) y);
        grpahics.drawLine(0, (int) y + 1, gm.frame.getWidth(), (int) y + 1);
        int countdown = (int) elapsedSeconds;
        if (elapsedSeconds >= 4) {
            grpahics.drawString("Level " + PlayData.get("Level"),
                    gm.frame.getWidth() / 2 - fontmatrix.stringWidth("Level " + PlayData.get("Level")) / 2,
                    gm.frame.getHeight() / 2);
        } else if (elapsedSeconds >= 1) {
            grpahics.drawString(Integer.toString(countdown),
                    gm.frame.getWidth() / 2 - fontmatrix.stringWidth(Integer.toString(countdown)),
                    gm.frame.getHeight() / 2);
        } else {
            grpahics.drawString("GO!", gm.frame.getWidth() / 2 - fontmatrix.stringWidth("GO!") / 2,
                    gm.frame.getHeight() / 2);
        }
    }
}
