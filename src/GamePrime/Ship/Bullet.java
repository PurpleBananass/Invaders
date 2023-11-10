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
import GamePrime.Image;
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
public class Bullet extends Component{

    public Point2D pos;
    Point2D dir;
    GameManager gm = GameManager.getInstance();
    GamePage gp;
    Image img;
    public int size = 30;

    public void SetVector(Message m){
        dir = (Point2D)m.obj.get("dir");
        pos = (Point2D)m.obj.get("pos");
    }


    public void Awake(){
        this.CustomEvent.put("SetVector", this::SetVector);
        gp = (GamePage)gm.CustomInstance;
        img = gp.ImgRes.get("Magic");
    }

    public void Start(){


    }


    public void Update(){
        if (((Number) gp.PlayData.get("ScreenIndex")).intValue() != 0) {
            return;
        }
        double ShotSpeed = ((Number) gp.PlayData.get("ShotSpeed")).doubleValue() * gm.Et.GetElapsedSeconds();
        pos = new Point2D.Double(pos.getX() + dir.getX()*ShotSpeed, pos.getY() + dir.getY()*ShotSpeed);
    }

    public void Render(){
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        img.RenderFixedHeight((int)Math.round(pos.getX()), (int)Math.round(pos.getY()), size);
    
    }
}
