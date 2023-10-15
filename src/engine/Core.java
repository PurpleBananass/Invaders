package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import screen.AchievementScreen;
import screen.GameScreen;
import screen.HighScoreScreen;
import screen.ScoreScreen;
import screen.Screen;
import screen.TitleScreen;

/**
 * Implements core game logic.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public final class Core {

	/** Width of current screen. */
	private static final int WIDTH = 448;
	/** Height of current screen. */
	private static final int HEIGHT = 520;
	/** Max fps of current screen. */
	private static final int FPS = 60;

	/** Max lives. */
	private static final int MAX_LIVES = 3;
	/** Levels between extra life. */
	private static final int EXTRA_LIFE_FRECUENCY = 3;
	/** Total number of levels. */
	private static final int NUM_LEVELS = 7;

	/** Difficulty settings for level 1. */
	private static final GameSettings SETTINGS_LEVEL_1 =
			new GameSettings(5, 4, 60, 2000);
	/** Difficulty settings for level 2. */
	private static final GameSettings SETTINGS_LEVEL_2 =
			new GameSettings(5, 5, 50, 2500);
	/** Difficulty settings for level 3. */
	private static final GameSettings SETTINGS_LEVEL_3 =
			new GameSettings(6, 5, 40, 1500);
	/** Difficulty settings for level 4. */
	private static final GameSettings SETTINGS_LEVEL_4 =
			new GameSettings(6, 6, 30, 1500);
	/** Difficulty settings for level 5. */
	private static final GameSettings SETTINGS_LEVEL_5 =
			new GameSettings(7, 6, 20, 1000);
	/** Difficulty settings for level 6. */
	private static final GameSettings SETTINGS_LEVEL_6 =
			new GameSettings(7, 7, 10, 1000);
	/** Difficulty settings for level 7. */
	private static final GameSettings SETTINGS_LEVEL_7 =
			new GameSettings(8, 7, 2, 500);

	/** Frame to draw the screen on. */
	private static Frame frame;
	/** Screen currently shown. */
	private static Screen currentScreen;
	/** Difficulty settings list. */
	private static List<GameSettings> gameSettings;
	/** Application logger. */
	private static final Logger LOGGER = Logger.getLogger(Core.class
			.getSimpleName());
	/** Logger handler for printing to disk. */
	private static Handler fileHandler;
	/** Logger handler for printing to console. */
	private static ConsoleHandler consoleHandler;

	private static SoundManager mainBgm = new SoundManager("res/menu.wav");


	/**
	 * Test implementation.
	 *
	 * @param args
	 *            Program args, ignored.
	 */
	public static void main(final String[] args) {
		try {
			LOGGER.setUseParentHandlers(false);

			fileHandler = new FileHandler("log");
			fileHandler.setFormatter(new MinimalFormatter());

			consoleHandler = new ConsoleHandler();
			consoleHandler.setFormatter(new MinimalFormatter());

			LOGGER.addHandler(fileHandler);
			LOGGER.addHandler(consoleHandler);
			LOGGER.setLevel(Level.ALL);

		} catch (Exception e) {
			// TODO handle exception
			e.printStackTrace();
		}

		frame = new Frame(WIDTH, HEIGHT);
		DrawManager.getInstance().setFrame(frame);
		int width = frame.getWidth();
		int height = frame.getHeight();

		gameSettings = new ArrayList<GameSettings>();
		gameSettings.add(SETTINGS_LEVEL_1);
		gameSettings.add(SETTINGS_LEVEL_2);
		gameSettings.add(SETTINGS_LEVEL_3);
		gameSettings.add(SETTINGS_LEVEL_4);
		gameSettings.add(SETTINGS_LEVEL_5);
		gameSettings.add(SETTINGS_LEVEL_6);
		gameSettings.add(SETTINGS_LEVEL_7);

		GameState gameState;
	
		int returnCode = 1;
		do {
			// TODO 1P mode와 2P mode 진입 구현
			// TODO gameState 생성자에 따라 1P와 2P mode 구분
			// gameState = new GameState(1, 0, MAX_LIVES, 0, 0);
			gameState = new GameState(1, 0, MAX_LIVES, MAX_LIVES, 0, 0, 0);

			switch (returnCode) {
			case 1:
				// Main menu.
				mainBgm.loop();
				currentScreen = new TitleScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " title screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing title screen.");
				break;
			case 2:
				// Game & score.
				do {
					mainBgm.stop();

					// One extra live every few levels.
					int mode = gameState.getMode();
					boolean bonusLife = gameState.getLevel() % EXTRA_LIFE_FRECUENCY == 0;

					if (mode == 1) {
						// 1P mode
						bonusLife = bonusLife && gameState.getLivesRemaining1p() < MAX_LIVES;
					} else {
						// 2P mode (Give bonusLife if either player has less than max lives.)
						bonusLife = bonusLife &&
								(gameState.getLivesRemaining1p() < MAX_LIVES
										|| gameState.getLivesRemaining2p() < MAX_LIVES);
					}

					currentScreen = new GameScreen(gameState,
							gameSettings.get(gameState.getLevel() - 1),
							bonusLife, width, height, FPS);
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " title screen at " + FPS + " fps.");
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing Game screen.");

					if (mode == 1) {
						gameState = ((GameScreen) currentScreen).getGameState1p();
						gameState = new GameState(gameState.getLevel() + 1,
								gameState.getScore(),
								gameState.getLivesRemaining1p(),
								gameState.getBulletsShot1(),
								gameState.getShipsDestroyed());
					} else {
						gameState = ((GameScreen) currentScreen).getGameState2p();
						gameState = new GameState(gameState.getLevel() + 1,
								gameState.getScore(),
								gameState.getLivesRemaining1p(),
								gameState.getLivesRemaining2p(),
								gameState.getBulletsShot1(),
								gameState.getBulletsShot2(),
								gameState.getShipsDestroyed());
					}
          AchievementManager.getInstance().checkAchievements(gameState);
				} while ((gameState.getMode() == 1 && gameState.getLivesRemaining1p() > 0)
						|| (gameState.getMode() == 2 && gameState.getLivesRemaining1p() > 0 && gameState.getLivesRemaining2p() > 0)
						&& gameState.getLevel() <= NUM_LEVELS);

				if (gameState.getMode() == 1) {
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " score screen at " + FPS + " fps, with a score of "
						+ gameState.getScore() + ", "
						+ gameState.getLivesRemaining1p() + " lives remaining for 1p, "
						+ gameState.getBulletsShot1() + " bullets shot and "
						+ gameState.getShipsDestroyed() + " ships destroyed.");
				} else {
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " score screen at " + FPS + " fps, with a score of "
							+ gameState.getScore() + ", "
							+ gameState.getLivesRemaining1p() + " lives remaining for 1p, "
							+ gameState.getLivesRemaining2p() + " lives remaining for 2p, "
							+ gameState.getBulletsShot1() + " bullets shot by 1p and "
							+ gameState.getBulletsShot2() + " bullets shot by 2p and "
							+ gameState.getShipsDestroyed() + " ships destroyed.");
				}
				currentScreen = new ScoreScreen(width, height, FPS, gameState);
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing score screen.");
				break;
			case 3:
				//  Achievement.
				currentScreen = new AchievementScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " high score screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing Achievement screen.");
				break;
			// case 3:
			// 	// High scores.
			// 	currentScreen = new HighScoreScreen(width, height, FPS);
			// 	LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
			// 			+ " high score screen at " + FPS + " fps.");
			// 	returnCode = frame.setScreen(currentScreen);
			// 	LOGGER.info("Closing high score screen.");
			// 	break;
			}

		} while (returnCode != 0);
		fileHandler.flush();
		fileHandler.close();
		System.exit(0);
	}

	/**
	 * Constructor, not called.
	 */
	private Core() {

	}

	/**
	 * Controls access to the logger.
	 *
	 * @return Application logger.
	 */
	public static Logger getLogger() {
		return LOGGER;
	}

	/**
	 * Controls access to the drawing manager.
	 *
	 * @return Application draw manager.
	 */
	public static DrawManager getDrawManager() {
		return DrawManager.getInstance();
	}

	/**
	 * Controls access to the input manager.
	 *
	 * @return Application input manager.
	 */
	public static InputManager getInputManager() {
		return InputManager.getInstance();
	}

	/**
	 * Controls access to the file manager.
	 *
	 * @return Application file manager.
	 */
	public static FileManager getFileManager() {
		return FileManager.getInstance();
	}

	/**
	 * Controls creation of new cooldowns.
	 *
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @return A new cooldown.
	 */
	public static Cooldown getCooldown(final int milliseconds) {
		return new Cooldown(milliseconds);
	}

	/**
	 * Controls creation of new cooldowns with variance.
	 *
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @param variance
	 *            Variation in the cooldown duration.
	 * @return A new cooldown with variance.
	 */
	public static Cooldown getVariableCooldown(final int milliseconds,
											   final int variance) {
		return new Cooldown(milliseconds, variance);
	}
	/**
	 * Get Max Lives.
	 *
	 * @return MAX_LIVES.
	 */
	public static int getMaxLives() {
		return MAX_LIVES;
	}
}