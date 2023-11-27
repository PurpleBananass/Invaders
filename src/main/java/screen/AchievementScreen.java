package screen;

import java.awt.event.KeyEvent;
import java.util.Map;

import engine.AchievementManager;
import engine.AchievementManager.Achievement;
import engine.SoundManager;

public class AchievementScreen extends Screen {
	private Map<Achievement, Boolean> achievements;

	/**
	 * Constructor, establishes the properties of the screen.
	 * 
	 * @param width
	 *               Screen width.
	 * @param height
	 *               Screen height.
	 * @param fps
	 *               Frames per second, frame rate at which the game is run.
	 */
	public AchievementScreen(final int width, final int height, final int fps) {
		super(width, height, fps);

		this.returnCode = 1;
		this.achievements = AchievementManager.getInstance().getAchievements();
	}

	public final int run() {
		super.run();
		return this.returnCode;
	}

	protected final void update() {
		super.update();
		draw();
		if ((inputManager.isKeyDown(KeyEvent.VK_ESCAPE) || inputManager.isKeyDown(KeyEvent.VK_SPACE)) && this.inputDelay.checkFinished()){
			SoundManager.playSound("SFX/S_MenuClick", "menu_select", false, false);
			this.isRunning = false;
		}
	}

	private void draw() {
		drawManager.initDrawing(this);
		drawManager.drawAchievements(this, achievements);
		drawManager.completeDrawing(this);
	}
}
