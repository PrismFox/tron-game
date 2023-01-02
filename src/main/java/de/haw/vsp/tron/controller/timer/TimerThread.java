package de.haw.vsp.tron.controller.timer;

public class TimerThread extends Thread {
    
    private Runnable callback;
    private int duration;
    private int cycles;

    public TimerThread(int duration, Runnable callback, int cycles) {
        this.callback = callback;
        this.duration = duration;
        this.cycles = cycles;
    }

    public TimerThread(int duration, Runnable callback) {
        this(duration, callback, 1);
    }

    @Override
    public void run() {
        for(int i = 0; i < this.cycles; i++) {
            if(interrupted()) {
                return;
            }
            try {
                System.out.println(duration);
                Thread.sleep(this.duration);
            } catch(InterruptedException e) {
                return;
            }
            this.callback.run();
        }
    }
}
