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
	/** Bullets shot until now from player1. */
	private int bulletsShot1;
	/** Bullets shot until now from player2. */
	private int bulletsShot2;
	/** Ships destroyed until now. */
	private int shipsDestroyed;
	/** Distinguish 1P and 2P mode. */
	private int gameMode = 0;
	/**
	 * Constructor for 1p mode.
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
	public GameState(final int level, final int score,
					final int livesRemaining, final int bulletsShot,
					final int shipsDestroyed) {
		this.gameMode = 1;
		this.level = level;
		this.score = score;
		this.livesRemaining1 = livesRemaining;
		this.bulletsShot1 = bulletsShot;
		this.shipsDestroyed = shipsDestroyed;
	}

	/**
	 * Constructor for 2p mode.
	 *
	 * @param level
	 *            Current game level.
	 * @param score
	 *            Current score.
	 * @param livesRemaining1
	 *            player1's Lives currently remaining.
	 * @param livesRemaining2
	 *            player2's Lives currently remaining.
	 * @param bulletsShot1
	 *            Bullets shot until now from player1.
	 * @param bulletsShot2
	 *            Bullets shot until now from player2.
	 * @param shipsDestroyed
	 *            Ships destroyed until now.
	 */
	public GameState(final int level, final int score,
					 final int livesRemaining1, final int livesRemaining2, final int bulletsShot1, final int bulletsShot2,
					 final int shipsDestroyed) {
		this.gameMode = 2;
		this.level = level;
		this.score = score;
		this.livesRemaining1 = livesRemaining1;
		this.livesRemaining2 = livesRemaining2;
		this.bulletsShot1 = bulletsShot1;
		this.bulletsShot2 = bulletsShot2;
		this.shipsDestroyed = shipsDestroyed;
	}

	/**
	 * Getter for level.
	 *
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
	 * Getter for remain lives from player1.
	 *
	 * @return the remain lives from player1
	 */
	public final int getLivesRemaining1p() {
		return livesRemaining1;
	}
	/**
	 * Getter for remain lives from player2.
	 *
	 * @return the remain lives from player2
	 */
	public final int getLivesRemaining2p() {
		return livesRemaining2;
	}

	/**
	 * Getter for shot bullets from player1.
	 *
	 * @return the bulletsShot from player1
	 */
	public final int getBulletsShot1() {
		return bulletsShot1;
	}
	/**
	 * Getter for shot bullets from player2.
	 *
	 * @return the bulletsShot from player2
	 */
	public final int getBulletsShot2() {
		return bulletsShot2;
	}

	/**
	 * @return the shipsDestroyed
	 */
	public final int getShipsDestroyed() {
		return shipsDestroyed;
	}

}
