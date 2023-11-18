package GamePrime.ETC;
import java.awt.image.BufferedImage;
import EnginePrime.GameManager;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;

public class Image {
    
    BufferedImage img;
    GameManager gm = GameManager.getInstance();    

    public Image(BufferedImage img){
        this.img = img;
    }

    public float GetWidthFixHeight(float height){
        return height * img.getWidth() / (float) img.getHeight();
    }

    public float GetHeightFixWidth(float width){
        return width * img.getHeight() / (float) img.getWidth();
    }

    public void RenderFixedWH(int x ,int y,int w ,int h){
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        Graphics2D graphics2D = (Graphics2D) grpahics;
        graphics2D.drawImage(img,x,y,w,h, null);
        
    }

    public void RenderFixedHeight(int x , int y , int height){

        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        Graphics2D graphics2D = (Graphics2D) grpahics;
        int width = Math.round(GetWidthFixHeight(height));

        graphics2D.drawImage(img,x-width/2,y-height/2,width,height, null);
        // graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
        // 1.0f));
        // graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
        // 0.2f));

    }

    public void RenderFixedHeight(int x, int y, int height,float alpha) {
        Graphics grpahics = gm.Rm.GetCurrentGraphic();
        Graphics2D graphics2D = (Graphics2D) grpahics;
        int width = Math.round(GetWidthFixHeight(height));
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        graphics2D.drawImage(img, x - width / 2, y - height / 2, width, height, null);
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
    }
}
