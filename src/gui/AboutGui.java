package gui;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

/**
 * Gui class for About window.
 * 
 * @author Austin Ayers
 * @version 9/28/18
 * 
 */
public class AboutGui extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    /**
     * Launch the About pop up window.
     */
    public static void newWindow() {
        try {
            AboutGui dialog = new AboutGui();
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
    public AboutGui() {
        setAlwaysOnTop(true);
        setTitle("About");
        setBounds(100, 100, 450, 300);

        // ************** Frame panels and panes ************** //
        getContentPane()
            .setLayout(new MigLayout("", "[434px]", "[228px][33px]"));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, "cell 0 0,grow");
        contentPanel.setLayout(
            new MigLayout("", "[217.00,grow][][]", "[][164.00][grow][][grow]"));

        JPanel aboutPanel = new JPanel();
        aboutPanel.setBorder(
            new EtchedBorder(EtchedBorder.LOWERED, null, null));
        contentPanel.add(aboutPanel, "cell 0 1,grow");
        aboutPanel.setLayout(
            new MigLayout("", "[grow][][]", "[][][55.00,grow][grow]"));

        JLabel lblHowToUse = new JLabel("How to use ProductivityPlus");
        aboutPanel.add(lblHowToUse, "cell 0 0");

        // ************** Text Areas ************** //
        JTextPane txtpnPressstartTimer = new JTextPane();
        txtpnPressstartTimer.setEditable(false);
        txtpnPressstartTimer.setText(
            "Press the Start button to begin tracking. ProductivityPlus will track everything you do and save it to a file ending in .map. You can view your data in the table on the main screen, or by exporting it into an Excel file. The Excel outputs are saved in the same directory as ProductivityPlus. The Excel files are titled by appending the day of the year to the year. So 10/9/18's data log files would export to ProductivityPlusData28218.xlsx");
        aboutPanel.add(txtpnPressstartTimer, "cell 0 1,grow");

        JTextPane txtpnYouCanSet = new JTextPane();
        txtpnYouCanSet.setEditable(false);
        txtpnYouCanSet.setText(
            "You can also edit how the program looks and outputs under the preferences menu.");
        aboutPanel.add(txtpnYouCanSet, "cell 0 2,grow");
    }
}
