package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import engine.Core;
import engine.Score;
import engine.SoundManager;

/**
 * Implements the high scores screen, it shows player records.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class HighScoreScreen extends Screen {

	/** List of past high scores from 1p mode. */
	private List<Score> highScores_1p;
	/** List of past high scores from 2p mode. */
	private List<Score> highScores_2p;

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
	public HighScoreScreen(final int width, final int height, final int fps) {
		super(width, height, fps);

		this.returnCode = 1;

		try {
			this.highScores_1p = Core.getFileManager().loadHighScores(1);
			this.highScores_2p = Core.getFileManager().loadHighScores(2);
		} catch (NumberFormatException | IOException e) {
			logger.warning("Couldn't load high scores!");
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
		if ((inputManager.isKeyDown(KeyEvent.VK_SPACE) || inputManager.isKeyDown(KeyEvent.VK_ESCAPE))
				&& this.inputDelay.checkFinished()) {
			SoundManager.playSound("SFX/S_MenuClick", "menu_select", false, false);
			this.isRunning = false;
		}
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);
		drawManager.drawHighScoreMenu(this);
		drawManager.drawHighScores_1p(this, this.highScores_1p);
		drawManager.drawHighScores_2p(this, this.highScores_2p);

		drawManager.completeDrawing(this);
	}
}
