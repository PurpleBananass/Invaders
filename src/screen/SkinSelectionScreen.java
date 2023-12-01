package screen;

import engine.Cooldown;
import engine.Core;
import engine.SoundManager;

import engine.DrawManager;
import engine.FileManager;

import java.awt.event.KeyEvent;
import java.util.Map;

public class SkinSelectionScreen extends Screen{
    private static final int SELECTION_TIME = 150;
    private Cooldown selectionCooldown;
    private int skincode_1p=0;
    private int skincode_2p=0;
    private static int colorCode_1P = 0;
    private static int colorCode_2P = 0;
    private boolean isColorSelection = false;
    private FileManager fileManager;

    private static Map<DrawManager.SpriteType, boolean[][]> spriteMap;

    public SkinSelectionScreen(final int width, final int height, final int fps) {

        super(width, height, fps);

        this.returnCode = 7;
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();

    }
    public final int run() {
        super.run();
        return this.returnCode;
    }
    protected final void update() {
        super.update();
        draw();
        if (this.selectionCooldown.checkFinished() && this.inputDelay.checkFinished()) {
            if(inputManager.isKeyDown(KeyEvent.VK_ESCAPE)){this.returnCode = 2; isRunning = false;}
            if (inputManager.isKeyDown(KeyEvent.VK_UP) && skincode_1p> 0) {
                skincode_1p--;
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_DOWN) && skincode_1p < 5) {
                skincode_1p++;
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_W) && skincode_2p > 0) {
                skincode_2p--;
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_S) && skincode_2p < 5) {
                skincode_2p++;
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_LEFT) && colorCode_1P> 0) {
                colorCode_1P--;
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_RIGHT) && colorCode_1P< 5) {
                colorCode_1P++;
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_A) && colorCode_2P > 0) {
                colorCode_2P--;
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_D) && colorCode_2P < 5) {
                colorCode_2P++;
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                SoundManager.playSound("SFX/S_MenuClick", "menu_select", false, false);
                this.isRunning = false;
            }
        }
    }

    public void draw(){
        drawManager.initDrawing(this);
        drawManager.drawSkinSelectionMenu(this, skincode_1p,skincode_2p);
        drawManager.drawColorSelectionMenu(this, colorCode_1P,colorCode_2P);
        drawManager.completeDrawing(this);
    }

    public static int getColorCode_1P(){
        return colorCode_1P;
    }

    public static int getColorCode_2P(){
        return colorCode_2P;
    }
}
