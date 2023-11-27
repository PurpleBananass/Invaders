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

	/** Number of enemy ships destroyed */
	private int shipsDestroyed1;
	private int shipsDestroyed2;

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
		this.shipsDestroyed1 = shipsDestroyed;
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
					 final int shipsDestroyed, final int shipsDestroyed2) {
		this.gameMode = 2;
		this.level = level;
		this.score = score;
		this.livesRemaining1 = livesRemaining1;
		this.livesRemaining2 = livesRemaining2;
		this.bulletsShot1 = bulletsShot1;
		this.bulletsShot2 = bulletsShot2;
		this.shipsDestroyed1 = shipsDestroyed;
		this.shipsDestroyed2 = shipsDestroyed2;
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
	/** Returns the number of ships destroyed by 1p */
	public final int getShipsDestroyed() {
		return shipsDestroyed1;
	}
	/** Returns the number of ships destroyed by 2p*/
	public final int getShipsDestroyed2() {
		return shipsDestroyed2;
	}

	public int livesRemaining1() {
		return 0;
	}
}
