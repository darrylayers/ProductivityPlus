package graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedHashMap;

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

public class PieChart extends ApplicationFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static LinkedHashMap<String, Double> orderedMap;

    public PieChart(String paramString) {
        super(paramString);
        JPanel localJPanel = createDemoPanel();
        localJPanel.setPreferredSize(new Dimension(1800, 1670));
        setContentPane(localJPanel);
    }

    private static PieDataset createDataset() {

        orderedMap = DataHandling.orderedMap();
        DefaultPieDataset localDefaultPieDataset = new DefaultPieDataset();

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
            System.out.println(keyList[i]);
            localDefaultPieDataset.setValue(
                keyList[i], orderedMap.get(keyList[i]));
        }
        return localDefaultPieDataset;
    }

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

    public static JPanel createDemoPanel() {
        JFreeChart localJFreeChart = createChart(createDataset());
        ChartPanel localChartPanel = new ChartPanel(localJFreeChart);
        localChartPanel.setMouseWheelEnabled(true);
        return localChartPanel;
    }
}
