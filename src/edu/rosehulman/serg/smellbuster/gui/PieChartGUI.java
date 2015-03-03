package edu.rosehulman.serg.smellbuster.gui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import edu.rosehulman.serg.smellbuster.util.StatisticsCalculator;

public class PieChartGUI extends JFrame {

	private static final long serialVersionUID = 5237374767292795971L;
	private StatisticsCalculator statsCalculator;
	private String title;
	private Map<String, Color> colorMap;
	private Map<String, String> chartLegend;
	private final String noData = "No Data Available";
	private final String extremelyBadCode = "Extremely Bad Code";
	private final String badCode = "Bad Code";
	private final String belowAverage = "Below Average";
	private final String average = "Average";
	private final String aboveAverage = "Above Average";
	private final String goodCode = "Good Code";
	private final String extremelyGoodCode = "Extremely Good Code";

	public PieChartGUI(String title, ResultTableGUI resultTableGUI) {
		super("Metric Analysis Statistics");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.title = title;
		this.colorMap = new HashMap<>();
		this.chartLegend = new HashMap<>();
		this.statsCalculator = new StatisticsCalculator(resultTableGUI);
		loadColorMap();
		loadChartLegend();
		setContentPane(getChartPanel());
	}

	private void loadColorMap() {
		this.colorMap.put(this.extremelyBadCode, new Color(255, 0, 0));
		this.colorMap.put(this.badCode, new Color(204, 0, 0));
		this.colorMap.put(this.belowAverage, new Color(159, 95, 0));
		this.colorMap.put(this.average, new Color(255, 102, 0));
		this.colorMap.put(this.aboveAverage, new Color(255, 204, 0));
		this.colorMap.put(this.goodCode, new Color(95, 159, 0));
		this.colorMap.put(this.extremelyGoodCode, new Color(0, 255, 0));
		this.colorMap.put(this.noData, Color.DARK_GRAY);
	}

	private void loadChartLegend() {
		this.chartLegend.put("1", this.extremelyBadCode);
		this.chartLegend.put("2", this.badCode);
		this.chartLegend.put("3", this.belowAverage);
		this.chartLegend.put("4", this.average);
		this.chartLegend.put("5", this.aboveAverage);
		this.chartLegend.put("6", this.goodCode);
		this.chartLegend.put("7", this.extremelyGoodCode);
	}

	public JPanel getChartPanel() {
		JFreeChart chart = createChart(createDataset());
		return new ChartPanel(chart);
	}

	private PieDataset createDataset() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		double sum = 0;
		Map<Integer, Double> dataMap = this.statsCalculator.getStats();
		for (int key : dataMap.keySet()) {
			sum += dataMap.get(key);
			dataset.setValue(this.chartLegend.get("" + key), dataMap.get(key));
		}
		dataset.setValue(this.noData, 100 - sum);
		return dataset;
	}

	/**
	 * Creates a chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * 
	 * @return A chart.
	 */
	private JFreeChart createChart(PieDataset dataset) {

		JFreeChart chart = ChartFactory.createPieChart(this.title, // chart
																	// title
				dataset, // data
				true, // include legend
				true, false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setNoDataMessage(this.noData);
		plot.setCircular(false);
		plot.setLabelGap(0.02);
		for (String category : this.colorMap.keySet()) {
			plot.setSectionPaint(category, this.colorMap.get(category));
		}

		return chart;

	}
}