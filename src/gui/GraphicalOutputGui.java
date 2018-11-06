package gui;

import java.awt.Font;
import java.awt.SystemColor;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SpinnerDateModel;
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
    @SuppressWarnings("deprecation")
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

        // ************** Label ************** //
        JLabel lblChooseADay = new JLabel("Choose a day to load");
        controlPanel.add(lblChooseADay, "cell 1 2");

        // ************** Spinner ************** //
        JSpinner spinner_1 = new JSpinner();
        spinner_1.setModel(new SpinnerDateModel(new Date(1539230400000L),
            new Date(1539230400000L), null, Calendar.DAY_OF_YEAR));
        controlPanel.add(spinner_1, "cell 1 3");

        // ************** Text panes ************** //
        JTextPane txtpnListIndividualPrograms = new JTextPane();
        txtpnListIndividualPrograms.setEditable(false);
        txtpnListIndividualPrograms.setBackground(SystemColor.controlHighlight);
        txtpnListIndividualPrograms
            .setText(
                "List individual programs you want to view. List them separated by"
                    + " commands like this: ProductivityPlus, Spotify, ...");
        controlPanel.add(txtpnListIndividualPrograms, "cell 1 4");

        JEditorPane editorPane = new JEditorPane();
        controlPanel.add(editorPane, "cell 1 5,grow");

        JTextPane txtpnnoticeHitSave_1 = new JTextPane();
        txtpnnoticeHitSave_1.setEditable(false);
        txtpnnoticeHitSave_1.setText(
            "(Notice: Hit save input if any changes are made, otherwise today's data is loaded.");
        txtpnnoticeHitSave_1.setBackground(SystemColor.controlHighlight);
        controlPanel.add(txtpnnoticeHitSave_1, "cell 1 18,grow");

        // ************** Buttons ************** //
        JButton btnRefreshGraphs = new JButton("Refresh graphs");
        controlPanel.add(btnRefreshGraphs, "cell 1 17");

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
