package controller.timer;

import java.util.concurrent.FutureTask;

public class ThreadedTimerImpl implements ITimer {

    private FutureTask<Void> gameTask = null;


    @Override
    public void startLobbyTimer(int duration, Runnable callback) {
        //TODO: Replace with runnable
        new TimerThread(duration, callback).start();
    }

    @Override
    public void startGameTimer(int duration, Runnable callback) {
        if(this.gameTask != null) {
            throw new IllegalStateException();
        }
        //this.gameTask = new TimerThread(duration, callback, Integer.MAX_VALUE);
        this.gameTask = new FutureTask<Void>(() -> {
            for(int i = 0; i < Integer.MAX_VALUE; i++) {
                try {
                    Thread.sleep(duration);
                } catch(InterruptedException e) {
                    return null;
                }
                callback.run();
            }
            return null;
        });
        this.gameTask.run();
    }

    @Override
    public void stopGameTimer() {
        if(this.gameTask == null) {
            throw new IllegalStateException();
        }
        this.gameTask.cancel(true);
        this.gameTask = null;
    }

    
}
