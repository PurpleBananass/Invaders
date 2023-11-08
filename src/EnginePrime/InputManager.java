package EnginePrime;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public final class InputManager implements GManager,KeyListener {

    private static InputManager instance;

	public static InputManager getInstance() {
		if (instance == null)
			instance = new InputManager();
		return instance;
	}
	private InputManager() {}

    private static final int NUM_KEYS = 256;
	/** Array with the keys marked as pressed or not. */
	private static KeyState[] state;
    private static KeyState[] newstate;


    public int GetAnyKeyDown(){
        for (int i = 0; i < NUM_KEYS; i++) {
            if(state[i] == KeyState.Down){
                return i;
            }

        }
        return -1;
    } 



    public void Initialize(){
		state = new KeyState[NUM_KEYS];
        newstate = new KeyState[NUM_KEYS];
        Arrays.fill(state, KeyState.Released);
        Arrays.fill(newstate, KeyState.Released);
    };
    public void PreUpdate(){
        UpdateKeyState();
    };
    public void LateUpdate(){};

    private void UpdateKeyState(){
        for (int i = 0; i < NUM_KEYS; i++) {
            if(newstate[i] == KeyState.Released){
                if(state[i] == KeyState.Up){
                    state[i] = KeyState.Released;
                }
                else if(state[i] == KeyState.Down){
                    state[i] = KeyState.Pressed;
                }
            }
            else{
                if(newstate[i] == KeyState.Up && state[i] != KeyState.Up && state[i] != KeyState.Released){
                    state[i] = KeyState.Up;
                }
                else if(newstate[i] == KeyState.Down){
                    if(state[i] == KeyState.Released){
                        state[i] = KeyState.Down;
                    }
                    else if(state[i] == KeyState.Down){
                        state[i] = KeyState.Pressed;
                    }
                }
            }
        }
        Arrays.fill(newstate, KeyState.Released);
    }
	public boolean isKeyDown(final int keyCode) {
		return state[keyCode] == KeyState.Down;
	}

	public boolean isKeyUp(int keyCode) {
		return state[keyCode] == KeyState.Up;
	}
	public boolean isKeyPressed(int keyCode) {
		return state[keyCode] == KeyState.Pressed;
	}
    	public boolean isKeyReleased(int keyCode) {
		return state[keyCode] == KeyState.Released;
	}

	@Override
	public void keyPressed(final KeyEvent key) {
		if (key.getKeyCode() >= 0 && key.getKeyCode() < NUM_KEYS)
			newstate[key.getKeyCode()] = KeyState.Down;
	}

	@Override
	public void keyReleased(final KeyEvent key) {
		if (key.getKeyCode() >= 0 && key.getKeyCode() < NUM_KEYS)
			newstate[key.getKeyCode()] = KeyState.Up;
	}

    @Override
	public void keyTyped(final KeyEvent key) {}


}