package screen;

import engine.*;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

/**
 * Implements the high scores screen, it shows player records.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 */


public class ItemShopScreen extends Screen {

    private static final int SELECTION_TIME = 300;

    public ItemShopScreen(final int width, final int height, final int fps) {
        super(width, height, fps);
        //defaults to center
        this.returnCode = 71;
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
    private Cooldown selectionCooldown;

    protected final void update() {
        super.update();

        draw();
        if (this.selectionCooldown.checkFinished() && this.inputDelay.checkFinished()) {
            if (inputManager.isKeyDown(KeyEvent.VK_LEFT) || inputManager.isKeyDown(KeyEvent.VK_A)) {
                previousMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_RIGHT) || inputManager.isKeyDown(KeyEvent.VK_D)) {
                nextMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                // Determine the selected item
                int selectedItem = this.returnCode;
                int itemPrice = 0;

                // Update currency and item based on the selected item
                if (selectedItem == 70) {
                    itemPrice = 10;
                    try {
                        if (Core.getFileManager().getCurrentPlayer().getItem().get(0) == true) {
                            logger.info("Player already has the item");
                            SoundManager.playSound("SFX/S_MenuClick", "menu_select", false, false);
                        } else if (Core.getFileManager().getCurrentPlayer().getCurrency() >= itemPrice) {
                            try {
                                Core.getFileManager().updatePlayerItem(0);
                                Core.getFileManager().updateCurrencyOfCurrentPlayer(-itemPrice);
                                SoundManager.playSound("SFX/S_Achievement", "S_achievement", false, false);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            logger.info("Player bought Speed item successfully");
                        } else {
                            logger.info("Player has Insufficient Balance");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else if (selectedItem == 71) {
                    itemPrice = 15;
                    try {
                        if (Core.getFileManager().getCurrentPlayer().getItem().get(1) == true) {
                            logger.info("Player already has the item");
                            SoundManager.playSound("SFX/S_MenuClick", "menu_select", false, false);
                        } else if (Core.getFileManager().getCurrentPlayer().getCurrency() >= itemPrice) {
                            try {
                                Core.getFileManager().updatePlayerItem(1);
                                Core.getFileManager().updateCurrencyOfCurrentPlayer(-itemPrice);
                                SoundManager.playSound("SFX/S_Achievement", "S_achievement", false, false);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            logger.info("Player bought Additional Health item successfully");
                        } else {
                            logger.info("Player has Insufficient Balance");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                } else if (selectedItem == 72) {
                    itemPrice = 20;
                    try {
                        if (Core.getFileManager().getCurrentPlayer().getItem().get(2) == true) {
                            logger.info("Player already has the item");
                            SoundManager.playSound("SFX/S_MenuClick", "menu_select", false, false);
                        } else if (Core.getFileManager().getCurrentPlayer().getCurrency() >= itemPrice) {
                            try {
                                Core.getFileManager().updatePlayerItem(2);
                                Core.getFileManager().updateCurrencyOfCurrentPlayer(-itemPrice);
                                SoundManager.playSound("SFX/S_Achievement", "S_achievement", false, false);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            logger.info("Player bought Shooting Faster item successfully");
                        } else {
                            logger.info("Player has Insufficient Balance");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
                this.selectionCooldown.reset();

            }
            if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
                this.returnCode = 1;
                this.isRunning = false;
            }
        }
    }


    /**
     * Shifts the focus to the next menu item (horizontally).
     */
    private void nextMenuItem() {
        if (this.returnCode == 72)
            this.returnCode = 70;
        else if (this.returnCode == 70)
            this.returnCode = 71;
        else if (this.returnCode == 71)
            this.returnCode = 72;
    }

    /**
     * Shifts the focus to the previous menu item (horizontally).
     */
    private void previousMenuItem() {
        if (this.returnCode == 70)
            this.returnCode = 72;
        else if (this.returnCode == 72)
            this.returnCode = 71;
        else if (this.returnCode == 71)
            this.returnCode = 70;
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);
        drawManager.drawItemShopMenu(this, this.returnCode);

        drawManager.completeDrawing(this);
    }
}