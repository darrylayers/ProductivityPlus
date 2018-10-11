package gui;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

/**
 * FAQ class for About window.
 * 
 * @author Austin Ayers
 * @version 10/10/18
 * 
 */
public class FaqGui extends JDialog {

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
            FaqGui dialog = new FaqGui();
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
    public FaqGui() {
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

        JLabel lblHowToUse =
            new JLabel("ProductivityPlus FAQ / troubleshooting");
        aboutPanel.add(lblHowToUse, "cell 0 0");

    }
}
