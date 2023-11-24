package screen;

import engine.Cooldown;
import engine.Core;
import engine.SoundManager;

import java.awt.event.KeyEvent;

public class SelectScreen extends Screen{

    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;

    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;
    /** Check 1P, 2P Mode */
    public static int gameMode = 1;
    /** Check Skill Mode */
    public static boolean skillModeOn = true;
    /** Separate function when space is down*/
    private boolean canEscape = false;

    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param width  Screen width.
     * @param height Screen height.
     * @param fps    Frames per second, frame rate at which the game is run.
     */
    public SelectScreen(int width, int height, int fps){
        super(width, height, fps);
        this.returnCode = 9;
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
            if(!canEscape){
                if(inputManager.isKeyDown(KeyEvent.VK_ESCAPE)){
                    this.returnCode = 1;
                    this.isRunning = false;
                }
                if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                        || inputManager.isKeyDown(KeyEvent.VK_D)|| inputManager.isKeyDown(KeyEvent.VK_LEFT)|| inputManager.isKeyDown(KeyEvent.VK_A)) {
                    if(gameMode == 1) gameMode = 2;
                    else gameMode= 1;
                    this.selectionCooldown.reset();}
                if (inputManager.isKeyDown(KeyEvent.VK_SPACE)){
                    SoundManager.playSound("SFX/S_MenuClick", "menu_select", false, false);
                    canEscape = true;
                    this.selectionCooldown.reset();


                }
            }
            else{
                if(inputManager.isKeyDown(KeyEvent.VK_ESCAPE)){
                    canEscape = false;
                    this.selectionCooldown.reset();
                }
                if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                        || inputManager.isKeyDown(KeyEvent.VK_D)|| inputManager.isKeyDown(KeyEvent.VK_LEFT)|| inputManager.isKeyDown(KeyEvent.VK_A)) {
                    skillModeOn = !skillModeOn;
                    this.selectionCooldown.reset();
                }
                if (inputManager.isKeyDown(KeyEvent.VK_SPACE)){
                    this.isRunning = false;
                    SoundManager.playSound("SFX/S_MenuClick", "menu_select", false, false);
                }

            }

        }
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawSelect2PModeAndSkillModeScreen(this,gameMode,skillModeOn, canEscape);

        drawManager.completeDrawing(this);
    }

}
