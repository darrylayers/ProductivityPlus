package core;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;

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
        frame.setSize(533, 381);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("ProductivityPlus");
        initializeComponents();
        frame.setResizable(false);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmItem = new JMenuItem("Item 1");
        mnFile.add(mntmItem);

        JMenuItem mntmItem_1 = new JMenuItem("Item 2");
        mnFile.add(mntmItem_1);

        JMenu mnEdit = new JMenu("Edit");
        menuBar.add(mnEdit);

        JMenuItem mntmItem_2 = new JMenuItem("Item 1");
        mnEdit.add(mntmItem_2);

        JMenu mnHelp = new JMenu("Help");
        menuBar.add(mnHelp);

        JMenuItem mntmItem_3 = new JMenuItem("Item 1");
        mnHelp.add(mntmItem_3);
        frame.getContentPane()
            .setLayout(new MigLayout("", "[517px]", "[326.00px]"));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane, "cell 0 0,growx,aligny top");

        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.setToolTipText("");
        tabbedPane.addTab("Program Timer", null, panel, null);
        panel.setLayout(new MigLayout("", "[]", "[288.00]"));

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(Color.PINK);
        tabbedPane.addTab("Pomodoro Timer", null, panel_1, null);

        JPanel panel_2 = new JPanel();
        panel_2.setToolTipText("");
        panel_2.setBackground(Color.GRAY);
        tabbedPane.addTab("Break Stopper", null, panel_2, null);
        panel_2.setLayout(new MigLayout("", "[]", "[]"));
        frame.setVisible(true);

    }

    /**
     * Method to initialize all the components onto the frame.
     */
    public void initializeComponents() {
    }

}
