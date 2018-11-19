package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;

import core.CloseToSystemTray;
import core.DataHandling;
import core.ProgramTimer;
import core.SingletonTimer;
import core.TableHelper;
import core.TimeConvert;
import net.miginfocom.swing.MigLayout;

/**
 * Main class, currently holding all the GUI code.
 * 
 * @author Austin Ayers
 */

public class Main {

    private final static String VALIDATE_STRINGS = "validate";
    private final static String DISPLAY_MODE = "mode";
    private final static String FIRST_CONSOLIDATE = "firstConsolidate";

    private static Preferences prefs = Preferences.userRoot().node("Main");
    private static JFrame frame;
    private static JLabel trackStatusLabel =
        new JLabel("Status: Currently not tracking.");
    private static JPanel mainPanel = new JPanel();
    private static Point windowLoc;
    private static JScrollPane inclusionScrollPane =
        new JScrollPane((Component) null);
    private static JScrollPane exclusionScrollPane =
        new JScrollPane((Component) null);
    private static DefaultTableModel model;
    private static JTable table;
    private static JScrollPane sc;
    private static JPanel displayPanel;

    public static JLabel secretLabel;
    public static JCheckBox chckbxConsolidatePrograms;

    private JTextField progTextField;
    private JButton btnStopTimer;
    private JRadioButton trackAllRButton = new JRadioButton("Track All");
    private JRadioButton trackInclusionsRButton =
        new JRadioButton("Track Inclusions");
    private JRadioButton trackExclusionsRButton =
        new JRadioButton("Track Exclusions");
    private boolean firstLaunch = prefs.getBoolean(FIRST_CONSOLIDATE, true);

    public static boolean toTrack = false;
    public static Map<String, Double> globalMap =
        new HashMap<String, Double>();

    /**
     * Main method that builds the GUI.
     */
    public static void main(String[] args) {
        new Main();
    }

    /**
     * Constructor that builds the frame.
     */
    private Main() {

        // Verify folders exists, if not create
        File f = new File("./output/");
        if (!f.exists()) {
            new File("./output").mkdirs();
        }
        f = new File("./saved_data/");
        if (!f.exists()) {
            new File("./saved_data").mkdirs();
        }

        // Load the hashmap info ProgramTimer.appMap
        try {
            DataHandling.loadMap();
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }

        // Frame creation
        frame = new JFrame();
        frame.setLocation(100, 100);
        frame.setSize(986, 554);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setTitle("ProductivityPlus");
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        // Set the frame icon
        frame.setIconImage(Toolkit.getDefaultToolkit()
            .getImage(getClass().getResource("/icon.png")));

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        // File button
        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        // Output folder button
        JMenuItem outputFolderItem = new JMenuItem("Open output folder");
        outputFolderItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    // Open the output folder, or make one.
                    Desktop.getDesktop().open(new File("./output"));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mnFile.add(outputFolderItem);

        // Saved data button
        JMenuItem savedDataItem = new JMenuItem("Open saved data folder");
        savedDataItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    // Open saved data folder or make one
                    Desktop.getDesktop().open(new File("./saved_data"));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mnFile.add(savedDataItem);

        // Edit menu button
        JMenu mnEdit = new JMenu("Edit");
        menuBar.add(mnEdit);

        // Preferences menu button
        JMenuItem preferencesItem = new JMenuItem("Preferences");
        mnEdit.add(preferencesItem);

        // Consolidate Programs menu button
        JMenuItem consolidationItem = new JMenuItem("Program Consolidation");
        consolidationItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // Create new Program Consolidation window
                ConsolidationGui.newWindow();
            }
        });
        mnEdit.add(consolidationItem);

        // Preferences menu listener
        preferencesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // This statement prohibits edits
                // to the preferences while the program
                // is currently tracking.
                if (ProgramTimer.trackIfTrue) {
                    JOptionPane.showMessageDialog(null,
                        "Cannot edit preferences while program tracker is running.");
                } else {
                    // Launch new preferences window
                    PreferencesGui.newWindow();
                }
            }
        });

        // Help menu button
        JMenu mnHelp = new JMenu("Help");
        menuBar.add(mnHelp);

        // About menu button
        JMenuItem aboutItem = new JMenuItem("About");
        // About menu listener
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // Create new about window
                AboutGui.newWindow();
            }
        });
        mnHelp.add(aboutItem);

        // Panel creations
        frame.getContentPane()
            .setLayout(new MigLayout("", "[1061.00px]", "[482.00px]"));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(
            new MigLayout("", "[110.00][224.00,grow]", "[35.00][414.00,grow]"));

        // Tab pane
        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
        frame.getContentPane().add(tabbedPane, "cell 0 0,growx,aligny top");
        tabbedPane.addTab("Program Timer", null, mainPanel, null);

        // What to display panel
        displayPanel = new JPanel();
        displayPanel.setToolTipText("");
        tabbedPane.addTab("What to Display", null, displayPanel, null);
        displayPanel.setLayout(new MigLayout("",
            "[310.00px,grow][450,grow][501.00,grow]", "[75][457.00px,grow]"));

        // Control panel
        JPanel controlPanel = new JPanel();
        displayPanel.add(controlPanel, "cell 0 1,grow");
        controlPanel.setLayout(new MigLayout("", "[132.00,grow]",
            "[][][][][][][][][][][][][][][]"));

        // Controls label
        JLabel controlsLabel = new JLabel("Controls");
        displayPanel.add(controlsLabel, "cell 0 0,alignx center,aligny center");

        // Inclusions label
        JLabel inclusionsLabel = new JLabel("Inclusions");
        inclusionsLabel.setToolTipText(
            "These are the ONLY programs that will be tracked if the \"Track Inclusions\" button is selected. This is useful if you only want to track a few programs.");
        displayPanel.add(inclusionsLabel,
            "cell 1 0,alignx center,aligny center");

        // Exclusions label
        JLabel exclusionsLabel = new JLabel("Exclusions");
        exclusionsLabel.setToolTipText(
            "These are the programs that will be excluded from tracking if the \"Track Exclusions\" button is selected. This is useful if you want to avoid tracking only a few programs.");
        displayPanel.add(exclusionsLabel,
            "cell 2 0,alignx center,aligny center");

        // Mode label
        JLabel modeLabel = new JLabel("Mode - What to Display");
        controlPanel.add(modeLabel, "cell 0 0,alignx center,aligny center");

        // Program entry text box
        progTextField = new JTextField();
        progTextField.setToolTipText("Put the program name here");
        controlPanel.add(progTextField, "cell 0 5,growx");
        progTextField.setColumns(10);

        // Add porogram button
        JButton btnAddProgram = new JButton("Add Program");
        btnAddProgram.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {

                // If the input is empty, alert user
                if (DataHandling.checkEmpty(progTextField.getText())) {
                    JOptionPane.showMessageDialog(null,
                        "Input string empty.");
                } else {
                    // If display all is selected, alert user
                    if (getMode() == 1) {
                        JOptionPane.showMessageDialog(null,
                            "Cannot add program. Display All is selected.");
                    }
                    // Filter results
                    else if (getMode() == 2 || getMode() == 3) {
                        updateLists();
                    }
                    // Used to update the UI
                    secretLabel.setText("  ");
                    secretLabel.setText("");
                }
            }
        });
        // Add button tool tip
        btnAddProgram.setToolTipText(
            "Add a program from whichever table is selected above.");
        controlPanel.add(btnAddProgram, "cell 0 6,grow");

        // Remove program button
        JButton btnRemoveProgram = new JButton("Remove Program");
        btnRemoveProgram.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // If the input is empty, alert user
                if (DataHandling.checkEmpty(progTextField.getText())) {
                    JOptionPane.showMessageDialog(null,
                        "Input string empty.");
                } else {
                    // Alert user if on Display All mode
                    if (getMode() == 1) {
                        JOptionPane.showMessageDialog(null,
                            "Cannot remove program. Track All is selected.");
                    }
                    // Filter results
                    else if (getMode() == 2 || getMode() == 3) {
                        updateLists();
                    }
                    // Refresh UI
                    secretLabel.setText("  ");
                    secretLabel.setText("");
                }
            }
        });

        // Remove button tool tip
        btnRemoveProgram.setToolTipText(
            "Remove a program from whichever table is selected above.");
        controlPanel.add(btnRemoveProgram, "cell 0 7,grow");

        JLabel secret_label = new JLabel("");
        controlPanel.add(secret_label, "cell 0 14");

        // Clear button
        JButton btnClearList = new JButton("Clear List");
        btnClearList.setToolTipText("Clear the selected list of all entries.");
        btnClearList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                TableHelper.clearList();
                displayPanel.remove(inclusionScrollPane);
                displayPanel.remove(exclusionScrollPane);
                refreshDisplayTable(displayPanel);
                secret_label.setText("  ");
                secret_label.setText("");
            }
        });
        controlPanel.add(btnClearList, "cell 0 8,grow");

        // Radio buttons
        trackAllRButton
            .setToolTipText("All programs will be tracked if this is chosen.");
        controlPanel.add(trackAllRButton, "cell 0 1");

        trackInclusionsRButton.setToolTipText(
            "Only the programs inside the Inclusions table will be tracked. This is useful if you only want to track a few programs.");
        controlPanel.add(trackInclusionsRButton, "cell 0 2");

        trackExclusionsRButton.setToolTipText(
            "Everything will be tracked except the programs in the Exclusions table if this is selected. This is useful if you want to avoid tracking only a few programs.");
        controlPanel.add(trackExclusionsRButton, "cell 0 3");

        ButtonGroup rbuttons = new ButtonGroup();
        rbuttons.add(trackExclusionsRButton);
        rbuttons.add(trackAllRButton);
        rbuttons.add(trackInclusionsRButton);

        // This sets the appropriate buttons to be selected
        if (getMode() == 1) {
            trackAllRButton.setSelected(true);
        } else if (getMode() == 2) {
            trackInclusionsRButton.setSelected(true);
        } else if (getMode() == 3) {
            trackExclusionsRButton.setSelected(true);
        }

        // Track exclusion listener
        trackExclusionsRButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (trackExclusionsRButton.isSelected()) {
                    trackInclusionsRButton.setSelected(false);
                    trackAllRButton.setSelected(false);
                } else {
                    trackExclusionsRButton.setSelected(true);
                }
                setMode();
            }
        });

        // Track inclusion listener
        trackInclusionsRButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (trackInclusionsRButton.isSelected()) {
                    trackAllRButton.setSelected(false);
                    trackExclusionsRButton.setSelected(false);
                } else {
                    trackInclusionsRButton.setSelected(true);
                }
                setMode();

            }
        });

        // Track all listener
        trackAllRButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (trackAllRButton.isSelected()) {
                    trackInclusionsRButton.setSelected(false);
                    trackExclusionsRButton.setSelected(false);
                } else {
                    trackAllRButton.setSelected(true);
                }
                setMode();

            }
        });

        // Refresh the display panel
        refreshDisplayTable(displayPanel);

        // Timer controls labels
        JLabel timerControlsLabel = new JLabel("     Timer Controls");
        timerControlsLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        mainPanel.add(timerControlsLabel, "cell 0 0,alignx left");
        trackStatusLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        mainPanel.add(trackStatusLabel, "cell 1 0");

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        mainPanel.add(buttonPanel, "cell 0 1,grow");
        buttonPanel.setLayout(new MigLayout("", "[100,grow]",
            "[25][][][][][][][27.00,grow][14.00][][][][][]"));

        // Start button
        JButton btnStartTimer = new JButton("Start Timer");
        btnStartTimer.setToolTipText("Start tracking what you do.");
        buttonPanel.add(btnStartTimer, "cell 0 0,growx");
        btnStartTimer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                startTimer();
                trackStatusLabel.setText("Status: Currently tracking...");
            }
        });

        // Stop button
        btnStopTimer = new JButton("Stop Timer");
        btnStopTimer.setToolTipText("Stop tracking what you do.");
        buttonPanel.add(btnStopTimer, "cell 0 1,growx");
        btnStopTimer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                SingletonTimer.setBeenCalled();
                ProgramTimer.stop();
                setStopLabel();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateTable(false);

            }
        });

        // Load data button
        JButton btnLoadData = new JButton("Load Data / Refresh");
        btnLoadData
            .setToolTipText("Refresh the table with data recorded previously");
        buttonPanel.add(btnLoadData, "cell 0 12,growx");

        // Graphical output button
        JButton btnGraphOutput = new JButton("Graphical Output");
        btnGraphOutput.setToolTipText("View what is in the table graphically.");
        btnGraphOutput.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                GraphicalOutputGui.newWindow();
            }
        });
        buttonPanel.add(btnGraphOutput, "cell 0 2,growx");

        // Explore data button
        JButton btnExploreData = new JButton("Explore Data");
        btnExploreData.setToolTipText("Explore your saved data.");
        btnExploreData.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                ExploreDataGui.newWindow();
            }
        });
        buttonPanel.add(btnExploreData, "cell 0 3,growx");

        // Consolidate Programs checkbox
        chckbxConsolidatePrograms = new JCheckBox("Consolidate Programs");
        chckbxConsolidatePrograms.setSelected(getChecked());
        chckbxConsolidatePrograms.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {

                // Check to see if it is the
                // first time Consolidate Programs
                // has been checked, if so, display
                // how-to message.
                if (firstLaunch) {
                    JOptionPane.showMessageDialog(null,
                        "Just a heads up! To use the Consolidate Programs "
                            + "feature, you must also create a list of programs "
                            + "you would like to consolidate under the "
                            + "Edit->Program Consolidation tab!");
                    prefs.putBoolean(FIRST_CONSOLIDATE, false);
                }
                // Store the current state
                prefs.putBoolean(VALIDATE_STRINGS,
                    chckbxConsolidatePrograms.isSelected());
            }
        });
        buttonPanel.add(chckbxConsolidatePrograms, "cell 0 5,growx");

        // Load table label
        JLabel lblLoadTable = new JLabel("Load Table");
        lblLoadTable.setFont(new Font("Tahoma", Font.BOLD, 12));
        buttonPanel.add(lblLoadTable,
            "flowx,cell 0 7,alignx center,aligny bottom");

        secretLabel = new JLabel("");
        buttonPanel.add(secretLabel, "cell 0 7,alignx center");

        // Refresh table button
        JButton btnRefreshTable = new JButton("Refresh Today's Data");
        btnRefreshTable
            .setToolTipText("Refresh the table with data recorded today.");
        btnRefreshTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                updateTable(false);
                secretLabel.setText("  ");
                secretLabel.setText("");
            }
        });
        buttonPanel.add(btnRefreshTable, "cell 0 4,growx");

        // Start date label
        JLabel lblStartDate = new JLabel("Start Date");
        buttonPanel.add(lblStartDate, "cell 0 8");

        // Date pickers
        DatePicker datePicker2 = new DatePicker((DatePickerSettings) null);
        datePicker2.getComponentDateTextField().setToolTipText("Ending date.");
        buttonPanel.add(datePicker2, "cell 0 11,grow");

        DatePicker datePicker = new DatePicker((DatePickerSettings) null);
        datePicker.addDateChangeListener(new DateChangeListener() {
            @Override
            public void dateChanged(DateChangeEvent arg0) {
                datePicker2.setDate(datePicker.getDate());
            }
        });
        datePicker.getComponentDateTextField()
            .setToolTipText("Beginning date.");
        buttonPanel.add(datePicker, "cell 0 9,grow");

        // End date label
        JLabel lblEndDate = new JLabel("End Date");
        lblEndDate
            .setToolTipText("Leave end date blank if only viewing one day");
        buttonPanel.add(lblEndDate, "cell 0 10");

        // Table
        updateTable(true);
        updateTable(false);
        secretLabel.setText("  ");
        secretLabel.setText("");

        // Load data listener
        btnLoadData.addMouseListener(new MouseAdapter() {
            @SuppressWarnings("unchecked")
            @Override
            public void mouseClicked(MouseEvent arg0) {
                LocalDate date = datePicker.getDate();
                LocalDate date2 = datePicker2.getDate();

                // If date 1 is empty
                if (date == null) {
                    JOptionPane.showMessageDialog(null,
                        "Please enter a start date.");
                } else {
                    // Format date 1
                    String formattedString;
                    String formattedString2;
                    new SimpleDateFormat("Dyy");
                    DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("Dyy");
                    formattedString = date.format(formatter);
                    // If date 2 is empty
                    if (date2 == null) {
                        try {
                            // Load the table with the single date's data
                            loadTable(
                                DataHandling.acceptDateTable(formattedString));
                            globalMap = DataHandling.convertMap(
                                DataHandling.acceptDateTable(formattedString));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    // If date 2 was sued
                    if (date2 != null) {
                        // Format date 2
                        formattedString2 = date2.format(formatter);
                        // Load list of dates we need
                        List<String> dates =
                            DataHandling.dateDiff(formattedString,
                                formattedString2);
                        // Load each map we need
                        @SuppressWarnings("rawtypes")
                        List<Map> maps = DataHandling.loadMaps(dates);
                        Map<String, Long> combinedMaps = new HashMap<>();
                        int i = 100 / maps.size();
                        // Combine the maps
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
                        // Filter the programs
                        Map<String, Long> loadedCurrentMap = new HashMap<>();
                        loadedCurrentMap = DataHandling.validate(combinedMaps);
                        // Save the map
                        globalMap = DataHandling.convertMap(loadedCurrentMap);
                        // Refresh the table
                        loadTable(DataHandling.convertMapToLong(globalMap));
                    }
                }
            }

        });

        // Close to tray
        CloseToSystemTray tray = new CloseToSystemTray();
        try {
            tray.startTray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (PreferencesGui.prefs.getBoolean("updateCheck",
            PreferencesGui.getUpdateStatus())) {
            // Check to see if there is a new version of the program
            // True if there is
            // TODO: This must be changed on each update
            if (checkForUpdate(
                "http://austinayers.com/ProductivityPlus_v1.html", 500)) {
                JOptionPane.showMessageDialog(null,
                    "There is a new version of this program at "
                        + "austinayers.com/ProductivityPlus.exe or /ProductivityPlus.jar");
            }
        }
    }

    /**
     * This method refreshes the table for inclusions and exclusions.
     * 
     * @param displayPanel,
     *            the panel the tables are attached to.
     */
    private void refreshDisplayTable(JPanel displayPanel) {
        List<String> inclusions = new ArrayList<>();
        inclusions = TableHelper.loadList("inclusion");
        List<String> exclusions = new ArrayList<>();
        exclusions = TableHelper.loadList("exclusion");
        exclusionScrollPane = TableHelper.loadTable(exclusions);
        inclusionScrollPane = TableHelper.loadTable(inclusions);
        displayPanel.add(inclusionScrollPane, "cell 1 1,grow");
        displayPanel.add(exclusionScrollPane, "cell 2 1,grow");
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
    private static void setStopLabel() {
        trackStatusLabel.setText("Status: Currently not tracking.");
    }

    /**
     * Start the program timer. This method creates a new thread from a
     * ProgramTimer object and starts tracking. We must use a new thread to not
     * 'hang' the GUI.
     */
    private static void startTimer() {
        SingletonTimer.getInstance().callTimer();
    }

    /**
     * This method updates the table found in the main window of the gui. It
     * works by destroying the current table object and creates a new one.
     * 
     * @param fresh,
     *            only true if the table is being launched for the first time.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void updateTable(boolean fresh) {
        // Remove old table object
        if (!fresh) {
            mainPanel.remove(sc);
            mainPanel.remove(table);
        }
        Map<String, Long> loadedCurrentMap = new HashMap<String, Long>();
        // Filter the date
        loadedCurrentMap = DataHandling.validate(ProgramTimer.appMap);
        // Conver the type of map
        globalMap = DataHandling.convertMap(loadedCurrentMap);

        // Load the table
        String columns[] = {"Program", TimeConvert.getUnit()};
        if (loadedCurrentMap.size() == 0) {
            model = new DefaultTableModel(1, 2);
            String[] colHeadings = {"Program", "Time"};
            model.setColumnIdentifiers(colHeadings);
        } else {
            model = new DefaultTableModel(
                getRows((TimeConvert.convertTime(loadedCurrentMap))), columns) {
                private static final long serialVersionUID =
                    6142201610590876247L;

                @Override
                public Class getColumnClass(int column) {
                    Class returnValue;
                    if ((column >= 0) && (column < getColumnCount())) {
                        returnValue = getValueAt(0, column).getClass();
                    } else {
                        returnValue = Object.class;
                    }
                    return returnValue;
                }
            };
        }
        // Create the table from the model
        table = new JTable(model);
        if (PreferencesGui.getDisplayIndex() == 3) {
            model = new DefaultTableModel();
            table = new JTable(model);
            model.addColumn("Program");
            // Add the keys
            for (String name : loadedCurrentMap.keySet()) {
                String key = name.toString();
                model.addRow(new Object[] {key});
            }
            // Convert the map to written
            Map<String, String> finalMap2 =
                TimeConvert.convertWritten(loadedCurrentMap);

            // Append a new column with copied data
            model.addColumn(TimeConvert.getUnit(),
                finalMap2.values().toArray());
        } else {
            // Allow for sorting
            RowSorter<TableModel> sorter =
                new TableRowSorter<TableModel>(model);
            table.setRowSorter(sorter);

        }
        // Move contents left justified
        DefaultTableCellRenderer renderer =
            (DefaultTableCellRenderer) table.getDefaultRenderer(Double.class);
        renderer.setHorizontalAlignment(SwingConstants.LEFT);

        // Add the scroll pane
        sc = new JScrollPane(table);
        mainPanel.add(sc, "growx");
        secretLabel.setText("  ");
        secretLabel.setText("");
    }

    /**
     * Get the rows of the map to display.
     * 
     * @param finalMap,
     *            the map to display.
     * @return Object[][] of the rows.
     */
    private static Object[][] getRows(Map<String, Double> finalMap) {
        // Iterate through the map param to get the key col
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
     * 
     * @param loadedMap,
     *            the map we're loading the table with.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void loadTable(Map<String, Long> loadedMap) {

        if (loadedMap.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "Warning: Loaded time table was empty");
        }
        // Remove old table object
        mainPanel.remove(sc);
        mainPanel.remove(table);

        String columns[] = {"Program", TimeConvert.getUnit()};
        if (loadedMap.size() == 0) {
            model = new DefaultTableModel(1, 2);
            String[] colHeadings = {"Program", "Time"};
            model.setColumnIdentifiers(colHeadings);
        } else {
            model = new DefaultTableModel(
                getRows((TimeConvert.convertTime(loadedMap))), columns) {

                private static final long serialVersionUID =
                    -5228479117217622133L;

                @Override
                public Class getColumnClass(int column) {
                    Class returnValue;
                    if ((column >= 0) && (column < getColumnCount())) {
                        returnValue = getValueAt(0, column).getClass();
                    } else {
                        returnValue = Object.class;
                    }
                    return returnValue;
                }
            };
        }
        // Create the table from the model
        table = new JTable(model);
        if (PreferencesGui.getDisplayIndex() == 3) {
            model = new DefaultTableModel();
            table = new JTable(model);
            model.addColumn("Program");
            // Add the keys
            for (String name : loadedMap.keySet()) {
                String key = name.toString();
                model.addRow(new Object[] {key});
            }
            // Convert the map to written
            Map<String, String> finalMap2 =
                TimeConvert.convertWritten(loadedMap);

            // Append a new column with copied data
            model.addColumn(TimeConvert.getUnit(),
                finalMap2.values().toArray());
        } else {
            // Allow for sorting
            RowSorter<TableModel> sorter =
                new TableRowSorter<TableModel>(model);
            table.setRowSorter(sorter);

        }
        // Move contents left justified
        DefaultTableCellRenderer renderer =
            (DefaultTableCellRenderer) table.getDefaultRenderer(Double.class);
        renderer.setHorizontalAlignment(SwingConstants.LEFT);

        // Add the scroll pane
        sc = new JScrollPane(table);
        mainPanel.add(sc);
        mainPanel.add(sc, "growx");
        secretLabel.setText("  ");
        secretLabel.setText("");
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

    /*
     * Getter for the mode, 1 = track all, 2 = track inclusions, 3 = track
     * exclusions
     */
    public static int getMode() {
        return prefs.getInt(DISPLAY_MODE, 1);
    }

    /*
     * Setter for the mode, 1 = track all, 2 = track inclusions, 3 = track
     * exclusions
     */
    private void setMode() {
        if (trackAllRButton.isSelected()) {
            prefs.putInt(DISPLAY_MODE, 1);
        } else if (trackInclusionsRButton.isSelected()) {
            prefs.putInt(DISPLAY_MODE, 2);
        } else if (trackExclusionsRButton.isSelected()) {
            prefs.putInt(DISPLAY_MODE, 3);
        }
    }

    /**
     * Check to see if there is an update for PP.
     * 
     * @param url
     * @param timeout
     * @return true if there is an update, false if not.
     */
    public static boolean checkForUpdate(String url, int timeout) {
        url = url.replaceFirst("^https", "http");
        try {
            HttpURLConnection connection =
                (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return !(200 <= responseCode && responseCode <= 399);
        } catch (IOException exception) {
            return true;
        }
    }

    /**
     * 
     */
    private void updateLists() {
        // Create lists of the inclusions/exclusions
        List<String> inclusions = new ArrayList<>();
        List<String> exclusions = new ArrayList<>();
        // Remove old scroll panes
        displayPanel.remove(inclusionScrollPane);
        displayPanel.remove(exclusionScrollPane);
        // Load the respective lists
        inclusions = TableHelper.loadList("inclusion");
        exclusions = TableHelper.loadList("exclusion");
        // Perforn the required action
        if (getMode() == 2) {
            inclusions.add("- " + progTextField.getText());
        } else {
            exclusions.add("- " + progTextField.getText());
        }
        // Save the lists
        TableHelper.saveList(inclusions, "inclusion");
        TableHelper.saveList(exclusions, "exclusion");
        inclusionScrollPane = TableHelper.loadTable(inclusions);
        // Add the lists back to the panel
        displayPanel.add(inclusionScrollPane, "cell 1 1,grow");
        exclusionScrollPane = TableHelper.loadTable(exclusions);
        displayPanel.add(exclusionScrollPane, "cell 2 1,grow");
        // Clear the text box
        progTextField.setText("");
    }
}
