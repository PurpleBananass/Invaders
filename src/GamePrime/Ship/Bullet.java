package GamePrime.Ship;

import java.awt.event.KeyEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.awt.Graphics;
import EnginePrime.Component;
import EnginePrime.Entity;
import EnginePrime.EventSystem;
import EnginePrime.GameManager;
import EnginePrime.Message;
import EnginePrime.Message.MessageType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import EnginePrime.FileManager;
import EnginePrime.GManager;
import EnginePrime.GameManager;
import EnginePrime.SoundManager;
import GamePrime.Define.KeyDefine;
import GamePrime.ETC.Image;
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
    float ShotSpeed;
    String MadeBY;


    public int size = 30;

    public void SetVector(Message m){
        dir = (Point2D)m.obj.get("dir");
        pos = (Point2D)m.obj.get("pos");
        ShotSpeed = ((Number)m.obj.get("ShotSpeed")).intValue();
        MadeBY = (String)m.obj.get("Madeby");
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
        pos = new Point2D.Double(pos.getX() + dir.getX()*ShotSpeed* gm.Et.GetElapsedSeconds(), pos.getY() + dir.getY()*ShotSpeed* gm.Et.GetElapsedSeconds());
    }
    public void Render(){
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        img.RenderFixedHeight((int)Math.round(pos.getX()), (int)Math.round(pos.getY()), size);
    
    }
    public static void MakeBullet(Point2D pos,Point2D dir,float ShotSpeed,String tag,String madeby){
        Entity bullet = EventSystem.Initiate();
        bullet.tag = tag;
        JSONObject Custommessage = new JSONObject();
        Custommessage.put("Func", "SetVector");
        Custommessage.put("dir", dir);
        Custommessage.put("pos", pos);
        Custommessage.put("ShotSpeed",ShotSpeed);
        Custommessage.put("Madeby",madeby);
        Message m = new Message(bullet, MessageType.Custom, Custommessage);
        Bullet b =  bullet.AddComponent(Bullet.class);
        b.SetVector(m);
    }
}
