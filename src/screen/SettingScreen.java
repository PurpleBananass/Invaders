package screen;
import engine.*;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;


public class SettingScreen extends Screen {

    private List<Settings> settings1 = null;


    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;

    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;

    /** What is selected setting item? */
    private int itemCode =0;

    /** Check select  */
    private boolean selected =false;
    /** Check what key is selected */
    private int keyNum =0;
    /** Check when user enters keyChange */
    private boolean keyChangeMode = false;
    /** For the situation some keys are not set */
    private int tempKeyCode;
    private String tempKeyString;



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
                        if(Core.soundVolume < 100) Core.soundVolume ++;
                        break;
                    /** BGM On/Off */
                    case 1:
                        Core.bgmOn = !Core.bgmOn;
                        this.selectionCooldown.reset();
                        break;
                    /** Keys Setting */
                    case 2:
                        if(keyNum>0) keyNum--;
                        this.selectionCooldown.reset();
                        break;
                    case 3:
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
                        if(Core.soundVolume >0) Core.soundVolume --;
                        break;
                    /** BGM On/Off */
                    case 1:
                        Core.bgmOn = !Core.bgmOn;
                        this.selectionCooldown.reset();
                        break;
                    /** Keys Setting */
                    case 2:
                        if(keyNum<7) keyNum++;
                        this.selectionCooldown.reset();
                        break;
                    case 3:
                        if(keyNum<7) keyNum++;
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
                tempKeyCode = Core.keySetting[keyNum];
                tempKeyString = Core.keySettingString[keyNum];
                Core.keySettingString[keyNum] = "";
                this.selectionCooldown.reset();
            }
            else if(itemCode == 3 && selected && !keyChangeMode &&(inputManager.isKeyDown(KeyEvent.VK_RIGHT) || inputManager.isKeyDown(KeyEvent.VK_D))){
                keyChangeMode =true;
                tempKeyCode = Core.keySetting[keyNum];
                tempKeyString = Core.keySettingString[keyNum];
                Core.keySettingString[keyNum + 8] = "";
                this.selectionCooldown.reset();
            }
            else if(itemCode == 2 && selected && keyChangeMode && inputManager.getcheck()){
                int temp = inputManager.getKeyCode();
                String tempS = inputManager.getKeyString();
                for(int i=0;i<14;i++){
                    if(Core.keySetting[i] == temp){
                        Core.keySetting[i] = tempKeyCode;
                        Core.keySettingString[i] = tempKeyString;
                    }
                }
                Core.keySettingString[keyNum] = tempS;
                Core.keySetting[keyNum] = temp;
                keyChangeMode = false;
                this.selectionCooldown.reset();
            }
            else if(itemCode == 3 && selected && keyChangeMode && inputManager.getcheck()){
                int temp = inputManager.getKeyCode();
                String tempS = inputManager.getKeyString();
                for(int i=0;i<16;i++){
                    if(Core.keySetting[i] == temp){
                        Core.keySetting[i] = tempKeyCode;
                        Core.keySettingString[i] = tempKeyString;
                    }
                }
                Core.keySettingString[keyNum + 8] = tempS;
                Core.keySetting[keyNum + 8] = temp;
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
        drawManager.drawSettingDetail(this, itemCode, selected, Core.soundVolume, Core.bgmOn, keyNum);

        drawManager.completeDrawing(this);
    }

    private void saveSetting(){
        Core.setting.get(0).value = Core.soundVolume;
        Core.setting.get(1).value = Core.bgmOn ? 1:0;
        for (int i =0; i < 16; i++) {
            Core.setting.get(i+2).value = Core.keySetting[i];
            Core.setting.get(i+2).name = Core.keySettingString[i];
        }
        try {
            FileManager.saveSettings(Core.setting);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final int getCoresoundVolume(){return Core.soundVolume;}
    public final boolean isbgmon(){return Core.bgmOn;}

    public final int[] getkeySetting(){return Core.keySetting;}
    public static final String[] getkeySettingString(){return Core.keySettingString;}
}
