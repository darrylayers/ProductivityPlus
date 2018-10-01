package GUI;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import core.DataHandling;
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
    String formattedString;

    /**
     * Launch the About pop up window.
     */
    public static void newWindow() {
        try {
            ExploreDataGui dialog = new ExploreDataGui();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
        setAlwaysOnTop(true);
        setTitle("Explore Data");
        setBounds(100, 100, 450, 300);

        // ************** Frame panels and panes ************** //
        getContentPane()
            .setLayout(new MigLayout("", "[434px]", "[228px][33px]"));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, "cell 0 0,grow");
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

        JPanel panel = new JPanel();
        panel.setBorder(
            new EtchedBorder(EtchedBorder.LOWERED, null, null));
        contentPanel.add(panel, "cell 0 1,grow");
        panel.setLayout(
            new MigLayout("", "[83.00,grow][][]", "[][][55.00,grow][grow]"));

        datePickerSettings.setFormatForDatesBeforeCommonEra("MM/dd/yyyy");
        datePickerSettings.setFormatForDatesCommonEra("MM/dd/yyyy");
        panel.add(datePicker, "");
        datePicker.setDateToToday();

        datePickerSettings2.setFormatForDatesBeforeCommonEra("MM/dd/yyyy");
        datePickerSettings2.setFormatForDatesCommonEra("MM/dd/yyyy");
        panel.add(datePicker2, "");

        JPanel panel_1 = new JPanel();
        contentPanel.add(panel_1, "cell 0 2,grow");
        panel_1.setLayout(new MigLayout("", "[][]", "[][]"));

        JRadioButton rdbtnExcelExport = new JRadioButton("Excel export");
        rdbtnExcelExport.setSelected(true);
        panel_1.add(rdbtnExcelExport, "cell 0 0");

        JButton btnOpenOutput = new JButton("Open Output");
        JButton btnCreateExportFile = new JButton("Create export file");
        btnCreateExportFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                LocalDate date = datePicker.getDate();
                // Pass the date(s) to DateHandling.java
                // TODO: Check for second date.
                @SuppressWarnings("unused")
                SimpleDateFormat dateFormatter =
                    new SimpleDateFormat("MMDDyy");

                DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("MMDDyy");
                formattedString = date.format(formatter);
                btnOpenOutput.setEnabled(true);
                // System.out.println(formattedString);
                try {
                    DataHandling.acceptDate(formattedString);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        btnCreateExportFile.setToolTipText(
            "File is exported to Productivity Plus installation directory");
        panel_1.add(btnCreateExportFile, "cell 0 1");

        btnOpenOutput.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                try {
                    Desktop.getDesktop().open(new File(
                        "ProductivityPlusData" + formattedString + ".xlsx"));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btnOpenOutput.setEnabled(false);
        panel_1.add(btnOpenOutput, "cell 1 1");
    }
}
