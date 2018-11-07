package gui;

import java.awt.Color;
import java.awt.Component;
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
    private static Set<String> keys;
    private static JScrollPane sc;
    private static JLabel secretLabel;

    private JTextField progTextField;
    private JButton btnStopTimer;
    private JRadioButton trackAllRButton = new JRadioButton("Track all");
    private JRadioButton trackInclusionsRButton =
        new JRadioButton("Track inclusions");
    private JRadioButton trackExclusionsRButton =
        new JRadioButton("Track exclusions");

    public static boolean toTrack = false;

    /**
     * Main method that builds the GUI.
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Main gui = new Main();
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
        frame.setSize(986, 554);
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

        JMenuItem outputFolderItem = new JMenuItem("Open output folder");
        outputFolderItem.addActionListener(new ActionListener() {
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

        mnFile.add(outputFolderItem);

        JMenuItem savedDataItem = new JMenuItem("Open saved data folder");
        savedDataItem.addActionListener(new ActionListener() {
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
        mnFile.add(savedDataItem);

        JMenu mnEdit = new JMenu("Edit");
        menuBar.add(mnEdit);

        JMenuItem preferencesItem = new JMenuItem("Preferences");
        mnEdit.add(preferencesItem);

        JMenuItem consolidationItem = new JMenuItem("Program Consolidation");
        consolidationItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                ConsolidationGui.newWindow();
            }
        });
        mnEdit.add(consolidationItem);
        preferencesItem.addActionListener(new ActionListener() {
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

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                AboutGui.newWindow();
            }
        });
        mnHelp.add(aboutItem);

        JMenuItem faqItem = new JMenuItem("FAQ");
        faqItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                FaqGui.newWindow();
            }
        });
        mnHelp.add(faqItem);

        // ************** Panel creations ************** //

        frame.getContentPane()
            .setLayout(new MigLayout("", "[1061.00px]", "[482.00px]"));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(
            new MigLayout("", "[110.00][224.00,grow]", "[35.00][414.00,grow]"));

        // ************** Tab pane ************** //

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane, "cell 0 0,growx,aligny top");
        tabbedPane.addTab("Program Timer", null, mainPanel, null);

        JPanel displayPanel = new JPanel();
        tabbedPane.addTab("What to display", null, displayPanel, null);
        displayPanel.setLayout(new MigLayout("",
            "[310.00px,grow][450,grow][501.00,grow]", "[75][457.00px,grow]"));

        JLabel controlsLabel = new JLabel("Controls");
        displayPanel.add(controlsLabel, "cell 0 0,alignx center,aligny center");

        JLabel inclusionsLabel = new JLabel("Inclusions");
        inclusionsLabel.setToolTipText(
            "These are the ONLY programs that will be tracked if the \"Track inclusions\" button is selected. This is useful if you only want to track a few programs.");
        displayPanel.add(inclusionsLabel,
            "cell 1 0,alignx center,aligny center");

        JLabel exclusionsLabel = new JLabel("Exclusions");
        exclusionsLabel.setToolTipText(
            "These are the programs that will be excluded from tracking if the \"Track exclusions\" button is selected. This is useful if you want to avoid tracking only a few programs.");
        displayPanel.add(exclusionsLabel,
            "cell 2 0,alignx center,aligny center");

        JPanel controlPanel = new JPanel();
        displayPanel.add(controlPanel, "cell 0 1,grow");
        controlPanel.setLayout(new MigLayout("", "[132.00,grow]",
            "[][][][][][][][][][][][][][][]"));

        JLabel modeLabel = new JLabel("Mode - What to display");
        controlPanel.add(modeLabel, "cell 0 0,alignx center,aligny center");

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

        if (getMode() == 1) {
            trackAllRButton.setSelected(true);
        }
        else if (getMode() == 2) {
            trackInclusionsRButton.setSelected(true);
        }
        else if (getMode() == 3) {
            trackExclusionsRButton.setSelected(true);
        }

        progTextField = new JTextField();
        progTextField.setToolTipText("Put the program name here");
        controlPanel.add(progTextField, "cell 0 5,growx");
        progTextField.setColumns(10);

        JButton btnAddProgram = new JButton("Add program");
        btnAddProgram.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {

                if (DataHandling.checkEmpty(progTextField.getText())) {
                    JOptionPane.showMessageDialog(null,
                        "Input string empty.");
                }
                else {
                    if (getMode() == 1) {
                        JOptionPane.showMessageDialog(null,
                            "Cannot add program. Track all is selected.");
                    }
                    else if (getMode() == 2 || getMode() == 3) {
                        List<String> inclusions = new ArrayList<>();
                        List<String> exclusions = new ArrayList<>();
                        displayPanel.remove(inclusionScrollPane);
                        displayPanel.remove(exclusionScrollPane);
                        inclusions = TableHelper.loadList("inclusion");
                        exclusions = TableHelper.loadList("exclusion");
                        if (getMode() == 2) {
                            inclusions.add("- " + progTextField.getText());
                        }
                        else {
                            exclusions.add("- " + progTextField.getText());
                        }
                        TableHelper.saveList(inclusions, "inclusion");
                        TableHelper.saveList(exclusions, "exclusion");
                        inclusionScrollPane = TableHelper.loadTable(inclusions);
                        displayPanel.add(inclusionScrollPane, "cell 1 1,grow");
                        exclusionScrollPane = TableHelper.loadTable(exclusions);
                        displayPanel.add(exclusionScrollPane, "cell 2 1,grow");
                        progTextField.setText("");
                    }
                    secretLabel.setText("  ");
                    secretLabel.setText("");
                }
            }
        });

        trackExclusionsRButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (trackExclusionsRButton.isSelected()) {
                    trackInclusionsRButton.setSelected(false);
                    trackAllRButton.setSelected(false);
                }
                else {
                    trackExclusionsRButton.setSelected(true);
                }
                setMode();
            }
        });

        trackInclusionsRButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (trackInclusionsRButton.isSelected()) {
                    trackAllRButton.setSelected(false);
                    trackExclusionsRButton.setSelected(false);
                }
                else {
                    trackInclusionsRButton.setSelected(true);
                }
                setMode();

            }
        });

        trackAllRButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (trackAllRButton.isSelected()) {
                    trackInclusionsRButton.setSelected(false);
                    trackExclusionsRButton.setSelected(false);
                }
                else {
                    trackAllRButton.setSelected(true);
                }
                setMode();

            }
        });

        btnAddProgram.setToolTipText(
            "Add a program from whichever table is selected above.");
        controlPanel.add(btnAddProgram, "cell 0 6,grow");

        JButton btnRemoveProgram = new JButton("Remove Program");
        btnRemoveProgram.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (DataHandling.checkEmpty(progTextField.getText())) {
                    JOptionPane.showMessageDialog(null,
                        "Input string empty.");
                }
                else {

                    if (getMode() == 1) {
                        JOptionPane.showMessageDialog(null,
                            "Cannot remove program. Track all is selected.");
                    }

                    else if (getMode() == 2 || getMode() == 3) {
                        List<String> inclusions = new ArrayList<>();
                        List<String> exclusions = new ArrayList<>();
                        displayPanel.remove(inclusionScrollPane);
                        displayPanel.remove(exclusionScrollPane);
                        inclusions = TableHelper.loadList("inclusion");
                        exclusions = TableHelper.loadList("exclusion");
                        if (getMode() == 2) {
                            inclusions.remove("- " + progTextField.getText());
                        }
                        else {
                            exclusions.remove("- " + progTextField.getText());
                        }
                        TableHelper.saveList(inclusions, "inclusion");
                        TableHelper.saveList(exclusions, "exclusion");
                        inclusionScrollPane = TableHelper.loadTable(inclusions);
                        displayPanel.add(inclusionScrollPane, "cell 1 1,grow");
                        exclusionScrollPane = TableHelper.loadTable(exclusions);
                        displayPanel.add(exclusionScrollPane, "cell 2 1,grow");
                        progTextField.setText("");
                    }
                    secretLabel.setText("  ");
                    secretLabel.setText("");
                }

            }
        });
        btnRemoveProgram.setToolTipText(
            "Remove a program from whichever table is selected above.");
        controlPanel.add(btnRemoveProgram, "cell 0 7,grow");

        List<String> inclusions = new ArrayList<>();
        inclusions = TableHelper.loadList("inclusion");
        List<String> exclusions = new ArrayList<>();
        exclusions = TableHelper.loadList("exclusion");
        exclusionScrollPane = TableHelper.loadTable(exclusions);
        inclusionScrollPane = TableHelper.loadTable(inclusions);
        displayPanel.add(inclusionScrollPane, "cell 1 1,grow");
        displayPanel.add(exclusionScrollPane, "cell 2 1,grow");

        // ************** Timer controls labels ************** //

        JLabel timerControlsLabel = new JLabel("     Timer Controls");
        timerControlsLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        mainPanel.add(timerControlsLabel, "cell 0 0,alignx left");
        trackStatusLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        mainPanel.add(trackStatusLabel, "cell 1 0");

        // ************** Button panel ************** //

        JPanel buttonPanel = new JPanel();
        buttonPanel.setToolTipText("Program data for today");
        buttonPanel.setBackground(Color.WHITE);
        mainPanel.add(buttonPanel, "cell 0 1,grow");
        buttonPanel.setLayout(new MigLayout("", "[100,grow]",
            "[25][][][][][][][][38.00,grow][][][][][]"));

        // ************** Start button ************** //

        JButton btnStartTimer = new JButton("Start Timer");
        buttonPanel.add(btnStartTimer, "cell 0 0,growx");
        btnStartTimer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                startTimer();
                trackStatusLabel.setText("Status: Currently tracking...");
            }
        });

        // ************** Stop button ************** //

        btnStopTimer = new JButton("Stop Timer");
        buttonPanel.add(btnStopTimer, "cell 0 1,growx");
        btnStopTimer.addMouseListener(new MouseAdapter() {
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

        // ************** Graphical output button ************** //

        JButton btnGraphOutput = new JButton("Graphical Output");
        btnGraphOutput.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                GraphicalOutputGui.newWindow();
            }
        });
        buttonPanel.add(btnGraphOutput, "cell 0 2,growx");

        // ************** Explore data button ************** //

        JButton btnExploreData = new JButton("Explore Data");
        btnExploreData.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                ExploreDataGui.newWindow();
            }
        });
        buttonPanel.add(btnExploreData, "cell 0 3,growx");

        JCheckBox chckbxConsolidatePrograms =
            new JCheckBox("Consolidate Programs");
        chckbxConsolidatePrograms.setSelected(getChecked());
        chckbxConsolidatePrograms.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                // setChecked();
                prefs.putBoolean(VALIDATE_STRINGS,
                    chckbxConsolidatePrograms.isSelected());
                updateTable(false);
                secretLabel.setText("  ");
                secretLabel.setText("");
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
                    // Pass the date(s) to DateHandling.java
                    @SuppressWarnings("unused")
                    SimpleDateFormat dateFormatter =
                        new SimpleDateFormat("Dyy");
                    DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("Dyy");
                    formattedString = date.format(formatter);
                    if (date2 == null) {
                        try {
                            loadTable(
                                DataHandling.acceptDateTable(formattedString));
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (date2 != null) {
                        formattedString2 = date2.format(formatter);
                        List<String> dates =
                            DataHandling.dateDiff(formattedString,
                                formattedString2);
                        @SuppressWarnings("rawtypes")
                        List<Map> maps = DataHandling.loadMaps(dates);
                        Map<String, Long> combinedMaps = new HashMap<>();
                        int i = 100 / maps.size();
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
                        Map<String, Long> loadedCurrentMap = new HashMap<>();
                        if (Main.getChecked()) {
                            loadedCurrentMap =
                                DataHandling.validateData(combinedMaps);
                        }
                        else {
                            loadedCurrentMap = combinedMaps;
                        }
                        if (Main.getMode() == 3 || Main.getMode() == 2) {
                            loadedCurrentMap =
                                DataHandling
                                    .validateWhatToDisplay(loadedCurrentMap);
                        }
                        else if (Main.getMode() == 1 && Main.getChecked()) {
                            loadedCurrentMap =
                                DataHandling.validateData(combinedMaps);
                        }
                        else {
                            loadedCurrentMap = combinedMaps;
                        }
                        loadTable(loadedCurrentMap);
                    }
                }
            }

        });

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
        trackStatusLabel.setText("Status: Currently not tracking.");
    }

    /**
     * Set the status label to the 'tracking' state.
     */
    public static void setStartLabel() {
        trackStatusLabel.setText("Status: Currently tracking...");
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
     * 
     * @param fresh,
     *            only true if the table is being launched for the first time.
     */
    @SuppressWarnings({"rawtypes", "unchecked", "serial"})
    public static void updateTable(boolean fresh) {
        // Remove old table object
        if (!fresh) {
            mainPanel.remove(sc);
            mainPanel.remove(table);
        }
        Map<String, Long> loadedCurrentMap = new HashMap<String, Long>();
        if (getChecked()) {
            loadedCurrentMap = DataHandling.validateData(ProgramTimer.appMap);
        }
        if (Main.getMode() == 3 || Main.getMode() == 2) {
            if (!getChecked()) {
                loadedCurrentMap = ProgramTimer.appMap;
            }
            loadedCurrentMap =
                DataHandling.validateWhatToDisplay(loadedCurrentMap);
        }
        else if (Main.getMode() == 1 && getChecked()) {
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
     * 
     * @param loadedMap,
     *            the map we're loading the table with.
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
        mainPanel.add(sc, "growx");
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
    public void setMode() {
        if (trackAllRButton.isSelected()) {
            prefs.putInt(DISPLAY_MODE, 1);
        }
        else if (trackInclusionsRButton.isSelected()) {
            prefs.putInt(DISPLAY_MODE, 2);
        }
        else if (trackExclusionsRButton.isSelected()) {
            prefs.putInt(DISPLAY_MODE, 3);
        }
    }
}
