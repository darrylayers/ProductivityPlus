package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

/**
 * Gui class for Preferences window.
 * 
 * @author Austin Ayers
 * 
 */
public class PreferencesGui extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private static Preferences prefs =
        Preferences.userRoot().node("PreferencesGui");
    private final static String IDLE_TIMER = "idleValue";
    private final static String IDLE_CHECK = "idleCheck";
    private final static String IDLE_AUTO_CHECK = "idleAutoCheck";
    private final static String DISPLAY_INDEX = "displayIndex";
    private final static String OUTPUT_INDEX = "outputIndex";
    private final static String DEC_VAL = "decVal";
    private JCheckBox idleTimerCheckBox;
    private JSpinner spinner = new JSpinner();
    private JSpinner numDecimalSpinner = new JSpinner();
    private static String[] exportTypes = {"Hours (ex: 1.3 hours)",
        "Minutes (ex: 95.2 minutes)", "Seconds (ex: 138 seconds)"};
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static JComboBox exportOptions = new JComboBox(exportTypes);
    private static String[] displayTypes = {"Hours (ex: 1.3 hours)",
        "Minutes (ex: 95.2 minutes)", "Seconds (ex: 138 seconds)",
        "Written (ex: 33 minutes 2 seconds)"};
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static JComboBox displayOptions = new JComboBox(displayTypes);

    /**
     * Launch the Preferences popup window.
     */
    public static void newWindow() {
        try {
            PreferencesGui dialog = new PreferencesGui();
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
    @SuppressWarnings("deprecation")
    public PreferencesGui() {
        setAlwaysOnTop(true);
        setTitle("Preferences");
        setBounds(100, 100, 502, 411);

        // ************** Frame panels and panes ************** //
        getContentPane()
            .setLayout(new MigLayout("", "[434px]", "[290.00px][33px]"));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, "cell 0 0,grow");
        contentPanel.setLayout(
            new MigLayout("", "[217.00,grow][][]",
                "[][63.00][33.00,grow][][40.00,grow][][40.00,grow][]"));

        JPanel prefTimerPanel = new JPanel();
        contentPanel.add(prefTimerPanel, "cell 0 1,grow");
        prefTimerPanel.setLayout(new MigLayout("", "[][][]", "[][][]"));

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, "cell 0 1,growx,aligny top");

        JPanel autoRestartPanel = new JPanel();
        contentPanel.add(autoRestartPanel, "cell 0 2");
        autoRestartPanel
            .setLayout(new MigLayout("", "[160px][21px]", "[21px]"));

        JPanel prefOutputPanel = new JPanel();
        contentPanel.add(prefOutputPanel, "cell 0 4,grow");
        prefOutputPanel.setLayout(new MigLayout("", "[][][]", "[]"));

        JPanel displayPanel = new JPanel();
        contentPanel.add(displayPanel, "cell 0 6,grow");
        displayPanel.setLayout(new MigLayout("", "[][][][][grow]", "[grow][]"));

        // ************** Program Timer Preferences Label ************** //
        JLabel lblProgramTimerPreferences =
            new JLabel("Program Timer Preferences");
        contentPanel.add(lblProgramTimerPreferences, "cell 0 0");

        // ************** Idle Interval Label ************** //
        JLabel lblIdleInterval = new JLabel("Idle Interval");
        prefTimerPanel.add(lblIdleInterval, "cell 0 1");
        lblIdleInterval.setEnabled(getIdleChecked());

        // ************** Idle Spinner ************** //
        prefTimerPanel.add(spinner, "cell 1 1");
        spinner.setEnabled(getIdleChecked());
        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                setIdleTimer();
            }
        });
        spinner.setModel(new SpinnerNumberModel(getIdleTimer(), new Long(0),
            null, new Long(1)));

        // ************** Minutes Label ************** //
        JLabel lblMinutes = new JLabel("(minutes)");
        prefTimerPanel.add(lblMinutes, "cell 2 1");
        lblMinutes.setEnabled(getIdleChecked());

        // ************** Output preferences Label ************** //
        JLabel lblProgramOutputPreferences = new JLabel(
            "Program Output Preferences (this does not change the display table)");
        contentPanel.add(lblProgramOutputPreferences, "cell 0 3");

        // ************** Idle Check box ************** //
        idleTimerCheckBox = new JCheckBox("Idle Timer");
        idleTimerCheckBox.setToolTipText(
            "If enabled, the program timer will stop timing when the mouse "
                + "is enactive for the selected idle interval.");
        prefTimerPanel.add(idleTimerCheckBox, "cell 0 0");
        idleTimerCheckBox.setSelected(getIdleChecked());

        // ************** Auto Restart Text ************** //
        JLabel lblAutorestartTimerOn =
            new JLabel("Auto-Restart Timer on movement");
        lblAutorestartTimerOn.setToolTipText("This doesn't work.");
        lblAutorestartTimerOn.setEnabled(getIdleChecked());
        autoRestartPanel.add(lblAutorestartTimerOn,
            "cell 0 0,alignx left,aligny center");

        // ************** Auto Restart Check Button ************** //
        JCheckBox autoRestartCheckBox = new JCheckBox("");
        autoRestartCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                setIdleAutoChecked();
            }
        });
        autoRestartCheckBox.setSelected(getIdleAutoChecked());
        autoRestartCheckBox.setEnabled(getIdleChecked());
        autoRestartCheckBox.setToolTipText(
            "If enabled, the timer restarts tracking when it detects mouse movement again");
        autoRestartPanel.add(autoRestartCheckBox,
            "cell 1 0,alignx left,aligny top");

        // ************** Display Table Preferences Label ************** //
        JLabel lblProgramDisplayTable = new JLabel(
            "Program display table settings (this refers to the table in main window)");
        contentPanel.add(lblProgramDisplayTable, "cell 0 5");

        // ************** Export Options listener ************** //
        exportOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                setExportIndex();
            }
        });
        exportOptions.setSelectedIndex(getExportIndex());
        prefOutputPanel.add(exportOptions, "cell 0 0,grow");

        // ************** Display options listener ************** //
        displayOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                setDisplayIndex();
            }
        });
        displayOptions.setSelectedIndex(getDisplayIndex());
        displayPanel.add(displayOptions, "cell 0 0,grow");

        JLabel lblNumberOfDecimal = new JLabel("Number of decimal places");
        lblNumberOfDecimal.setEnabled(false);
        displayPanel.add(lblNumberOfDecimal, "cell 3 0");
        numDecimalSpinner.setEnabled(false);

        // ************** Decimal Spinner ************** //
        displayPanel.add(numDecimalSpinner, "cell 4 0");
        numDecimalSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                setNumberSpinner();
            }
        });
        numDecimalSpinner.setValue(getNumberSpinner());

        // ************** Idle Check Box Listener ************** //
        idleTimerCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                setIdleChecked();
                if (!getIdleChecked()) {
                    lblIdleInterval.setEnabled(false);
                    lblMinutes.setEnabled(false);
                    spinner.setEnabled(false);
                    autoRestartCheckBox.setEnabled(false);
                    lblAutorestartTimerOn.setEnabled(false);
                }
                else {
                    lblIdleInterval.setEnabled(true);
                    lblMinutes.setEnabled(true);
                    spinner.setEnabled(true);
                    autoRestartCheckBox.setEnabled(true);
                    lblAutorestartTimerOn.setEnabled(true);
                }
            }
        });

        // ************** Save Button ************** //
        JButton btnSave = new JButton("Save");
        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                dispose();
            }
        });
        btnSave.setActionCommand("Save");
        buttonPane.add(btnSave);
        getRootPane().setDefaultButton(btnSave);

        // ************** Cancel Button ************** //
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                dispose();
            }
        });
        btnCancel.setActionCommand("Cancel");
        buttonPane.add(btnCancel);
    }

    /**
     * Save the display index to memory, this method saves the current selected
     * index.
     */
    public void setDisplayIndex() {
        int indexVal = displayOptions.getSelectedIndex();
        prefs.putInt(DISPLAY_INDEX, indexVal);
    }

    /**
     * Save the export index to memory, this method saves the current selected
     * index.
     */
    public void setExportIndex() {
        int indexVal = exportOptions.getSelectedIndex();
        prefs.putInt(OUTPUT_INDEX, indexVal);
    }

    /**
     * Get the display index from memory, if nothing is saved return 3.
     * 
     * @return returns saved value, if nothing is saved return 3.
     */
    public static int getDisplayIndex() {
        return prefs.getInt(DISPLAY_INDEX, 3);
    }

    /**
     * Get the export index from memory.
     * 
     * @return returns saved value, if nothing is saved return 2.
     */
    public static int getExportIndex() {
        return prefs.getInt(OUTPUT_INDEX, 2);
    }

    /**
     * Set the Idle value from its spinner value.
     */
    public void setNumberSpinner() {
        int numSpinner = (int) numDecimalSpinner.getValue();
        prefs.putInt(DEC_VAL, numSpinner);
    }

    /**
     * Returns the Idle timer value from its saved preference, if never saved
     * then set to 1 by default.
     * 
     * @return returns long idle value, 1 if nothing is saved.
     */
    public static int getNumberSpinner() {
        return prefs.getInt(DEC_VAL, 2);
    }

    /**
     * Set the Idle value from its spinner value.
     */
    public void setIdleTimer() {
        Long idleValue = (Long) spinner.getValue();
        prefs.putLong(IDLE_TIMER, idleValue);
    }

    /**
     * Returns the Idle timer value from its saved preference, if never saved
     * then set to 1 by default.
     * 
     * @return returns long idle value, 1 if nothing is saved.
     */
    public static Long getIdleTimer() {
        return prefs.getLong(IDLE_TIMER, 1);
    }

    /**
     * Set the Idle check box from its saved preference, if never saved then set
     * to false by default.
     */
    public void setIdleChecked() {
        if (getIdleChecked()) {
            prefs.putBoolean(IDLE_CHECK, false);
        }
        else {
            prefs.putBoolean(IDLE_CHECK, true);
        }
    }

    /**
     * Returns the status of the Idle check box, if the box does not have a save
     * preference it returns false by default.
     * 
     * @return boolean, false is no preference saved.
     */
    public static boolean getIdleChecked() {
        return prefs.getBoolean(IDLE_CHECK, false);
    }

    /**
     * Set the Idle Auto Restart check box from its saved preference, if never
     * saved then set to false by default.
     */
    public void setIdleAutoChecked() {
        if (getIdleAutoChecked()) {
            prefs.putBoolean(IDLE_AUTO_CHECK, false);
        }
        else {
            prefs.putBoolean(IDLE_AUTO_CHECK, true);
        }
    }

    /**
     * Returns the status of the Idle Auto Restart check box, if the box does
     * not have a save preference it returns false by default.
     * 
     * @return returns boolean, false if never saved.
     */
    public static boolean getIdleAutoChecked() {
        return prefs.getBoolean(IDLE_AUTO_CHECK, false);
    }
}
