package gui;

import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

/**
 * Gui class for About window.
 * 
 * @author Austin Ayers
 * 
 */
public class AboutGui extends JDialog {

    private static final long serialVersionUID = 3642805775576134791L;
    private final JPanel contentPanel = new JPanel();

    /**
     * Launch the About pop up window.
     */
    public static void newWindow() {
        try {
            AboutGui dialog = new AboutGui();
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            // Launch the window to match the parent window
            Main.setWindowLoc();
            dialog.setLocation(Main.getWindowLoc().x, Main.getWindowLoc().y);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    private AboutGui() {
        setTitle("About");
        setBounds(100, 100, 625, 559);

        // Frame panels and panes
        getContentPane()
            .setLayout(new MigLayout("", "[434px,grow]", "[228px,grow]"));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, "cell 0 0,grow");
        contentPanel
            .setLayout(new MigLayout("", "[513px,grow]", "[360.00px,grow]"));

        // About panel
        JPanel aboutPanel = new JPanel();
        aboutPanel.setBorder(
            new EtchedBorder(EtchedBorder.LOWERED, null, null));
        contentPanel.add(aboutPanel, "cell 0 0,grow");
        aboutPanel.setLayout(
            new MigLayout("", "[grow][][]", "[][grow]"));

        // How to use label
        JLabel lblHowToUse = new JLabel(
            "How to use ProductivityPlus - What does each button do?");
        aboutPanel.add(lblHowToUse, "cell 0 0");

        // Text areas
        JTextPane txtpnPressstartTimer = new JTextPane();
        txtpnPressstartTimer.setBackground(new Color(240, 240, 240));
        txtpnPressstartTimer.setEditable(false);
        txtpnPressstartTimer.setText(
            "Start Timer - Start recording what you're doing. \n \n"
                + "Stop Timer - Stop the timer. \n \n"
                + "Graphical Output - Graphically view the data currently displayed in the table. "
                + "Tip: Graphs can be right-clicked and saved, or rotated with the scrollwheel. \n \n"
                + "Explore Data - You can export data here or view stats about a single program. \n \n"
                + "Refresh Today's Data - Refreshes the main window table with today's data. \n \n"
                + "Consolidate Programs - If checked, the program filters the displayed data from your "
                + "list of programs inside of Edit->Program Consolidation. This is useful because with "
                + "this enabled you can combine many processes of the same application into one. \n \n"
                + "Load Table - Load data from a single date or date range into the displayed table, "
                + "this button is also used to refresh the table for displayed dates. \n \n"
                + "What to Display - Choose to display all data, or a select few to include or exlude. \n \n"
                + "Preferences->Program Output / Display Preferences - These options change how the "
                + "time is displayed or exported, keep in mind there is an option for each displaying and exporting. \n \n"
                + "NOTE: All features of this app use your settings from 'What to display'.");
        aboutPanel.add(txtpnPressstartTimer, "cell 0 1,grow");
    }
}
