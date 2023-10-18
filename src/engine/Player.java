package engine;

/**
 * This class represents a high currency record for a player in a game.
 * It implements the Comparable interface for comparing players based on their currency.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 */
public class Player implements Comparable<Player> {

	/**
	 * Player's name.
	 */
	private String name;
	/**
	 * currency points.
	 */
	private int currency;

	/**
	 * Constructor.
	 *
	 * @param name     Player name, three letters.
	 * @param currency Player currency.
	 */
	public Player(final String name, final int currency) {
		this.name = name;
		this.currency = currency;
	}

	/**
	 * Getter for the player's name.
	 *
	 * @return Name of the player.
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * Getter for the player's currency.
	 *
	 * @return High currency.
	 */
	public final int getCurrency() {
		return this.currency;
	}
	/**
	 * Setter for updating the player's name.
	 *
	 * @param name The new name for the player.
	 */

	public void setName(String name) {
		this.name = name;
	}
	//Setter for updating the player's currency points.@param currency The new currency points for the player.

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	/**
	 * Orders the currencys descending by currency.
	 *
	 * @param currency
	 *            currency to compare the current one with.
	 * @return Comparison between the two currencys. Positive if the current one is
	 *         smaller, positive if its bigger, zero if its the same.
	 */
	@Override
	public final int compareTo(final Player currency) {
		int comparison = this.currency < currency.getCurrency() ? 1 : this.currency > currency
				.getCurrency() ? -1 : 0;
		return comparison;
	}
}
