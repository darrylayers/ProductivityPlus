package graphs;

import java.awt.Dimension;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import core.TimeConvert;

public class LineChart extends ApplicationFrame {

    private static final long serialVersionUID = 6135356681382865439L;

    public LineChart(String paramString) {

        super(paramString);
        JPanel localJPanel = createDemoPanel();
        localJPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(localJPanel);
    }

    private static XYDataset createDataset() {

        XYSeries localXYSeries1 = new XYSeries("First");
        localXYSeries1.add(1.0D, 1.0D);
        localXYSeries1.add(2.0D, 4.0D);
        localXYSeries1.add(3.0D, 3.0D);
        localXYSeries1.add(4.0D, 5.0D);
        localXYSeries1.add(5.0D, 5.0D);
        localXYSeries1.add(6.0D, 7.0D);
        localXYSeries1.add(7.0D, 7.0D);
        localXYSeries1.add(8.0D, 8.0D);
        XYSeries localXYSeries2 = new XYSeries("Second");
        localXYSeries2.add(1.0D, 5.0D);
        localXYSeries2.add(2.0D, 7.0D);
        localXYSeries2.add(3.0D, 6.0D);
        localXYSeries2.add(4.0D, 8.0D);
        localXYSeries2.add(5.0D, 4.0D);
        localXYSeries2.add(6.0D, 4.0D);
        localXYSeries2.add(7.0D, 2.0D);
        localXYSeries2.add(8.0D, 1.0D);
        XYSeries localXYSeries3 = new XYSeries("Third");
        localXYSeries3.add(3.0D, 4.0D);
        localXYSeries3.add(4.0D, 3.0D);
        localXYSeries3.add(5.0D, 2.0D);
        localXYSeries3.add(6.0D, 3.0D);
        localXYSeries3.add(7.0D, 6.0D);
        localXYSeries3.add(8.0D, 3.0D);
        localXYSeries3.add(9.0D, 4.0D);
        localXYSeries3.add(10.0D, 3.0D);
        XYSeriesCollection localXYSeriesCollection = new XYSeriesCollection();
        localXYSeriesCollection.addSeries(localXYSeries1);
        localXYSeriesCollection.addSeries(localXYSeries2);
        localXYSeriesCollection.addSeries(localXYSeries3);
        return localXYSeriesCollection;
    }

    private static JFreeChart createChart(XYDataset paramXYDataset) {

        JFreeChart localJFreeChart =
            ChartFactory.createXYLineChart(
                "Line Chart for data displayed in table", "Programs",
                "Time " + TimeConvert.getUnit(),
                paramXYDataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot localXYPlot = (XYPlot) localJFreeChart.getPlot();
        localXYPlot.setDomainPannable(true);
        localXYPlot.setRangePannable(true);
        XYLineAndShapeRenderer localXYLineAndShapeRenderer =
            (XYLineAndShapeRenderer) localXYPlot.getRenderer();
        localXYLineAndShapeRenderer.setBaseShapesVisible(true);
        localXYLineAndShapeRenderer.setBaseShapesFilled(true);
        NumberAxis localNumberAxis = (NumberAxis) localXYPlot.getRangeAxis();
        localNumberAxis
            .setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return localJFreeChart;
    }

    public static JPanel createDemoPanel() {

        JFreeChart localJFreeChart = createChart(createDataset());
        ChartPanel localChartPanel = new ChartPanel(localJFreeChart);
        localChartPanel.setMouseWheelEnabled(true);
        return localChartPanel;
    }

}
