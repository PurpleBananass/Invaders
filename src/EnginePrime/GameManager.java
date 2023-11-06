package EnginePrime;
import GamePrime.Entry;

public final class GameManager implements GManager{
    
    public static boolean running = true;
    
    
    public static Timer EngineTime = new Timer();
    public static GameManager instance = null;
    public static GManager CustomInstance = null; 
    
    private GameManager(){};

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
            CustomInstance = new Entry();
        }
        return instance;
    }

    public void Initialize(){
        EngineTime.Reset();
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
