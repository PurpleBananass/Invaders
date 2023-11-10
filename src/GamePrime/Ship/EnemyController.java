package GamePrime.Ship;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.ArrayList;
import EnginePrime.Component;
import EnginePrime.Entity;
import EnginePrime.EventSystem;
import EnginePrime.GameManager;
import EnginePrime.Message;
import EnginePrime.Message.MessageType;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import EnginePrime.FileManager;
import EnginePrime.GManager;
import EnginePrime.GameManager;
import EnginePrime.SoundManager;
import GamePrime.Image;
import GamePrime.ItemDefine;
import GamePrime.KeyDefine;
import GamePrime.ShipDefine;
import GamePrime.Page.GamePage;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class EnemyController extends Component {
    public static enum EnemyType {
        Easy,
        Hard,
    }

    int downPixel = 100;
    GamePage gp;
    GameManager gm = GameManager.getInstance();
    float dir;
    ArrayList<ArrayList<Enemy>> EnemyPool = new ArrayList<ArrayList<Enemy>>();
    int col;
    int row;
    int enemynum;
    float Shotdelay;

    public void Start() {
        Shotdelay = 2;
        enemynum = 0;
        gp = (GamePage) gm.CustomInstance;
        dir = 1;
        col = 0;
        row = 0;
        int level = ((Number) gp.PlayData.get("Level")).intValue();
        switch (level) {
            case 1:     
                Level1(3, 4);
                break;
            case 2:
                Level2(1, 2);
                break;
            case 3:
                Level2(2, 2);
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
            }
            enemynum -= 1;
            int point = ((Number) gp.PlayData.get("Point")).intValue() + e.Point;
            gp.PlayData.put("Point", point);
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
        if (type == EnemyType.Easy) {
            Custommessage.put("Life", 1);
            Custommessage.put("Point", 10);
            Custommessage.put("ShotSpeed", 300);
            Custommessage.put("ShotDelay", 2.0f);
            Custommessage.put("Life", 1);
            Custommessage.put("Type", type);
            Custommessage.put("IdleImg", "Cirno");
            Custommessage.put("DieImg", "Cirno");
            Custommessage.put("Item",new Random().nextInt(ItemDefine.ActiveItem.length+1)-1);
        } else if (type == EnemyType.Hard) {
            Custommessage.put("Life", 3);
            Custommessage.put("Point", 50);
            Custommessage.put("ShotSpeed", 500);
            Custommessage.put("ShotDelay", 1.0f);
            Custommessage.put("Type", type);
            Custommessage.put("IdleImg", "Flandre");
            Custommessage.put("DieImg", "Flan_Fuck");
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
                Enemy e = CreateEnemy(EnemyType.Easy);
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

                EnemyType type = EnemyType.Easy;
                if (i % 2 == 1) {
                    type = EnemyType.Hard;
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
            moveY += downPixel;
        } else if (x + moveX < 0) {
            dir = 1;
            moveX = -x;
            moveY += downPixel;
        }

        EventSystem.FindEntity("Player1").GetComponent(Player.class);
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
