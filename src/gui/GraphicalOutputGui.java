package gui;

import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;

import core.DataHandling;
import graphs.PieChart;
import net.miginfocom.swing.MigLayout;

/**
 * Gui class for Graphical Output window.
 * 
 * @author Austin Ayers
 * 
 */
public class GraphicalOutputGui extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static JSpinner spinner = new JSpinner();
    @SuppressWarnings("unused")
    private static String[] displayTypes = {"Pie Chart",
        "Chart 2", "Chart 3",
        "Chart 4"};

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
        setBounds(100, 100, 1084, 582);

        // ************** Frame panels and panes ************** //
        getContentPane().setLayout(
            new MigLayout("", "[156.00,grow][817.00,grow]", "[][503.00,grow]"));

        JLabel lblGraphSettings = new JLabel("Graph Settings and controls");
        getContentPane().add(lblGraphSettings, "cell 0 0,alignx center");
        lblGraphSettings.setFont(new Font("Verdana", Font.PLAIN, 11));

        JPanel controlPanel = new JPanel();
        getContentPane().add(controlPanel, "cell 0 1,grow");
        controlPanel
            .setLayout(
                new MigLayout("", "[94.00][231.00,grow,left][-163.00][-64.00]",
                    "[18.00][][][][32.00][][][][][][][][][][][][][][]"));

        JPanel panel_7 = new JPanel();
        controlPanel.add(panel_7, "cell 1 0,alignx left,growy");

        JLabel lblNumberOfItmes = new JLabel("Number of items");
        panel_7.add(lblNumberOfItmes);
        lblNumberOfItmes.setToolTipText(
            "This is the number of items that will be plotted.");
        lblNumberOfItmes.setFont(new Font("Verdana", Font.PLAIN, 11));

        panel_7.add(spinner);
        spinner.setModel(
            new SpinnerNumberModel(new Integer(5), null, null, new Integer(1)));

        JPanel graphPanel = new JPanel();
        getContentPane().add(graphPanel, "cell 1 1,grow");
        graphPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        graphPanel.add(tabbedPane, "cell 0 0,grow");

        JPanel panel = new JPanel();
        tabbedPane.addTab("Pie Chart", null, panel, null);
        panel.add(PieChart.createDemoPanel());

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);

    }

    /**
     * Get the number of progs to display in the graph.
     * 
     * @return 5, or the size of the ordered map.
     */
    public static int getNumProgs() {
        int size = DataHandling.orderedMap().size();
        if (size >= 5) {
            return 5;
        }
        return size;
    }
}
