import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class PieChart {

    private DefaultPieDataset dataset;
    private JFreeChart chart;
    private ChartPanel chartPanel;

    PieChart(String chartTitle, HashMap data, int x, int y, int width, int height) throws IOException {
        //Dataset information - What's stored on the Pie-Chart
        dataset = new DefaultPieDataset();
        //Loops through the HashMap and adds an entry in the pie-chart for each K/V pair
        data.forEach((key, value) -> dataset.setValue(key.toString(), (int) value));

        //Creates the pie-chart using the pre-specified dataset
        chart = ChartFactory.createPieChart(chartTitle, dataset, false, true, false);

        //Creates and presets info. for the chartPanel, decides placement and appearance of the pie-chart on a JFrame.
        chartPanel = new ChartPanel(chart);
        chartPanel.setLocation(x, y);
        chartPanel.setSize(width, height);

        //Sets the color of each part of the pie chart.
        PiePlot plot = (PiePlot) chart.getPlot();

        //If dataset[0] is equal to the percentSpent, then make dataset[0]'s color red, else make it blue.
        if (dataset.getValue(0).equals((int) ((FinancialManagementSystem.getSpentSinceLastDeposit() / FinancialManagementSystem.getLastDeposit()) * 100))){
            plot.setSectionPaint(dataset.getKey(0), new Color(0, 34, 227));
            plot.setSectionPaint(dataset.getKey(1), new Color(196, 0, 16));
        }else{
            plot.setSectionPaint(dataset.getKey(0), new Color(196, 0, 16));
            plot.setSectionPaint(dataset.getKey(1), new Color(0, 34, 227));
        }
    }

    //Getters + Setters
    public ChartPanel getChartPanel(){
        return chartPanel;
    }
}
