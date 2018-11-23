package gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
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
        setBounds(100, 100, 625, 667);

        // Frame panels and panes
        getContentPane()
            .setLayout(new MigLayout("", "[434px,grow]", "[228px,grow]"));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, "cell 0 0,grow");
        contentPanel
            .setLayout(
                new MigLayout("", "[513px,grow]", "[360.00px,grow][grow]"));

        // About panel
        JPanel aboutPanel = new JPanel();
        aboutPanel.setBorder(
            new EtchedBorder(EtchedBorder.LOWERED, null, null));
        contentPanel.add(aboutPanel, "cell 0 0,grow");
        aboutPanel.setLayout(
            new MigLayout("", "[grow][][]", "[][353.00][grow,fill][grow]"));

        // How to use label
        JLabel lblHowToUse = new JLabel(
            "How to use ProductivityPlus - What does each button do?");
        lblHowToUse.setFont(new Font("Tahoma", Font.BOLD, 12));
        aboutPanel.add(lblHowToUse, "cell 0 0");

        // Text areas
        JTextPane txtpnPressstartTimer = new JTextPane();
        txtpnPressstartTimer.setEditable(false);
        txtpnPressstartTimer.setBackground(new Color(240, 240, 240));
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

        JLabel futureFeatures = new JLabel("Future features:");
        futureFeatures.setFont(new Font("Tahoma", Font.BOLD, 12));
        aboutPanel.add(futureFeatures, "cell 0 2");

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setText(
            "Improved program consolidation to allow grouping of child programs. \n \n "
                + "More graphs to view data. \n \n"
                + "I would also like to work the main GUI window. \n \n"
                + "Idle timer to stop the program when the user is idle.");
        textPane.setBackground(SystemColor.menu);
        aboutPanel.add(textPane, "cell 0 3,grow");

        JEditorPane dtrpnOpenHowTo = new JEditorPane();
        dtrpnOpenHowTo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                URL url = null;
                try {
                    // TODO: Change the url to the how to video url
                    url = new URL("http://austinayers.com/");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                openWebpage(url);

            }
        });
        dtrpnOpenHowTo.setEditable(false);
        dtrpnOpenHowTo.setBackground(new Color(240, 240, 240));

        dtrpnOpenHowTo.setText("Check out the how to video (click to view)");
        contentPanel.add(dtrpnOpenHowTo, "cell 0 1,grow");
    }

    public static boolean openWebpage(URI uri) {
        Desktop desktop =
            Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean openWebpage(URL url) {
        try {
            return openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }
}
