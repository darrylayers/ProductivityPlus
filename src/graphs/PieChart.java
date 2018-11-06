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

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static Map<String, Double> orderedMap;

    /**
     * Constuctor for the chart.
     * 
     * @param paramString,
     *            the chart title.
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
    private static PieDataset createDataset() {
        orderedMap = DataHandling.orderedMap();
        DefaultPieDataset localDefaultPieDataset = new DefaultPieDataset();
        if (orderedMap.size() == 0) {
            return localDefaultPieDataset;
        }
        String keyList[] = new String[GraphicalOutputGui.getNumProgs()];
        String lKeyLast = "";
        int k = 1;
        int counter = 0;
        for (String key : orderedMap.keySet()) {
            lKeyLast = key;
        }
        keyList[0] = lKeyLast;
        String oldKey = "";

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
        for (int i = 0; i < GraphicalOutputGui.getNumProgs(); i++) {
            localDefaultPieDataset.setValue(
                keyList[i], orderedMap.get(keyList[i]));
        }
        return localDefaultPieDataset;
    }

    /**
     * Create and return the pie chart from the data.
     * 
     * @return returns the actual pie chart.
     * @param the
     *            pie chart's data set.
     */
    private static JFreeChart createChart(PieDataset paramPieDataset) {
        JFreeChart localJFreeChart = ChartFactory.createPieChart(
            "Pie Chart for " + DataHandling.getDate() + ".map", paramPieDataset,
            true, true, false);
        PiePlot localPiePlot = (PiePlot) localJFreeChart.getPlot();

        for (int j = 0; j < GraphicalOutputGui.getNumProgs() - 1; j++) {
            localPiePlot.setSectionPaint(
                orderedMap.entrySet().iterator().next().getKey(),
                new Color(160, 160, 255));
        }

        localPiePlot.setNoDataMessage("No data available");

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

    /**
     * This method returns the minimum number of values that we can graph.
     * Default 5.
     * 
     * @return 5, or less if less values are in map.
     */
    public static int checkValues() {
        int size = orderedMap.size();
        if (size < 5) {
            return size;
        }
        return 5;

    }

}
