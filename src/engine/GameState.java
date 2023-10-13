package engine;

/**
 * Implements an object that stores the state of the game between levels.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class GameState {

	/** Current game level. */
	private int level;
	/** Current score. */
	private int score;
	/** 1p's Lives currently remaining. */
	private int livesRemaining1;
	/** 2p's Lives currently remaining. */
	private int livesRemaining2;
	/** Bullets shot until now. */
	private int bulletsShot;
	/** Ships destroyed until now. */
	private int shipsDestroyed;
	/** Distinguish 1P and 2P mode. */
	private int gameMode = 0;
	/**
	 * Constructor.
	 * 
	 * @param level
	 *            Current game level.
	 * @param score
	 *            Current score.
	 * @param livesRemaining
	 *            Lives currently remaining.
	 * @param bulletsShot
	 *            Bullets shot until now.
	 * @param shipsDestroyed
	 *            Ships destroyed until now.
	 */

	// 1p mode
	public GameState(final int level, final int score,
					final int livesRemaining, final int bulletsShot,
					final int shipsDestroyed) {
		this.gameMode = 1;
		this.level = level;
		this.score = score;
		this.livesRemaining1 = livesRemaining;
		this.bulletsShot = bulletsShot;
		this.shipsDestroyed = shipsDestroyed;
	}

	// 2p mode
	public GameState(final int level, final int score,
					 final int livesRemaining1, final int livesRemaining2, final int bulletsShot,
					 final int shipsDestroyed) {
		this.gameMode = 1;
		this.level = level;
		this.score = score;
		this.livesRemaining1 = livesRemaining1;
		this.livesRemaining2 = livesRemaining2;
		this.bulletsShot = bulletsShot;
		this.shipsDestroyed = shipsDestroyed;
	}

	/**
	 * @return the level
	 */
	public final int getLevel() {
		return level;
	}

	/**
	 * @return the gameMode
	 */
	public final int getMode() {
		return gameMode;
	}

	/**
	 * @return the score
	 */
	public final int getScore() {
		return score;
	}

	/**
	 * @return the livesRemaining
	 */
	public final int getLivesRemaining1p() {
		return livesRemaining1;
	}

	public final int getLivesRemaining2p() {
		return livesRemaining2;
	}

	/**
	 * @return the bulletsShot
	 */
	public final int getBulletsShot() {
		return bulletsShot;
	}

	/**
	 * @return the shipsDestroyed
	 */
	public final int getShipsDestroyed() {
		return shipsDestroyed;
	}

}
