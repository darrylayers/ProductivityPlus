package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import core.CloseToSystemTray;
import core.DataHandling;
import core.ProgramTimer;
import core.SingletonTimer;
import net.miginfocom.swing.MigLayout;

/**
 * Main class, currently holding all the GUI code.
 * 
 * @author Austin Ayers
 * @version 9/11/18
 */

public class Main {

    public static final int FRAME_SIZE = 300;
    public static JFrame frame;
    JButton btnNewButton_1;
    public static Main gui;
    public static JLabel lblCurrentlyNotTracking =
        new JLabel("Currently not tracking");
    static JPanel panel = new JPanel();
    // Table fields
    private static DefaultTableModel model;
    private static JTable table;
    private static Vector data;
    private static Vector row;
    private static List colData;
    private static Set<String> keys;
    private static JScrollPane sc;

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

        try {
            DataHandling.loadMap();
        }
        catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }

        frame = new JFrame();
        frame.setLocation(100, 100);
        frame.setSize(533, 381);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setTitle("ProductivityPlus");
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

        JMenuItem mntmItem_3 = new JMenuItem("Preferences");
        mntmItem_3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                PreferencesGui.newWindow();
            }
        });
        mnHelp.add(mntmItem_3);

        JMenuItem mntmAbout = new JMenuItem("About");
        mntmAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                AboutGui.newWindow();
            }
        });
        mnHelp.add(mntmAbout);
        frame.getContentPane()
            .setLayout(new MigLayout("", "[531.00px]", "[337.00px]"));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane, "cell 0 0,growx,aligny top");

        panel.setBackground(Color.WHITE);
        panel.setToolTipText("");
        tabbedPane.addTab("Program Timer", null, panel, null);
        panel.setLayout(
            new MigLayout("", "[110.00][224.00,grow]", "[35.00][200.00,grow]"));

        JLabel lblTimerControls = new JLabel("     Timer Controls");
        lblTimerControls.setFont(new Font("Verdana", Font.BOLD, 12));
        panel.add(lblTimerControls, "cell 0 0,alignx left");

        lblCurrentlyNotTracking.setFont(new Font("Verdana", Font.BOLD, 12));
        panel.add(lblCurrentlyNotTracking, "cell 1 0");

        JPanel panel_3 = new JPanel();
        panel_3.setToolTipText("Program data for today");
        panel_3.setBackground(Color.WHITE);
        panel.add(panel_3, "cell 0 1,grow");
        panel_3.setLayout(new MigLayout("", "[100]", "[25][][][]"));

        JButton btnNewButton = new JButton("Start Timer");
        panel_3.add(btnNewButton, "cell 0 0");
        btnNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                startTimer();
                lblCurrentlyNotTracking.setText("Currently tracking...");
            }
        });
        btnNewButton.setFont(new Font("Verdana", Font.PLAIN, 11));

        btnNewButton_1 = new JButton("Stop Timer");
        panel_3.add(btnNewButton_1, "cell 0 1");
        btnNewButton_1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                SingletonTimer.setBeenCalled();
                ProgramTimer.stop();
                setStopLabel();
                try {
                    Thread.sleep(50);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateTable();

            }
        });
        btnNewButton_1.setFont(new Font("Verdana", Font.PLAIN, 11));

        JButton btnNewButton_2 = new JButton("Graphical Output");
        panel_3.add(btnNewButton_2, "cell 0 2");
        btnNewButton_2.setFont(new Font("Verdana", Font.PLAIN, 11));

        JButton btnNewButton_3 = new JButton("Explore Data");
        btnNewButton_3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                ExploreDataGui.newWindow();
            }
        });
        panel_3.add(btnNewButton_3, "cell 0 3");
        btnNewButton_3.setFont(new Font("Verdana", Font.PLAIN, 11));

        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            }
        });

        // ************** Table ************** //
        keys = ProgramTimer.appMap.keySet();
        try {
            model = new DefaultTableModel();
            table = new JTable(model);
            model.addColumn("Program");

            for (String name : ProgramTimer.appMap.keySet()) {
                String key = name.toString();
                model.addRow(new Object[] {key});
            }

            data = model.getDataVector();
            row = (Vector) data.elementAt(1);

            int mColIndex = 0;
            colData = new ArrayList(table.getRowCount());
            for (int i = 0; i < table.getRowCount(); i++) {
                row = (Vector) data.elementAt(i);
                colData.add(row.get(mColIndex));
            }

            // Append a new column with copied data
            model.addColumn("",
                ProgramTimer.appMap.values().toArray());

            sc = new JScrollPane(table);

            panel.add(sc);
        }
        catch (ArrayIndexOutOfBoundsException exception) {
            model = new DefaultTableModel(2, 2);
            String[] colHeadings = {"Program", "Time (seconds)"};
            model.setColumnIdentifiers(colHeadings);
            table = new JTable(model);
            sc = new JScrollPane(table);
            panel.add(sc);
        }

        // ************** Table ************** //

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(Color.PINK);
        tabbedPane.addTab("Pomodoro Timer", null, panel_1, null);

        JPanel panel_2 = new JPanel();
        panel_2.setToolTipText("");
        panel_2.setBackground(Color.GRAY);
        tabbedPane.addTab("Break Stopper", null, panel_2, null);
        panel_2.setLayout(new MigLayout("", "[]", "[343.00]"));
        frame.setVisible(true);

        CloseToSystemTray tray = new CloseToSystemTray();
        try {
            tray.startTray();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Set the status label to the 'not tracking' state.
     */
    public static void setStopLabel() {
        lblCurrentlyNotTracking.setText("Currently not tracking.");
    }

    /**
     * Set the status label to the 'tracking' state.
     */
    public static void setStartLabel() {
        lblCurrentlyNotTracking.setText("Currently tracking...");
    }

    /**
     * This method stops the program and changes the status label to being
     * stopped.
     * 
     * @throws IOException
     */
    public static void simulateClick() {
        SingletonTimer.setBeenCalled();
        ProgramTimer.stop();
        setStopLabel();

    }

    /**
     * Start the program timer. This method creates a new thread from a
     * ProgramTimer object and starts tracking. We must use a new thread to not
     * 'hang' the GUI.
     */
    public static void startTimer() {

        SingletonTimer.getInstance().callTimer();
    }

    /**
     * Set the frame visible.
     */
    public static void setFrameVisible() {
        frame.setVisible(true);
        frame.toFront();
    }

    public static void updateTable() {
        panel.remove(sc);
        keys = ProgramTimer.appMap.keySet();
        System.out.println(ProgramTimer.appMap);

        model = new DefaultTableModel();
        table = new JTable(model);
        model.addColumn("Program");

        for (String name : ProgramTimer.appMap.keySet()) {
            String key = name.toString();
            model.addRow(new Object[] {key});
        }

        data = model.getDataVector();
        System.out.println(data);
        row = (Vector) data.elementAt(0);

        int mColIndex = 0;
        colData = new ArrayList(table.getRowCount());
        for (int i = 0; i < table.getRowCount(); i++) {
            row = (Vector) data.elementAt(i);
            colData.add(row.get(mColIndex));
        }

        // Append a new column with copied data
        model.addColumn("Time (seconds)",
            ProgramTimer.appMap.values().toArray());

        sc = new JScrollPane(table);

        panel.add(sc);

    }
}
