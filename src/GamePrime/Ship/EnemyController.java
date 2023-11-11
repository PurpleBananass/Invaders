package GamePrime.Ship;

import java.awt.geom.Point2D;
import org.json.simple.JSONObject;
import java.util.Iterator;
import java.util.ArrayList;
import EnginePrime.Component;
import EnginePrime.Entity;
import EnginePrime.EventSystem;
import EnginePrime.GameManager;
import EnginePrime.Message;
import EnginePrime.Message.MessageType;
import java.util.Random;
import java.io.File;
import EnginePrime.SoundManager;
import GamePrime.Define.ItemDefine;
import GamePrime.Page.GamePage;

public class EnemyController extends Component {
    public final static int MaxStageCount = 3;
    final int MoveDownPixel = 100;
    public static enum EnemyType {
        EnemyType1,
        EnemyType2,
    }
    
    GamePage gp;
    GameManager gm = GameManager.getInstance();
    JSONObject res = gm.GlobalData.get("Resource");

    float dir;
    ArrayList<ArrayList<Enemy>> EnemyPool = new ArrayList<ArrayList<Enemy>>();
    int col;
    int row;
    int enemynum;
    float Shotdelay;



    SoundManager.PlayProp DestroySoundProp;
    SoundManager.PlayProp DestroySoundProp2;
    SoundManager.PlayProp ItemSoundProp;
    
    
    
    public void Start() {
        JSONObject BGM = (JSONObject)res.get("BGM");
        JSONObject SFX = (JSONObject)res.get("SFX");
        ItemSoundProp = gm.Sm.new PlayProp("Sound" + File.separator + "SFX" + File.separator + (String)SFX.get("ItemCreate"), null);
        DestroySoundProp = gm.Sm.new PlayProp("Sound" + File.separator + "SFX" + File.separator + (String)SFX.get("Destroyed"), null);
        DestroySoundProp2 = gm.Sm.new PlayProp("Sound" + File.separator + "SFX" + File.separator + (String)SFX.get("Bomb"), null);
        Shotdelay = 2;
        enemynum = 0;
        gp = (GamePage) gm.CustomInstance;
        dir = 1;
        col = 0;
        row = 0;
        int level = ((Number) gp.PlayData.get("Level")).intValue();
        SoundManager.PlayProp BgmProp;
        switch (level) {
            case 1:     
                Level1(3, 4);
                BgmProp = gm.Sm.new PlayProp("Sound" + File.separator + "BGM" + File.separator +(String)BGM.get("Level1"), "BGM");
                BgmProp.count = -1;
                gm.Sm.stopClip("BGM", 1);
                gm.Sm.playSound(BgmProp);
                break;
            case 2:
                Level2(1, 2);
                BgmProp = gm.Sm.new PlayProp("Sound" + File.separator + "BGM" + File.separator +(String)BGM.get("Level2"), "BGM");
                BgmProp.count = -1;
                gm.Sm.stopClip("BGM", 1);
                gm.Sm.playSound(BgmProp);
                break;
            case 3:
                Level2(4, 4);
                BgmProp = gm.Sm.new PlayProp("Sound" + File.separator + "BGM" + File.separator +(String)BGM.get("Level3"), "BGM");
                BgmProp.count = -1;
                gm.Sm.stopClip("BGM", 1);
                gm.Sm.playSound(BgmProp);
                break;
            default:
                break;
        }
    };

    public void Update() {
        if (((Number) gp.PlayData.get("ScreenIndex")).intValue() != 0) {
            return;
        }
        if (EnemyPool.size() == 0) {
            gp.PlayData.put("ScreenIndex", 3);
            gp.PlayData.put("LevelClear", true);
            return;
        }
        if (Shotdelay == 0) {
            Random random = new Random();
            for (int i = 0; i < random.nextInt(enemynum); i++) {
                int randRow = random.nextInt(EnemyPool.size());
                int randCol = random.nextInt(EnemyPool.get(randRow).size());
                EnemyPool.get(randRow).get(randCol).Shoot();
            }
            Shotdelay = random.nextInt(enemynum);
        }
        Shotdelay -= gm.Et.GetElapsedSeconds();
        if (Shotdelay <= 0) {
            Shotdelay = 0;
        }
        Move();
    };

    public void CheckCollsion(Bullet bullet) {
        int height = ((Number) gp.PlayData.get("ImgHeight")).intValue();
        double bulletMinX = bullet.pos.getX() - bullet.size / 2;
        double bulletMaxX = bullet.pos.getX() + bullet.size / 2;
        double bulletMinY = bullet.pos.getY() - bullet.size / 2;
        double bulletMaxY = bullet.pos.getY() + bullet.size / 2;

        for (ArrayList<Enemy> eList : EnemyPool) {
            if (!bullet.Obj.isAlve) {
                return;
            }
            Iterator<Enemy> iterator = eList.iterator();
            while (iterator.hasNext()) {
                Enemy e = iterator.next();
                double MinX = e.pos.getX() - e.IdleImg.GetWidthFixHeight(height) / 2;
                double MaxX = e.pos.getX() + e.IdleImg.GetWidthFixHeight(height) / 2;
                double MinY = e.pos.getY() - height / 2;
                double MaxY = e.pos.getY() + height / 2;
                if ((MaxX >= bulletMinX && MinX <= bulletMaxX) &&
                        (MaxY >= bulletMinY && MinY <= bulletMaxY)) {
                    Collsion(e,bullet);
                    if(e.life == 0){
                        iterator.remove();
                    }
                    break;
                }
            }
        }

        Iterator<ArrayList<Enemy>> iterator = EnemyPool.iterator();
        while (iterator.hasNext()) {
            ArrayList<Enemy> array = iterator.next();
            if (array.size() == 0) {
                iterator.remove();
            }
        }
    }

    private void Collsion(Enemy e, Bullet bullet) {
        e.Attacked();
        if (e.life == 0) {
            if(e.item != -1){
                Entity itemEntity = EventSystem.Initiate();
                Item item =  itemEntity.AddComponent(Item.class);
                itemEntity.tag = "Item";
                JSONObject Custommessage = new JSONObject();
                Custommessage.put("Func", "SetVector");
                Custommessage.put("pos", e.pos);
                Custommessage.put("Item", e.item);
                Message m = new Message(this.Obj, MessageType.Custom, Custommessage);
                item.SetVector(m);
                gm.Sm.playSound(ItemSoundProp);
            }
            enemynum -= 1;
            if (bullet instanceof Bomb) {
                gm.Sm.playSound(DestroySoundProp2);
            }else{
                gm.Sm.playSound(DestroySoundProp);
            }
            int point = ((Number) gp.PlayData.get("Point")).intValue() + e.Point;
            gp.PlayData.put("Point", point);
            if(bullet.MadeBY == "Player1"){
                gp.PlayData.put("KillCount",((Number) gp.PlayData.get("KillCount")).intValue()+1);
            }else{
                
                gp.PlayData.put("KillCount2",((Number) gp.PlayData.get("KillCount2")).intValue()+1);
            }

        } 
        if (bullet instanceof Bomb) {
            Bomb bomb = (Bomb)bullet;
            for(Point2D p : bomb.dirList){
                Bullet.MakeBullet(bullet.pos, p, bullet.ShotSpeed, "PBullet",bullet.MadeBY);
            }
        }
        EventSystem.Destroy(bullet.Obj);
    }

    private Enemy CreateEnemy(EnemyType type) {
        Enemy enemy = EventSystem.Initiate().AddComponent(Enemy.class, 2);
        SetEnemyInfo(enemy, type);
        return enemy;
    };

    public void SetEnemyInfo(Enemy e, EnemyType type) {
        JSONObject Custommessage = new JSONObject();
        Custommessage.put("Entity", e.Obj);
        Custommessage.put("Func", "SetInfo");
        if (type == EnemyType.EnemyType1) {
            Custommessage.put("Life", 1);
            Custommessage.put("Point", 10);
            Custommessage.put("ShotSpeed", 300);
            Custommessage.put("ShotDelay", 2.0f);
            Custommessage.put("Life", 1);
            Custommessage.put("Type", type);
            Custommessage.put("IdleImg", "EnemyType1.Idle");
            Custommessage.put("DieImg", "EnemyType1.Destroyed");
            Custommessage.put("Item",new Random().nextInt(ItemDefine.ActiveItem.length+1)-1);
        } else if (type == EnemyType.EnemyType2) {
            Custommessage.put("Life", 3);
            Custommessage.put("Point", 50);
            Custommessage.put("ShotSpeed", 500);
            Custommessage.put("ShotDelay", 1.0f);
            Custommessage.put("Type", type);
            Custommessage.put("IdleImg", "EnemyType2.Idle");
            Custommessage.put("DieImg", "EnemyType2.Destroyed");
            Custommessage.put("Item",new Random().nextInt(ItemDefine.ActiveItem.length+1)-1);
        }
        Message m = new Message(this.Obj, MessageType.Custom, Custommessage);
        e.SetInfo(m);
    }

    public void SetEnemyPos(Enemy e, Point2D pos) {
        JSONObject Custommessage = new JSONObject();
        Custommessage.put("Entity", e.Obj);
        Custommessage.put("Func", "SetVector");
        Custommessage.put("pos", new Point2D.Double(pos.getX(), pos.getY()));
        Message m = new Message(this.Obj, MessageType.Custom, Custommessage);
        e.SetVector(m);
    }

    public void Level1(int x, int y) {
        enemynum = x * y;
        int posx = gm.frame.getWidth() * 4 / 10;
        int posy = gm.frame.getHeight() / 10;
        this.col = x;
        this.row = y;
        for (int i = 0; i < y; i++) {
            ArrayList<Enemy> EnemyList = new ArrayList();
            EnemyPool.add(EnemyList);
            for (int j = 0; j < x; j++) {
                Enemy e = CreateEnemy(EnemyType.EnemyType1);
                Point2D pos = new Point2D.Double(posx, posy);
                SetEnemyPos(e, pos);
                posx += e.ImgGetWidth();
                EnemyList.add(e);
            }
            posx = gm.frame.getWidth() * 4 / 10;
            posy += ((Number) gp.PlayData.get("ImgHeight")).intValue();
        }
    }

    public void Level2(int x, int y) {
        enemynum = x * y;
        int posx = gm.frame.getWidth() * 4 / 10;
        int posy = gm.frame.getHeight() / 10;
        this.col = x;
        this.row = y;
        for (int i = 0; i < y; i++) {
            ArrayList<Enemy> EnemyList = new ArrayList();
            EnemyPool.add(EnemyList);
            for (int j = 0; j < x; j++) {

                EnemyType type = EnemyType.EnemyType1;
                if (i % 2 == 1) {
                    type = EnemyType.EnemyType2;
                }
                Enemy e = CreateEnemy(type);
                Point2D pos = new Point2D.Double(posx, posy);
                SetEnemyPos(e, pos);
                posx += e.ImgGetWidth();
                EnemyList.add(e);
            }
            posx = gm.frame.getWidth() * 4 / 10;
            posy += ((Number) gp.PlayData.get("ImgHeight")).intValue();
        }

    }

    void Move() {
        if(enemynum==0){
            return;
        }
        ArrayList<Enemy> EnemyList = EnemyPool.get(0);
        Enemy first = EnemyList.get(0);
        EnemyList = EnemyPool.get(EnemyPool.size() - 1);
        Enemy lastY = EnemyList.get(EnemyList.size() - 1);

        int maxSize = 0;
        for (ArrayList<Enemy> eList : EnemyPool) {
            int currentSize = eList.size();
            // 현재 리스트가 이전에 찾은 가장 큰 크기보다 큰 경우
            if (currentSize > maxSize) {
                // 가장 큰 크기를 갱신하고 해당 리스트를 저장
                maxSize = currentSize;
                EnemyList = eList;
            }
        }
        Enemy lastX = EnemyList.get(EnemyList.size() - 1);
        float w = (float) (lastX.pos.getX() - first.pos.getX() + lastX.ImgGetWidth());
        float h = (float) (lastY.pos.getY() - first.pos.getY() + ((Number) gp.PlayData.get("ImgHeight")).intValue());

        float x = (float) first.pos.getX() - first.ImgGetWidth() / 2;
        float y = (float) first.pos.getY() - ((Number) gp.PlayData.get("ImgHeight")).intValue() / 2;

        float moveX = (int) Math.round(50 * dir * gm.Et.GetElapsedSeconds());
        float moveY = 0;
        if (x + moveX + w > gm.frame.getWidth()) {
            dir = -1;
            moveX = gm.frame.getWidth() - w - x;
            moveY += MoveDownPixel;
        } else if (x + moveX < 0) {
            dir = 1;
            moveX = -x;
            moveY += MoveDownPixel;
        }

        if (y + h + moveY > gm.frame.getHeight() - ((Number) gp.PlayData.get("ImgHeight")).intValue() * 3) {
            moveY = gm.frame.getHeight() - ((Number) gp.PlayData.get("ImgHeight")).intValue() * 3 - y - h;
        }
        for (ArrayList<Enemy> eList : EnemyPool) {
            for (Enemy e : eList) {
                SetEnemyPos(e, new Point2D.Double(e.pos.getX() + moveX, e.pos.getY() + moveY));
            }
        }
    }
}
