package engine;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import engine.DrawManager.SpriteType;

/**
 * Manages files used in the application.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 */
public final class FileManager {

    /**
     * Singleton instance of the class.
     */
    private static FileManager instance;
    /**
     * Application logger.
     */
    private static Logger logger;
    /**
     * Max number of high scores.
     */
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
     * @param spriteMap Mapping of sprite type and empty boolean matrix that will
     *                  contain the image.
     * @throws IOException In case of loading problems.
     */
    public void loadSprite(Map<SpriteType, boolean[][]> spriteMap)
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
     * Change sprites from disk.
     *
     * @param spriteMap,spriteType,graphicsNum Changing boolean matrix that will
     *                                         change the image.
     *                                         graphicsNum is col_num(each graphics)
     * @throws IOException In case of changing problems.
     */
    public void changeSprite(Map<SpriteType, boolean[][]> spriteMap, SpriteType spriteType, int graphicsNum)
            throws IOException {
        InputStream inputStream = checkSpriteType(spriteType);
        try {
            char c;
            for (Map.Entry<SpriteType, boolean[][]> sprite : spriteMap
                    .entrySet()) {
                if (sprite.getKey() == spriteType) {
                    for (int k = -1; k < graphicsNum; k++) {
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
                    }
                    logger.fine("Sprite " + spriteType + " changed.");
                    break;
                }
            }
            if (inputStream != null)
                inputStream.close();
        } finally {
            if (inputStream != null)
                inputStream.close();
        }
    }

    /**
     * Check Sprite Type.
     *
     * @param spriteType Point size of the font.
     * @return inputStream.
     * @throws IOException In case of loading problems.
     */
    public InputStream checkSpriteType(SpriteType spriteType) {
        InputStream inputStream = null;
        if (spriteType == SpriteType.Bullet) {
            inputStream = DrawManager.class.getClassLoader()
                    .getResourceAsStream("bulletGraphics");
        } else if (spriteType == SpriteType.Ship) {
            inputStream = DrawManager.class.getClassLoader()
                    .getResourceAsStream("shipGraphics");
        } else if (spriteType == SpriteType.Ship2) {
            inputStream = DrawManager.class.getClassLoader()
                    .getResourceAsStream("shipGraphics");
        } else if (spriteType == SpriteType.EnemyBullet) {
            inputStream = DrawManager.class.getClassLoader()
                    .getResourceAsStream("bulletGraphics");
        } else if (spriteType == SpriteType.EnemyShipA1) {
            inputStream = DrawManager.class.getClassLoader()
                    .getResourceAsStream("enemyshipGraphics");
        } else if (spriteType == SpriteType.EnemyShipA2) {
            inputStream = DrawManager.class.getClassLoader()
                    .getResourceAsStream("enemyshipGraphics");
        } else if (spriteType == SpriteType.EnemyShipB1) {
            inputStream = DrawManager.class.getClassLoader()
                    .getResourceAsStream("enemyshipGraphics");
        } else if (spriteType == SpriteType.EnemyShipB2) {
            inputStream = DrawManager.class.getClassLoader()
                    .getResourceAsStream("enemyshipGraphics");
        } else if (spriteType == SpriteType.EnemyShipC1) {
            inputStream = DrawManager.class.getClassLoader()
                    .getResourceAsStream("enemyshipGraphics");
        } else if (spriteType == SpriteType.EnemyShipC2) {
            inputStream = DrawManager.class.getClassLoader()
                    .getResourceAsStream("enemyshipGraphics");
        } else if (spriteType == SpriteType.EnemyShipSpecial) {
            inputStream = DrawManager.class.getClassLoader()
                    .getResourceAsStream("specialenemyGraphics");
        }
        return inputStream;
    }


    /**
     * Loads a font of a given size.
     *
     * @param size Point size of the font.
     * @return New font.
     * @throws IOException         In case of loading problems.
     * @throws FontFormatException In case of incorrect font format.
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
     * @throws IOException In case of loading problems.
     */
    private List<Score> loadDefaultHighScores() throws IOException {
        List<Score> highScores = new ArrayList<Score>();
        InputStream inputStream = null;
        BufferedReader reader = null;

        try {
            File scoresFile = new File("res" + File.separator + "scores");
            if (!scoresFile.exists())
                scoresFile.createNewFile();

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
     * @param gameMode The game mode.
     * @return Sorted list of scores - players.
     * @throws IOException In case of loading problems.
     */
    public List<Score> loadHighScores(final int gameMode) throws IOException {

        List<Score> highScores = new ArrayList<Score>();
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;

        try {
            String scoresPath = "";
            scoresPath += File.separator;
            if (gameMode == 1)
                scoresPath += "scores_1p";
            else
                scoresPath += "scores_2p";

            File scoresFile = new File("res" + File.separator + scoresPath);
            if (!scoresFile.exists())
                scoresFile.createNewFile();

            inputStream = new FileInputStream(scoresFile);
            bufferedReader = new BufferedReader(new InputStreamReader(
                    inputStream, Charset.forName("UTF-8")));

            logger.info("Loading user high scores " + "from 'scores_" + gameMode + "p'");

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
     * @param highScores High scores to save.
     * @throws IOException In case of loading problems.
     */
    public void saveHighScores(final List<Score> highScores, final int gameMode)
            throws IOException {
        OutputStream outputStream = null;
        BufferedWriter bufferedWriter = null;

        try {
            String scoresPath = "";
            if (gameMode == 1)
                scoresPath += "scores_1p";
            else
                scoresPath += "scores_2p";

            File scoresFile = new File("res" + File.separator + scoresPath);
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

    public List<Settings> loadSettings() throws IOException {

        List<Settings> settings = new ArrayList<Settings>();
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;

        try {
            String jarPath = FileManager.class.getProtectionDomain()
                    .getCodeSource().getLocation().getPath();
            jarPath = URLDecoder.decode(jarPath, "UTF-8");

            String settingPath = new File(jarPath).getParent();
            settingPath += File.separator;
            settingPath += "settings";

            File scoresFile = new File(settingPath);
            inputStream = new FileInputStream(scoresFile);
            bufferedReader = new BufferedReader(new InputStreamReader(
                    inputStream, Charset.forName("UTF-8")));

            logger.info("Loading settings.");

            Settings settings1 = null;
            String name = bufferedReader.readLine();
            String value = bufferedReader.readLine();
            settings1 = new Settings(name, Integer.parseInt(value));
            settings.add(settings1);

            name = bufferedReader.readLine();
            value = bufferedReader.readLine();
            settings1 = new Settings(name, Integer.parseInt(value));
            settings.add(settings1);

            name = bufferedReader.readLine();
            value = bufferedReader.readLine();
            while ((name != null) && (value != null)) {
                settings1 = new Settings(name, Integer.parseInt(value, 16));
                settings.add(settings1);
                name = bufferedReader.readLine();
                value = bufferedReader.readLine();
            }

        } catch (FileNotFoundException e) {
            // loads default if there's no settings.
            logger.info("Loading default Settings.");
            settings = loaddefaultSettings();
        } finally {
            if (bufferedReader != null)
                bufferedReader.close();
        }
        return settings;
    }

    public List<Settings> loaddefaultSettings() throws IOException {
        List<Settings> Setting = new ArrayList<Settings>();
        InputStream inputStream = null;
        BufferedReader reader = null;

        try {
            inputStream = FileManager.class.getClassLoader()
                    .getResourceAsStream("settings");
            reader = new BufferedReader(new InputStreamReader(inputStream));

            Settings Setting1 = null;
            String name = reader.readLine();
            String value = reader.readLine();
            Setting1 = new Settings(name, Integer.parseInt(value));
            Setting.add(Setting1);

            name = reader.readLine();
            value = reader.readLine();
            Setting1 = new Settings(name, Integer.parseInt(value));
            Setting.add(Setting1);

            name = reader.readLine();
            value = reader.readLine();
            while ((name != null) && (value != null)) {
                Setting1 = new Settings(name, Integer.parseInt(value.substring(2), 16));
                Setting.add(Setting1);
                name = reader.readLine();
                value = reader.readLine();
            }

            logger.info("Successfully load");
        } finally {
            if (inputStream != null)
                inputStream.close();
        }

        return Setting;
    }


    public static void saveSettings(final List<Settings> setting)
            throws IOException {
        OutputStream outputStream = null;
        BufferedWriter bufferedWriter = null;

        try {
            String jarPath = FileManager.class.getProtectionDomain()
                    .getCodeSource().getLocation().getPath();
            jarPath = URLDecoder.decode(jarPath, "UTF-8");

            String settingPath = new File(jarPath).getParent() + File.separator + "settings";
            File settingFlie = new File(settingPath);

            if (!settingFlie.exists())
                settingFlie.createNewFile();

            outputStream = new FileOutputStream(settingFlie);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    outputStream, Charset.forName("UTF-8")));

            logger.info("Saving user settings.");
            bufferedWriter.write(setting.get(0).getName());
            bufferedWriter.newLine();
            bufferedWriter.write(Integer.toString(setting.get(0).getValue()));
            bufferedWriter.newLine();
            bufferedWriter.write(setting.get(1).getName());
            bufferedWriter.newLine();
            bufferedWriter.write(Integer.toString(setting.get(1).getValue()));
            bufferedWriter.newLine();
            // Saves settings.
            for (int i = 2; i < 18; i++) {
                bufferedWriter.write(setting.get(i).getName());
                bufferedWriter.newLine();
                bufferedWriter.write(Integer.toHexString(setting.get(i).getValue()));
                bufferedWriter.newLine();
            }

        } finally {
            if (bufferedWriter != null)
                bufferedWriter.close();
        }
    }

    //loads player object by its name as a character and writes the player data in the currentPlayer text file
    public Player loadPlayer(char[] name) throws IOException {

        Player player = null;

        String jarPath = FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);

        String playerPath = new File(jarPath).getParent() + File.separator + "accounts";
        String currentPlayerPath = new File(jarPath).getParent() + File.separator + "currentPlayer";

        File playerFile = new File(playerPath);
        File currentPlayerFile = new File(currentPlayerPath);
        currentPlayerFile.createNewFile();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(playerFile), Charset.forName("UTF-8")));
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(currentPlayerFile, false), Charset.forName("UTF-8")))) {

            String loadedName = bufferedReader.readLine();
            String currency = bufferedReader.readLine();
            String loginTime = bufferedReader.readLine();
            String itemList = bufferedReader.readLine();

            while ((loadedName != null) && (currency != null) && (loginTime != null) && (itemList != null)) {
                if (loadedName.equals(String.valueOf(name))) {
                    List<Boolean> items = convertStringToBooleanList(itemList);
                    player = new Player(loadedName, Integer.parseInt(currency), loginTime, items);
                    bufferedWriter.write(loadedName);
                    bufferedWriter.newLine();
                    bufferedWriter.write(currency);
                    bufferedWriter.newLine();
                    bufferedWriter.write(loginTime);
                    bufferedWriter.newLine();
                    bufferedWriter.write(itemList);
                    bufferedWriter.newLine();

                    bufferedWriter.flush();
                    break;
                } else {
                    loadedName = bufferedReader.readLine();
                    currency = bufferedReader.readLine();
                    loginTime = bufferedReader.readLine();
                    itemList = bufferedReader.readLine();
                }
            }

        } catch (FileNotFoundException e) {
            // create new player if player not found.
            logger.info("Account list not found.");
        }

        return player;
    }

    //new player is being created and being saved in accounts.txt
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
            bufferedWriter.write(currentDate());
            bufferedWriter.newLine();
            bufferedWriter.write("false, false, false");
            bufferedWriter.newLine();
            bufferedWriter.flush();
            loadPlayer(name); //I know I can make a separate function to overwrite it but I have to set priorities on other things
        } catch (IOException e) {
            logger.warning("Failed to write new player data to file: " + e.getMessage());
            throw e; // Re-throw the exception after logging it.
        }
    }

    //Overwrites the content of currentPlayer.txt to accounts.txt.
    public void updateAccounts() throws IOException {

        // Get the path to the JAR file
        String jarPath = FileManager.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath();
        jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);

        // Construct the path to the player data file
        Path playerPath = Paths.get(new File(jarPath).getParent(), "accounts");

        // Check if the player data file exists
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
            String loginTime = currentBufferedReader.readLine();
            String itemList = currentBufferedReader.readLine();

            if (loadedName == null || currency == null || loginTime == null || itemList == null) {
                logger.warning("Invalid data in current player file");
                return;
            }

            Player player = loadPlayer(loadedName.toCharArray());
            List<Boolean> items = player.getItem();
            String inputStr = inputBuffer.toString().replace(
                    loadedName + "\n" + player.getCurrency() + "\n" + player.getLoginTime() + "\n" + items.get(0) + ", " + items.get(1) + ", " + items.get(2) + "\n",
                    loadedName + "\n" + currency + "\n" + loginTime + "\n" + itemList + "\n");

            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(playerPath, StandardCharsets.UTF_8)) {
                bufferedWriter.write(inputStr);
                logger.info("Successfully updated amount of player");
            } catch (IOException e) {
                logger.warning("Failed to write updated player data to file: " + e.getMessage());
                throw e;
            }
        } catch (IOException e) {
            logger.warning("Failed to read current player data from file: " + e.getMessage());
            throw e;
        }
    }

    //update function for easier manipulation of currency of current player
    public void updateCurrencyOfCurrentPlayer(int difference) throws IOException {

        // Get the path to the JAR file
        String jarPath = FileManager.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath();
        jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);

        // Construct the path to the player data file
        Path playerPath = Paths.get(new File(jarPath).getParent(), "currentPlayer");

        // Check if the player data file exists
        if (!Files.exists(playerPath)) {
            logger.warning("Player file not found at: " + playerPath);
            return;
        }

        int linesBelongingToAPlayer = 4;
        List<String> lines = Files.readAllLines(playerPath, StandardCharsets.UTF_8);
        if (lines.size() < linesBelongingToAPlayer) {
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

    //get the current login time
    public String currentDate() throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    //logic for updating the login time of current player
    public void updateLoginTimeOfCurrentPlayer() throws IOException {
        // Get the path to the JAR file
        String jarPath = FileManager.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath();
        jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);

        // Construct the path to the player data file
        Path playerPath = Paths.get(new File(jarPath).getParent(), "currentPlayer");

        // Check if the player data file exists
        if (!Files.exists(playerPath)) {
            logger.warning("Player file not found at: " + playerPath);
            return;
        }

        // Read the lines from the player data file
        List<String> lines = Files.readAllLines(playerPath, StandardCharsets.UTF_8);

        if (lines.size() < 3) {
            logger.warning("Invalid data in current player file");
            return;
        }
        String loadedName = lines.get(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newDateStr = dateFormat.format(new Date());
        Date currentDate = null;
        Date newDate;
        try {
            newDate = dateFormat.parse(newDateStr);
            currentDate = dateFormat.parse(lines.get(2));
        } catch (ParseException e) {
            logger.warning("Invalid date value in current player file");
            return;
        }

        // Calculate the time difference in milliseconds
        long timeDifference = (newDate.getTime() - currentDate.getTime());


        // Check if the time difference is more than 24 hours (in milliseconds)
        if (timeDifference > 24 * 60 * 60 * 1000) {
            // Update the date only if the condition is met
            lines.set(2, dateFormat.format(newDate));

            try {
                Files.write(playerPath, lines, StandardCharsets.UTF_8);
                logger.info("Successfully updated player's date and login bonus: " + loadedName);
                updateCurrencyOfCurrentPlayer(10);
            } catch (IOException e) {
                logger.warning("Failed to write updated player data to file: " + e.getMessage());
                throw e;
            }
        } else {
            logger.info("Time difference is less than 24 hours, no update is made.");
        }
    }

    //converts a string of booleans in the form of "boolean, boolean..." to a list of booleans
    public static List<Boolean> convertStringToBooleanList(String input) {
        String[] splitStrings = input.split(",");
        List<Boolean> booleanList = new ArrayList<>();

        for (String s : splitStrings) {
            booleanList.add(Boolean.parseBoolean(s.trim()));
        }

        return booleanList;
    }

    //retrieves the current player's data from the currentPlayer text file.
    public Player getCurrentPlayer() throws IOException {
        // Get the path to the JAR file
        String jarPath = FileManager.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath();
        jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);

        // Construct the path to the player data file
        Path playerPath = Paths.get(new File(jarPath).getParent(), "currentPlayer");

        // Check if the player data file exists
        if (!Files.exists(playerPath)) {
            logger.warning("Player file not found at: " + playerPath);
            throw new FileNotFoundException("Player file not found at: " + playerPath);
        }

        // Read the lines from the player data file
        List<String> lines = Files.readAllLines(playerPath, StandardCharsets.UTF_8);

        // Check if the file contains valid data
        if (lines.size() < 4) {
            logger.warning("Invalid data in current player file");
            throw new IOException("Invalid data in current player file");
        }

        Player player;
        try {
            // Parse the player data from the file
            player = new Player(lines.get(0), Integer.parseInt(lines.get(1)), lines.get(2), convertStringToBooleanList(lines.get(3)));
        } catch (NumberFormatException e) {
            logger.warning("Invalid value in current player file");
            throw new NumberFormatException("Invalid value in current player file");
        }

		return player;
	}
	public void updatePlayerItem(int itemNumber) throws IOException {
		// Get the path to the JAR file
		String jarPath = FileManager.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8.toString());

		// Construct the path to the player data file
		Path playerPath = Paths.get(new File(jarPath).getParent(), "currentPlayer");

		// Check if the player data file exists
		if (!Files.exists(playerPath)) {
			logger.warning("Player file not found at: " + playerPath);
			throw new FileNotFoundException("Player file not found at: " + playerPath);
		}

		// Read the lines from the player data file
		List<String> lines = Files.readAllLines(playerPath, StandardCharsets.UTF_8);
		String itemsString = lines.get(3);
		List<Boolean> items = convertStringToBooleanList(itemsString);

		if (!items.get(itemNumber)){
			items.set(itemNumber, true);
		}

		StringBuilder itemResultBuilder = new StringBuilder();
		for (int i = 0; i < items.size(); i++) {
			itemResultBuilder.append(items.get(i).toString());
			if (i < items.size() - 1) {  // If it's not the last item, append ", "
				itemResultBuilder.append(", ");
			}
		}
		String itemResult = itemResultBuilder.toString();

		lines.set(3, itemResult);
		Files.write(playerPath, lines, StandardCharsets.UTF_8);
	}
	public void resetPlayerItem() throws IOException {
		// Get the path to the JAR file
		String jarPath = FileManager.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8.toString());

		// Construct the path to the player data file
		Path playerPath = Paths.get(new File(jarPath).getParent(), "currentPlayer");

		// Check if the player data file exists
		if (!Files.exists(playerPath)) {
			logger.warning("Player file not found at: " + playerPath);
			throw new FileNotFoundException("Player file not found at: " + playerPath);
		}

		// Read the lines from the player data file
		List<String> lines = Files.readAllLines(playerPath, StandardCharsets.UTF_8);


		lines.set(3, "false, false, false");
		Files.write(playerPath, lines, StandardCharsets.UTF_8);
	}
}
