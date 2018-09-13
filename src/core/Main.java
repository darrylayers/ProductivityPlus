package core;

/*
 * TODO: Need to find a way to stop people from hitting track multiple times.
 *  more than one thread is being created and the times are being added.
 *  
 *  TODO: Add test cases
 * 
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Main class, currently holding all the GUI code.
 * 
 * @author Austin Ayers
 * @version 9/11/18
 */

public class Main {

    public static final int FRAME_SIZE = 300;
    public JFrame frame;
    public static Main gui;
    public static JLabel isTracking;

    /**
     * Main method that builds the GUI.
     */
    public static void main(String[] args) {
        gui = new Main();
    }

    /**
     * Constructor that builds the frame.
     */
    public Main() {
        frame = new JFrame();
        frame.setLocation(100, 100);
        frame.setSize(FRAME_SIZE, FRAME_SIZE - 120);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("ProductivityPlus");
        initializeComponents();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * Method to initialize all the components onto the frame.
     */
    public void initializeComponents() {

        // Create the buttons
        JButton start = new JButton("Start");
        start.setName("start");
        start.addActionListener(new Start());
        JButton stop = new JButton("Stop");
        stop.setName("stop");
        stop.addActionListener(new Stop());

        // Create the button panel
        JPanel positPanel = new JPanel();

        // Add button to the panel
        positPanel.add(start);
        positPanel.add(stop);

        // Add the panel to the bottom of the calculator window
        frame.add(positPanel, BorderLayout.CENTER);
        isTracking = new JLabel("Currently not tracking.");
        JPanel labelPanel = new JPanel();
        // Add the result label to the label panel, then frame.
        labelPanel.add(isTracking);
        frame.add(labelPanel, BorderLayout.PAGE_START);
    }

    /**
     * Class that performs the start function.
     */
    public class Start implements ActionListener {

        /**
         * ActionEvent for addition.
         * 
         * @param e
         *            takes the listener.
         */

        @Override
        public void actionPerformed(ActionEvent e) {
            ProgramTimer programTimer = new ProgramTimer();
            Thread t = new Thread(programTimer);
            t.start();
            isTracking.setText("Currently tracking...");

        }
    }

    /**
     * Class that performs the stop function.
     */
    public class Stop implements ActionListener {
        /**
         * ActionEvent for addition.
         * 
         * @param e
         *            takes the listener.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            ProgramTimer.stop();
            isTracking.setText("Currently not tracking.");

        }
    }

}
