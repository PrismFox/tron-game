package controller.timer;

public class ThreadedTimerImpl implements ITimer {

    private Thread gameThread = null;

    @Override
    public void startLobbyTimer(int duration, Runnable callback) {
        new TimerThread(duration, callback).start();
    }

    @Override
    public void startGameTimer(int duration, Runnable callback) {
        if(gameThread != null) {
            throw new IllegalStateException();
        }
        this.gameThread = new TimerThread(duration, callback, Integer.MAX_VALUE);
        this.gameThread.start();
    }

    @Override
    public void stopGameTimer() {
        if(this.gameThread == null) {
            throw new IllegalStateException();
        }
        this.gameThread.interrupt();
        this.gameThread = null;
    }

    
}
