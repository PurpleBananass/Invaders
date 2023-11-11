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
import GamePrime.Define.ItemDefine;
import GamePrime.Define.KeyDefine;
import GamePrime.ETC.Image;
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
    SoundManager.PlayProp ShootSoundProp;
    SoundManager.PlayProp ShootSoundProp2;
    SoundManager.PlayProp ShootSoundProp3;
    SoundManager.PlayProp ShootSoundProp4;

    SoundManager.PlayProp DestroyedProp;
    SoundManager.PlayProp DestroyedProp2;
    SoundManager.PlayProp ItemGetProp;
    SoundManager.PlayProp SubshipProp;
    SoundManager.PlayProp SpeedUpProp;
    SoundManager.PlayProp InvicibleProp;
    SoundManager.PlayProp BombEquipProp;
        public void Awake() {
        gp = (GamePage) gm.CustomInstance;
        SubshipProp = gm.Sm.new PlayProp(
            "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_Item_SubShip.wav", null);
        SpeedUpProp = gm.Sm.new PlayProp(
            "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_Item_SpeedUp.wav", null);
        InvicibleProp = gm.Sm.new PlayProp(
            "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_Item_Invicible.wav", null);
        BombEquipProp = gm.Sm.new PlayProp(
            "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_Item_Bomb_Equipped.wav", null);

        ShootSoundProp = gm.Sm.new PlayProp(
            "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_Ally_Shoot_a.wav", null);
        ShootSoundProp2 = gm.Sm.new PlayProp(
            "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_Ally_Shoot_b.wav", null);
        ShootSoundProp3 = gm.Sm.new PlayProp(
            "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_Ally_Shoot_c.wav", null);
        ShootSoundProp4 = gm.Sm.new PlayProp(
            "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_Ally_Shoot_d.wav", null);

        DestroyedProp = gm.Sm.new PlayProp(
            "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_Ally_Destroy_a.wav", null);
        DestroyedProp2 = gm.Sm.new PlayProp(
            "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_Ally_Destroy_b.wav", null);
        ItemGetProp = gm.Sm.new PlayProp(
            "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_Item_Get.wav", null);

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

    public void CheckCollsion(Item item) {
        int height = ((Number) gp.PlayData.get("ImgHeight")).intValue();
        double itemMinX = item.pos.getX() - item.size / 2;
        double itemMaxX = item.pos.getX() + item.size / 2;
        double itemMinY = item.pos.getY() - item.size / 2;
        double itemMaxY = item.pos.getY() + item.size / 2;

        Image curimg = img.get(State[StateIndex]);

        double PlayerMinX = PosX - curimg.GetWidthFixHeight(height) / 2;
        double PlayerMaxX = PosX + curimg.GetWidthFixHeight(height) / 2;
        double PlayerMinY = PosY - height / 2;
        double PlayerMaxY = PosY + height / 2;
        if ((PlayerMaxX >= itemMinX && PlayerMinX <= itemMaxX) &&
                (PlayerMaxY >= itemMinY && PlayerMinY <= itemMaxY)) {
            EventSystem.Destroy(item.Obj);
            AquiredItem(item.itemIndex);
        }
    }

    void AquiredItem(int item) {
        JSONArray itemList = (JSONArray) PlayData.get("ActiveItem");
        if(this.Obj.name == "Player2"){
            itemList = (JSONArray) PlayData.get("ActiveItem2");
        }
        if (itemList.size() < 3) {
            itemList.add(item);
            gm.Sm.playSound(ItemGetProp);
        }
    }

    public void CheckCollsion(Bullet bullet) {
        if (ActiveItem[0]) {
            return;
        }

        int height = ((Number) gp.PlayData.get("ImgHeight")).intValue();
        double bulletMinX = bullet.pos.getX() - bullet.size / 2;
        double bulletMaxX = bullet.pos.getX() + bullet.size / 2;
        double bulletMinY = bullet.pos.getY() - bullet.size / 2;
        double bulletMaxY = bullet.pos.getY() + bullet.size / 2;

        Image curimg = img.get(State[StateIndex]);

        double PlayerMinX = PosX - curimg.GetWidthFixHeight(height) / 2;
        double PlayerMaxX = PosX + curimg.GetWidthFixHeight(height) / 2;
        double PlayerMinY = PosY - height / 2;
        double PlayerMaxY = PosY + height / 2;
        if ((PlayerMaxX >= bulletMinX && PlayerMinX <= bulletMaxX) &&
                (PlayerMaxY >= bulletMinY && PlayerMinY <= bulletMaxY)) {
            EventSystem.Destroy(bullet.Obj);
            Attacked();
        }
    }

    public void Attacked() {
        if (StateIndex == 0) {
            StateIndex = 1;
            int life;
            if (Obj.name == "Player1") {
                life = ((Number) PlayData.get("Life")).intValue() - 1;
                PlayData.put("Life", life);
            } else {
                life = ((Number) PlayData.get("Life2")).intValue() - 1;
                PlayData.put("Life2", life);
            }
            if (life == 0) {
                gm.Sm.playSound(DestroyedProp2);
                EventSystem.Destroy(this.Obj);
            }else{
                gm.Sm.playSound(DestroyedProp);
            }
        }
        Delay = 2;
        int point = ((Number) PlayData.get("Point")).intValue() - 30;
        PlayData.put("Point", point);
    }

    int GetMoveSpeed() {
        int basicspeed = ((Number) PlayData.get("MoveSpeed")).intValue();
        if (ActiveItem[3]) {
            basicspeed += 100;
        }
        return basicspeed;
    }

    

    public void Start() {
        Shotdelay = 0;
        Delay = 0;
        PosY = gm.frame.getHeight() - ((Number) gp.PlayData.get("ImgHeight")).intValue() * 2;
        PosX = gm.frame.getWidth() / 2;
        ActiveItem = new boolean[ItemDefine.ActiveItem.length];
        for (int i = 0; i < ItemDefine.ActiveItem.length; i++) {
            ActiveItem[i] = false;
        }
    };

    void activeItem(int index, double time) {
        ActiveItem[index] = true;
        EngineTimer.ExecuteTimer(new Runnable() {
            public void run() {
                ActiveItem[index] = false;
            }
        }, time);
    }

    public void UseItem() {
        JSONArray itemList = (JSONArray) PlayData.get("ActiveItem");
        if (Obj.name == "Player2") {
            itemList = (JSONArray) PlayData.get("ActiveItem2");
        }
        int size = itemList.size();
        if (size > 0) {
            int item = ((Number) itemList.get(size - 1)).intValue();
            if(ActiveItem[item]== true){
                return;
            }
            itemList.remove(size - 1);
            switch (ItemDefine.ActiveItem[item]) {
                case "Ghost":
                    activeItem(0, 7);
                    gm.Sm.playSound(InvicibleProp);
                    break;
                case "Auxiliary":
                    activeItem(1, 5);
                    gm.Sm.playSound(SubshipProp);
                    break;
                case "Bomb":
                    ActiveItem[2] = true;
                    gm.Sm.playSound(BombEquipProp);
                    break;
                case "SpeedUp":
                    activeItem(3, 7);
                    gm.Sm.playSound(SpeedUpProp);
                    break;
                default:
                    break;
            }
        }
    }

    public void Update() {
        if ((StateIndex == 1) || ((Number) PlayData.get("ScreenIndex")).intValue() != 0) {
            if (StateIndex == 1 && ((Number) PlayData.get("ScreenIndex")).intValue() == 0) {
                Delay -= gm.Et.GetElapsedSeconds();
                if (Delay < 0) {
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
            float max = PosX + curimg.GetHeightFixWidth(((Number) gp.PlayData.get("ImgHeight")).intValue() / 2);

            if (max > gm.frame.getWidth()) {
                PosX = gm.frame.getWidth()
                        - curimg.GetHeightFixWidth(((Number) gp.PlayData.get("ImgHeight")).intValue() / 2);
            }
        }

        if (gm.Im.isKeyPressed(KeyFunc.get("LEFT"))) {
            int movespeed = GetMoveSpeed();
            PosX -= movespeed * gm.Et.GetElapsedSeconds();
            Image curimg = img.get(State[StateIndex]);
            float min = PosX - curimg.GetHeightFixWidth(((Number) gp.PlayData.get("ImgHeight")).intValue() / 2);
            if (min < 0) {
                PosX = curimg.GetHeightFixWidth(((Number) gp.PlayData.get("ImgHeight")).intValue() / 2);
            }
        }

        if (gm.Im.isKeyDown(KeyFunc.get("ITEM"))) {
            UseItem();
        }

        if (gm.Im.isKeyPressed(KeyFunc.get("ATTACK"))) {
            if (Shotdelay == 0) {
                if (gp.HardMode && GetBullet() == 0) {
                    // 못쏴요
                } else {
                    MakeBullet(new Point2D.Float(PosX, PosY), new Point2D.Float(0, -1.0f),
                            (float) PlayData.get("ShotSpeed"));
                    if(this.Obj.name == "Player1"){
                        gm.Sm.playSound(ShootSoundProp);

                    }else{
                        gm.Sm.playSound(ShootSoundProp3);
                    }

                    if (ActiveItem[1]) {
                        MakeBullet(new Point2D.Float(PosX + 50, PosY), new Point2D.Float(0, -1.0f),
                                (float) PlayData.get("ShotSpeed"));
                        MakeBullet(new Point2D.Float(PosX - 50, PosY), new Point2D.Float(0, -1.0f),
                                (float) PlayData.get("ShotSpeed"));
                        if(this.Obj.name == "Player1"){
                            gm.Sm.playSound(ShootSoundProp2);

                        }else{
                            gm.Sm.playSound(ShootSoundProp4);
                        }
                    }
                    if (ActiveItem[2]) {
                        ActiveItem[2] = false;
                    }
                    if (gp.HardMode) {
                        SetBullet(GetBullet() - 1);
                    }

                    if (Obj.name == "Player1") {
                        PlayData.put("ShotCount",((Number) gp.PlayData.get("ShotCount")).intValue()+1);
                    }else{
                        PlayData.put("ShotCount",((Number) gp.PlayData.get("ShotCount2")).intValue()+1);
                    }
                    Shotdelay = ((Number) PlayData.get("ShotDelay")).floatValue();

                }
            }
        }

        Shotdelay -= gm.Et.GetElapsedSeconds();
        if (Shotdelay <= 0) {
            Shotdelay = 0;
        }

        if (gm.Im.isKeyPressed(KeyFunc.get("RELOAD"))) {
            if (gp.HardMode) {
                if (GetBullet() == 0 &&
                        GetMegazine() != 0) {
                    SetBullet(10);
                    SetMegazine(GetMegazine() - 1);
                }
            }
        }
    };

    void SetMegazine(int m) {
        if (Obj.name == "Player1") {

            PlayData.put("Magazine1", m);

        } else {
            PlayData.put("Magazine2", m);
        }
    }

    int GetMegazine() {
        if (Obj.name == "Player1") {

            return ((Number) PlayData.get("Magazine1")).intValue();

        } else {
            return ((Number) PlayData.get("Magazine2")).intValue();
        }
    }

    void SetBullet(int m) {
        if (Obj.name == "Player1") {

            PlayData.put("Bullet1", m);

        } else {
            PlayData.put("Bullet2", m);
        }
    }

    int GetBullet() {
        if (Obj.name == "Player1") {

            return ((Number) PlayData.get("Bullet1")).intValue();

        } else {
            return ((Number) PlayData.get("Bullet2")).intValue();
        }
    }

    public void MakeBullet(Point2D pos, Point2D dir, float ShotSpeed) {
        if (ActiveItem[2]) {
            Bomb.MakeBomb(pos, dir, ShotSpeed, "PBullet",Obj.name);
        } else {
            Bullet.MakeBullet(pos, dir, ShotSpeed, "PBullet",Obj.name);
        }
    }

    public void Render() {
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        Graphics2D graphics2D = (Graphics2D) grpahics;
        Image curimg = img.get(State[StateIndex]);
        curimg.RenderFixedHeight(Math.round(PosX), Math.round(PosY),
                ((Number) gp.PlayData.get("ImgHeight")).intValue());
    };
}
