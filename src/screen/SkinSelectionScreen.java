package screen;

import engine.Core;
import java.awt.event.KeyEvent;

public class SkinSelectionScreen extends Screen{
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
                && this.inputDelay.checkFinished())
            this.isRunning = false;
    }

    public void draw(){
        drawManager.initDrawing(this);
        drawManager.drawSkinSelectionMenu(this);
        drawManager.completeDrawing(this);
    }
}
