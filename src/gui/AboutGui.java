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
            "Press 'Start Timer' to begin the program timer. This will track every process you open and log how"
                + " long you are using it inside an Excel file in the same directory that this program is saved it."
                + " The Excel file is named ProductivityPlusData.xlsx");
        aboutPanel.add(txtpnPressstartTimer, "cell 0 1,grow");

        JTextPane txtpnYouCanSet = new JTextPane();
        txtpnYouCanSet.setEditable(false);
        txtpnYouCanSet.setText(
            "You can set some preferences under the Preferences menu. Currently you can choose to stop the "
                + "program if the program itself stops detecting mouse movement.");
        aboutPanel.add(txtpnYouCanSet, "cell 0 2,grow");
    }
}
