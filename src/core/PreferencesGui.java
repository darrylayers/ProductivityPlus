package core;

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
 */
public class PreferencesGui extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private static Preferences prefs =
        Preferences.userRoot().node("PreferencesGui");

    private final static String IDLE_TIMER = "idleValue";
    private final static String IDLE_CHECK = "idleCheck";
    private JCheckBox chckbxNewCheckBox;
    private JSpinner spinner = new JSpinner();

    /**
     * Launch the application.
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
    public PreferencesGui() {
        setTitle("Preferences");
        setBounds(100, 100, 450, 300);
        getContentPane()
            .setLayout(new MigLayout("", "[434px]", "[228px][33px]"));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, "cell 0 0,grow");
        contentPanel.setLayout(new MigLayout("", "[][][]", "[][][][]"));

        JLabel lblProgramTimerPreferences =
            new JLabel("Program Timer Preferences");
        contentPanel.add(lblProgramTimerPreferences, "cell 0 0");

        JLabel lblIdleInterval = new JLabel("Idle Interval");
        lblIdleInterval.setEnabled(getIdleChecked());
        contentPanel.add(lblIdleInterval, "cell 0 3");

        spinner.setEnabled(getIdleChecked());
        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                setIdleTimer();
            }
        });

        spinner.setModel(new SpinnerNumberModel(getIdleTimer(), new Long(0),
            null, new Long(1)));
        contentPanel.add(spinner, "cell 1 3");

        JLabel lblminutes = new JLabel("(minutes)");
        lblminutes.setEnabled(getIdleChecked());
        contentPanel.add(lblminutes, "cell 2 3");

        // ******* Idle Check bBx ******* //
        chckbxNewCheckBox = new JCheckBox("Idle Timer");
        chckbxNewCheckBox.setSelected(getIdleChecked());
        chckbxNewCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                setIdleChecked();
                if (!getIdleChecked()) {

                    lblIdleInterval.setEnabled(false);
                    lblminutes.setEnabled(false);
                    spinner.setEnabled(false);

                }
                else {
                    lblIdleInterval.setEnabled(true);
                    lblminutes.setEnabled(true);
                    spinner.setEnabled(true);

                }

            }
        });
        contentPanel.add(chckbxNewCheckBox, "cell 0 2");

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, "cell 0 1,growx,aligny top");

        // ******* Save Button ******* //
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

        // ******* Cancel Button ******* //
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                dispose();
            }
        });
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);

    }

    public void setIdleTimer() {
        Long idleValue = (Long) spinner.getValue();
        prefs.putLong(IDLE_TIMER, idleValue);
    }

    public static Long getIdleTimer() {
        return prefs.getLong(IDLE_TIMER, 1);
    }

    public void setIdleChecked() {
        if (getIdleChecked()) {
            prefs.putBoolean(IDLE_CHECK, false);
        }
        else {
            prefs.putBoolean(IDLE_CHECK, true);
        }
    }

    public static boolean getIdleChecked() {
        return prefs.getBoolean(IDLE_CHECK, false);
    }

}
