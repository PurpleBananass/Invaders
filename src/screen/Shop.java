package screen;

import java.awt.event.KeyEvent;

import java.io.IOException;
import java.util.List;

import engine.Cooldown;
import engine.Core;
import engine.Score;
import engine.Shopitem;
import engine.FileManager;
/**
 * Implements the Shop screen, it shows items can buy.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Shop extends Screen {

	/** List of past high scores. */
	private List<Shopitem> items;
	/** Time between changes in user selection. */
	private Cooldown selectionCooldown;
	/** Milliseconds between changes in user selection. */
	private static final int SELECTION_TIME = 200;
	private int option = 0;
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
	public Shop(final int width, final int height, final int fps) {
		super(width, height, fps);

		this.returnCode = 1;
		this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
		this.selectionCooldown.reset();

		try {
			this.items = Core.getFileManager().loadShop();
		} catch (NumberFormatException | IOException e) {
			logger.warning("Couldn't load Shop items!");
		}
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
			if (inputManager.isKeyDown(KeyEvent.VK_UP)
					|| inputManager.isKeyDown(KeyEvent.VK_W)) {
				previousItem();
				this.selectionCooldown.reset();
			}
			if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
					|| inputManager.isKeyDown(KeyEvent.VK_S)) {
				nextItem();
				this.selectionCooldown.reset();
			}
			if (inputManager.isKeyDown(KeyEvent.VK_ENTER)) {
				buyItem();
				this.selectionCooldown.reset();
			}
		}
		if (inputManager.isKeyDown(KeyEvent.VK_SPACE)
				&& this.inputDelay.checkFinished())
			this.isRunning = false;
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawShopMenu(this);
		drawManager.drawItems(this, this.items,option);

		drawManager.completeDrawing(this);
	}
	private void nextItem() {
		if (option == 3)
			option = 0;
		else
			option++;
	}
	private void previousItem() {
		if (option == 0)
			option = 3;
		else
			option--;
	}
	private void buyItem() {
		try {
			FileManager.buyItemLead(option);
		}
		catch(IOException e) {
			logger.info("Fail to buy item");
		}
	}
	
}
