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
@SuppressWarnings("serial")
public class PreferencesGui extends JDialog {

    private static final String IDLE_TIMER = "idleValue";
    private static final String IDLE_CHECK = "idleCheck";
    private static final String DISPLAY_INDEX = "displayIndex";
    private static final String OUTPUT_INDEX = "outputIndex";

    private static String[] exportTypes = {"Hours (ex: 1.3 hours)",
        "Minutes (ex: 95.2 minutes)", "Seconds (ex: 138 seconds)"};
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static JComboBox exportOptions = new JComboBox(exportTypes);
    private static String[] displayTypes = {"Hours (ex: 1.3 hours)",
        "Minutes (ex: 95.2 minutes)", "Seconds (ex: 138 seconds)",
        "Written (ex: 33 minutes 2 seconds)"};
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static JComboBox displayOptions = new JComboBox(displayTypes);
    private static int export;
    private static int display;
    private static Long idle;
    private static boolean idleStatus;
    private static Preferences prefs =
        Preferences.userRoot().node("PreferencesGui");

    private final JPanel contentPanel = new JPanel();

    private static JCheckBox idleTimerChkBx;
    private JSpinner spinner = new JSpinner();

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
            bundle();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save all of the current settings at launch to fields.
     */
    private static void bundle() {
        export = getExportIndex();
        display = getDisplayIndex();
        idle = getIdleTimer();
        idleStatus = getIdleChecked();
    }

    /**
     * Used when the user cancels any changes made. This method restores the
     * saved prefences to the original state before the user changed them.
     */
    private static void saveBundle() {
        prefs.putInt(OUTPUT_INDEX, getExport());
        prefs.putInt(DISPLAY_INDEX, getDisplay());
        prefs.putLong(IDLE_TIMER, getIdle());
        prefs.putBoolean(IDLE_CHECK, isIdleStatus());
    }

    // Simple getters for fields for each saved preference
    // these are used to restore old saves when the user
    // decides to cancel any saved changes made.
    public static int getExport() {
        return export;
    }

    public static int getDisplay() {
        return display;
    }

    public static Long getIdle() {
        return idle;
    }

    public static boolean isIdleStatus() {
        return idleStatus;
    }

    /**
     * Create the dialog.
     */
    public PreferencesGui() {
        setAlwaysOnTop(true);
        setTitle("Preferences");
        setBounds(100, 100, 502, 349);

        // ************** Frame panels and panes ************** //
        getContentPane()
            .setLayout(new MigLayout("", "[434px]", "[233.00px][33px]"));
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
        spinner.setToolTipText(
            "the number is how long the program idles before turning off in minutes.");

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
        idleTimerChkBx = new JCheckBox("Idle Timer");
        idleTimerChkBx.setToolTipText(
            "If enabled, the program timer will stop timing when the mouse "
                + "is enactive for the selected idle interval.");
        prefTimerPanel.add(idleTimerChkBx, "cell 0 0");
        idleTimerChkBx.setSelected(getIdleChecked());

        // ************** Display Table Preferences Label ************** //
        JLabel lblProgramDisplayTable = new JLabel(
            "Program Display Preferences (this refers to the table in main window)");
        contentPanel.add(lblProgramDisplayTable, "cell 0 5");
        exportOptions.setToolTipText("This changes the unit of time that "
            + "exported data is displayed in.");

        // ************** Export Options listener ************** //
        exportOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                setExportIndex();
            }
        });
        exportOptions.setSelectedIndex(getExportIndex());
        prefOutputPanel.add(exportOptions, "cell 0 0,grow");
        displayOptions.setToolTipText(
            "This changes the unit of time that displayed in-app data is displayed in.");

        // ************** Display options listener ************** //
        displayOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                setDisplayIndex();
            }
        });
        displayOptions.setSelectedIndex(getDisplayIndex());
        displayPanel.add(displayOptions, "cell 0 0,grow");

        // ************** Idle Check Box Listener ************** //
        idleTimerChkBx.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
            	boolean bool = prefs.getBoolean(IDLE_CHECK, false);
                prefs.putBoolean(IDLE_CHECK, !bool);
                if (!idleTimerChkBx.isSelected()) {
                    lblIdleInterval.setEnabled(false);
                    lblMinutes.setEnabled(false);
                    spinner.setEnabled(false);
                }
                else {
                    lblIdleInterval.setEnabled(true);
                    lblMinutes.setEnabled(true);
                    spinner.setEnabled(true);
                }
            }
        });

        // ************** Save Button ************** //
        JButton btnSave = new JButton("Save");
        btnSave.setToolTipText("Save changes");
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
        btnCancel.setToolTipText("Revert changes");
        btnCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                saveBundle();
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
        if (idleTimerChkBx.isSelected()) {
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


}
