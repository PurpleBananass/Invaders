package EnginePrime;



public class Timer {
    
    long previousTime;
    long currentTime;
    double elapsedSeconds;

    public void Reset(){
        previousTime = System.nanoTime();
        currentTime = previousTime;
        elapsedSeconds = 0;
    }

    public void Update(){
        currentTime = System.nanoTime();
        long elapsedTime = currentTime - previousTime;
        elapsedSeconds = (double) elapsedTime / 1_000_000_000.0;
        previousTime = currentTime;
    }

    public double GetElapsedSeconds(){
        return elapsedSeconds;
    }
}