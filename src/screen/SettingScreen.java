package screen;
import engine.*;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SettingScreen extends Screen {

    private List<Settings> settings1 = null;

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

    private int[] keySetting = new int[10];;
    private static String[] keySettingString = new String[10];
    private int keyNum =0;
    private boolean keyChangeMode = false;



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
            if(this.setting.get(1).getValue()==1){
                bgmOn = true;
            }
            else bgmOn = false;
            for (int i =2; i < 12; i++) {
                keySettingString[i-2] = this.setting.get(i).getName();
                keySetting[i-2] = this.setting.get(i).getValue();
            }

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
            /** Move up and down when not selected */
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

            /** Move up and down when selected */
            if((inputManager.isKeyDown(KeyEvent.VK_UP)
                    || inputManager.isKeyDown(KeyEvent.VK_W)) && selected && !keyChangeMode){
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
                    /** Keys Setting */
                    case 2, 3:
                        if(keyNum>0) keyNum--;
                        this.selectionCooldown.reset();
                        break;
                    default:
                        break;
                }
            }
            if((inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) && selected && !keyChangeMode){
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
                    /** Keys Setting */
                    case 2, 3:
                        if(keyNum<4) keyNum++;
                        this.selectionCooldown.reset();
                        break;
                    default:
                        break;
                }
            }

            if (inputManager.isKeyDown(KeyEvent.VK_SPACE) && !selected){
                saveSetting();
                this.isRunning = false;
            }
            /**
             * 1P Keys Setting Mode
             * 2P Keys Setting Mode
             * Receive 1P Keys Setting Input
             * Receive 2P Keys Setting Input
             * Selected
             * */
            if (itemCode == 2 && selected && !keyChangeMode &&(inputManager.isKeyDown(KeyEvent.VK_RIGHT) || inputManager.isKeyDown(KeyEvent.VK_D))) {
                keyChangeMode =true;
                keySettingString[keyNum] = "";
                this.selectionCooldown.reset();
            }
            else if(itemCode == 3 && selected && !keyChangeMode &&(inputManager.isKeyDown(KeyEvent.VK_RIGHT) || inputManager.isKeyDown(KeyEvent.VK_D))){
                keyChangeMode =true;
                keySettingString[keyNum +5] = "";
                this.selectionCooldown.reset();
            }
            else if(itemCode == 2 && selected && keyChangeMode && inputManager.getcheck()){
                int temp = inputManager.getKeyCode();
                String tempS = inputManager.getKeyString();
                for(int i=0;i<5;i++){
                    if(keySetting[i] == temp){
                        keySetting[i] = keySetting[keyNum];
                        keySettingString[i] = keySettingString[keyNum];
                    }
                }
                keySettingString[keyNum] = tempS;
                keySetting[keyNum] = temp;
                keyChangeMode = false;
                this.selectionCooldown.reset();
            }
            else if(itemCode == 3 && selected && keyChangeMode && inputManager.getcheck()){
                keySettingString[keyNum + 5] = inputManager.getKeyString();
                keySetting[keyNum + 5] = inputManager.getKeyCode();
                keyChangeMode = false;
                this.selectionCooldown.reset();
            }
            else if ((inputManager.isKeyDown(KeyEvent.VK_LEFT) || inputManager.isKeyDown(KeyEvent.VK_A)
                    || inputManager.isKeyDown(KeyEvent.VK_SPACE)) && selected && !keyChangeMode) {
                keyNum = 0;
                selected = false;
                this.selectionCooldown.reset();
            }
            else if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                    || inputManager.isKeyDown(KeyEvent.VK_D) && !selected && !keyChangeMode) {
                selected = true;
                this.selectionCooldown.reset();
            }
        }
    }

    /**
     * Shifts the focus to the next setting item.
     */
    private void nextSettingItem() {
        if (this.itemCode == 3)
            this.itemCode = 0;
        else
            this.itemCode++;
    }

    /**
     * Shifts the focus to the previous setting item.
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
        drawManager.drawSettingDetail(this, itemCode, selected, soundVolume, bgmOn, keyNum);

        drawManager.completeDrawing(this);
    }

    private void saveSetting(){
        this.setting.get(0).value = soundVolume;
        this.setting.get(1).value = bgmOn ? 1:0;
        for (int i =0; i < 10; i++) {
            this.setting.get(i+2).value = keySetting[i];
            this.setting.get(i+2).name = keySettingString[i];
        }
        try {
            FileManager.saveSettings(this.setting);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final int getSoundVolume(){return soundVolume;}
    public final boolean isBgmOn(){return bgmOn;}

    public final int[] getKeySetting(){return keySetting;}
    public static final String[] getKeySettingString(){return keySettingString;}
}
