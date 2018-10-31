package gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import core.CloseToSystemTray;
import core.DataHandling;
import core.ProgramTimer;
import core.SingletonTimer;
import core.TimeConvert;
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
    public static boolean toTrack = false;
    private static Preferences prefs =
        Preferences.userRoot().node("Main");
    private final static String VALIDATE_STRINGS = "validate";
    public static JFrame frame;
    JButton stopTimerBtn;
    public static Main gui;
    public static JLabel lblCurrentlyNotTracking =
        new JLabel("Currently not tracking.");
    static JPanel mainPanel = new JPanel();
    private static Point windowLoc;

    // Table fields
    private static DefaultTableModel model;
    static JTable table;
    private static Set<String> keys;
    private static JScrollPane sc;
    private static JLabel secretLabel;

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
        frame.setSize(666, 554);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setTitle("ProductivityPlus");
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // ************** Menu Bar ************** //

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmItem = new JMenuItem("Open output folder");
        mntmItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    Desktop.getDesktop().open(new File("./output"));

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mnFile.add(mntmItem);

        JMenuItem mntmItem_1 = new JMenuItem("Open saved data folder");
        mntmItem_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    Desktop.getDesktop().open(new File("./saved_data"));

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        mnFile.add(mntmItem_1);

        JMenu mnEdit = new JMenu("Edit");
        menuBar.add(mnEdit);

        JMenuItem mnPreferences = new JMenuItem("Preferences");
        mnEdit.add(mnPreferences);

        JMenuItem consolidation = new JMenuItem("Program Consolidation");
        consolidation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                ConsolidationGui.newWindow();
            }
        });
        mnEdit.add(consolidation);
        mnPreferences.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // This statement prohibits edits
                // to the preferences while the program
                // is currently tracking.
                if (ProgramTimer.trackIfTrue) {
                    JOptionPane.showMessageDialog(null,
                        "Cannot edit preferences while program tracker is running.");
                }
                else {
                    PreferencesGui.newWindow();
                }
            }
        });

        JMenu mnHelp = new JMenu("Help");
        menuBar.add(mnHelp);

        JMenuItem mnAbout = new JMenuItem("About");
        mnAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                AboutGui.newWindow();
            }
        });
        mnHelp.add(mnAbout);

        JMenuItem mntmFaq = new JMenuItem("FAQ");
        mntmFaq.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                FaqGui.newWindow();
            }
        });
        mnHelp.add(mntmFaq);

        // ************** Panel creations ************** //

        frame.getContentPane()
            .setLayout(new MigLayout("", "[678.00px]", "[482.00px]"));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(
            new MigLayout("", "[110.00][224.00,grow]", "[35.00][414.00,grow]"));

        // ************** Tab pane ************** //

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane, "cell 0 0,growx,aligny top");
        tabbedPane.addTab("Program Timer", null, mainPanel, null);

        JPanel trackPanel = new JPanel();
        tabbedPane.addTab("What to track", null, trackPanel, null);

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
        buttonPanel.setLayout(new MigLayout("", "[100,grow]",
            "[25][][][][][][][][38.00,grow][][][][][]"));

        // ************** Start button ************** //

        JButton startTimerBtn = new JButton("Start Timer");
        buttonPanel.add(startTimerBtn, "cell 0 0,growx");
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
        buttonPanel.add(stopTimerBtn, "cell 0 1,growx");
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
                updateTable(false);

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
        buttonPanel.add(graphOutputBtn, "cell 0 2,growx");
        graphOutputBtn.setFont(new Font("Verdana", Font.PLAIN, 11));

        // ************** Explore data button ************** //

        JButton exploreDataBtn = new JButton("Explore Data");
        exploreDataBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                ExploreDataGui.newWindow();
            }
        });
        buttonPanel.add(exploreDataBtn, "cell 0 3,growx");
        exploreDataBtn.setFont(new Font("Verdana", Font.PLAIN, 11));

        JCheckBox chckbxConsolidatePrograms =
            new JCheckBox("Consolidate Programs");
        chckbxConsolidatePrograms.setSelected(getChecked());
        chckbxConsolidatePrograms.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                setChecked();
            }
        });
        buttonPanel.add(chckbxConsolidatePrograms, "cell 0 5,growx");

        secretLabel = new JLabel("");
        buttonPanel.add(secretLabel, "cell 0 7,alignx center");

        JButton btnRefreshTable = new JButton("Refresh Table");
        btnRefreshTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                updateTable(false);
                secretLabel.setText("  ");
                secretLabel.setText("");

            }
        });
        btnRefreshTable.setFont(new Font("Verdana", Font.PLAIN, 11));
        buttonPanel.add(btnRefreshTable, "cell 0 4,growx");

        JLabel lblLoadTable = new JLabel("Load Table");
        lblLoadTable.setFont(new Font("Tahoma", Font.BOLD, 12));
        buttonPanel.add(lblLoadTable, "cell 0 8,alignx center,aligny bottom");

        JLabel lblStartDate = new JLabel("Start Date");
        buttonPanel.add(lblStartDate, "cell 0 9");

        DatePicker datePicker = new DatePicker((DatePickerSettings) null);
        buttonPanel.add(datePicker, "cell 0 10,grow");

        JLabel lblEndDate = new JLabel("End Date");
        lblEndDate
            .setToolTipText("Leave end date blank if only viewing one day");
        buttonPanel.add(lblEndDate, "cell 0 11");

        DatePicker datePicker2 = new DatePicker((DatePickerSettings) null);
        buttonPanel.add(datePicker2, "cell 0 12,grow");

        JButton btnLoadData = new JButton("Load Data");
        btnLoadData.addMouseListener(new MouseAdapter() {
            @SuppressWarnings("unchecked")
            @Override
            public void mouseClicked(MouseEvent arg0) {

                LocalDate date = datePicker.getDate();
                LocalDate date2 = datePicker2.getDate();

                if (date == null) {
                    JOptionPane.showMessageDialog(null,
                        "Please enter a start date.");
                }
                else {
                    String formattedString;
                    String formattedString2;
                    @SuppressWarnings("unused")
                    SimpleDateFormat dateFormatter =
                        new SimpleDateFormat("Dyy");
                    DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("Dyy");
                    formattedString = date.format(formatter);
                    if (date2 == null) {

                        if (getChecked()) {
                            try {

                                DataHandling.validateData(
                                    DataHandling
                                        .acceptDateTable(formattedString));
                                loadTable(DataHandling.validateData(
                                    DataHandling
                                        .acceptDateTable(formattedString)));
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        else {
                            try {
                                DataHandling.acceptDateTable(formattedString);
                                loadTable(DataHandling
                                    .acceptDateTable(formattedString));
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (date2 != null) {

                        formattedString2 = date2.format(formatter);

                        List<String> dates = DataHandling
                            .dateDiff(formattedString, formattedString2);
                        @SuppressWarnings("rawtypes")
                        List<Map> maps = DataHandling.loadMaps(dates);

                        Map<String, Long> combinedMaps = new HashMap<>();

                        int i;
                        if (maps.size() != 0) {
                            i = 100 / maps.size();
                        }
                        i = 1;

                        for (Map<String, Long> map : maps) {
                            for (Map.Entry<String, Long> entry : map
                                .entrySet()) {
                                String key = entry.getKey();
                                Long current = combinedMaps.get(key);
                                combinedMaps.put(key,
                                    current == null ? entry.getValue()
                                        : entry.getValue() + current);
                            }
                            ExploreDataGui.updateBar(i);
                            i = 2 * i;
                        }

                        if (getChecked()) {
                            loadTable(DataHandling.validateData(combinedMaps));
                        }
                        else {
                            loadTable(combinedMaps);
                        }
                    }
                }
            }

        });

        btnLoadData.setFont(new Font("Verdana", Font.PLAIN, 11));
        buttonPanel.add(btnLoadData, "cell 0 13,growx");

        // ************** Table ************** //

        updateTable(true);
        updateTable(false);
        secretLabel.setText("  ");
        secretLabel.setText("");

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
     * This method updates the table found in the main window of the gui. It
     * works by destroying the current table object and creates a new one.
     */
    @SuppressWarnings({"rawtypes", "unchecked", "serial"})
    public static void updateTable(boolean fresh) {
        // Remove old table object
        if (!fresh) {
            mainPanel.remove(sc);
            mainPanel.remove(table);
        }

        Map<String, Long> loadedCurrentMap;
        if (getChecked()) {
            loadedCurrentMap = DataHandling.validateData(ProgramTimer.appMap);
        }
        else {
            loadedCurrentMap = ProgramTimer.appMap;
        }

        // All the keys we need are loaded from the map
        setKeys(loadedCurrentMap.keySet());

        String columns[] = {"Program", TimeConvert.getUnit()};

        if (loadedCurrentMap.size() == 0) {
            model = new DefaultTableModel(1, 2);
            String[] colHeadings = {"Program", "Time"};
            model.setColumnIdentifiers(colHeadings);
        }
        else {
            model = new DefaultTableModel(
                getRows((TimeConvert.convertTime(loadedCurrentMap))), columns) {
                @Override
                public Class getColumnClass(int column) {
                    Class returnValue;
                    if ((column >= 0) && (column < getColumnCount())) {
                        returnValue = getValueAt(0, column).getClass();
                    }
                    else {
                        returnValue = Object.class;
                    }
                    return returnValue;
                }
            };
        }
        table = new JTable(model);
        if (PreferencesGui.getDisplayIndex() == 3) {
            model = new DefaultTableModel();
            table = new JTable(model);
            model.addColumn("Program");
            setKeys(loadedCurrentMap.keySet());
            for (String name : loadedCurrentMap.keySet()) {
                String key = name.toString();
                model.addRow(new Object[] {key});
            }
            Map<String, String> finalMap2 =
                TimeConvert.convertWritten(loadedCurrentMap);

            // Append a new column with copied data
            model.addColumn(TimeConvert.getUnit(),
                finalMap2.values().toArray());
        }
        else {

            RowSorter<TableModel> sorter =
                new TableRowSorter<TableModel>(model);
            table.setRowSorter(sorter);

        }
        DefaultTableCellRenderer renderer =
            (DefaultTableCellRenderer) table.getDefaultRenderer(Double.class);
        renderer.setHorizontalAlignment(JLabel.LEFT);

        sc = new JScrollPane(table);
        mainPanel.add(sc);
        secretLabel.setText("  ");
        secretLabel.setText("");
    }

    public static Object[][] getRows(Map<String, Double> finalMap) {
        Object[][] rows = new Object[finalMap.size()][2];
        Set<Entry<String, Double>> entries = finalMap.entrySet();
        Iterator<Entry<String, Double>> entriesIterator = entries.iterator();
        int i = 0;
        while (entriesIterator.hasNext()) {
            Entry<String, Double> mapping = entriesIterator.next();
            rows[i][0] = mapping.getKey();
            rows[i][1] = mapping.getValue();
            i++;
        }
        return rows;
    }

    /**
     * Getter for keys set.
     * 
     * @return returns keys for appMap
     */
    public static Set<String> getKeys() {
        return keys;
    }

    /**
     * Setter for keys set.
     * 
     * @param keys
     *            Set<String>
     */
    public static void setKeys(Set<String> keys) {
        Main.keys = keys;
    }

    /**
     * Setter to set the frame location variables to be used to create new
     * windows that match the position of the main gui.
     */
    public static void setWindowLoc() {
        windowLoc = frame.getLocation();
    }

    /**
     * Getter used to return the Point of the gui (x and y).
     * 
     * @return Point of main gui.
     */
    public static Point getWindowLoc() {
        return windowLoc;
    }

    /**
     * This method updates the table found in the main window of the gui. It
     * works by destroying the current table object and creates a new one.
     */
    @SuppressWarnings({"rawtypes", "unchecked", "serial"})
    public static void loadTable(Map<String, Long> loadedMap) {

        if (loadedMap.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "Warning: Loaded time table was empty");
        }

        // Remove old table object
        mainPanel.remove(sc);
        mainPanel.remove(table);

        // All the keys we need are loaded from the map
        setKeys(loadedMap.keySet());

        String columns[] = {"Program", TimeConvert.getUnit()};

        if (loadedMap.size() == 0) {
            model = new DefaultTableModel(1, 2);
            String[] colHeadings = {"Program", "Time"};
            model.setColumnIdentifiers(colHeadings);
        }
        else {

            model = new DefaultTableModel(
                getRows((TimeConvert.convertTime(loadedMap))), columns) {
                @Override
                public Class getColumnClass(int column) {
                    Class returnValue;
                    if ((column >= 0) && (column < getColumnCount())) {
                        returnValue = getValueAt(0, column).getClass();
                    }
                    else {
                        returnValue = Object.class;
                    }
                    return returnValue;
                }
            };
        }
        table = new JTable(model);

        if (PreferencesGui.getDisplayIndex() == 3) {
            model = new DefaultTableModel();
            table = new JTable(model);
            model.addColumn("Program");
            setKeys(loadedMap.keySet());
            for (String name : loadedMap.keySet()) {
                String key = name.toString();
                model.addRow(new Object[] {key});
            }
            Map<String, String> finalMap2 =
                TimeConvert.convertWritten(loadedMap);

            // Append a new column with copied data
            model.addColumn(TimeConvert.getUnit(),
                finalMap2.values().toArray());
        }
        else {

            RowSorter<TableModel> sorter =
                new TableRowSorter<TableModel>(model);
            table.setRowSorter(sorter);

        }
        DefaultTableCellRenderer renderer =
            (DefaultTableCellRenderer) table.getDefaultRenderer(Double.class);
        renderer.setHorizontalAlignment(JLabel.LEFT);

        sc = new JScrollPane(table);
        mainPanel.add(sc);
        secretLabel.setText("  ");
        secretLabel.setText("");

    }

    /**
     * Set the Idle check box from its saved preference, if never saved then set
     * to false by default.
     */
    public void setChecked() {
        if (getChecked()) {
            prefs.putBoolean(VALIDATE_STRINGS, false);
        }
        else {
            prefs.putBoolean(VALIDATE_STRINGS, true);
        }
    }

    /**
     * Returns the status of the Idle check box, if the box does not have a save
     * preference it returns false by default.
     * 
     * @return boolean, false is no preference saved.
     */
    public static boolean getChecked() {
        return prefs.getBoolean(VALIDATE_STRINGS, false);
    }
}
