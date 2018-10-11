package gui;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import graphs.PieChart;
import net.miginfocom.swing.MigLayout;

/**
 * Gui class for Graphical Output window.
 * 
 * @author Austin Ayers
 * @version 9/28/18
 * 
 */
public class GraphicalOutputGui extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Launch the graphical output pop up window.
     */
    public static void newWindow() {
        try {
            GraphicalOutputGui dialog = new GraphicalOutputGui();
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
    public GraphicalOutputGui() {
        setAlwaysOnTop(true);
        setTitle("Graphs");
        setBounds(100, 100, 994, 633);
        getContentPane().setLayout(
            new MigLayout("", "[156.00,grow][817.00,grow]", "[503.00,grow]"));

        JPanel controlPanel = new JPanel();
        getContentPane().add(controlPanel, "cell 0 0,grow");
        controlPanel.setLayout(new MigLayout("", "[]", "[][][][][][][][]"));

        JLabel lblGraphSettings = new JLabel("Graph Settings and controls");
        controlPanel.add(lblGraphSettings, "cell 0 0");

        JPanel graphPanel = new JPanel();
        getContentPane().add(graphPanel, "cell 1 0,grow");
        graphPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        graphPanel.add(tabbedPane, "cell 0 0,grow");

        JPanel panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);
        panel.add(PieChart.createDemoPanel());

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);

        JPanel panel_2 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_2, null);

        JPanel panel_3 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_3, null);

        JPanel panel_4 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_4, null);

        JPanel panel_5 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_5, null);

        JPanel panel_6 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_6, null);

    }
}
