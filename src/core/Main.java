package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
    private JTextField txtNotTracking;

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
            .setLayout(new MigLayout("", "[531.00px]", "[337.00px]"));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane, "cell 0 0,growx,aligny top");

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setToolTipText("");
        tabbedPane.addTab("Program Timer", null, panel, null);
        panel.setLayout(
            new MigLayout("", "[121.00,grow][][][][grow][][][][][][][][]",
                "[25.00][25.00,grow][25.00][25.00][25.00][][][][]"));

        JTextArea txtrTimerControls = new JTextArea();
        txtrTimerControls.setFont(new Font("Verdana", Font.BOLD, 14));
        txtrTimerControls.setBackground(Color.WHITE);
        txtrTimerControls.setText("Timer Controls");
        panel.add(txtrTimerControls, "cell 0 1,grow");

        JButton btnNewButton = new JButton("Start Timer");
        btnNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                ProgramTimer programTimer = new ProgramTimer();
                Thread t = new Thread(programTimer);
                t.start();
                txtNotTracking.setText("Currently tracking...");
            }
        });
        btnNewButton.setFont(new Font("Verdana", Font.BOLD, 11));
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            }
        });

        txtNotTracking = new JTextField();
        txtNotTracking.setText("Currently not tracking.");
        panel.add(txtNotTracking, "cell 4 1,growx");
        txtNotTracking.setColumns(10);
        panel.add(btnNewButton, "cell 0 2");

        JButton btnNewButton_1 = new JButton("Stop Timer");
        btnNewButton_1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                ProgramTimer.stop();
                txtNotTracking.setText("Currently not tracking.");
            }
        });
        btnNewButton_1.setFont(new Font("Verdana", Font.BOLD, 11));
        panel.add(btnNewButton_1, "cell 0 3");

        JButton btnNewButton_2 = new JButton("Open Output");
        btnNewButton_2.setFont(new Font("Verdana", Font.BOLD, 11));
        panel.add(btnNewButton_2, "cell 0 4");

        JButton btnNewButton_3 = new JButton("Explore Data");
        btnNewButton_3.setFont(new Font("Verdana", Font.BOLD, 11));
        panel.add(btnNewButton_3, "cell 0 5");

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(Color.PINK);
        tabbedPane.addTab("Pomodoro Timer", null, panel_1, null);

        JPanel panel_2 = new JPanel();
        panel_2.setToolTipText("");
        panel_2.setBackground(Color.GRAY);
        tabbedPane.addTab("Break Stopper", null, panel_2, null);
        panel_2.setLayout(new MigLayout("", "[]", "[343.00]"));
        frame.setVisible(true);

    }

    /**
     * Method to initialize all the components onto the frame.
     */
    public void initializeComponents() {
    }

}
