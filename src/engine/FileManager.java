package engine;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import engine.DrawManager.SpriteType;

/**
 * Manages files used in the application.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public final class FileManager {

	/** Singleton instance of the class. */
	private static FileManager instance;
	/** Application logger. */
	private static Logger logger;
	/** Max number of high scores. */
	private static final int MAX_SCORES = 7;

	/**
	 * private constructor.
	 */
	private FileManager() {
		logger = Core.getLogger();
	}

	/**
	 * Returns shared instance of FileManager.
	 * 
	 * @return Shared instance of FileManager.
	 */
	protected static FileManager getInstance() {
		if (instance == null)
			instance = new FileManager();
		return instance;
	}

	/**
	 * Loads sprites from disk.
	 * 
	 * @param spriteMap
	 *            Mapping of sprite type and empty boolean matrix that will
	 *            contain the image.
	 * @throws IOException
	 *             In case of loading problems.
	 */
	public void loadSprite(final Map<SpriteType, boolean[][]> spriteMap)
			throws IOException {
		InputStream inputStream = null;

		try {
			inputStream = DrawManager.class.getClassLoader()
					.getResourceAsStream("graphics");
			char c;

			// Sprite loading.
			for (Map.Entry<SpriteType, boolean[][]> sprite : spriteMap
					.entrySet()) {
				for (int i = 0; i < sprite.getValue().length; i++)
					for (int j = 0; j < sprite.getValue()[i].length; j++) {
						do
							c = (char) inputStream.read();
						while (c != '0' && c != '1');

						if (c == '1')
							sprite.getValue()[i][j] = true;
						else
							sprite.getValue()[i][j] = false;
					}
				logger.fine("Sprite " + sprite.getKey() + " loaded.");
			}
			if (inputStream != null)
				inputStream.close();
		} finally {
			if (inputStream != null)
				inputStream.close();
		}
	}

	/**
	 * Loads a font of a given size.
	 * 
	 * @param size
	 *            Point size of the font.
	 * @return New font.
	 * @throws IOException
	 *             In case of loading problems.
	 * @throws FontFormatException
	 *             In case of incorrect font format.
	 */
	public Font loadFont(final float size) throws IOException,
			FontFormatException {
		InputStream inputStream = null;
		Font font;

		try {
			// Font loading.
			inputStream = FileManager.class.getClassLoader()
					.getResourceAsStream("font.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(
					size);
		} finally {
			if (inputStream != null)
				inputStream.close();
		}

		return font;
	}

	/**
	 * Returns the application default scores if there is no user high scores
	 * file.
	 * 
	 * @return Default high scores.
	 * @throws IOException
	 *             In case of loading problems.
	 */
	private List<Score> loadDefaultHighScores() throws IOException {
		List<Score> highScores = new ArrayList<Score>();
		InputStream inputStream = null;
		BufferedReader reader = null;

		try {
			inputStream = FileManager.class.getClassLoader()
					.getResourceAsStream("scores");
			reader = new BufferedReader(new InputStreamReader(inputStream));

			Score highScore = null;
			String name = reader.readLine();
			String score = reader.readLine();

			while ((name != null) && (score != null)) {
				highScore = new Score(name, Integer.parseInt(score));
				highScores.add(highScore);
				name = reader.readLine();
				score = reader.readLine();
			}
		} finally {
			if (inputStream != null)
				inputStream.close();
		}

		return highScores;
	}

	/**
	 * Loads high scores from file, and returns a sorted list of pairs score -
	 * value.
	 * 
	 * @return Sorted list of scores - players.
	 * @throws IOException
	 *             In case of loading problems.
	 */
	public List<Score> loadHighScores() throws IOException {

		List<Score> highScores = new ArrayList<Score>();
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String scoresPath = new File(jarPath).getParent();
			scoresPath += File.separator;
			scoresPath += "scores";

			File scoresFile = new File(scoresPath);
			inputStream = new FileInputStream(scoresFile);
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream, Charset.forName("UTF-8")));

			logger.info("Loading user high scores.");

			Score highScore = null;
			String name = bufferedReader.readLine();
			String score = bufferedReader.readLine();

			while ((name != null) && (score != null)) {
				highScore = new Score(name, Integer.parseInt(score));
				highScores.add(highScore);
				name = bufferedReader.readLine();
				score = bufferedReader.readLine();
			}

		} catch (FileNotFoundException e) {
			// loads default if there's no user scores.
			logger.info("Loading default high scores.");
			highScores = loadDefaultHighScores();
		} finally {
			if (bufferedReader != null)
				bufferedReader.close();
		}

		Collections.sort(highScores);
		return highScores;
	}

	/**
	 * Saves user high scores to disk.
	 * 
	 * @param highScores
	 *            High scores to save.
	 * @throws IOException
	 *             In case of loading problems.
	 */
	public void saveHighScores(final List<Score> highScores) 
			throws IOException {
		OutputStream outputStream = null;
		BufferedWriter bufferedWriter = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String scoresPath = new File(jarPath).getParent();
			scoresPath += File.separator;
			scoresPath += "scores";

			File scoresFile = new File(scoresPath);

			if (!scoresFile.exists())
				scoresFile.createNewFile();

			outputStream = new FileOutputStream(scoresFile);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					outputStream, Charset.forName("UTF-8")));

			logger.info("Saving user high scores.");

			// Saves 7 or less scores.
			int savedCount = 0;
			for (Score score : highScores) {
				if (savedCount >= MAX_SCORES)
					break;
				bufferedWriter.write(score.getName());
				bufferedWriter.newLine();
				bufferedWriter.write(Integer.toString(score.getScore()));
				bufferedWriter.newLine();
				savedCount++;
			}

		} finally {
			if (bufferedWriter != null)
				bufferedWriter.close();
		}
	}
	public Player loadPlayer(char[] name) throws IOException {

		Player player = null;
		int amountOfItems = 0;

		String jarPath = FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		jarPath = URLDecoder.decode(jarPath, "UTF-8");

		String playerPath = new File(jarPath).getParent() + File.separator + "accounts";
		String currentPlayerPath = new File(jarPath).getParent() + File.separator + "currentPlayer";

		File playerFile = new File(playerPath);
		File currentPlayerFile = new File(currentPlayerPath);
		currentPlayerFile.createNewFile();



		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(playerFile), Charset.forName("UTF-8")));
			 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(currentPlayerFile, false), Charset.forName("UTF-8")))) {

			String loadedName = bufferedReader.readLine();
			String currency = bufferedReader.readLine();

			while ((loadedName != null) && (currency != null)) {
				if (loadedName.equals(String.valueOf(name))) {
					player = new Player(loadedName, Integer.parseInt(currency));
					logger.info(String.valueOf(player.getCurrency()));
					bufferedWriter.write(loadedName);
					bufferedWriter.newLine();
					bufferedWriter.write(currency);
					bufferedWriter.newLine();
					for (int i = 0; i < amountOfItems; i++) {
						bufferedWriter.write(bufferedReader.readLine());
						bufferedWriter.newLine();
					}
					bufferedWriter.flush();
					break;
				}else {
					loadedName = bufferedReader.readLine();
					currency = bufferedReader.readLine();
				}
			}

		} catch (FileNotFoundException e) {
			// create new player if player not found.
			logger.info("Account list not found.");
		}

		return player;
	}
	public void saveNewPlayer(final char[] name) throws IOException {
		// Get the path to the JAR file.
		String jarPath = FileManager.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);

		// Construct the path to the player file.
		Path playerPath = Paths.get(new File(jarPath).getParent(), "accounts");

		// Create the player file if it doesn't exist.
		File playerFile = playerPath.toFile();
		if (!playerFile.exists() && !playerFile.createNewFile()) {
			logger.warning("Failed to create new player file at: " + playerPath);
			return;
		}

		// Write the new player data to the file.
		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(playerPath, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
			logger.info("Creating new user with name: " + String.valueOf(name));
			bufferedWriter.write(String.valueOf(name));
			bufferedWriter.newLine();
			bufferedWriter.write("0");
			bufferedWriter.newLine();
		} catch (IOException e) {
			logger.warning("Failed to write new player data to file: " + e.getMessage());
			throw e; // Re-throw the exception after logging it.
		}
	}

	//Overwrites the content of currentPlayer.txt to accounts.txt.
	public void updateAccounts() throws IOException {
		String jarPath = FileManager.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);

		Path playerPath = Paths.get(new File(jarPath).getParent(), "accounts");

		if (!Files.exists(playerPath)) {
			logger.warning("Player file not found at: " + playerPath);
			return;
		}

		StringBuilder inputBuffer = new StringBuilder();
		try (BufferedReader bufferedReader = Files.newBufferedReader(playerPath, StandardCharsets.UTF_8)) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				inputBuffer.append(line).append('\n');
			}
		} catch (IOException e) {
			logger.warning("Failed to read player data from file: " + e.getMessage());
			throw e;
		}

		Path currentPlayerPath = Paths.get(new File(jarPath).getParent(), "currentPlayer");

		try (BufferedReader currentBufferedReader = Files.newBufferedReader(currentPlayerPath, StandardCharsets.UTF_8)) {
			String loadedName = currentBufferedReader.readLine();
			String currency = currentBufferedReader.readLine();

			if (loadedName == null || currency == null) {
				logger.warning("Invalid data in current player file");
				return;
			}

			Player player = loadPlayer(loadedName.toCharArray());
			String inputStr = inputBuffer.toString().replace(
					loadedName + "\n" + player.getCurrency() + "\n",
					loadedName + "\n" + currency + "\n");

			try (BufferedWriter bufferedWriter = Files.newBufferedWriter(playerPath, StandardCharsets.UTF_8)) {
				bufferedWriter.write(inputStr);
				logger.info("Successfully changed amount of player: " + loadedName + " to " + currency);
			} catch (IOException e) {
				logger.warning("Failed to write updated player data to file: " + e.getMessage());
				throw e;
			}
		} catch (IOException e) {
			logger.warning("Failed to read current player data from file: " + e.getMessage());
			throw e;
		}
	}

	public void updateCurrencyOfCurrentPlayer(int difference) throws IOException {
		String jarPath = FileManager.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);

		Path playerPath = Paths.get(new File(jarPath).getParent(), "currentPlayer");

		if (!Files.exists(playerPath)) {
			logger.warning("Player file not found at: " + playerPath);
			return;
		}

		List<String> lines = Files.readAllLines(playerPath, StandardCharsets.UTF_8);
		if (lines.size() < 2) {
			logger.warning("Invalid data in current player file");
			return;
		}

		String loadedName = lines.get(0);
		int currentCurrency;
		try {
			currentCurrency = Integer.parseInt(lines.get(1));
		} catch (NumberFormatException e) {
			logger.warning("Invalid currency value in current player file");
			return;
		}

		int newBalance = currentCurrency + difference;
		lines.set(1, String.valueOf(newBalance));

		try {
			Files.write(playerPath, lines, StandardCharsets.UTF_8);
			logger.info("Successfully changed amount of player: " + loadedName + " to " + newBalance);
		} catch (IOException e) {
			logger.warning("Failed to write updated player data to file: " + e.getMessage());
			throw e;
		}
	}

	public int getCurrentPlayerCurrency() throws IOException {
		String jarPath = FileManager.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);

		Path playerPath = Paths.get(new File(jarPath).getParent(), "currentPlayer.txt");

		if (!Files.exists(playerPath)) {
			logger.warning("Player file not found at: " + playerPath);
			throw new FileNotFoundException("Player file not found at: " + playerPath);
		}

		List<String> lines = Files.readAllLines(playerPath, StandardCharsets.UTF_8);
		if (lines.size() < 2) {
			logger.warning("Invalid data in current player file");
			throw new IOException("Invalid data in current player file");
		}

		int currency;
		try {
			currency = Integer.parseInt(lines.get(1));
		} catch (NumberFormatException e) {
			logger.warning("Invalid currency value in current player file");
			throw new NumberFormatException("Invalid currency value in current player file");
		}

		return currency;
	}
}
