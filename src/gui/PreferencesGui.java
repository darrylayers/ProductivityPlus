package gui;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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
 * @version 9/25/18
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
    private JCheckBox idleTimerCheckBox;
    private JSpinner spinner = new JSpinner();

    /**
     * Launch the Preferences popup window.
     */
    public static void newWindow() {
        try {
            PreferencesGui dialog = new PreferencesGui();
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
    @SuppressWarnings("deprecation")
    public PreferencesGui() {
        setTitle("Preferences");
        setBounds(100, 100, 450, 300);

        // ************** Frame panels and panes ************** //
        getContentPane()
            .setLayout(new MigLayout("", "[434px]", "[228px][33px]"));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, "cell 0 0,grow");
        contentPanel.setLayout(
            new MigLayout("", "[217.00,grow][][]", "[][63.00][grow][][grow]"));

        JPanel prefPanel = new JPanel();
        contentPanel.add(prefPanel, "cell 0 1,grow");
        prefPanel.setLayout(new MigLayout("", "[][][]", "[][][]"));

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, "cell 0 1,growx,aligny top");

        JPanel autoRestartPanel = new JPanel();
        contentPanel.add(autoRestartPanel, "cell 0 2,grow");
        autoRestartPanel.setLayout(new MigLayout("", "[160px][21px]", "[21px]"));
        // ************** Frame panels and panes ************** //

        // ************** Program Timer Preferences Label ************** //
        JLabel lblProgramTimerPreferences =
            new JLabel("Program Timer Preferences");
        contentPanel.add(lblProgramTimerPreferences, "cell 0 0");
        // ************** Program Timer Preferences Label ************** //

        // ************** Idle Interval Label ************** //
        JLabel lblIdleInterval = new JLabel("Idle Interval");
        prefPanel.add(lblIdleInterval, "cell 0 1");
        lblIdleInterval.setEnabled(getIdleChecked());
        // ************** Idle Interval Label ************** //

        // ************** Idle Spinner ************** //
        prefPanel.add(spinner, "cell 1 1");
        spinner.setEnabled(getIdleChecked());
        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                setIdleTimer();
            }
        });
        spinner.setModel(new SpinnerNumberModel(getIdleTimer(), new Long(0),
            null, new Long(1)));
        // ************** Idle Spinner ************** //

        // ************** Minutes Label ************** //
        JLabel lblMinutes = new JLabel("(minutes)");
        prefPanel.add(lblMinutes, "cell 2 1");
        lblMinutes.setEnabled(getIdleChecked());
        // ************** Minutes Label ************** //

        // ************** Idle Check box ************** //
        idleTimerCheckBox = new JCheckBox("Idle Timer");
        idleTimerCheckBox.setToolTipText(
            "If enabled, the program timer will stop timing when the mouse is enactive for the selected idle interval.");
        prefPanel.add(idleTimerCheckBox, "cell 0 0");
        idleTimerCheckBox.setSelected(getIdleChecked());
        // ************** Idle Check box ************** //

        // ************** Auto Restart Text ************** //
        JLabel lblAutorestartTimerOn =
            new JLabel("Auto-Restart Timer on movement");
        lblAutorestartTimerOn.setToolTipText("This doesn't work.");
        lblAutorestartTimerOn.setEnabled(getIdleChecked());
        autoRestartPanel.add(lblAutorestartTimerOn,
            "cell 0 0,alignx left,aligny center");
        // ************** Auto Restart Text ************** //

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
        autoRestartPanel.add(autoRestartCheckBox, "cell 1 0,alignx left,aligny top");
        // ************** Auto Restart Check Button ************** //

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
        // ************** Idle Check Box Listener ************** //

        // ************** Save Button ************** //
        JButton saveButton = new JButton("Save");
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                dispose();
            }
        });
        saveButton.setActionCommand("Save");
        buttonPane.add(saveButton);
        getRootPane().setDefaultButton(saveButton);
        // ************** Save Button ************** //

        // ************** Cancel Button ************** //
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                dispose();
            }
        });
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);
        // ************** Cancel Button ************** //

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
