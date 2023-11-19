package screen;

import engine.Cooldown;
import engine.Core;
import engine.SoundManager;

import java.awt.event.KeyEvent;

public class LevelSelectionScreen extends Screen{

    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;
    /** Time between changes in user selection. */
    final private Cooldown selectionCooldown;
    /** number of level. */
    private int numberLevel;

    public static int levelCode = 1;

    public LevelSelectionScreen(final int width, final int height, final int fps, int numberLevel) {
        super(width, height, fps);

        // Defaults to play.
        this.returnCode = 7;
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.numberLevel = numberLevel;
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
        if (this.selectionCooldown.checkFinished() && this.inputDelay.checkFinished()) {
            if ((inputManager.isKeyDown(KeyEvent.VK_UP) || inputManager.isKeyDown(KeyEvent.VK_W)) && (levelCode > 1)) {
                levelCode--;
                this.selectionCooldown.reset();
            }
            else if ((inputManager.isKeyDown(KeyEvent.VK_DOWN) || inputManager.isKeyDown(KeyEvent.VK_S)) && (levelCode < numberLevel)) {
                levelCode++;
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)){
                SoundManager.playSound("SFX/S_MenuClick", "menu_select", false, false);
                this.isRunning = false;
            }
        }
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);
        drawManager.drawLevelSelectionMenu(this, this.levelCode, this.numberLevel);
        drawManager.completeDrawing(this);
    }
}
