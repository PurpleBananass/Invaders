package EnginePrime;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;

import GamePrime.Entry;

public final class GameManager implements GManager{
    

    public static Frame frame = null;
    public static boolean running = true;
    public static EngineTimer Et = EngineTimer.getInstance();
    public static RenderManager Rm = RenderManager.getInstance();
    public static InputManager Im = InputManager.getInstance();
    public static GameManager instance = null;
    private static GManager CustomInstance = null; 
    
    public Map<String, JSONObject> GlobalData = new HashMap<>();

    private GameManager(){};

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
            CustomInstance = new Entry();
        }
        return instance;
    }

    public void Initialize(){
        EngineTimer.getInstance().Reset();
        SetInstance(new Entry());
    } 

    public void SetInstance(GManager inst){
        CustomInstance = inst;
        if(CustomInstance!=null){
            CustomInstance.Initialize();
        }
    }

    public void PreUpdate(){
        if(CustomInstance!=null){
            CustomInstance.PreUpdate();

        }
    } 
    public void LateUpdate(){
        if(CustomInstance!=null){
            CustomInstance.LateUpdate();

        }
    } 

}
