package engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import engine.AchievementManager.Achievement;
import screen.*;

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
			new GameSettings(5, 4, 60, 2000, 5.5);
	/** Difficulty settings for level 2. */
	private static final GameSettings SETTINGS_LEVEL_2 =
			new GameSettings(5, 5, 50, 2500, 5.5);
	/** Difficulty settings for level 3. */
	private static final GameSettings SETTINGS_LEVEL_3 =
			new GameSettings(6, 5, 40, 1500, 5.5);
	/** Difficulty settings for level 4. */
	private static final GameSettings SETTINGS_LEVEL_4 =
			new GameSettings(6, 6, 30, 1500, 5);
	/** Difficulty settings for level 5. */
	private static final GameSettings SETTINGS_LEVEL_5 =
			new GameSettings(7, 6, 20, 1000,5);
	/** Difficulty settings for level 6. */
	private static final GameSettings SETTINGS_LEVEL_6 =
			new GameSettings(7, 7, 10, 1000,4.8);
	/** Difficulty settings for level 7. */
	private static final GameSettings SETTINGS_LEVEL_7 =
			new GameSettings(8, 7, 2, 500,4.8);

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

	private static int[] keySetting = new int[16];
	/** { 1P.LEFT, 1P.RIGHT, 1P.ATTACK, 1P.BURST 1, 1P.BURST 2, 1P.RELOAD, 1P.BOOSTER, 1P.ITEM,
	 * 2P.LEFT, 2P.RIGHT, 2P.ATTACK, 2P.BURST 1, 2P.BURST 2, 2P.RELOAD, 2P.BOOSTER, 2P.ITEM} */
	private static String[] keySettingString = new String[16];
	/** Sound Volume  */
	public static int soundVolume;
	/** Check BGM is On/Off  */
	public static boolean bgmOn;
	public static List<Settings> setting;
	/** control bottom HUD height */
	public static int bottomHudHeight = 40;
	/**
	 * Test implementation.
	 *
	 * @param args
	 *            Program args, ignored.
	 */
	public static void main(final String[] args) {
		try {
			setting = Core.getFileManager().loadSettings();
			soundVolume = setting.get(0).getValue();
			if(setting.get(1).getValue()==1){
				bgmOn = true;
				SoundManager.bgmSetting(true);
			}
			else{
				bgmOn = false;
				SoundManager.bgmSetting(false);
			}
			for (int i =2; i < 18; i++) {
				keySettingString[i-2] = setting.get(i).getName();
				keySetting[i-2] = setting.get(i).getValue();
			}

		} catch (NumberFormatException | IOException e) {
			LOGGER.info("Couldn't load Settings!");
		}
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

		frame = new Frame(WIDTH, HEIGHT, bottomHudHeight);
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

		AchievementManager.getInstance().markAchievementAsAchieved(Achievement.ADVENTURE_START);

		GameState gameState;

		int returnCode = 0;
		do {
			if(SelectScreen.gameMode == 1) gameState = new GameState(LevelSelectionScreen.levelCode, 0, MAX_LIVES, 0, 0);
			else gameState = new GameState(LevelSelectionScreen.levelCode, 0, MAX_LIVES, MAX_LIVES, 0, 0, 0, 0);

			switch (returnCode) {
                case 0:
                    currentScreen = new LoginScreen(width, height, FPS);LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
                        + " title screen at " + FPS + " fps.");
                    returnCode = frame.setScreen(currentScreen);
                    LOGGER.info("Closing title screen.");
                    break;
			case 1:
				// Main menu.
				SoundManager.resetBGM();
				SoundManager.stopSound("selection",2f);
				SoundManager.playSound("BGM/B_Main_a", "menu", true, true, 2f);
				currentScreen = new TitleScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " title screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing title screen.");
				break;
			case 2:
				// Select Mode.
				SoundManager.stopSound("menu",2f);
				SoundManager.playSound("BGM/B_Main_c", "selection", true, true);
				currentScreen = new SelectScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " select screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing select screen.");
				break;
			case 3:
				// High scores.
				SoundManager.stopSound("menu",1f);
				SoundManager.playSound("BGM/B_HighScore", "highscore", true, true);
				currentScreen = new HighScoreScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " high score screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing high score screen.");
				SoundManager.stopSound("highscore",2f);
				break;
			case 4:
				// Shop
				currentScreen = new ItemShopScreen(width,height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " item shop screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing item shop screen");
				break;
			case 5:
				// Setting.
				currentScreen = new SettingScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " setting screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing setting screen.");
				break;
			case 6:
				//  Achievement.
				SoundManager.stopSound("menu",1f);
				SoundManager.playSound("BGM/B_Achieve", "achievement", true, true);
				currentScreen = new AchievementScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " achievement screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing Achievement screen.");
				SoundManager.stopSound("achievement",2f);
				break;
			case 7:
				// Game & score.
				do {
					SoundManager.stopSound("selection",2f);
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

					SoundManager.resetBGM();
					SoundManager.playBGM(gameState.getLevel());

					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " title screen at " + FPS + " fps.");
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing Game screen.");

					if (returnCode == 1) break;

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
								gameState.getShipsDestroyed(),
								gameState.getShipsDestroyed2());
					}
					AchievementManager.getInstance().checkAchievements(gameState);
					if (((gameState.getMode() == 1 && gameState.getLivesRemaining1p() > 0)
							|| (gameState.getMode() == 2 && gameState.getLivesRemaining1p() > 0 && gameState.getLivesRemaining2p() > 0))
							&& gameState.getLevel() <= NUM_LEVELS) {
						currentScreen = new ClearScreen(width, height, FPS, gameState);
						LOGGER.info("Starting 	" + WIDTH + "x" + HEIGHT
								+ " clear screen at " + FPS + " fps.");
						returnCode = frame.setScreen(currentScreen);
						LOGGER.info("Closing clear screen.");
						if (returnCode == 1) break;
					}
				} while ((gameState.getMode() == 1 && gameState.getLivesRemaining1p() > 0)
						|| (gameState.getMode() == 2 && (gameState.getLivesRemaining1p() > 0 || gameState.getLivesRemaining2p() > 0))
						&& gameState.getLevel() <= NUM_LEVELS);

				if (returnCode == 1) break;

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
				SoundManager.resetBGM();
				SoundManager.stopSound("ship_moving");
				SoundManager.playSound("BGM/B_Gameover", "B_gameover", true, true, 2f);
				SoundManager.playSound("SFX/S_Gameover","S_gameover",false,false);
				returnCode = frame.setScreen(currentScreen);
				SoundManager.stopSound("B_gameover",2f);
				LOGGER.info("Closing score screen.");
				break;
			case 8:
				// Select Skin.
				currentScreen = new SkinSelectionScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " Skin Selection screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing SkinSelection screen.");
				break;
			case 9:
				//Select level.
				currentScreen = new LevelSelectionScreen(width, height, FPS, gameSettings.size());
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " Level Selection screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Selected level " + LevelSelectionScreen.levelCode + ".");
				LOGGER.info("Closing LevelSelection screen.");
				break;
			default:
				break;
			}

		} while (returnCode != 0);
		try {
			getFileManager().updateAccounts();
		}catch(Exception e){
			e.printStackTrace();
		}
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
	/**
	 * Get Key Setting Code
	 */
	public static int getKeySettingCode(int num){
		if(num<0 || num>16) throw new NullPointerException("it exceeds array");
		return keySetting[num];
	}
	/**
	 * Get Key Setting String
	 */
	public static String getKeySettingString(int num){
		if(num<0 || num>16) throw new NullPointerException("it exceeds array");
		return keySettingString[num];
	}
	/**
	 * Get Key Setting Code Array
	 */
	public static int[] getKeySettingCodeArray(){return keySetting;}
	/**
	 * Get Key Setting String Array
	 */
	public static String[] getKeySettingStringArray(){return keySettingString;}
	/**
	 * Set Key Setting Code
	 */
	public static void setKeySettingCode(int num, int value){
		if(num<0 || num>16) throw new IndexOutOfBoundsException("it exceeds array");
		keySetting[num] = value;
	}
	/**
	 * Set Key Setting String
	 */
	public static void setKeySettingString(int num, String value){
		if(num<0 || num>16) throw new IndexOutOfBoundsException("it exceeds array");
		keySettingString[num] = value;
	}
}