package gui;

import java.awt.Font;
import java.awt.SystemColor;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;

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
    public static JSpinner spinner = new JSpinner();
    private static String[] displayTypes = {"Pie Chart",
        "Chart 2", "Chart 3",
        "Chart 4"};
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static JComboBox displayOptions = new JComboBox(displayTypes);

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
            new MigLayout("", "[156.00,grow][817.00,grow]", "[][503.00,grow]"));

        JLabel lblGraphSettings = new JLabel("Graph Settings and controls");
        getContentPane().add(lblGraphSettings, "cell 0 0,alignx center");
        lblGraphSettings.setFont(new Font("Verdana", Font.PLAIN, 11));

        JPanel controlPanel = new JPanel();
        getContentPane().add(controlPanel, "cell 0 1,grow");
        controlPanel
            .setLayout(
                new MigLayout("", "[94.00][231.00,grow,left][-163.00][-64.00]",
                    "[18.00][][][][32.00][][][][][][][][][][][][][][grow]"));

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

        JLabel lblChooseADay = new JLabel("Choose a day to load");
        controlPanel.add(lblChooseADay, "cell 1 2");

        JSpinner spinner_1 = new JSpinner();
        spinner_1.setModel(new SpinnerDateModel(new Date(1539230400000L),
            new Date(1539230400000L), null, Calendar.DAY_OF_YEAR));
        controlPanel.add(spinner_1, "cell 1 3");

        JTextPane txtpnListIndividualPrograms = new JTextPane();
        txtpnListIndividualPrograms.setEditable(false);
        txtpnListIndividualPrograms.setBackground(SystemColor.controlHighlight);
        txtpnListIndividualPrograms
            .setText(
                "List individual programs you want to view. List them separated by commands like this: ProductivityPlus, Spotify, ...");
        controlPanel.add(txtpnListIndividualPrograms, "cell 1 4");

        JEditorPane editorPane = new JEditorPane();
        controlPanel.add(editorPane, "cell 1 5,grow");

        JTextPane txtpnSelectAGraph = new JTextPane();
        txtpnSelectAGraph.setEditable(false);
        txtpnSelectAGraph.setText("Select a graph to output as .jpg");
        txtpnSelectAGraph.setBackground(SystemColor.controlHighlight);
        controlPanel.add(txtpnSelectAGraph, "cell 1 7,grow");

        controlPanel.add(displayOptions, "cell 1 8,growx");

        JButton btnExport = new JButton("Export");
        controlPanel.add(btnExport, "cell 1 9");

        JButton btnOpenjpgLocation = new JButton("Open .jpg location");
        controlPanel.add(btnOpenjpgLocation, "cell 1 10");

        JButton btnSaveInput = new JButton("Save input");
        controlPanel.add(btnSaveInput, "cell 1 15");

        JButton btnRefreshGraphs = new JButton("Refresh graphs");
        controlPanel.add(btnRefreshGraphs, "cell 1 16");

        JTextPane txtpnnoticeHitSave_1 = new JTextPane();
        txtpnnoticeHitSave_1.setEditable(false);
        txtpnnoticeHitSave_1.setText(
            "(Notice: Hit save input if any changes are made, otherwise today's data is loaded.");
        txtpnnoticeHitSave_1.setBackground(SystemColor.controlHighlight);
        controlPanel.add(txtpnnoticeHitSave_1, "cell 1 17,grow");

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

    public static int getNumProgs() {
        return 5;
    }
}
