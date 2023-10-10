package screen;

import engine.Cooldown;
import engine.Core;

import java.awt.event.KeyEvent;

public class Select2PScreen extends Screen{

    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;

    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;

    public static boolean twoPlayerModeOn = false;

    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param width  Screen width.
     * @param height Screen height.
     * @param fps    Frames per second, frame rate at which the game is run.
     */
    public Select2PScreen(int width, int height, int fps){
        super(width, height, fps);
        this.returnCode = 7;
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();
    }

    /**
     * Starts the action.
     *
     * @return Next screen code.
     */
    public final int run() {
        super.run();

        return this.returnCode;
    }

    /**
     * Updates the elements on screen and checks for events.
     */
    protected final void update() {
        super.update();

        draw();
        if (this.selectionCooldown.checkFinished()
                && this.inputDelay.checkFinished()) {
            if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                    || inputManager.isKeyDown(KeyEvent.VK_D)) {
                if(twoPlayerModeOn) twoPlayerModeOn=false;
                else twoPlayerModeOn=true;
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_LEFT)
                    || inputManager.isKeyDown(KeyEvent.VK_A)) {
                if(twoPlayerModeOn) twoPlayerModeOn=false;
                else twoPlayerModeOn=true;
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE))
                this.isRunning = false;
        }
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawSelect2PModeScreen(this,twoPlayerModeOn);

        drawManager.completeDrawing(this);
    }

}
