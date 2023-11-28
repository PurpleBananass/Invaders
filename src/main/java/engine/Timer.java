package engine;

public class Timer {

    private long startTime;
    private long pausedTime;
    private boolean isRunning;
    private long gameCompletionTime;
    private boolean isPaused;

    public Timer() {
        reset();
    }

    public void start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis() - elapsed();
            isRunning = true;
            isPaused = false;
        }
    }

    public void stop() {
        if (isRunning) {
            if (isPaused) {
                startTime += (System.currentTimeMillis() - pausedTime);
                isPaused = false;
            }
            gameCompletionTime = System.currentTimeMillis() - startTime;
            isRunning = false;
        }
    }

    public void pause() {
        if (isRunning && !isPaused) {
            pausedTime = System.currentTimeMillis();
            isPaused = true;
        }
    }

    public void resume() {
        if (isPaused) {
            startTime += (System.currentTimeMillis() - pausedTime);
            isPaused = false;
        }
    }

    public long elapsed() {
        if (isRunning) {
            return isPaused ? pausedTime - startTime : System.currentTimeMillis() - startTime;
        }
        return 0;
    }

    public void recordGameCompletionTime() {
        if (!isPaused && isRunning) {
            gameCompletionTime = System.currentTimeMillis() - startTime;
        }
    }

    public long getGameCompletionTime() {
        return gameCompletionTime;
    }

    public void reset() {
        startTime = System.currentTimeMillis();
        pausedTime = 0;
        isRunning = false;
        gameCompletionTime = 0;
        isPaused = false;
    }
}