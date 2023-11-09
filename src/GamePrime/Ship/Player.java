package GamePrime.Ship;
import java.awt.event.KeyEvent;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.awt.Graphics;
import EnginePrime.Component;
import EnginePrime.GameManager;
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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Player extends Component{

    public float Pos;
    GameManager gm = GameManager.getInstance();
    JSONObject PlayData;
    private Map<String, BufferedImage> img = new HashMap<>();
    String imgString[] = { "Idle", "Die" };
    private HashMap<String, Integer> KeyFunc = new HashMap<>();


    public void Awake() {
        FileManager fm = new FileManager();
        String ship = null;
        JSONObject data = gm.GlobalData.get("Setting");
        if (this.Obj.name == "Player1") {
            ship = (String) gm.GlobalData.get("LocalData").get("P1_Ship");
            JSONArray keySettings = (JSONArray) data.get("KeySetting_1p");
            for (int i = 0; i < KeyDefine.KeyFunc.length; i++){
                KeyFunc.put(KeyDefine.KeyFunc[i], ((Number) keySettings.get(i)).intValue());
            }
        }
        else if(this.Obj.name == "Player2"){
            ship = (String) gm.GlobalData.get("LocalData").get("P2_Ship");
            JSONArray keySettings = (JSONArray) data.get("KeySetting_2p");
            for (int i = 0; i < KeyDefine.KeyFunc.length; i++){
                KeyFunc.put(KeyDefine.KeyFunc[i], ((Number) keySettings.get(i)).intValue());
            }
        }

        if(ship == ShipDefine.Ship[0]){
            img.put("Idle", fm.GetImage("res" + File.separator + "Img" + File.separator + "Reimu.png"));
            img.put("Die", fm.GetImage("res" + File.separator + "Img" + File.separator + "Reimu.png"));
        }else if(ship == ShipDefine.Ship[1]){
            img.put("Idle", fm.GetImage("res" + File.separator + "Img" + File.separator + "Marisa.png"));
            img.put("Die", fm.GetImage("res" + File.separator + "Img" + File.separator + "Marisa.png"));
        }
    }


    public void Start(){
        Pos = gm.frame.getWidth()/2;
        PlayData =(JSONObject)gm.GlobalData.get("LocalData").get("PlayData");
    };
    public void Update(){

        if(gm.Im.isKeyPressed(KeyFunc.get("RIGHT"))){
            int movespeed = ((Number) PlayData.get("MoveSpeed")).intValue();
            Pos += movespeed *gm.Et.GetElapsedSeconds();
        }

        if(gm.Im.isKeyPressed(KeyFunc.get("LEFT"))){
            int movespeed = ((Number) PlayData.get("MoveSpeed")).intValue();
            Pos -= movespeed *gm.Et.GetElapsedSeconds();
        }
    };
    public void Ondestroy(){


    };
    public void Render(){

        Graphics grpahics = gm.Rm.GetCurrentGraphic();


        Graphics2D graphics2D = (Graphics2D)grpahics;
        BufferedImage curimg = img.get(imgString[0]);
        int height = 100;
        float width = height*curimg.getWidth()/(float)curimg.getHeight();
        graphics2D.drawImage(curimg, Math.round(Pos- width/2),gm.frame.getHeight()- height - 100, Math.round(width),height,null);
        
        //graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        //graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        


    };
}
