package core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

import gui.Main;

/**
 * ProgramTimer class that performs the timing feature and records all the data.
 * 
 * @author Austin Ayers
 */

public class ProgramTimer implements Runnable {

    private static char[] buffer = new char[4096];
    private static HWND hwnd;
    private static String prog = "";
    private static String progLast = "";
    private static long newTime;
    private static long oldTime;
    private static long start;
    private static long end;
    private static boolean leftApp = false;

    public static boolean trackIfTrue;

    public static Map<String, Long> appMap = new HashMap<>();

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
            // Grab the current in-focus window
            hwnd = User32.INSTANCE.GetForegroundWindow();
            User32.INSTANCE.GetWindowText(hwnd, buffer, buffer.length);
            prog = Native.toString(buffer);

            // Check to see if current in focus is the same as previous.
            if (!progLast.equals(prog) && !progLast.equals("")) {
                leftApp = true; // change the flag since user leg app
                endTime(); // stop the timer

                try {
                    // Save the time
                    mapTime();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startTime(); // start the timer again
            }
            progLast = prog; // save the last prog
            // Pause before next check to eliminate wasted checks / resources
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // If the user has not left the app
        if (!leftApp) {
            endTime();
            try {
                mapTime();
                Main.updateTable(false);
                Main.secretLabel.setText("  ");
                Main.secretLabel.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        printMap();
    }

    /**
     * This method is used to change the tracking flag to exit the while loop
     * created to check active programs.
     */
    public static void stop() {
        trackIfTrue = (false);
    }

    /**
     * Adds the last calculated program time to the appropriate location in the
     * hashmap.
     */
    private static void mapTime() throws IOException {
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
        DataHandling.saveMap();
    }

    /**
     * Starts the program timer over when a new window is in focus.
     */
    private static void startTime() {
        start = System.nanoTime();
    }

    /**
     * Method to stop tracking when an old window is in focus. Must divide to
     * convert to seconds from nanoseconds.
     */
    private static void endTime() {
        end = System.nanoTime();
        newTime = (end - start) / 1000000000;
        System.out
            .println("Closing session and counted " + newTime + " seconds.");
    }

    /**
     * Print the hashmap.
     */
    public static void printMap() {
        System.out.println("\n=====================");
        for (String name : appMap.keySet()) {
            String key = name.toString();
            long value = appMap.get(name);

            if (value > 60) {
                System.out.print(
                    "Time spent in " + key + " was " + value / 60
                        + " minutes and " + value % 60 + " seconds.");
            } else {
                System.out.println(
                    "Time spent in " + key + " was " + value + " seconds.");
            }
        }
        System.out.println("=====================\n");
    }
}
