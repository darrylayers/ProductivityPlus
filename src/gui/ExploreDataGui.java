package gui;

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
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private DatePickerSettings datePickerSettings = new DatePickerSettings();
    private DatePicker datePicker = new DatePicker(datePickerSettings);
    private DatePickerSettings datePickerSettings2 = new DatePickerSettings();
    private DatePicker datePicker2 = new DatePicker(datePickerSettings2);
    private String formattedString;
    private String formattedString2;
    private JRadioButton rdbtntxtExport = new JRadioButton(".txt export");
    private JRadioButton rdbtnExcelExport = new JRadioButton("Excel export");
    private JRadioButton rdbtnodsopenOffice =
        new JRadioButton(".ods (Open Office)");
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
    private boolean singleData = false;

    /**
     * Launch the About pop up window.
     */
    public static void newWindow() {
        try {
            ExploreDataGui dialog = new ExploreDataGui();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            Main.setWindowLoc();
            dialog.setLocation(Main.getWindowLoc().x, Main.getWindowLoc().y);
            dialog.setVisible(true);
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * Create the dialog.
     */
    public ExploreDataGui() {
        textField.setColumns(10);
        setTitle("Explore Data");
        setBounds(100, 100, 450, 334);

        // ************** Frame panels and panes ************** //
        getContentPane()
            .setLayout(
                new MigLayout("", "[grow]", "[228px,grow]"));

        getContentPane().add(tabbedPane, "cell 0 0,grow");
        tabbedPane.addTab("Explore large data", null, contentPanel, null);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(
            new MigLayout("", "[304.00,left][-37.00][]",
                "[grow][70.00,grow][215.00,grow]"));

        // ************** Dates ************** //

        JPanel datePanel = new JPanel();
        datePanel.setBorder(
            new EtchedBorder(EtchedBorder.LOWERED, null, null));
        contentPanel.add(datePanel, "cell 0 1,grow");
        datePanel.setLayout(
            new MigLayout("", "[83.00,grow][][]", "[][][55.00,grow][grow]"));
        datePanel.add(datePicker, "");
        datePicker.setDateToToday();
        datePanel.add(datePicker2, "cell 1 0");

        // ************** Exports ************** //

        JPanel exportPanel = new JPanel();
        contentPanel.add(exportPanel, "cell 0 2,grow");
        exportPanel
            .setLayout(new MigLayout("", "[142.00][142.00][]", "[][][33.00]"));
        rdbtnExcelExport.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                rdbtnodsopenOffice.setSelected(false);
                rdbtntxtExport.setSelected(false);
            }
        });

        rdbtnExcelExport.setSelected(true);
        // exportPanel.add(rdbtnExcelExport, "cell 0 0");

        JButton btnOpenOutput = new JButton("Open Output");
        JButton btnCreateExportFile = new JButton("Create export file");
        btnCreateExportFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {

                ExploreDataGui.updateBar(0);
                btnOpenOutput.setEnabled(true);
                loadMapsFromMem();
            }
        });

        rdbtnodsopenOffice.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                rdbtntxtExport.setSelected(false);
                rdbtnExcelExport.setSelected(false);

            }
        });
        // exportPanel.add(rdbtnodsopenOffice, "cell 1 0");
        rdbtntxtExport.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                rdbtnodsopenOffice.setSelected(false);
                rdbtnExcelExport.setSelected(false);
            }
        });

        // exportPanel.add(rdbtntxtExport, "cell 2 0");

        btnCreateExportFile.setToolTipText(
            "File is exported to Excel format (.xlsx)");
        exportPanel.add(btnCreateExportFile, "cell 0 1,grow");

        btnOpenOutput.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                try {
                    Desktop.getDesktop().open(new File(
                        "./output/ProductivityPlusData" + ExcelWriter.getDate()
                            + ".xlsx"));
                }
                catch (java.lang.IllegalArgumentException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btnOpenOutput.setEnabled(false);
        exportPanel.add(btnOpenOutput, "cell 1 1,grow");
        progressBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                progressBar.setValue(0);
            }
        });

        // ************** Progress Bar ************** //
        exportPanel.add(progressBar, "cell 0 2");
        progressBar.setMinimum(barMin);
        progressBar.setMaximum(barMax);

        // ************** Single Program Search ************** //

        tabbedPane.addTab("Single program search", null, singleProgPanel, null);
        singleProgPanel.setLayout(
            new MigLayout("", "[427.00,grow]", "[][][grow]"));

        singleProgPanel.add(progInputPanel, "cell 0 0,grow");
        progInputPanel.setLayout(new MigLayout("", "[grow]", "[][]"));

        progInputPanel.add(lblEnterProgramName, "cell 0 0");

        progInputPanel.add(textField, "cell 0 1,growx");

        singleProgPanel.add(calendarPanel, "cell 0 1,grow");
        calendarPanel.setLayout(new MigLayout("", "[grow][grow]", "[grow][]"));

        calendarPanel.add(datePicker_1, "cell 0 0");

        calendarPanel.add(datePicker_2, "cell 1 0");
        btnSubmitSearch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                singleData = true;
                loadMapsFromMem();
            }
        });

        calendarPanel.add(btnSubmitSearch, "cell 1 1");

        singleProgPanel.add(displayPanel, "cell 0 2,grow");
        displayPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
        txtrTest.setText("Program name: <Search submission needed> \n"
            + "Total time over the range: <Time will be here in unit preference> \n"
            + "Number of days over the range the program was accessed: 0 \n \n"
            + "Hint: This info is copy/pastable!");

        displayPanel.add(txtrTest, "cell 0 0,grow");

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
     * Method that returns what value the progress bar is at.
     * 
     * @return int value of progress bar.
     */
    public static int getBarValue() {
        return progressBar.getValue();
    }

    /**
     * Load the maps from memory given the dates. This is the method used for
     * both features in Explore Data.
     */
    @SuppressWarnings("unchecked")
    public void loadMapsFromMem() {
        LocalDate date;
        LocalDate date2;

        if (!singleData) {
            date = datePicker.getDate();
            date2 = datePicker2.getDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Dyy");
            formattedString = date.format(formatter);

            if (date2 != null) {
                formattedString2 = date2.format(formatter);
                List<String> dates =
                    DataHandling.dateDiff(formattedString, formattedString2);
                @SuppressWarnings("rawtypes")
                List<Map> maps = DataHandling.loadMaps(dates);
                try {
                    DataHandling.writeDates(maps, dates);
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            else {
                try {
                    DataHandling.acceptDate(formattedString, false);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            int dateR = 0;
            date = datePicker_1.getDate();
            date2 = datePicker_2.getDate();
            Map<String, Long> comboMap = new HashMap<String, Long>();

            String textInput = textField.getText(); // get prog name from app
            singleData = false;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Dyy");
            formattedString = date.format(formatter);

            if (date2 != null) {
                formattedString2 = date2.format(formatter);
                System.out.println(formattedString + " " + formattedString2);
                List<String> dates =
                    DataHandling.dateDiff(formattedString, formattedString2);
                System.out.print(dates);
                System.out.print(
                    DataHandling.dateDiff(formattedString, formattedString2));
                dateR = dates.size();

                @SuppressWarnings("rawtypes")
                List<Map> maps = DataHandling.loadMaps(dates);
                for (Map<String, Long> currentMap : maps) {
                    for (Map.Entry<String, Long> entry : currentMap
                        .entrySet()) {
                        String key = entry.getKey();
                        Long current = comboMap.get(key);

                        if (current == null) {
                            comboMap.put(key, entry.getValue());
                        }
                        else {
                            comboMap.put(key, entry.getValue() + current);
                        }

                    }
                }
            }
            else {
                dateR = 1;

                try {
                    comboMap = DataHandling.acceptDate(formattedString, true);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Map<String, Long> loadedCurrentMap = new HashMap<String, Long>();
            if (Main.getChecked()) {
                loadedCurrentMap = DataHandling.validateData(comboMap);
            }
            if (Main.getMode() == 3 || Main.getMode() == 2) {
                if (!Main.getChecked()) {
                    loadedCurrentMap = comboMap;
                }
                loadedCurrentMap =
                    DataHandling.validateWhatToDisplay(loadedCurrentMap);
            }

            else if (Main.getMode() == 1 && Main.getChecked()) {
                loadedCurrentMap = DataHandling.validateData(comboMap);
            }

            Map<String, Double> displayMap =
                TimeConvert.convertTime(loadedCurrentMap);
            
            System.out.println(loadedCurrentMap);
            System.out.println(displayMap);

            Double time = displayMap.get(textInput);

            String strTime = "";
            
            if (time == null) {
            	JOptionPane.showMessageDialog(null,
                        "No time found, check input or display mode!");
            } else {
                if (TimeConvert.getUnit().equals("Time (Written)")) {
                    strTime = DataHandling.convertToWritten(time);
                }
                else {
                    strTime = time.toString();
                }

            }

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
