package voon.truongvan.english_for_all_level.util;

/**
 * Created by voqua on 3/8/2016.
 */
public class SafeThread extends Thread implements Runnable{
    private boolean isCancelled;

    private int TIME_INTERVAL = 1000;
    private Runnable onUpdate;

    public SafeThread(){
        isCancelled = false;
    }

    public void cancel(){
        isCancelled = true;
    }


    public void setOnUpdate(Runnable runnable){
        this.onUpdate = runnable;
    }

    public void run(){
        while (!isCancelled){
            this.onUpdate.run();
            try {
                Thread.sleep(TIME_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
