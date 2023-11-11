package GamePrime;
import java.io.File;


import EnginePrime.GManager;
import EnginePrime.GameManager;
import GamePrime.Page.LoginPage;

import java.awt.Font;


public class Entry implements GManager{

    GameManager gm = GameManager.getInstance();

    public void Initialize(){
        gm.frame.SetSize(800, 1000);
        gm.Et.SetMaxFps(60);
        Font font = gm.Rm.LoadFont("res" + File.separator +"font.ttf");
        gm.Rm.CreateFont(font, "Regular", 14.0f);
        gm.Rm.CreateFont(font, "Big", 24.0f);
        GameManager.getInstance().SetInstance(new LoginPage());
    }

    public void PreUpdate(){};
 
    public void LateUpdate(){};
    public void PreRender(){};
    public void LateRender(){};
    public void Exit(){};
}

