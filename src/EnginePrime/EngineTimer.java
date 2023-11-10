package EnginePrime;



public class EngineTimer implements GManager{
    
    long previousTime;
    long currentTime;
    double elapsedSeconds;
    double maxfps;


    public void PreRender(){};
    
    public void LateRender(){};
    private EngineTimer(){
        maxfps = 0;
    };

    public static EngineTimer instance = null;


    public void Initialize(){
        Reset();
    };

    public void PreUpdate(){
        currentTime = System.nanoTime();
                long elapsedTime = currentTime - previousTime;
                elapsedSeconds = (double) elapsedTime / 1_000_000_000.0;
                previousTime = currentTime;

                double t = (maxfps - elapsedSeconds)*1000;

                if(t>0){
                    try {
                        Thread.sleep((long)t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }   
    };

    public void LateUpdate(){};

    public static EngineTimer getInstance() {
        if (instance == null) {
            instance = new EngineTimer();
        }
        return instance;
    }

    public void Reset(){
        previousTime = System.nanoTime();
        currentTime = previousTime;
        elapsedSeconds = 0;
    }

    public void SetMaxFps(int f){

        maxfps = 1/(double)f;
    }

    public double GetElapsedSeconds(){
        return elapsedSeconds;
    }
}