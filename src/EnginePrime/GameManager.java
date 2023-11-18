package EnginePrime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;

import GamePrime.Entry;

public final class GameManager implements GManager {

    public static Frame frame = null;
    public static boolean running = true;
    public static EngineTimer Et = EngineTimer.getInstance();
    public static RenderManager Rm = RenderManager.getInstance();
    public static InputManager Im = InputManager.getInstance();
    public static SoundManager Sm = SoundManager.getInstance();
    public static GameManager instance = null;
    public static GManager CustomInstance = null;

    public static boolean InstanceChanged = false;
    public static Map<String, JSONObject> GlobalData = new HashMap<>();
    public static ArrayList<Runnable> ExitCode = new ArrayList<>();

    private GameManager() {
    };
    public void Exit(){};
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
            CustomInstance = new Entry();
        }
        return instance;
    }

    public void Initialize() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (Runnable code : ExitCode) {
                code.run();
            }
        }));
        Et.Initialize();
        Rm.Initialize();
        Im.Initialize();
        Sm.Initialize();
        GlobalData.put("LocalData",  new JSONObject());
        SetInstance(new Entry());
    }

    public void SetInstance(GManager inst) {
        if(CustomInstance!= null){
            CustomInstance.Exit();
        }

        CustomInstance = inst;
        if (CustomInstance != null) {
            InstanceChanged = true;
            CustomInstance.Initialize();
        }
    }

    public void PreUpdate() {
        Et.PreUpdate();
        Rm.PreUpdate();
        Im.PreUpdate();
        Sm.PreUpdate();
        if (CustomInstance != null && !InstanceChanged) {
            CustomInstance.PreUpdate();
        }
    }

    public void LateUpdate() {
        Et.LateUpdate();
        Rm.LateUpdate();
        Im.LateUpdate();
        Sm.LateUpdate();
        if (CustomInstance != null && !InstanceChanged) {

            CustomInstance.LateUpdate();
        }
    }

    public void PreRender(){
        Et.PreRender();
        Rm.PreRender();
        Im.PreRender();
        Sm.PreRender();
        if (CustomInstance != null && !InstanceChanged) {

            CustomInstance.PreRender();
        }
    }

    public void LateRender(){
        Et.LateRender();
        Rm.LateRender();
        Im.LateRender();
        Sm.LateRender();
        if (CustomInstance != null && !InstanceChanged) {

            CustomInstance.LateRender();
        }
    }


}
