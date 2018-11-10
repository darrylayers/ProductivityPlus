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
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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

    private static final long serialVersionUID = 3774721756310500262L;
    private JPanel pie = new JPanel();
    private JPanel panel = new JPanel();
    public static JSpinner spinner = new JSpinner();
    private static Preferences prefs =
        Preferences.userRoot().node("GraphGui");
    private static final String NUM_DISPLAY = "display";
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

        JLabel secretLabel = new JLabel("");
        controlPanel.add(secretLabel, "cell 1 16");

        JPanel panel_7 = new JPanel();
        controlPanel.add(panel_7, "cell 1 0,alignx left,growy");

        JLabel lblNumberOfItmes = new JLabel("Number of items");
        panel_7.add(lblNumberOfItmes);
        lblNumberOfItmes.setToolTipText(
            "This is the number of items that will be plotted.");
        lblNumberOfItmes.setFont(new Font("Verdana", Font.PLAIN, 11));

        panel_7.add(spinner);
        spinner.setModel(
            new SpinnerNumberModel(getNumProgs(), new Integer(1), null,
                new Integer(1)));

        JButton btnRefreshGraphs = new JButton("Refresh graphs");
        btnRefreshGraphs.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {

                panel.remove(pie);
                pie = PieChart.createDemoPanel();
                panel.add(pie);
                secretLabel.setText("  ");
                secretLabel.setText("");

            }
        });
        controlPanel.add(btnRefreshGraphs, "cell 1 1,grow");

        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                setProgCount();
                panel.remove(pie);
                pie = PieChart.createDemoPanel();
                panel.add(pie);
                secretLabel.setText("  ");
                secretLabel.setText("");
            }
        });

        JPanel graphPanel = new JPanel();
        getContentPane().add(graphPanel, "cell 1 1,grow");
        graphPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        graphPanel.add(tabbedPane, "cell 0 0,grow");

        tabbedPane.addTab("Pie Chart", null, panel, null);
        pie = PieChart.createDemoPanel();
        panel.add(pie);

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
}
