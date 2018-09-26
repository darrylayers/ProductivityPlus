package core;

import java.io.IOException;
import java.util.HashMap;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

/**
 * ProgramTimer class that performs the timing feature and records all the data.
 * 
 * @author Austin Ayers
 * @version 9/11/18
 */

public class ProgramTimer implements Runnable {

    protected static HashMap<String, Long> appMap = new HashMap<>();
    private static char[] buffer = new char[4096];
    private static HWND hwnd;
    private static String prog = "";
    private static String progLast = "";
    private static long newTime;
    private static long oldTime;
    private static long start;
    private static long end;
    protected static boolean trackIfTrue;
    private static boolean leftApp = false;
    private static CheckIdle idle;

    /**
     * Overridden run method to run the timer on a new thread. Tracks to see
     * when the user swaps the program in focus and records the time
     * differences. Operates on a pause set to 1/10 per second.
     */
    @Override
    public void run() {
        startTime();
        trackIfTrue = true;
        leftApp = false;

        while (trackIfTrue) {

            // As soon as tracking begins, log the mouse location to determine
            if (PreferencesGui.getIdleChecked()) {
                CheckIdle idle = new CheckIdle();
            }

            // Grab the current in-focus window
            hwnd = User32.INSTANCE.GetForegroundWindow();
            User32.INSTANCE.GetWindowText(hwnd, buffer, buffer.length);
            prog = Native.toString(buffer);

            // Check to see if current in focus is the same as previous.
            if (!progLast.equals(prog) && !progLast.equals("")) {
                leftApp = true;
                endTime();
                try {
                    mapTime();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                startTime();
            }

            progLast = prog;
            // Pause before next check to eliminate wasted checks / resources
            try {
                Thread.sleep(5);
                /*
                 * if (CheckIdle.checkMouse()) { stop(); }
                 */
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        if (!leftApp) {
            endTime();
            try {
                mapTime();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        printMap();
    }

    /*
     * Method to stop the infinite tracking while loop.
     */
    public static void stop() {
        trackIfTrue = (false);
    }

    /*
     * Method to add the new program time count to the Hashmap.
     */
    public static void mapTime() throws IOException {
        // If the program has time existing, we need to add the new to the old
        if (appMap.containsKey(progLast)) {
            oldTime = appMap.get(progLast);
            oldTime += newTime;
            appMap.put(progLast, oldTime);
        }
        // else if the program has no time, simply put the key-value
        else {
            appMap.put(progLast, newTime);
        }
        ExcelWriter.write(appMap);
    }

    /*
     * Method to start tracking the time a new window is in focus.
     */
    public static void startTime() {
        start = System.nanoTime();
    }

    /*
     * Method to stop tracking when an old window is in focus. Must divide to
     * convert to seconds from nanoseconds.
     */
    public static void endTime() {
        end = System.nanoTime();
        newTime = (end - start) / 1000000000;
        System.out
            .println("Closing session and counted " + newTime + " seconds.");
    }

    /*
     * Method to print the entire hashmap of program names and times.
     */
    public static void printMap() {
        System.out.println("\n=====================");
        for (String name : appMap.keySet()) {
            String key = name.toString();
            // String value = appMap.get(name).toString();
            long value = appMap.get(name);

            if (value > 60) {
                System.out.print(
                    "Time spent in " + key + " was " + value / 60
                        + " minutes and " + value % 60 + " seconds.");
            }
            else {
                System.out.println(
                    "Time spent in " + key + " was " + value + " seconds.");
            }
        }
        System.out.println("\n=====================\n");
    }

}
