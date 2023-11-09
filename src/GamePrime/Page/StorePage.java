package GamePrime.Page;
import EnginePrime.FileManager;
import EnginePrime.GManager;
import EnginePrime.GameManager;
import EnginePrime.SoundManager;
import GamePrime.ItemDefine;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.FontMetrics;
import org.json.simple.JSONObject;

public class StorePage implements GManager {
    private SoundManager.PlayProp menuSoundProp;
    GameManager gm = GameManager.getInstance();
    private Map<String, BufferedImage> img = new HashMap<>();
    String imgString[] = { "Flan's Shadow", "Sakuya" };

    int SelectIndex;
    public void Initialize(){
        SelectIndex = 0;
        FileManager fm = new FileManager();

        img.put("Flan's Shadow", fm.GetImage("res" + File.separator + "Img" + File.separator + "Flan's Shadow.png"));
        img.put("Sakuya", fm.GetImage("res" + File.separator + "Img" + File.separator + "Sakuya.png"));
        menuSoundProp = gm.Sm.new PlayProp(
            "res" + File.separator + "Sound" + File.separator + "SFX" + File.separator + "S_MenuClick.wav", null);


    };

    public void PreUpdate(){
        if (gm.Im.isKeyDown(KeyEvent.VK_LEFT)) {
            SelectIndex = SelectIndex== 0 ? ItemDefine.StoreItem.length -1: SelectIndex-1;
        }
        if (gm.Im.isKeyDown(KeyEvent.VK_RIGHT)) {
            SelectIndex = SelectIndex== ItemDefine.StoreItem.length -1 ? 0: SelectIndex+1;
        }
        if (gm.Im.isKeyDown(KeyEvent.VK_SPACE)) {

            String name = ItemDefine.StoreItem[SelectIndex].name;
            int price = ItemDefine.StoreItem[SelectIndex].value;
            int money = ((Number)gm.GlobalData.get("LocalData").get("Money")).intValue();
            JSONObject item = (JSONObject)gm.GlobalData.get("LocalData").get("Item");
            if(money >= price && !(boolean)item.get(name)){
                money = money-price;
                ((JSONObject)(gm.GlobalData.get("LocalData").get("Item"))).put(name,true);
                gm.GlobalData.get("LocalData").put("Money",money);
                item.put(name, true);
                gm.Sm.playSound(menuSoundProp);
            }
        }
    };

    public void LateUpdate() {

        Draw();
        if (gm.Im.isKeyDown(KeyEvent.VK_ESCAPE)) {
            FileManager fm = new FileManager();
            JSONObject database = fm.LoadJsonObject("DataBase");
            JSONObject UserData = (JSONObject)database.get(gm.GlobalData.get("LocalData").get("Player"));
            int money = ((Number)gm.GlobalData.get("LocalData").get("Money")).intValue();
            JSONObject item = (JSONObject)gm.GlobalData.get("LocalData").get("Item");
            UserData.put("Money",money);
            UserData.put("Item",item);
            fm.SaveString("DataBase", database.toJSONString(), true);
            gm.SetInstance(new MenuPage());
        }
    }

    private void Draw(){

        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        grpahics.setColor(Color.GREEN);
        FontMetrics fontmatrix = gm.Rm.SetFont("Regular");
        int height = 100;
		for (int i = 0; i < ItemDefine.StoreItem.length; i++) {
            if (SelectIndex == i) {
                grpahics.setColor(Color.GREEN);
            } else {
                grpahics.setColor(Color.LIGHT_GRAY);
            }
            BufferedImage curimg = img.get(imgString[Math.min(1, i)]);
            float width = height*curimg.getWidth()/(float)curimg.getHeight();
            int x = Math.round( width+ i*gm.frame.getWidth() / ItemDefine.StoreItem.length-width/2);
            grpahics.drawRect( x , gm.frame.getHeight() / 2, Math.round(width), height);
            grpahics.drawImage(curimg,x, gm.frame.getHeight() / 2, Math.round(width),height, null);

            String name = ItemDefine.StoreItem[i].name;
            grpahics.setColor(Color.GRAY);
            if((boolean)((JSONObject)(gm.GlobalData.get("LocalData").get("Item"))).get(name)){
                grpahics.setColor(Color.GREEN);
            }

            grpahics.drawString(name, x
                - fontmatrix.stringWidth(name) / 2 + Math.round(width)/2,gm.frame.getHeight()/2 + height+20);
             grpahics.setColor(Color.white);
            String price = "Price: $" + ItemDefine.StoreItem[i].value;
            grpahics.drawString(price, x
                - fontmatrix.stringWidth(price) / 2 + Math.round(width)/2,gm.frame.getHeight()/2 + height + 40);
        }

        // The title/guide for the item shop:
            grpahics.setColor(Color.GREEN);
            grpahics.drawString("Item Shop",gm.frame.getWidth()/2
                - fontmatrix.stringWidth("Item Shop") / 2 ,gm.frame.getHeight() / 10);
            grpahics.drawString("Buy Your Upgrades Here!",gm.frame.getWidth()/2
                - fontmatrix.stringWidth("Buy Your Upgrades Here!") / 2 ,gm.frame.getHeight() / 6);
            grpahics.drawString("Press Space to Buy",gm.frame.getWidth()/2
            - fontmatrix.stringWidth("Press Space to Buy") / 2 ,gm.frame.getHeight() / 4);
            grpahics.drawString("*If Player has the item already, Can't buy.*",gm.frame.getWidth()/2
                - fontmatrix.stringWidth("*If Player has the item already, Can't buy.*") / 2 ,gm.frame.getHeight() / 5);
            grpahics.setColor(Color.GRAY);
            grpahics.drawString("Press Esc to Go to Menu",gm.frame.getWidth()/2
                - fontmatrix.stringWidth("Press Esc to Go to Menu") / 2 ,gm.frame.getHeight()-fontmatrix.getHeight());
            JSONObject local = (JSONObject)gm.GlobalData.get("LocalData");
            int money = ((Number)local.get("Money")).intValue();
            grpahics.setColor(Color.white);
            grpahics.drawString("Current credits : "+money,gm.frame.getWidth()/2
                - fontmatrix.stringWidth("Current credits : "+money) / 2 ,gm.frame.getHeight()/3);
        }
}
