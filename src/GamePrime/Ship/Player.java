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
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import EnginePrime.FileManager;
import EnginePrime.GManager;
import EnginePrime.GameManager;
import EnginePrime.SoundManager;
import EnginePrime.Message.MessageType;
import GamePrime.Image;
import GamePrime.KeyDefine;
import GamePrime.ShipDefine;
import GamePrime.Page.GamePage;

import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Player extends Component {

    String State[] = { "Idle", "Die" };
    public float PosX;
    public float PosY;
    GameManager gm = GameManager.getInstance();
    GamePage gp;
    JSONObject PlayData;
    private Map<String, Image> img = new HashMap<>();
    int StateIndex = 0;
    private HashMap<String, Integer> KeyFunc = new HashMap<>();
    float Shotdelay;
    float Delay;


    public void CheckCollsion(Bullet bullet){
        int height = ((Number)gp.PlayData.get("ImgHeight")).intValue();
        double bulletMinX = bullet.pos.getX() - bullet.size/2; 
        double bulletMaxX = bullet.pos.getX() + bullet.size/2; 
        double bulletMinY = bullet.pos.getY() - bullet.size/2; 
        double bulletMaxY = bullet.pos.getY() + bullet.size/2; 
        
        Image curimg = img.get(State[StateIndex]);

        double PlayerMinX = PosX - curimg.GetWidthFixHeight(height) /2; 
        double PlayerMaxX = PosX + curimg.GetWidthFixHeight(height)/2; 
        double PlayerMinY = PosY - height/2; 
        double PlayerMaxY = PosY + height/2; 
        if ((PlayerMaxX >= bulletMinX && PlayerMinX <= bulletMaxX) && 
            (PlayerMaxY >= bulletMinY && PlayerMinY <= bulletMaxY)) {
                EventSystem.Destroy(bullet.Obj);                
                Attacked();
        } 
    }

    public void Attacked(){
        if(StateIndex == 0){
            StateIndex = 1;
            int life = ((Number) PlayData.get("Life")).intValue()-1;
            Delay = 2;
            PlayData.put("Life", life);
            if(life == 0){
                PlayData.put("ScreenIndex",3);
            }
        }
    }

    public void Awake() {
        gp = (GamePage) gm.CustomInstance;
        String ship = null;
        JSONObject data = gm.GlobalData.get("Setting");
        PlayData = (JSONObject) gm.GlobalData.get("LocalData").get("PlayData");
        if (this.Obj.name == "Player1") {
            ship = (String) gm.GlobalData.get("LocalData").get("P1_Ship");
            JSONArray keySettings = (JSONArray) data.get("KeySetting_1p");
            for (int i = 0; i < KeyDefine.KeyFunc.length; i++) {
                KeyFunc.put(KeyDefine.KeyFunc[i], ((Number) keySettings.get(i)).intValue());
            }
        } else if (this.Obj.name == "Player2") {
            ship = (String) gm.GlobalData.get("LocalData").get("P2_Ship");
            JSONArray keySettings = (JSONArray) data.get("KeySetting_2p");
            for (int i = 0; i < KeyDefine.KeyFunc.length; i++) {
                KeyFunc.put(KeyDefine.KeyFunc[i], ((Number) keySettings.get(i)).intValue());
            }
        }
        if (ship == ShipDefine.Ship[0]) {
            img.put(State[0], gp.ImgRes.get("Reimu"));
            img.put(State[1], gp.ImgRes.get("Marisa"));
        } else if (ship == ShipDefine.Ship[1]) {
            img.put(State[0], gp.ImgRes.get("Marisa"));
            img.put(State[1], gp.ImgRes.get("Reimu"));
        }
    }

    public void Start() {
        Shotdelay = 0;
        Delay = 0;
        PosY =  gm.frame.getHeight() - ((Number)gp.PlayData.get("ImgHeight")).intValue()* 2;
        PosX = gm.frame.getWidth() / 2;
    };

    public void Update() {
        if ((StateIndex == 1) || ((Number) PlayData.get("ScreenIndex")).intValue() != 0) {
            if (StateIndex == 1 && ((Number) PlayData.get("ScreenIndex")).intValue() == 0){
                Delay -=  gm.Et.GetElapsedSeconds();
                if(Delay < 0 )
                {
                    StateIndex = 0;
                    Delay = 0;
                }
            }
            return;
        }
        if (gm.Im.isKeyPressed(KeyFunc.get("RIGHT"))) {
            int movespeed = ((Number) PlayData.get("MoveSpeed")).intValue();
            PosX += movespeed * gm.Et.GetElapsedSeconds();
            Image curimg = img.get(State[StateIndex]);
            float max = PosX +curimg.GetHeightFixWidth(((Number)gp.PlayData.get("ImgHeight")).intValue()/2);

            if(max>gm.frame.getWidth()){
                PosX = gm.frame.getWidth() - curimg.GetHeightFixWidth(((Number)gp.PlayData.get("ImgHeight")).intValue()/2);
            }
        }

        if (gm.Im.isKeyPressed(KeyFunc.get("LEFT"))) {
            int movespeed = ((Number) PlayData.get("MoveSpeed")).intValue();
            PosX -= movespeed * gm.Et.GetElapsedSeconds();
            Image curimg = img.get(State[StateIndex]);
            float min = PosX -curimg.GetHeightFixWidth(((Number)gp.PlayData.get("ImgHeight")).intValue()/2);

            if(min<0){
                PosX = curimg.GetHeightFixWidth(((Number)gp.PlayData.get("ImgHeight")).intValue()/2);
            }
        }
        if (gm.Im.isKeyPressed(KeyFunc.get("ATTACK"))) {
            if (Shotdelay == 0) {
                Entity bullet = EventSystem.Initiate();
                Bullet b =  bullet.AddComponent(Bullet.class);
                bullet.tag = "PBullet";
                JSONObject Custommessage = new JSONObject();
                Custommessage.put("Entity", bullet);
                Custommessage.put("Func", "SetVector");
                Custommessage.put("dir", new Point2D.Float(0, -1.0f));
                Custommessage.put("pos", new Point2D.Float(PosX, PosY));
                Message m = new Message(this.Obj, MessageType.Custom, Custommessage);
                b.SetVector(m);
                Shotdelay = ((Number) PlayData.get("ShotDelay")).floatValue();
            }
        }

        Shotdelay -= gm.Et.GetElapsedSeconds();
        if (Shotdelay <= 0) {
            Shotdelay = 0;
        }

        if (gm.Im.isKeyPressed(KeyFunc.get("RELOAD"))) {

        }
        if (gm.Im.isKeyPressed(KeyFunc.get("ITEM"))) {

        }
    };

    public void Render() {
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        Graphics2D graphics2D = (Graphics2D) grpahics;
        Image curimg = img.get(State[StateIndex]);
        curimg.RenderFixedHeight(Math.round(PosX), Math.round(PosY), ((Number)gp.PlayData.get("ImgHeight")).intValue());
    };
}
