package GamePrime;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;


import EnginePrime.GManager;
import EnginePrime.GameManager;

import java.awt.Font;


public class Entry implements GManager{

    GameManager gm = GameManager.getInstance();

    public void Initialize(){
        gm.Et.SetMaxFps(60);
        Font font = gm.Rm.LoadFont("res" + File.separator + "font.ttf");
        gm.Rm.CreateFont(font, "Regular", 14);
        gm.Rm.CreateFont(font, "Big", 24);
        GameManager.getInstance().SetInstance(new LoginPage());
    }

    public void PreUpdate(){};

    public void LateUpdate(){};

   

}

