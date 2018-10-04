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

	// Gui fields
    public static final int FRAME_SIZE = 300;
    public static JFrame frame;
    JButton stopTimerBtn;
    public static Main gui;
    public static JLabel lblCurrentlyNotTracking =
        new JLabel("Currently not tracking.");
    static JPanel mainPanel = new JPanel();
    
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

    	// Load the hashmap info ProgramTimer.appMap
        try {
            DataHandling.loadMap();
        }
        catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }

        // Frame creation
        
        frame = new JFrame();
        frame.setLocation(100, 100);
        frame.setSize(533, 381);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setTitle("ProductivityPlus");
        frame.setResizable(false);
        frame.setVisible(true);

        // ************** Menu Bar ************** //
        
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

        JMenuItem mnPreferences = new JMenuItem("Preferences");
        mnPreferences.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                PreferencesGui.newWindow();
            }
        });
        mnHelp.add(mnPreferences);

        JMenuItem mnAbout = new JMenuItem("About");
        mnAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                AboutGui.newWindow();
            }
        });
        mnHelp.add(mnAbout);
        
        // ************** Panel creations ************** //
        
        frame.getContentPane()
            .setLayout(new MigLayout("", "[531.00px]", "[337.00px]"));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(
            new MigLayout("", "[110.00][224.00,grow]", "[35.00][200.00,grow]"));
        
        // ************** Tab pane ************** //
        
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane, "cell 0 0,growx,aligny top");
        tabbedPane.addTab("Program Timer", null, mainPanel, null);


        // ************** Timer controls labels ************** //
        
        JLabel lblTimerControls = new JLabel("     Timer Controls");
        lblTimerControls.setFont(new Font("Verdana", Font.BOLD, 12));
        mainPanel.add(lblTimerControls, "cell 0 0,alignx left");
        lblCurrentlyNotTracking.setFont(new Font("Verdana", Font.BOLD, 12));
        mainPanel.add(lblCurrentlyNotTracking, "cell 1 0");

        // ************** Button panel ************** //
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setToolTipText("Program data for today");
        buttonPanel.setBackground(Color.WHITE);
        mainPanel.add(buttonPanel, "cell 0 1,grow");
        buttonPanel.setLayout(new MigLayout("", "[100]", "[25][][][]"));

        // ************** Start button ************** //
        
        JButton startTimerBtn = new JButton("Start Timer");
        buttonPanel.add(startTimerBtn, "cell 0 0");
        startTimerBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                startTimer();
                lblCurrentlyNotTracking.setText("Currently tracking...");
            }
        });
        startTimerBtn.setFont(new Font("Verdana", Font.PLAIN, 11));

        // ************** Stop button ************** //
       
        stopTimerBtn = new JButton("Stop Timer");
        buttonPanel.add(stopTimerBtn, "cell 0 1");
        stopTimerBtn.addMouseListener(new MouseAdapter() {
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
        stopTimerBtn.setFont(new Font("Verdana", Font.PLAIN, 11));
        
        // ************** Graphical output button ************** //

        JButton graphOutputBtn = new JButton("Graphical Output");
        graphOutputBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                GraphicalOutputGui.newWindow();
            }
        });
        buttonPanel.add(graphOutputBtn, "cell 0 2");
        graphOutputBtn.setFont(new Font("Verdana", Font.PLAIN, 11));

        // ************** Explore data button ************** //
        JButton exploreDataBtn = new JButton("Explore Data");
        exploreDataBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                ExploreDataGui.newWindow();
            }
        });
        buttonPanel.add(exploreDataBtn, "cell 0 3");
        exploreDataBtn.setFont(new Font("Verdana", Font.PLAIN, 11));



        // ************** Table ************** //
        
        // All the keys we need are loaded from the map
        setKeys(ProgramTimer.appMap.keySet());
        try {
     
            model = new DefaultTableModel();
            table = new JTable(model);
            model.addColumn("Program");

            // Iterate through the entire map printing
            // the keys (program names) to the table left
            // program name coloumn.
            for (String name : ProgramTimer.appMap.keySet()) {
                String key = name.toString();
                model.addRow(new Object[] {key});
            }

            data = model.getDataVector();
            row = (Vector) data.elementAt(0);

            // Load all the program times to the table
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

            mainPanel.add(sc);
        }
        catch (ArrayIndexOutOfBoundsException exception) {
            model = new DefaultTableModel(1, 2);
            String[] colHeadings = {"Program", "Time (seconds)"};
            model.setColumnIdentifiers(colHeadings);
            table = new JTable(model);
            sc = new JScrollPane(table);
            mainPanel.add(sc);
        }
        
        // ************** Pomodoro Timer ************** //
        JPanel pomodoroPanel= new JPanel();
        pomodoroPanel.setBackground(Color.PINK);
        tabbedPane.addTab("Pomodoro Timer", null, pomodoroPanel, null);

        // ************** Break Stopper ************** //
        JPanel breakPanel = new JPanel();
        breakPanel.setBackground(Color.GRAY);
        tabbedPane.addTab("Break Stopper", null, breakPanel, null);
        breakPanel.setLayout(new MigLayout("", "[]", "[343.00]"));
        
        // ************** Close to Tray ************** //
        CloseToSystemTray tray = new CloseToSystemTray();
        try {
            tray.startTray();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Set the frame visible.
     */
    public static void setFrameVisible() {
        frame.setVisible(true);
        frame.toFront();
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
     * This method updates the table found in the main
     * window of the gui. It works by destroying the current table
     * object and creates a new one.
     */
    public static void updateTable() {
    	
    	// Remove old table object
    	mainPanel.remove(sc);
        // All the keys we need are loaded from the map
        setKeys(ProgramTimer.appMap.keySet());

        model = new DefaultTableModel();
        table = new JTable(model);
        model.addColumn("Program");

        // Iterate through the entire map printing
        // the keys (program names) to the table left
        // program name coloumn.
        for (String name : ProgramTimer.appMap.keySet()) {
            String key = name.toString();
            model.addRow(new Object[] {key});
        }

       
        data = model.getDataVector();
        row = (Vector) data.elementAt(0);

        // Load all the program times to the table
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
        mainPanel.add(sc);
    }

    /**
     * Getter for keys set.
     * @return returns keys for appMap
     */
	public static Set<String> getKeys() {
		return keys;
	}

	/**
	 * Setter for keys set.
	 * @param keys Set<String> 
	 */
	public static void setKeys(Set<String> keys) {
		Main.keys = keys;
	}
}
