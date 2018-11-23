package graphs;

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import core.DataHandling;
import core.TimeConvert;
import gui.GraphicalOutputGui;

/**
 * BarChart.java This class creates the bar chart that is displayed within
 * Graphical Output.
 * 
 * @author Austin
 *
 */
public class BarChart extends ApplicationFrame {

    private static final long serialVersionUID = -7957255223166147473L;

    /**
     * Constructor to create the chart panel
     * 
     * @param paramString,
     *            title of bar chart.
     * @return the bar chart itself.
     */
    private BarChart(String paramString) {
        super(paramString);
        JPanel localJPanel = createPanel();
        localJPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(localJPanel);
    }

    /**
     * Create the data set that is used in the bar chart.
     * 
     * @return the chart's data set.
     */
    private static CategoryDataset createDataset() {
        DefaultCategoryDataset localDefaultCategoryDataset =
            new DefaultCategoryDataset();

        String str = "";

        Map<String, Double> map = DataHandling.orderedMap(); // this is the
                                                             // ordered map of
                                                             // the top x
                                                             // entries.

        String keyList[] = new String[GraphicalOutputGui.getProgCount()];
        String lKeyLast = "";
        int k = 1;
        int counter = 0;
        // Find the last key
        for (String key : map.keySet()) {
            lKeyLast = key;
        }
        keyList[0] = lKeyLast;
        String oldKey = "";

        // Create an array of the ordered list
        for (int p = 0; p < GraphicalOutputGui.getNumProgs() - 1; p++) {
            for (String key : map.keySet()) {
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
            localDefaultCategoryDataset.addValue(map.get(keyList[i]), str,
                keyList[i]);
        }
        return localDefaultCategoryDataset;
    }

    /**
     * Create the bar chart itself from the data set.
     * 
     * @param paramCategoryDataset,
     *            the data for the bar chart.
     * @return the finished bar chart.
     */
    private static JFreeChart createChart(
        CategoryDataset paramCategoryDataset) {

        // Chart title and axies
        JFreeChart localJFreeChart =
            ChartFactory.createBarChart("Bar Chart for Data Displayed in Table",
                "Program", "" + TimeConvert.getUnit(),
                paramCategoryDataset,
                PlotOrientation.HORIZONTAL, false, true, false);
        CategoryPlot localCategoryPlot =
            (CategoryPlot) localJFreeChart.getPlot();

        // No data message
        localCategoryPlot.setNoDataMessage("No data available");

        // Chart settings
        localCategoryPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        localCategoryPlot.setRangePannable(true);
        BarRenderer localBarRenderer =
            (BarRenderer) localCategoryPlot.getRenderer();
        localBarRenderer.setItemLabelAnchorOffset(9.0D);
        localBarRenderer.setBaseItemLabelsVisible(true);
        localBarRenderer.setBaseItemLabelGenerator(
            new StandardCategoryItemLabelGenerator());
        localBarRenderer.setBaseToolTipGenerator(
            new StandardCategoryToolTipGenerator(
                "{1} = {2} " + TimeConvert.getUnit().substring(4,
                    TimeConvert.getUnit().length()),
                new DecimalFormat("0")));
        CategoryAxis localCategoryAxis = localCategoryPlot.getDomainAxis();
        localCategoryAxis.setCategoryMargin(0.25D);
        localCategoryAxis.setUpperMargin(0.02D);
        localCategoryAxis.setLowerMargin(0.02D);
        NumberAxis localNumberAxis =
            (NumberAxis) localCategoryPlot.getRangeAxis();
        localNumberAxis
            .setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        localNumberAxis.setUpperMargin(0.1D);
        ChartUtilities.applyCurrentTheme(localJFreeChart);

        return localJFreeChart;
    }

    /**
     * Create the chart and panel, return the combination.
     * 
     * @return the panel with the chart.
     */
    public static JPanel createPanel() {
        JFreeChart localJFreeChart = createChart(createDataset());
        ChartPanel localChartPanel = new ChartPanel(localJFreeChart);
        localChartPanel.setMouseWheelEnabled(true);
        return localChartPanel;
    }
}
