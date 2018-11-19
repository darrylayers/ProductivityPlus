package gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import core.DataHandling;
import core.ExcelWriter;
import core.TimeConvert;
import net.miginfocom.swing.MigLayout;

/**
 * Gui class for About window.
 * 
 * @author Austin Ayers
 * 
 */
public class ExploreDataGui extends JDialog {

    private static final long serialVersionUID = 7452480813384342669L;

    private final JPanel contentPanel = new JPanel();

    private DatePickerSettings datePickerSettings = new DatePickerSettings();
    private DatePicker datePicker = new DatePicker(datePickerSettings);
    private DatePickerSettings datePickerSettings2 = new DatePickerSettings();
    private DatePicker datePicker2 = new DatePicker(datePickerSettings2);
    private String formattedString;
    private String formattedString2;
    private boolean singleData = false;

    private final static JProgressBar progressBar = new JProgressBar();

    private static int barMin = 0;
    private static int barMax = 100;

    private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    private final JPanel singleProgPanel = new JPanel();
    private final JPanel progInputPanel = new JPanel();
    private final JPanel calendarPanel = new JPanel();
    private final JPanel displayPanel = new JPanel();
    private final JLabel lblEnterProgramName = new JLabel("Enter Program Name");
    private final JTextField textField = new JTextField();
    private final DatePicker datePicker_1 =
        new DatePicker((DatePickerSettings) null);
    private final DatePicker datePicker_2 =
        new DatePicker((DatePickerSettings) null);
    private final JButton btnSubmitSearch = new JButton("Submit Search");
    private final JTextArea txtrTest = new JTextArea();
    private final JTextPane txtpnTheLeftDate = new JTextPane();
    private final JTextPane textPane = new JTextPane();

    /**
     * Launch the About pop up window.
     */
    public static void newWindow() {
        try {
            ExploreDataGui dialog = new ExploreDataGui();
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            // Launch the window to match the parent window
            Main.setWindowLoc();
            dialog.setLocation(Main.getWindowLoc().x, Main.getWindowLoc().y);
            dialog.setVisible(true);
            // Set progress bar to 0
            progressBar.setValue(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    private ExploreDataGui() {
        textField.setToolTipText("Enter program name to look up.");
        textField.setColumns(10);
        setTitle("Explore Data");
        setBounds(100, 100, 450, 396);

        // Frame panels and panes
        getContentPane()
            .setLayout(
                new MigLayout("", "[grow]", "[228px,grow]"));

        getContentPane().add(tabbedPane, "cell 0 0,grow");
        tabbedPane.addTab("Explore Large Data", null, contentPanel, null);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(
            new MigLayout("", "[304.00,grow,left][-37.00][]",
                "[grow][70.00,grow][215.00,grow]"));

        // Text pane settings for date picker info
        textPane.setBackground(new Color(240, 240, 240));
        textPane.setEditable(false);
        textPane.setText(
            "The left date picker is the start date, the right date picker is"
                + " the end date. You can optionally leave the right one empty.");

        contentPanel.add(textPane, "cell 0 0,grow");

        // Dates
        JPanel datePanel = new JPanel();
        datePanel.setBorder(
            new EtchedBorder(EtchedBorder.LOWERED, null, null));
        contentPanel.add(datePanel, "cell 0 1,grow");
        datePanel.setLayout(
            new MigLayout("", "[83.00,grow][][]", "[][][55.00,grow][grow]"));
        datePicker.getComponentDateTextField()
            .setToolTipText("Enter start date.");
        datePanel.add(datePicker, "");
        datePicker.setDateToToday();
        datePicker_1.setDateToToday();
        datePicker2.getComponentDateTextField().setToolTipText(
            "Enter end date. Can be left empty for single date search.");
        datePanel.add(datePicker2, "cell 1 0");

        // Exports panel
        JPanel exportPanel = new JPanel();
        contentPanel.add(exportPanel, "cell 0 2,grow");
        exportPanel
            .setLayout(new MigLayout("", "[142.00][142.00][]", "[][][33.00]"));

        // Open output button
        JButton btnOpenOutput = new JButton("Open Output");
        btnOpenOutput.setToolTipText("Open the created Excel file.");
        // Create export file button
        JButton btnCreateExportFile = new JButton("Create export file");
        btnCreateExportFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                // If the day is empty, give alert.
                if (datePicker.getDate() == null) {
                    JOptionPane.showMessageDialog(null,
                        "Please enter a start date.");
                } else {
                    ExploreDataGui.updateBar(0);
                    // Reenable the open output button
                    btnOpenOutput.setEnabled(true);
                    loadMapsFromMem();
                }
            }
        });

        btnCreateExportFile.setToolTipText(
            "File is exported to Excel format (.xlsx).");
        exportPanel.add(btnCreateExportFile, "cell 0 1,grow");

        // Open output listener
        btnOpenOutput.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                try {
                    Desktop.getDesktop().open(new File(
                        "./output/ProductivityPlusData" + ExcelWriter.getDate()
                            + ".xlsx"));
                    progressBar.setValue(0);
                } catch (java.lang.IllegalArgumentException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        // Disable the open output button
        btnOpenOutput.setEnabled(false);
        exportPanel.add(btnOpenOutput, "cell 1 1,grow");
        // Progress bar listener
        progressBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                progressBar.setValue(0);
            }
        });
        // Progress Bar
        exportPanel.add(progressBar, "cell 0 2");
        progressBar.setMinimum(barMin);
        progressBar.setMaximum(barMax);
        // Single Program Search
        tabbedPane.addTab("Single Program Search", null, singleProgPanel, null);
        singleProgPanel.setLayout(
            new MigLayout("", "[427.00,grow]", "[][grow][][][grow]"));

        singleProgPanel.add(progInputPanel, "cell 0 0,grow");
        progInputPanel.setLayout(new MigLayout("", "[grow]", "[][]"));
        // Labels and text fields
        progInputPanel.add(lblEnterProgramName, "cell 0 0");
        progInputPanel.add(textField, "cell 0 1,growx");
        txtpnTheLeftDate.setEditable(false);
        txtpnTheLeftDate.setBackground(new Color(240, 240, 240));
        txtpnTheLeftDate.setText(
            "The left date picker is the start date, the right date picker is"
                + " the end date. You can optionally leave the right one empty. "
                + "Tip: It is usually best to be on 'Display All Mode' when using this feature.");

        singleProgPanel.add(txtpnTheLeftDate, "cell 0 1,grow");
        // Calendars
        singleProgPanel.add(calendarPanel, "cell 0 3,grow");
        calendarPanel.setLayout(new MigLayout("", "[grow][grow]", "[grow][]"));
        datePicker_1.getComponentDateTextField()
            .setToolTipText("Starting look up date.");
        calendarPanel.add(datePicker_1, "cell 0 0");
        datePicker_2.getComponentDateTextField()
            .setToolTipText("Ending look up date.");
        calendarPanel.add(datePicker_2, "cell 1 0");
        // Submit button
        btnSubmitSearch.setToolTipText("Submit program search.");
        btnSubmitSearch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (datePicker_1.getDate() == null) {
                    JOptionPane.showMessageDialog(null,
                        "Please enter a start date.");
                } else {
                    singleData = true;
                    loadMapsFromMem();
                }
            }
        });
        calendarPanel.add(btnSubmitSearch, "cell 1 1");
        // Single program search result panel
        singleProgPanel.add(displayPanel, "cell 0 4,grow");
        displayPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
        txtrTest.setText("Program name: <Search submission needed> \n"
            + "Total time over the range: <Time will be here in unit preference> \n"
            + "Number of days over the range the program was accessed: 0 \n \n"
            + "Hint: This info is copy/pastable!");

        displayPanel.add(txtrTest, "cell 0 0,grow");
        // Date settings
        datePickerSettings.setFormatForDatesBeforeCommonEra("MM/dd/yyyy");
        datePickerSettings.setFormatForDatesCommonEra("MM/dd/yyyy");
        datePickerSettings2.setFormatForDatesBeforeCommonEra("MM/dd/yyyy");
        datePickerSettings2.setFormatForDatesCommonEra("MM/dd/yyyy");
    }

    /**
     * Method that updates the current progression the progress bar is at.
     * 
     * @param newValue
     */
    public static void updateBar(int newValue) {
        progressBar.setValue(newValue);
    }

    /**
     * Load the maps from memory given the dates. This is the method used for
     * both features in Explore Data.
     */
    @SuppressWarnings("unchecked")
    private void loadMapsFromMem() {
        LocalDate date;
        LocalDate date2;

        // If export is option chosen
        if (!singleData) {
            // Grab both dates
            date = datePicker.getDate();
            date2 = datePicker2.getDate();
            // Convert date 1
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Dyy");
            formattedString = date.format(formatter);

            // If date 2 was used
            if (date2 != null) {
                // Format the date
                formattedString2 = date2.format(formatter);

                // Load dates with an arraylist of all the dates we need
                List<String> dates =
                    DataHandling.dateDiff(formattedString, formattedString2);

                // Load maps with each map we need
                @SuppressWarnings("rawtypes")
                List<Map> maps = DataHandling.loadMaps(dates);

                // Write each map using DataHandling
                try {
                    DataHandling.writeDates(maps, dates);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                try {
                    // Write the single date to the file
                    DataHandling.acceptDate(formattedString, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Single program search was used
            int dateR = 0; // to be used to dates.size();
            // Grab both dates
            date = datePicker_1.getDate();
            date2 = datePicker_2.getDate();

            // Initialize new map
            Map<String, Long> comboMap = new HashMap<String, Long>();

            String textInput = textField.getText(); // get prog name from app
            singleData = false;

            // Format date 1
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Dyy");
            formattedString = date.format(formatter);

            // If date 2 was used
            if (date2 != null) {
                // Format the date
                formattedString2 = date2.format(formatter);
                // Load dates with an arraylist of all the dates we need
                List<String> dates =
                    DataHandling.dateDiff(formattedString, formattedString2);
                dateR = dates.size();
                // Load maps with each map we need
                @SuppressWarnings("rawtypes")
                List<Map> maps = DataHandling.loadMaps(dates);
                // Combine the appropriate maps
                for (Map<String, Long> currentMap : maps) {
                    for (Map.Entry<String, Long> entry : currentMap
                        .entrySet()) {
                        String key = entry.getKey();
                        Long current = comboMap.get(key);

                        if (current == null) {
                            comboMap.put(key, entry.getValue());
                        } else {
                            comboMap.put(key, entry.getValue() + current);
                        }
                    }
                }
            } else {
                dateR = 1;
                try {
                    // Set comboMap to the combined map we need.
                    comboMap = DataHandling.acceptDate(formattedString, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Modify the map using the user's preferences
            Map<String, Long> loadedCurrentMap = new HashMap<String, Long>();

            // Filter what needs to be displayed
            loadedCurrentMap = DataHandling.validate(comboMap);

            Map<String, Double> displayMap =
                TimeConvert.convertTime(loadedCurrentMap);

            Double time = displayMap.get(textInput);

            String strTime = "";

            // If no time was found, return no time error.
            if (time == null) {
                JOptionPane.showMessageDialog(null,
                    "No time found, check input or display mode!");
            } else {
                // Display the appropriate time message
                if (TimeConvert.getUnit().equals("Time (Written)")) {
                    strTime = DataHandling.convertToWritten(time);
                } else {
                    strTime = time.toString();
                }
            }
            // Set the rest of the result text
            txtrTest.setText("Program name: " + textInput + "\n"
                + "Total time over the range: " + strTime
                + TimeConvert.getUnit().substring(4,
                    TimeConvert.getUnit().length())
                + "\n"
                + "Number of days examined: " + dateR
                + "\n \n"
                + "Hint: This info is copy/pastable!");
        }
    }
}
