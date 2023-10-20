package screen;

import engine.Cooldown;
import engine.Core;
import engine.GameState;
import engine.Score;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class ClearScreen extends Screen {

    /** Height of the interface separation line. */
    private static final int SEPARATION_LINE_HEIGHT = 40;

    private GameState gameState;

    /** Current game level. */
    private int level;
    /** Current score. */
    private int score;
    /** First Player's lives left. */
    private int lives;
    /** Second Player's lives left. */
    private int lives2;
    /** list of past high scores */
    private int highScore;

    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param width  Screen width.
     * @param height Screen height.
     * @param fps    Frames per second, frame rate at which the game is run.
     */
    public ClearScreen(final int width, final int height, final int fps,
                       final GameState gameState) {
        super(width, height, fps);

        this.gameState = gameState;
        this.level = gameState.getLevel()-1;
        this.score = gameState.getScore();
        this.lives = gameState.getLivesRemaining1p();
        if (gameState.getMode() == 2)
            this.lives2 = gameState.getLivesRemaining2p();

        try {
            if (this.gameState.getMode() == 1) {
                List<Score> highScores = Core.getFileManager().loadHighScores(1);
                this.highScore = highScores.stream().mapToInt(Score::getScore).max().orElseThrow(NoSuchElementException::new);
            } else {
                List<Score> highScores = Core.getFileManager().loadHighScores(2);
                this.highScore = highScores.stream().mapToInt(Score::getScore).max().orElseThrow(NoSuchElementException::new);
            }
        } catch (NumberFormatException | NoSuchElementException | IOException e) {
            logger.warning("Couldn't load high score!");
        }

        this.returnCode = 2;
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
        if (inputManager.isKeyDown(KeyEvent.VK_UP)
                || inputManager.isKeyDown(KeyEvent.VK_W)
                || inputManager.isKeyDown(KeyEvent.VK_DOWN)
                || inputManager.isKeyDown(KeyEvent.VK_S)) {
            nextMenuItem();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) { }
        }
        if (inputManager.isKeyDown(KeyEvent.VK_SPACE))
            this.isRunning = false;
    }

    /**
     * Shifts the focus to the next menu item.
     */
    private void nextMenuItem() {
        if (this.returnCode == 1)
            this.returnCode = 2;
        else if (this.returnCode == 2)
            this.returnCode = 1;
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawScore(this, this.score);
        drawManager.drawLives(this, this.lives);
        if (this.gameState.getMode() == 2) drawManager.drawLives2(this, this.lives2);
        drawManager.drawHighScore(this, this.highScore);
        drawManager.drawHorizontalLine(this, SEPARATION_LINE_HEIGHT - 1, Color.GREEN);

        drawManager.drawClear(this, this.returnCode, this.level);

        drawManager.completeDrawing(this);
    }

}
