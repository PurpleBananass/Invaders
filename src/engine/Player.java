package engine;

import java.util.Date;

/**
 * Implements a high currency record.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Player implements Comparable<Player> {

	/** Player's name. */
	private String name;
	/** currency points. */
	private int currency;
	private String loginTime;
	/**
	 * Constructor.
	 *
	 * @param name
	 *            Player name, three letters.
	 * @param currency
	 *            Player currency.
	 */
	public Player(final String name, final int currency, final String loginTime) {
		this.name = name;
		this.currency = currency;
		this.loginTime = loginTime;
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

	public final String getLoginTime() {
		return this.loginTime;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public void setLoginTime(String loginTime) {this.loginTime = loginTime;}

	/**
	 * Orders the currencys descending by currency.
	 * 
	 * @param currency
	 *            currency to compare the current one with.
	 * @return Comparison between the two currencys. Positive if the current one is
	 *         smaller, positive if its bigger, zero if its the same.
	 */
	@Override
	public int compareTo(Player o) {
		return 0;
	}
}
