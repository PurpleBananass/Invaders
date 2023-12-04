package engine;

import java.util.Date;

import java.util.List;

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
	/** item list */
	private List<Boolean> item;

	private int skincode; //플레이어의 정보에 스킨코드값 추가

	/**
	 * Constructor.
	 *
	 * @param name
	 *            Player name, three letters.
	 * @param currency
	 *            Player currency.
	 */
	public Player(final String name, final int currency, final String loginTime, final List<Boolean> item, final int skincode) {
		this.name = name;
		this.currency = currency;
		this.loginTime = loginTime;
		this.item = item;
		this.skincode = skincode;
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
	 * @return currency.
	 */
	public final int getCurrency() {
		return this.currency;
	}

	public final String getLoginTime() {
		return this.loginTime;
	}

	public final List<Boolean> getItem() {
		return this.item;
	}

	public final int getSkincode(){
		return this.skincode;
	} //스킨코드를 반환해주는 메소드
	public void setName(String name) {
		this.name = name;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public void setLoginTime(String loginTime) {this.loginTime = loginTime;}

	public void setItem(List<Boolean> item) {
		this.item = item;
	}
	public void setSkincode(int skincode) {this.skincode = skincode;}

	@Override
	public int compareTo(Player o) {
		return 0;
	}
}
