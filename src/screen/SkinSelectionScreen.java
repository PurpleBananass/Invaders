package screen;

import engine.Cooldown;
import engine.Core;
import java.awt.event.KeyEvent;

public class SkinSelectionScreen extends Screen{
    private static final int SELECTION_TIME = 200;
    private Cooldown selectionCooldown;
    private int skincode_1p=0;
    public SkinSelectionScreen(final int width, final int height, final int fps) {

        super(width, height, fps);

        this.returnCode = 1;

    }
    public final int run() {
        super.run();
        return this.returnCode;
    }
    protected final void update() {
        super.update();
        draw();

            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)
                    && this.inputDelay.checkFinished()) {
                this.isRunning = false;
            }
        }

    public void draw(){
        drawManager.initDrawing(this);
        drawManager.drawSkinSelectionMenu(this, skincode_1p);
        drawManager.completeDrawing(this);
    }
}
