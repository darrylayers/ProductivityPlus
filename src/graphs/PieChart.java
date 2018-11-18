package graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;

import core.DataHandling;
import gui.GraphicalOutputGui;

/**
 * PieChart.java This class is used to create a pie chart of the top 5 entries
 * from whatever data is being displayed in the table in the main GUI.
 * 
 * @author Austin Ayers
 */
public class PieChart extends ApplicationFrame {

    public static final long serialVersionUID = -617075449581724970L;

    public static Map<String, Double> orderedMap; // this is the ordered map of
                                                  // entries to graph

    /**
     * Constuctor for the chart.
     * 
     * @param paramString,
     *            the chart title.
     * @return the pie chart itself.
     */
    public PieChart(String paramString) {
        super(paramString);
        JPanel localJPanel = createDemoPanel();
        localJPanel.setPreferredSize(new Dimension(1800, 1670));
        setContentPane(localJPanel);
    }

    /**
     * Create the data set for the pie chart by organizing the map.
     * 
     * @return the data set for the pie chart.
     */
    public static PieDataset createDataset() {
        // Create var with an ordered map
        orderedMap = DataHandling.orderedMap();
        // Create empty data set.
        DefaultPieDataset localDefaultPieDataset = new DefaultPieDataset();
        // If the map set is empty, return empty data set.
        if (orderedMap.size() == 0) {
            return localDefaultPieDataset;
        }
        // Create string array with size of list of progs
        String keyList[] = new String[GraphicalOutputGui.getProgCount()];
        // Find the last key
        String lKeyLast = "";
        int k = 1;
        int counter = 0;
        for (String key : orderedMap.keySet()) {
            lKeyLast = key;
        }
        keyList[0] = lKeyLast;
        String oldKey = "";
        // Create an array of the ordered list
        for (int p = 0; p < GraphicalOutputGui.getNumProgs() - 1; p++) {
            for (String key : orderedMap.keySet()) {
                if (key == keyList[counter]) {
                    keyList[k] = oldKey;
                    k++;
                    counter++;
                }
                oldKey = key;
            }
        }
        // Add how ever many desired data entries from the data array to the bar
        // chart data set.
        for (int i = 0; i < GraphicalOutputGui.getNumProgs(); i++) {
            localDefaultPieDataset.setValue(
                keyList[i], orderedMap.get(keyList[i]));
        }
        return localDefaultPieDataset;
    }

    /**
     * Create and return the pie chart from the data.
     * 
     * @param the
     *            pie chart's data set.
     * @return returns the actual pie chart.
     */
    public static JFreeChart createChart(PieDataset paramPieDataset) {
        // Chart title and axies
        JFreeChart localJFreeChart = ChartFactory.createPieChart(
            "Pie Chart for Data Displayed in Table", paramPieDataset,
            true, true, false);
        PiePlot localPiePlot = (PiePlot) localJFreeChart.getPlot();
        for (int j = 0; j < GraphicalOutputGui.getNumProgs() - 1; j++) {
            localPiePlot.setSectionPaint(
                orderedMap.entrySet().iterator().next().getKey(),
                new Color(160, 160, 255));
        }
        // No data message
        localPiePlot.setNoDataMessage("No data available");
        // Chart settings
        localPiePlot.setLabelGenerator(
            new StandardPieSectionLabelGenerator("{0} ({2} percent)"));
        localPiePlot.setLabelBackgroundPaint(new Color(220, 220, 220));
        localPiePlot.setSimpleLabels(true);
        localPiePlot.setInteriorGap(0.0D);
        return localJFreeChart;
    }

    /**
     * Create and return the panel the pie chart is in.
     * 
     * @return return the panel the pie chart is in.
     */
    public static JPanel createDemoPanel() {
        JFreeChart localJFreeChart = createChart(createDataset());
        ChartPanel localChartPanel = new ChartPanel(localJFreeChart);
        localChartPanel.setMouseWheelEnabled(true);
        return localChartPanel;
    }
}
