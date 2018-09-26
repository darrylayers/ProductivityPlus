package core;

import java.awt.MouseInfo;
import java.awt.Point;

/**
 * CheckIdle.java is used to determine if the user is idle and decides if the
 * data being recorded should be recorded and used in the output.
 * 
 * @author Austin Ayers
 * @version 9/20/18
 *
 */
public class CheckIdle {

    // TODO: What determines a user is idle?
    // Lack of keystrokes and lack of mouse movement.
    // Sometimes the user will be "active" but not moving
    // their mouse or typing, so this option should be
    // optional and have a variable/customizable threshold for the time.
    // TODO: Stop user from editing preferences while program is running

    public static Point old_p;
    public static Point new_p;
    static int old_x;
    private static int old_y;
    private static int new_x;
    private static int new_y;
    CheckIdle idle;
    PreferencesGui prefs;

    public CheckIdle() {
        this.saveMouse();
    }

    /*
     * This method saves the (x,y) location of the mouse on the screen.
     */
    public void saveMouse() {
        // Grab the current mouse point
        old_p = MouseInfo.getPointerInfo().getLocation();
        old_x = old_p.x;
        old_y = old_p.y;
    }

    /*
     * This method checks for mouse movement for the given threshold by comparing
     * new coord values to old ones.
     */
    public boolean checkMouse() {
        // Grab the current mouse point
        new_p = MouseInfo.getPointerInfo().getLocation();
        new_x = new_p.x;
        new_y = new_p.y;

        // this compares the old points to the new ones.
        // only true when the mouse is in the same location.
        if (old_x == new_x && old_y == new_y) {
            return true;
        }
        else {
            return false;
        }
    }

    public void run() {

        try {

            Thread.sleep(PreferencesGui.getIdleTimer() * 1000);

            if (this.checkMouse()) {
                Main.simulateClick();
                new_p.x++;
                new_p.y++;
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
