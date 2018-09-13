package core;

import java.util.Random;

import javax.swing.JFrame;

/**
 * Window class used to make dummy windows.
 * 
 * @author Austin Ayers
 * @version 9/12/18
 */

public class CreateDummyWindow {

    public static final int FRAME_SIZE = 300;
    public JFrame frame;
    public static CreateDummyWindow gui;
    public Random rand = new Random();

    /**
     * Constructor that builds the frame.
     */
    public CreateDummyWindow(String windowName) {
        frame = new JFrame();
        frame.setLocation(100 + rand.nextInt(15), 100);
        frame.setSize(FRAME_SIZE, FRAME_SIZE - 120);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(windowName);
        frame.setVisible(true);
    }

}
