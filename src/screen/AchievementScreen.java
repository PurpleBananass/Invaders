package screen;
import java.awt.event.KeyEvent;


public class AchievementScreen extends Screen {
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
	public AchievementScreen(final int width, final int height, final int fps) {
		super(width, height, fps);

        this.returnCode = 1;
	}
    public final int run() {
        super.run();
        return this.returnCode;
    }

	protected final void update() {
		super.update();

		draw();
		if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)
				&& this.inputDelay.checkFinished())
			this.isRunning = false;
	}


	private void draw() {
		drawManager.initDrawing(this);
        /**
         * Draws the elements associated with the screen.
         */
		drawManager.completeDrawing(this);
	}


}
