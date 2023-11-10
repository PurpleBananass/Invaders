package GamePrime.Ship;

import java.awt.event.KeyEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.awt.Graphics;
import EnginePrime.Component;
import EnginePrime.GameManager;
import EnginePrime.Message;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import EnginePrime.FileManager;
import EnginePrime.GManager;
import EnginePrime.GameManager;
import EnginePrime.SoundManager;
import GamePrime.KeyDefine;
import GamePrime.ShipDefine;
import GamePrime.Page.GamePage;

import java.awt.geom.Point2D;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public class Item extends Component{

    Point2D pos;
    Point2D dir;
    GameManager gm = GameManager.getInstance();
    GamePage gp;
    BufferedImage img;
    int height = 30;
    
    public void SetVector(Message m){
        dir = (Point2D)m.obj.get("dir");
        pos = (Point2D)m.obj.get("pos");
    }

    public void Awake(){
        this.CustomEvent.put("SetVector", this::SetVector);
        gp = (GamePage)gm.CustomInstance;
        img = gp.ImgRes.get("Magic2");
    }

    public void Start(){



    }

    public void Update(){

        double ShotSpeed = ((Number) gp.PlayData.get("ShotSpeed")).doubleValue() * gm.Et.GetElapsedSeconds();
        pos = new Point2D.Double(pos.getX() + dir.getX()*ShotSpeed, pos.getY() + dir.getY()*ShotSpeed);
    }

    public void Render(){
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        Graphics2D graphics2D = (Graphics2D)grpahics;
        float width = height*img.getWidth()/(float)img.getHeight();
        graphics2D.drawImage(img, (int)Math.round(pos.getX()- width/2), (int)Math.round(pos.getY() - height/2.0f), Math.round(width),height,null);
    }
}
