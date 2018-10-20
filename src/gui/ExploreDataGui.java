package gui;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import core.DataHandling;
import core.ExcelWriter;
import net.miginfocom.swing.MigLayout;

/**
 * Gui class for About window.
 * 
 * @author Austin Ayers
 * @version 9/28/18
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
    private final JPanel panel = new JPanel();

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
        setTitle("Explore Data");
        setBounds(100, 100, 450, 334);

        // ************** Frame panels and panes ************** //
        getContentPane()
            .setLayout(
                new MigLayout("", "[grow][434px]", "[228px,grow][33px]"));

        getContentPane().add(tabbedPane, "cell 0 0,grow");
        tabbedPane.addTab("Explore large data", null, contentPanel, null);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(
            new MigLayout("", "[304.00,grow,left][-37.00][grow][][][][]",
                "[grow][70.00,grow][-60.00,grow]"));

        JTextPane txtpnSimplySelectA = new JTextPane();
        txtpnSimplySelectA.setEditable(false);
        txtpnSimplySelectA.setText(
            "Simply select a date or date range and choose your output method (Excel only for now). "
                + "The left calendar is the start date, the right calendar is the end date. "
                + "If you only want to view one date, only leave the right calendare blank with "
                + "the left one selected with the date you desire to examine.");
        contentPanel.add(txtpnSimplySelectA, "cell 0 0,grow");

        // ************** Dates ************** //

        JPanel datePanel = new JPanel();
        datePanel.setBorder(
            new EtchedBorder(EtchedBorder.LOWERED, null, null));
        contentPanel.add(datePanel, "cell 0 1,grow");
        datePanel.setLayout(
            new MigLayout("", "[83.00,grow][][]", "[][][55.00,grow][grow]"));
        datePanel.add(datePicker, "");
        datePicker.setDateToToday();
        datePanel.add(datePicker2, "");

        // ************** Exports ************** //

        JPanel exportPanel = new JPanel();
        contentPanel.add(exportPanel, "cell 0 2,grow");
        exportPanel
            .setLayout(new MigLayout("", "[][][]", "[][][33.00][22.00]"));
        rdbtnExcelExport.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                rdbtnodsopenOffice.setSelected(false);
                rdbtntxtExport.setSelected(false);
            }
        });

        rdbtnExcelExport.setSelected(true);
        exportPanel.add(rdbtnExcelExport, "cell 0 0");

        JButton btnOpenOutput = new JButton("Open Output");
        JButton btnCreateExportFile = new JButton("Create export file");
        btnCreateExportFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {

                ExploreDataGui.updateBar(0);
                btnOpenOutput.setEnabled(true);
                LocalDate date = datePicker.getDate();
                LocalDate date2 = datePicker2.getDate();
                // Pass the date(s) to DateHandling.java

                @SuppressWarnings("unused")
                SimpleDateFormat dateFormatter =
                    new SimpleDateFormat("Dyy");

                DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("Dyy");
                formattedString = date.format(formatter);
                if (!(date2 == null)) {
                    formattedString2 = date2.format(formatter);

                    List<String> dates =
                        DataHandling.dateDiff(formattedString,
                            formattedString2);
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
                        DataHandling.acceptDate(formattedString);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        rdbtnodsopenOffice.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                rdbtntxtExport.setSelected(false);
                rdbtnExcelExport.setSelected(false);

            }
        });
        exportPanel.add(rdbtnodsopenOffice, "cell 1 0");
        rdbtntxtExport.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                rdbtnodsopenOffice.setSelected(false);
                rdbtnExcelExport.setSelected(false);
            }
        });

        exportPanel.add(rdbtntxtExport, "cell 2 0");

        btnCreateExportFile.setToolTipText(
            "File is exported to Productivity Plus installation directory");
        exportPanel.add(btnCreateExportFile, "cell 0 1");

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
        exportPanel.add(btnOpenOutput, "cell 1 1");
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

        tabbedPane.addTab("Single program search", null, panel, null);
        panel.setLayout(new MigLayout("", "[]", "[]"));

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
}
