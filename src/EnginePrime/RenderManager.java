package EnginePrime;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;

public final class RenderManager implements GManager{
    public void PreRender(){};
    
    public void LateRender(){};
    static boolean FrontBuffer;
    static BufferedImage backBuffer[];
    static Graphics graphics[];
    private static RenderManager instance = null;
    public static Map<String, Font> fontList = new HashMap<>();
    private RenderManager(){};

    public static RenderManager getInstance() {
        if (instance == null) {
            instance = new RenderManager();
        }
        return instance;
    }

    public void Initialize(){};
    public void PreUpdate(){};
    public void LateUpdate(){};


    public static Font LoadFont(String path){
        try {
            FileInputStream inputStream = new FileInputStream( "res"+File.separator+path);
            Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            return font;
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static FontMetrics SetFont(String name) {
        Font font = fontList.get(name);
        Graphics g = GetCurrentGraphic();
        g.setFont(font);
        return g.getFontMetrics(font);
    }

    public static Font CreateFont(Font font , String name, float size) {
        Font newfont = font.deriveFont(size);
        fontList.put(name,newfont);
        return newfont;
    }

    public static void Fill(){
        Fill(Color.BLACK);
    }

    public static BufferedImage GetCurrentBuffer(){
        return backBuffer[((FrontBuffer) ? 1 : 0)];
    };

    public static Graphics GetCurrentGraphic(){
        return graphics[((FrontBuffer) ? 1 : 0)];
    };
    public void Exit(){};
    public void Render(Frame f){

        f.getGraphics().drawImage(GetCurrentBuffer(),0,0, f);
        FrontBuffer = !FrontBuffer;
        RenderManager.Fill();
    }

    public static void Fill(Color col){

        BufferedImage buffer = GetCurrentBuffer();
        Graphics graphic = buffer.getGraphics();

        graphic.setColor(col);
        graphic.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
    }

    public void Resize(final int width, final int height){
        backBuffer = new BufferedImage[]{new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB), new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)};
        graphics = new Graphics[]{backBuffer[0].getGraphics(),backBuffer[1].getGraphics()};
    }
}