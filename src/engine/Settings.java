package engine;

/**
 * Implements a high score record.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Settings{

	/** Setting's name. */
	public String name;
	/** Setting's value. */
	public int value;

	/**
	 * Constructor.
	 *
	 * @param name
	 *            Player name, three letters.
	 * @param value
	 *            Player score.
	 */
	public Settings(final String name, final int value) {
		this.name = name;
		this.value = value;
	}

	
  
	

	/**
	 * Getter for the setting's name.
	 * 
	 * @return Name of the player.
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * Getter for the setting's value.
	 * 
	 * @return High score.
	 */
	public final int getValue() {
		return this.value;
	}

}
