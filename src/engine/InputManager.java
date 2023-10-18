package engine;

import engine.Core;
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
	private boolean checkKeyPressed=false;
	private int[] keySetting = Core.getKeySettingCodeArray();

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
		if(!(keyCode == KeyEvent.VK_CONTROL || keyCode == KeyEvent.VK_SHIFT))
			checkKeyPressed = true;
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
		checkKeyPressed = false;
		int code = key.getKeyCode();
		if(code == keySetting[3]){countH_u++;one++;}
		if(code == keySetting[4]){countH_d++;two++;}
		if(code == keySetting[5]) magazine = true;
		if(code == keySetting[6]) {speed++; speed1++;}
		if(code == keySetting[11]) seven++;
		if(code == keySetting[12]) eight++;
		if(code == keySetting[13]) magazine2=true;
		if(code == keySetting[14]) speed2++;
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
	public boolean getcheck(){return checkKeyPressed;}
}