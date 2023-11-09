package GamePrime.Page;

import EnginePrime.Entity;
import EnginePrime.EventSystem;
import EnginePrime.GManager;
import EnginePrime.GameManager;
import EnginePrime.SoundManager;
import GamePrime.Ship.Player;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.FontMetrics;
import org.json.simple.JSONObject;
public class GamePage implements GManager{

    GameManager gm = GameManager.getInstance();
    int SelectIndex;
    int PlayMode;
    boolean HardMode;
    public void Initialize(){
        SelectIndex = 1;
        PlayMode = ((Number)gm.GlobalData.get("LocalData").get("PlayMode")).intValue();
        HardMode =(boolean) gm.GlobalData.get("LocalData").get("HardMode");

        JSONObject ItemData = (JSONObject)gm.GlobalData.get("LocalData").get("Item");
        JSONObject PlayData = (JSONObject)gm.GlobalData.get("LocalData").get("PlayData");
        if(PlayData == null){
            PlayData= new JSONObject();
            gm.GlobalData.get("LocalData").put("PlayData",PlayData);
            PlayData.put("Life",3);
            PlayData.put("MoveSpeed",100);
            PlayData.put("ShotSpeed",1);
        }
        PlayData.put("Pause",true);
        if((boolean)ItemData.get("BonusLife")){
            ItemData.put("BonusLife",false);
            PlayData.put("Life",((Number)PlayData.get("Life")).intValue()+1);
        }
        if((boolean)ItemData.get("MoveSpeed")){
            ItemData.put("MoveSpeed",false);
            PlayData.put("MoveSpeed",((Number)PlayData.get("MoveSpeed")).intValue()+100);
        }
        if((boolean)ItemData.get("ShotSpeed")){
            ItemData.put("ShotSpeed",false);
            PlayData.put("ShotSpeed",((Number)PlayData.get("ShotSpeed")).intValue()+1);
        }
        EntityInitialize();
    };

    private void EntityInitialize(){
        EventSystem.DestroyAll();
        Entity player =  EventSystem.Initiate("Player1");
        player.AddComponent(Player.class);

        if (PlayMode == 1){
            Entity player2 =  EventSystem.Initiate("Player2");
            player2.AddComponent(Player.class);
        }
    }

    public void PreUpdate(){
        switch (SelectIndex) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }


    };

    public void LateUpdate(){
    
        



    };
}
