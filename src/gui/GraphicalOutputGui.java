package gui;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import core.DataHandling;
import graphs.BarChart;
import graphs.LineChart;
import graphs.PieChart;
import net.miginfocom.swing.MigLayout;

/**
 * Gui class for Graphical Output window.
 * 
 * @author Austin Ayers
 * 
 */
public class GraphicalOutputGui extends JDialog {

    private static final long serialVersionUID = 3774721756310500262L;
    private JPanel pie = new JPanel();
    private JPanel bar = new JPanel();
    private JPanel line = new JPanel();
    private JPanel scatter = new JPanel();
    private JPanel piechartPanel = new JPanel();
    private JPanel bargraphPanel = new JPanel();
    private JPanel linechartPanel = new JPanel();
    private JPanel scatterplotPanel = new JPanel();
    private JTabbedPane tabbedPane;
    public static JSpinner spinner = new JSpinner();
    private static Preferences prefs =
        Preferences.userRoot().node("GraphGui");
    private static final String NUM_DISPLAY = "display";
    @SuppressWarnings("unused")
    private static String[] displayTypes = {"Pie Chart",
        "Bar Graph", "Chart 3",
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
        setBounds(100, 100, 1030, 634);

        // ************** Frame panels and panes ************** //
        getContentPane().setLayout(
            new MigLayout("", "[184.00][817.00,grow]", "[][503.00,grow]"));

        JLabel lblGraphSettings = new JLabel("Graph Settings and controls");
        getContentPane().add(lblGraphSettings, "cell 0 0,alignx center");
        lblGraphSettings.setFont(new Font("Verdana", Font.PLAIN, 11));

        JPanel controlPanel = new JPanel();
        getContentPane().add(controlPanel, "cell 0 1,growy");
        controlPanel
            .setLayout(
                new MigLayout("", "[204.00,grow][-58.00][-84.00][71.00]",
                    "[18.00][][8.00][][][][][][][]"));

        JTextPane txtpnDatePickerFor = new JTextPane();
        txtpnDatePickerFor.setText("Date picker for range to use ");
        controlPanel.add(txtpnDatePickerFor, "cell 0 2");

        JLabel lblStartDate = new JLabel("Start date:");
        controlPanel.add(lblStartDate, "cell 0 3");

        DatePicker datePicker = new DatePicker((DatePickerSettings) null);
        controlPanel.add(datePicker, "cell 0 4,growx");

        JLabel lblEndDate = new JLabel("End date:");
        controlPanel.add(lblEndDate, "cell 0 5");

        DatePicker datePicker_1 = new DatePicker((DatePickerSettings) null);
        controlPanel.add(datePicker_1, "cell 0 6,growx");

        JLabel lblclickRefreshTo =
            new JLabel("(Click refresh to update with new dates)");
        controlPanel.add(lblclickRefreshTo, "cell 0 7");

        JTextPane txtpnHelpfulTipYou = new JTextPane();
        txtpnHelpfulTipYou.setText(
            "Helpful tip: You can use the scroll wheel on the graphs to rotate. \n\n"
                + "You can also right-click the graphs for more options, including the option to save them.");
        txtpnHelpfulTipYou.setEditable(false);
        controlPanel.add(txtpnHelpfulTipYou, "cell 0 8,grow");

        JPanel spinnerPanel = new JPanel();
        controlPanel.add(spinnerPanel, "cell 0 0,alignx left,growy");

        JLabel lblNumberOfItmes = new JLabel("Number of items");
        spinnerPanel.add(lblNumberOfItmes);
        lblNumberOfItmes.setToolTipText(
            "This is the number of items that will be plotted.");
        lblNumberOfItmes.setFont(new Font("Verdana", Font.PLAIN, 11));

        spinnerPanel.add(spinner);
        spinner.setModel(
            new SpinnerNumberModel(getNumProgs(), new Integer(1), null,
                new Integer(1)));

        JButton btnRefreshGraphs = new JButton("Refresh graphs");
        btnRefreshGraphs.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                tabbedPane.removeAll();
                buildGraphs();

            }
        });
        controlPanel.add(btnRefreshGraphs, "cell 0 1,grow");

        JLabel secretLabel = new JLabel("");
        controlPanel.add(secretLabel, "cell 0 9");

        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                setProgCount();
                tabbedPane.removeAll();
                buildGraphs();
                secretLabel.setText("  ");
                secretLabel.setText("");
            }
        });

        JPanel graphPanel = new JPanel();
        getContentPane().add(graphPanel, "cell 1 1,grow");
        graphPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        graphPanel.add(tabbedPane, "cell 0 0,grow");

        buildGraphs();

    }

    /**
     * Get the number of progs to display in the graph.
     * 
     * @return 5, or the size of the ordered map.
     */
    public static int getNumProgs() {
        int size = DataHandling.orderedMap().size();
        if (size >= getProgCount()) {
            return getProgCount();
        }
        return size;
    }

    /**
     * Set the Idle value from its spinner value.
     */
    public void setProgCount() {
        int displayCount = (int) spinner.getValue();
        prefs.putInt(NUM_DISPLAY, displayCount);
    }

    /**
     * Returns the Idle timer value from its saved preference, if never saved
     * then set to 1 by default.
     * 
     * @return returns long idle value, 1 if nothing is saved.
     */
    public static int getProgCount() {
        return prefs.getInt(NUM_DISPLAY, 1);
    }

    public void buildGraphs() {
        JPanel piechartPanel = new JPanel();
        pie = PieChart.createDemoPanel();
        piechartPanel.add(pie);
        tabbedPane.addTab("Pie Chart", null, piechartPanel, null);

        JPanel bargraphPanel = new JPanel();
        tabbedPane.addTab("Bar Graph", null, bargraphPanel, null);
        bar = BarChart.createDemoPanel();
        bargraphPanel.add(bar);

        JPanel linechartPanel = new JPanel();
        tabbedPane.addTab("Line Chart", null, linechartPanel, null);
        line = LineChart.createDemoPanel();
        linechartPanel.add(line);
    }
}
