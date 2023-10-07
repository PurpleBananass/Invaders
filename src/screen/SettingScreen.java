package screen;
import engine.Settings;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import engine.Cooldown;
import engine.Core;
import engine.GameState;
import engine.Score;

public class SettingScreen extends Screen {

    /** List of Settings. */
    private List<Settings> setting;
    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;

    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;

    /** What is selected setting item? */
    private int itemCode =0;

    /** Check select  */
    private boolean selected =false;

    /** Sound Volume  */
    private int soundVolume;

    /** Check BGM is On/Off  */
    private boolean bgmOn;

    /** Frame Size*/
    private int frameSize ;



    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param width
     *            Screen width.
     * @param height
     *            Screen height.
     * @param fps
     *            Frames per second, frame rate at which the game is run.
     */
    public SettingScreen(final int width, final int height, final int fps) {
        super(width, height, fps);
        try {
            this.setting = Core.getFileManager().loadSettings();

            soundVolume = this.setting.get(0).getValue();

            if(this.setting.get(1).getValue() == 1) bgmOn = true;
            else bgmOn =false;
            frameSize = this.setting.get(2).getValue();



        } catch (NumberFormatException | IOException e) {
            logger.warning("Couldn't load Settings!");
        }

        this.returnCode = 1;
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
            if ((inputManager.isKeyDown(KeyEvent.VK_UP)
                    || inputManager.isKeyDown(KeyEvent.VK_W)) && !selected) {
                previousSettingItem();
                this.selectionCooldown.reset();
            }
            if ((inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) && !selected) {
                nextSettingItem();
                this.selectionCooldown.reset();
            }

            if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                    || inputManager.isKeyDown(KeyEvent.VK_D) && !selected) {
                selected = true;
                this.selectionCooldown.reset();
            }
            if((inputManager.isKeyDown(KeyEvent.VK_UP)
                    || inputManager.isKeyDown(KeyEvent.VK_W)) && selected){
                switch (itemCode){
                    /** Entire Sound Setting */
                    case 0:
                        if(soundVolume < 100) soundVolume ++;
                        break;
                    /** BGM On/Off */
                    case 1:
                        bgmOn = !bgmOn;
                        this.selectionCooldown.reset();
                        break;
                    /** Frame Size Setting */
                    case 2:
                        if(frameSize<3) frameSize++;
                        this.selectionCooldown.reset();
                        break;
                    /** Keys Setting */
                    case 3:
                        break;
                    default:
                        break;
                }
            }

            if((inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) && selected){
                switch (itemCode){
                    /** Entire Sound Setting */
                    case 0:
                        if(soundVolume >0) soundVolume --;
                        break;
                    /** BGM On/Off */
                    case 1:
                        bgmOn = !bgmOn;
                        this.selectionCooldown.reset();
                        break;
                    /** Frame Size Setting */
                    case 2:
                        if(frameSize>1) frameSize--;
                        this.selectionCooldown.reset();
                        break;
                    /** Keys Setting */
                    case 3:
                        break;
                    default:
                        break;
                }
            }



            if (inputManager.isKeyDown(KeyEvent.VK_SPACE) && !selected){
                this.setting.get(0).value = soundVolume;
                this.setting.get(1).value = bgmOn ? 1:0;
                this.setting.get(2).value = frameSize;
                try {
                    Core.getFileManager().saveSettings(this.setting);
                } catch (IOException e) {
                    logger.warning("Couldn't save settings!");
                }
                this.isRunning = false;}
            if (inputManager.isKeyDown(KeyEvent.VK_LEFT) || inputManager.isKeyDown(KeyEvent.VK_A)
                    || inputManager.isKeyDown(KeyEvent.VK_SPACE) && selected) {
                selected = false;
                this.selectionCooldown.reset();
            }
        }
    }

    /**
     * Shifts the focus to the next menu item.
     */
    private void nextSettingItem() {
        if (this.itemCode == 3)
            this.itemCode = 0;
        else
            this.itemCode++;
    }

    /**
     * Shifts the focus to the previous menu item.
     */
    private void previousSettingItem() {
        if (this.itemCode == 0)
            this.itemCode = 3;
        else
            this.itemCode--;
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);
        drawManager.drawSetting(this, itemCode, selected);
        drawManager.drawSettingDetail(this, itemCode, selected, soundVolume, bgmOn, frameSize);
        drawManager.completeDrawing(this);
    }

    public final int getSoundVolume(){return soundVolume;}
    public final boolean isBgmOn(){return bgmOn;}
}
