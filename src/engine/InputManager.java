package engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Manages keyboard input for the provided screen.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public final class InputManager implements KeyListener {

	/** Number of recognised keys. */
	private static final int NUM_KEYS = 256;
	/** Array with the keys marked as pressed or not. */
	private static boolean[] keys;
	/** Singleton instance of the class. */
	private static InputManager instance;

	private static int keyUp = 0;

	private static Integer keyCode;
	private static String keyString;
	private boolean check=false;

	/**
	 * Private constructor.
	 */
	private InputManager() {
		keys = new boolean[NUM_KEYS];
	}

	/**
	 * Returns shared instance of InputManager.
	 * 
	 * @return Shared instance of InputManager.
	 */
	protected static InputManager getInstance() {
		if (instance == null)
			instance = new InputManager();
		return instance;
	}

	/**
	 * Returns true if the provided key is currently pressed.
	 * 
	 * @param keyCode
	 *            Key number to check.
	 * @return Key state.
	 */
	public boolean isKeyDown(final int keyCode) {
		return keys[keyCode];
	}

	/**
	 * Changes the state of the key to pressed.
	 * 
	 * @param key
	 *            Key pressed.
	 */
	@Override
	public void keyPressed(final KeyEvent key) {
		if (key.getKeyCode() >= 0 && key.getKeyCode() < NUM_KEYS)
			keys[key.getKeyCode()] = true;
		keyCode = key.getKeyCode();
		keyString = key.getKeyText(keyCode);
		check = true;
	}
	public boolean isKeyUp(int keyCode) {
		return !keys[keyCode];
	}



	public int countH_u=0;
	public int countH_d=0;
	public int one=0, two=0;
	public int seven=0, eight=0;
	public int speed = 0;
	public int speed1=0, speed2=0;
	public boolean magazine = false;
	public boolean magazine2 = false;
	/**
	 * Changes the state of the key to not pressed.
	 * 
	 * @param key
	 *            Key released.
	 */
	@Override
	public void keyReleased(final KeyEvent key) {
		if (key.getKeyCode() >= 0 && key.getKeyCode() < NUM_KEYS)
			keys[key.getKeyCode()] = false;
		check = false;

		switch (key.getKeyCode()){
			case 38: // 위
				countH_u++;
				break;
			case 40: // 아래
				countH_d++;
				break;
			case 32: // 스페이스
				if (keyUp == 0) {
					keyUp = 1;
				}
				break;
			case 83: //s
				speed ++;
				break;
			case 81: //q
				magazine = true;
				break;

			case 48: // 0
				magazine2=true; break;
			case 49: // 1
				one++; break;
			case 50: // 2
				two++; break;
			case 51: //3
				speed1++; break;
			case 52: //4
				magazine = true; break;
			case 55: //7
				seven++; break;
			case 56: //8
				eight++; break;
			case 57: //9
				speed2++; break;
		}
	}



	/**
	 * Does nothing.
	 * 
	 * @param key
	 *            Key typed.
	 */
	@Override
	public void keyTyped(final KeyEvent key) {

	}

	public Integer getKeyCode(){return keyCode;}
	public String getKeyString(){return keyString;}
	public boolean getcheck(){return check;}
}