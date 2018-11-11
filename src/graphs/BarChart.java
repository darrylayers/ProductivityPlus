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

public class BarChart extends ApplicationFrame {

    private static final long serialVersionUID = -7957255223166147473L;

    public BarChart(String paramString) {

        super(paramString);
        JPanel localJPanel = createDemoPanel();
        localJPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(localJPanel);
    }

    private static CategoryDataset createDataset() {

        DefaultCategoryDataset localDefaultCategoryDataset =
            new DefaultCategoryDataset();

        String str = "";

        Map<String, Double> map = DataHandling.orderedMap();

        String keyList[] = new String[GraphicalOutputGui.getProgCount()];
        String lKeyLast = "";
        int k = 1;
        int counter = 0;
        for (String key : map.keySet()) {
            lKeyLast = key;
        }
        keyList[0] = lKeyLast;
        String oldKey = "";

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
        for (int i = 0; i < GraphicalOutputGui.getNumProgs(); i++) {
            localDefaultCategoryDataset.addValue(map.get(keyList[i]), str,
                keyList[i]);
        }
        return localDefaultCategoryDataset;
    }

    private static JFreeChart createChart(
        CategoryDataset paramCategoryDataset) {

        JFreeChart localJFreeChart =
            ChartFactory.createBarChart("Bar Chart for Data Displayed in Table",
                "Program", "Time " + TimeConvert.getUnit(),
                paramCategoryDataset,
                PlotOrientation.HORIZONTAL, false, true, false);
        CategoryPlot localCategoryPlot =
            (CategoryPlot) localJFreeChart.getPlot();

        localCategoryPlot.setNoDataMessage("No data available");

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

    public static JPanel createDemoPanel() {

        JFreeChart localJFreeChart = createChart(createDataset());
        ChartPanel localChartPanel = new ChartPanel(localJFreeChart);
        localChartPanel.setMouseWheelEnabled(true);
        return localChartPanel;
    }

}
