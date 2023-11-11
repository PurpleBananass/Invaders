package GamePrime.Ship;

import java.awt.geom.Point2D;

import org.json.simple.JSONObject;

import java.awt.geom.Point2D;
import EnginePrime.Entity;
import EnginePrime.EventSystem;
import EnginePrime.Message;
import EnginePrime.Message.MessageType;

public class Bomb extends Bullet {
    Point2D dirList[];
    public void Awake() {
        super.Awake();
        dirList = new Point2D[]{ 
            new Point2D.Double(-1,1),
            new Point2D.Double(-1,-1),
        new Point2D.Double(1,-1),
        new Point2D.Double(1,1),
        new Point2D.Double(0,1)};
    }


    public static void MakeBomb(Point2D pos,Point2D dir,float ShotSpeed,String tag,String madeby){
        Entity bullet = EventSystem.Initiate();
        bullet.tag = "PBullet";
        JSONObject Custommessage = new JSONObject();
        Custommessage.put("Func", "SetVector");
        Custommessage.put("dir", new Point2D.Float(0, -1.0f));
        Custommessage.put("pos", pos);
        Custommessage.put("ShotSpeed",ShotSpeed);
        Custommessage.put("Madeby",madeby);
        Message m = new Message(bullet, MessageType.Custom, Custommessage);
        Bomb b =  bullet.AddComponent(Bomb.class);
        b.SetVector(m);
    }
}
