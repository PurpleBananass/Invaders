package screen;
import engine.*;

import java.awt.event.KeyEvent;
import java.io.IOException;


public class SettingScreen extends Screen {
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

            /** Setting Sound */
            if((inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                    || inputManager.isKeyDown(KeyEvent.VK_D)) && itemCode<2 && selected && !keyChangeMode){
                switch (itemCode){
                    /** Entire Sound Setting */
                    case 0:
                        if(Core.soundVolume < 100){
                            Core.soundVolume ++;
                            SoundManager.setMasterVolume(Core.soundVolume);
                        }
                        break;
                    /** BGM On/Off */
                    case 1:
                        Core.bgmOn = !Core.bgmOn;
                        SoundManager.bgmSetting(Core.bgmOn);
                        this.selectionCooldown.reset();
                        break;
                    /** Keys Setting */
                    case 2: case 3:
                        if(keyNum>0) keyNum--;
                        this.selectionCooldown.reset();
                        break;
                    default:
                        break;
                }
            }
            if((inputManager.isKeyDown(KeyEvent.VK_LEFT)
                    || inputManager.isKeyDown(KeyEvent.VK_A)) && itemCode<2 && selected && !keyChangeMode){
                switch (itemCode){
                    /** Entire Sound Setting */
                    case 0:
                        if(Core.soundVolume >0){
                            Core.soundVolume --;
                            SoundManager.setMasterVolume(Core.soundVolume);
                        }
                        break;
                    /** BGM On/Off */
                    case 1:
                        Core.bgmOn = !Core.bgmOn;
                        SoundManager.bgmSetting(Core.bgmOn);
                        this.selectionCooldown.reset();
                        break;
                    default:
                        break;
                }
            }

            /** Move up and down when selecting key */
            if((inputManager.isKeyDown(KeyEvent.VK_UP)
                    || inputManager.isKeyDown(KeyEvent.VK_W)) && itemCode>1 && selected && !keyChangeMode){
                /** Keys Setting */
                if(keyNum>0) keyNum--;
                this.selectionCooldown.reset();
            }
            if((inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) && itemCode>1 && selected && !keyChangeMode){
                /** Keys Setting */
                if(keyNum<7) keyNum++;
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE) && !selected){
                SoundManager.playSound("SFX/S_MenuClick", "menu_select", false, false);
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
            if (itemCode == 2 && selected && !keyChangeMode && inputManager.isKeyDown(KeyEvent.VK_SPACE)){
                keyChangeMode =true;
                tempKeyCode = Core.getKeySettingCode(keyNum);
                tempKeyString = Core.getKeySettingString(keyNum);
                Core.setKeySettingString(keyNum, "");
                this.selectionCooldown.reset();
            }
            else if(itemCode == 3 && selected && !keyChangeMode && inputManager.isKeyDown(KeyEvent.VK_SPACE)){
                keyChangeMode =true;
                tempKeyCode = Core.getKeySettingCode(keyNum +8);
                tempKeyString = Core.getKeySettingString(keyNum +8);
                Core.setKeySettingString(keyNum +8, "");
                this.selectionCooldown.reset();
            }
            else if(itemCode == 2 && selected && keyChangeMode && inputManager.getcheck()){
                int temp = inputManager.getKeyCode();
                String tempS = inputManager.getKeyString();
                for(int i=0;i<16;i++){
                    if(Core.getKeySettingCode(i) == temp){
                        Core.setKeySettingCode(i,tempKeyCode);
                        Core.setKeySettingString(i,tempKeyString);
                    }
                }
                Core.setKeySettingString(keyNum, tempS);
                Core.setKeySettingCode(keyNum, temp);
                keyChangeMode = false;
                this.selectionCooldown.reset();
            }
            else if(itemCode == 3 && selected && keyChangeMode && inputManager.getcheck()){
                int temp = inputManager.getKeyCode();
                String tempS = inputManager.getKeyString();
                for(int i=0;i<16;i++){
                    if(Core.getKeySettingCode(i) == temp){
                        Core.setKeySettingCode(i, tempKeyCode);
                        Core.setKeySettingString(i, tempKeyString);
                    }
                }
                Core.setKeySettingString(keyNum +8, tempS);
                Core.setKeySettingCode(keyNum +8, temp);
                keyChangeMode = false;
                this.selectionCooldown.reset();
            }
            else if ((inputManager.isKeyDown(KeyEvent.VK_SPACE) ||  inputManager.isKeyDown(KeyEvent.VK_ESCAPE))
                    && selected && !keyChangeMode && itemCode<2) {
                keyNum = 0;
                selected = false;
                this.selectionCooldown.reset();
            }
            else if ((inputManager.isKeyDown(KeyEvent.VK_LEFT) || inputManager.isKeyDown(KeyEvent.VK_A)
                    || inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) && selected && !keyChangeMode && itemCode>1) {
                SoundManager.playSound("SFX/S_MenuClick", "menu_select", false, false);
                keyNum = 0;
                selected = false;
                this.selectionCooldown.reset();
            }
            else if (inputManager.isKeyDown(KeyEvent.VK_SPACE) && !selected && !keyChangeMode) {
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
            Core.setting.get(i+2).value = Core.getKeySettingCode(i);
            Core.setting.get(i+2).name = Core.getKeySettingString(i);
        }
        try {
            FileManager.saveSettings(Core.setting);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
