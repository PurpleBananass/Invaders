package GamePrime.Page;

import java.awt.event.KeyEvent;
import org.json.simple.JSONObject;

import java.awt.Graphics;
import java.awt.Color;

import java.awt.FontMetrics;
import EnginePrime.GManager;
import EnginePrime.GameManager;
import GamePrime.AchievDefine;

public class AchievementPage implements GManager {

    GameManager gm = GameManager.getInstance();
    JSONObject Achievement;
    public void PreRender(){};
    
    public void LateRender(){};
    public void Initialize() {
        Achievement = (JSONObject)gm.GlobalData.get("LocalData").get("Achievement");
    };

    public void PreUpdate() {

    };

    public void LateUpdate() {
        Draw();
        if ((gm.Im.isKeyDown(KeyEvent.VK_ESCAPE) || gm.Im.isKeyDown(KeyEvent.VK_SPACE))) {
            gm.SetInstance(new MenuPage());
        }
    };

    private void Draw() {

        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        FontMetrics fontmatrix = gm.Rm.SetFont("Regular");
        grpahics.setColor(Color.GREEN);

        int x = 20; // Fixed X-coordinate for achievement titles.
        int y = 50; // Fixed Y-coordinate for the initial position.

        for (String name : AchievDefine.Achieve) {

            grpahics.setColor(Color.YELLOW);
            grpahics.drawString(name, x, y);
            if ((boolean) Achievement.get(name)) {
                grpahics.setColor(Color.GREEN);
            } else {
                grpahics.setColor(Color.RED);
            }
            // Calculate the position to display achievementStatus (completed or incomplete)
            // on the right of achievementTitle.
            int titleWidth = fontmatrix.stringWidth(name);
            int statusX = x + titleWidth + 10; // You can adjust the spacing as needed.

            // Display whether the achievement is completed or not.
            String achievementStatus = (boolean) Achievement.get(name) ? "Completed" : "Incomplete";
            grpahics.drawString(achievementStatus, statusX, y);

            // You can add more information about the achievement if needed.
            // For example, you can display the progress or description.

            // Increase the Y-coordinate for the next achievement entry.
            y += fontmatrix.getHeight()+10;
        }
    }

}
