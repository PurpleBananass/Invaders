package screen;

import engine.Cooldown;
import engine.Core;
import engine.Player;

import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Implements the title screen.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */

public class LoginScreen extends Screen {

	/** Milliseconds between changes in user selection. */
	private static final int SELECTION_TIME = 200;
	private char[] name;
	/** Character of players name selected for change. */
	private int nameCharSelected;
	/** Time between changes in user selection. */

	private Player player;

	private Cooldown selectionCooldown;
	/** Code of first mayus character. */
	private static final int FIRST_CHAR = 65;
	/** Code of last mayus character. */
	private static final int LAST_CHAR = 90;


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
	public LoginScreen(final int width, final int height, final int fps) {
		super(width, height, fps);

		// Defaults to play.
		this.returnCode = 2;
		this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
		this.selectionCooldown.reset();
		this.name = "AAA".toCharArray();
		this.nameCharSelected = 0;
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
				if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
					this.nameCharSelected = this.nameCharSelected == 2 ? 0
							: this.nameCharSelected + 1;
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
					this.nameCharSelected = this.nameCharSelected == 0 ? 2
							: this.nameCharSelected - 1;
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_UP)) {
					this.name[this.nameCharSelected] =
							(char) (this.name[this.nameCharSelected]
									== LAST_CHAR ? FIRST_CHAR
									: this.name[this.nameCharSelected] + 1);
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_DOWN)) {
					this.name[this.nameCharSelected] =
							(char) (this.name[this.nameCharSelected]
									== FIRST_CHAR ? LAST_CHAR
									: this.name[this.nameCharSelected] - 1);
					this.selectionCooldown.reset();
				}
				if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
					try {
						Player loadedPlayer = Core.getFileManager().loadPlayer(name);
						if(loadedPlayer == null){
							Core.getFileManager().saveNewPlayer(name);
							logger.info("New player saved successfully");
						} else {
							Core.getFileManager().updateLoginTimeOfCurrentPlayer();
							logger.info("Player loaded successfully");
						}
					} catch (IOException e) {
						logger.warning("Couldn't load or save player! Error: " + e.getMessage());
					}

					// Proceed to main menu.
					this.returnCode = 1;
					this.isRunning = false;
			}
		}

	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);
		drawManager.drawUsernameInput(this, this.name, this.nameCharSelected);

		drawManager.completeDrawing(this);
	}
}
