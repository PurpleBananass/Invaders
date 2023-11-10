package GamePrime.Ship;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.awt.Graphics;

import java.util.ArrayList;
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
import GamePrime.Image;
import GamePrime.KeyDefine;
import GamePrime.ShipDefine;
import GamePrime.Page.GamePage;
import GamePrime.Ship.EnemyController.EnemyType;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Enemy extends Component{   
        public float ShotSpeed;
        int life;
        int Point;
        float Shotdelay;
        float curdelay;
        Point2D pos;
        EnemyController.EnemyType type;
        Image img;
        GameManager gm = GameManager.getInstance();
        GamePage gp = (GamePage)gm.CustomInstance;

        public float ImgGetWidth(){

            return img.GetWidthFixHeight(((Number)gp.PlayData.get("ImgHeight")).intValue());
        }

        public void SetVector(Message m){
            pos = (Point2D)m.obj.get("pos");
        }
        public void SetInfo(Message m){
            life = ((Number)m.obj.get("Life")).intValue();
            Point = ((Number)m.obj.get("Point")).intValue();
            ShotSpeed = ((Number)m.obj.get("ShotSpeed")).intValue();
            Shotdelay = ((Number)m.obj.get("ShotDelay")).floatValue();
            type = (EnemyType)m.obj.get("Type");
            img = gp.ImgRes.get(m.obj.get("Img"));
        }
    
        public void Awake(){
            this.CustomEvent.put("SetVector", this::SetVector);
            this.CustomEvent.put("SetInfo", this::SetInfo); 
        }


        public void Start(){
            curdelay = 0;
            Shotdelay = 0;
        }

        public void Shoot(){
            if (curdelay == 0) {
                Entity bullet = EventSystem.Initiate();
                Bullet b =  bullet.AddComponent(Bullet.class);
                bullet.tag = "EBullet";
                JSONObject Custommessage = new JSONObject();
                Custommessage.put("Entity", bullet);
                Custommessage.put("Func", "SetVector");
                Custommessage.put("dir", new Point2D.Float(0, 1.0f));
                Custommessage.put("pos", pos);
                Message m = new Message(this.Obj, MessageType.Custom, Custommessage);
                b.SetVector(m);
                curdelay = Shotdelay;
            }
        }
        public void Update(){
            curdelay -= gm.Et.GetElapsedSeconds();
            if (curdelay <= 0) {
                curdelay = 0;
            }
        }

        public void Render(){
            Graphics grpahics = gm.Rm.GetCurrentGraphic();
            Graphics2D graphics2D = (Graphics2D) grpahics;            
            Image curimg = img;
            curimg.RenderFixedHeight((int)Math.round(pos.getX()),(int)Math.round(pos.getY()), ((Number)gp.PlayData.get("ImgHeight")).intValue());
        };
    }