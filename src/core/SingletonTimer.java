package core;

/**
 * SingletonTimer.java creates a single instance of the program timer. This is
 * important because it insures only one tracker can be created at a time.
 * 
 * @author Austin Ayers
 */
public class SingletonTimer {

    private static SingletonTimer timer;
    private static boolean beenCalled = false;

    /**
     * Private constructor used to make this class a singleton.
     */
    private SingletonTimer() {

    }

    /**
     * Getter that returns a single instance from the SingletonTimer's private
     * constructor.
     * 
     * @return returns a singleton SingletonTimer.
     */
    public static SingletonTimer getInstance() {
        if (timer == null) {
            timer = new SingletonTimer();
        }
        return timer;
    }

    /**
     * This method creates a new program timer object if the boolean field
     * beenCalled is false, meaning there has not yet been a program timer
     * created.
     */
    public void callTimer() {
        if (beenCalled == false) {
            beenCalled = true;
            ProgramTimer programTimer = new ProgramTimer();
            Thread t = new Thread(programTimer);
            t.start();
        }
    }

    /**
     * This method allows the callTimer() method to be able to create a new
     * program timer object.
     */
    public static void setBeenCalled() {
        beenCalled = false;
    }
}
