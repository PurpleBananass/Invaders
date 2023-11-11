package GamePrime.Ship;

import java.awt.event.KeyEvent;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.awt.Graphics;
import EnginePrime.Component;
import EnginePrime.EngineTimer;
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
import GamePrime.ItemDefine;
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
    boolean ActiveItem[];

    public void CheckCollsion(Item item){
        int height = ((Number)gp.PlayData.get("ImgHeight")).intValue();
        double itemMinX = item.pos.getX() - item.size/2; 
        double itemMaxX = item.pos.getX() + item.size/2; 
        double itemMinY = item.pos.getY() - item.size/2; 
        double itemMaxY = item.pos.getY() + item.size/2; 
        
        Image curimg = img.get(State[StateIndex]);

        double PlayerMinX = PosX - curimg.GetWidthFixHeight(height) /2; 
        double PlayerMaxX = PosX + curimg.GetWidthFixHeight(height)/2; 
        double PlayerMinY = PosY - height/2; 
        double PlayerMaxY = PosY + height/2; 
        if ((PlayerMaxX >= itemMinX && PlayerMinX <= itemMaxX) && 
            (PlayerMaxY >= itemMinY && PlayerMinY <= itemMaxY)) {
                EventSystem.Destroy(item.Obj);                
                AquiredItem(item.itemIndex);
        } 
    }

    void AquiredItem(int item){
        JSONArray itemList = (JSONArray)PlayData.get("ActiveItem");
        if(itemList.size()<3){
            itemList.add(item);
        }
    }


    public void CheckCollsion(Bullet bullet){
        if(ActiveItem[0]){
            return;
        }

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
            int point = ((Number) PlayData.get("Point")).intValue()-30;
            PlayData.put("Point", point);

            PlayData.put("Life", life);
            if(life == 0){
                PlayData.put("ScreenIndex",3);
            }
        }
    }


    int GetMoveSpeed(){
        int basicspeed =((Number) PlayData.get("MoveSpeed")).intValue();
        if(ActiveItem[3]){
            basicspeed +=100;
        }
        return basicspeed;
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
        ActiveItem = new boolean[ItemDefine.ActiveItem.length];
        for(int i = 0 ; i<ItemDefine.ActiveItem.length; i++){
            ActiveItem[i] = false;
        }
    };



    void activeItem(int index,double time) {
        ActiveItem[index] = true;
        EngineTimer.ExecuteTimer(new Runnable() {
            public void run() {
                ActiveItem[index] = false;
            }
        }, time);
    }


    public void UseItem(){
        JSONArray itemList = (JSONArray)PlayData.get("ActiveItem");
        int size = itemList.size();
        if(size>0){
            int item = ((Number)itemList.get(size-1)).intValue();
            itemList.remove(size-1);
            switch (ItemDefine.ActiveItem[item]) {
                case "Ghost":
                    activeItem(0,7);
                    break;
                case "Auxiliary":
                    activeItem(1,5);
                    break;
                case "Bomb":
                    ActiveItem[2] = true;
                    break;
                case "SpeedUp":
                    activeItem(3,7);
                    break;
                default:
                    break;
            }
        }
    }

    public void Update() {
        ActiveItem[2] = true;



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
            int movespeed = GetMoveSpeed();
            PosX += movespeed * gm.Et.GetElapsedSeconds();
            Image curimg = img.get(State[StateIndex]);
            float max = PosX +curimg.GetHeightFixWidth(((Number)gp.PlayData.get("ImgHeight")).intValue()/2);

            if(max>gm.frame.getWidth()){
                PosX = gm.frame.getWidth() - curimg.GetHeightFixWidth(((Number)gp.PlayData.get("ImgHeight")).intValue()/2);
            }
        }

        if (gm.Im.isKeyPressed(KeyFunc.get("LEFT"))) {
            int movespeed = GetMoveSpeed();
            PosX -= movespeed * gm.Et.GetElapsedSeconds();
            Image curimg = img.get(State[StateIndex]);
            float min = PosX -curimg.GetHeightFixWidth(((Number)gp.PlayData.get("ImgHeight")).intValue()/2);
            if(min<0){
                PosX = curimg.GetHeightFixWidth(((Number)gp.PlayData.get("ImgHeight")).intValue()/2);
            }
        }

        if (gm.Im.isKeyDown(KeyFunc.get("ITEM"))) {
            UseItem();
        }

        if (gm.Im.isKeyPressed(KeyFunc.get("ATTACK"))) {
            if (Shotdelay == 0) {
                MakeBullet(new Point2D.Float(PosX, PosY), new Point2D.Float(0, -1.0f),(float)PlayData.get("ShotSpeed"));
                if(ActiveItem[1]){
                    MakeBullet(new Point2D.Float(PosX+50, PosY), new Point2D.Float(0, -1.0f), (float)PlayData.get("ShotSpeed"));
                    MakeBullet(new Point2D.Float(PosX-50, PosY), new Point2D.Float(0, -1.0f),(float)PlayData.get("ShotSpeed"));
                }
                if(ActiveItem[2]){
                    ActiveItem[2] = false;
                }
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

    public void MakeBullet(Point2D pos,Point2D dir,float ShotSpeed){
        if(ActiveItem[2]){
            Bomb.MakeBomb(pos, dir, ShotSpeed, "PBullet");
        }else{
            Bullet.MakeBullet(pos, dir, ShotSpeed, "PBullet");
        }
    }

    public void Render() {
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        Graphics2D graphics2D = (Graphics2D) grpahics;
        Image curimg = img.get(State[StateIndex]);
        curimg.RenderFixedHeight(Math.round(PosX), Math.round(PosY), ((Number)gp.PlayData.get("ImgHeight")).intValue());
    };
}
