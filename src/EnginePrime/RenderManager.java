package EnginePrime;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;


public final class RenderManager {

    static boolean FrontBuffer;
    static BufferedImage backBuffer[];
    private static RenderManager instance = null;


    private RenderManager(){};

    public static RenderManager getInstance() {
        if (instance == null) {
            instance = new RenderManager();
        }
        return instance;
    }

    public static void Fill(){
        Fill(Color.BLACK);
    }

    public static BufferedImage GetCurrentBuffer(){
        return backBuffer[((FrontBuffer) ? 1 : 0)];
    };

    public static Graphics GetCurrentGraphic(){
        return GetCurrentBuffer().getGraphics();
    };

    public void Render(Frame f){

        f.getGraphics().drawImage(GetCurrentBuffer(),0,0, f);
        FrontBuffer = !FrontBuffer;
    }

    public static void Fill(Color col){

        BufferedImage buffer = GetCurrentBuffer();
        Graphics graphic = buffer.getGraphics();

        graphic.setColor(col);
        graphic.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
    }

    public void Resize(final int width, final int height){
        backBuffer = new BufferedImage[]{new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB), new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)};
    }
}