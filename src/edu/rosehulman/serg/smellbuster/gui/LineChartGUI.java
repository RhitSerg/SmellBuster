package edu.rosehulman.serg.smellbuster.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import edu.rosehulman.serg.smellbuster.util.*;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

public class LineChartGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<MetricDOMObject> classList;
	String[] metrics = new String[] { "wmc", "noc", "cbo", "lcom3", "cam",
			"ic", "cbm", "amc", "cc" };
	private final int CHART_WIDTH = 700;
	private final int CHART_HEIGHT = 270;
	private final int NUM_OF_ROWS = 1;
	private final int NUM_OF_COLS = 1;
	private String title;
	private double[] xaxisLabels;

	/**
	 * Constructor for initializing the charts with provided title.
	 * 
	 * @param title
	 */
	public LineChartGUI(final String title, ArrayList<MetricDOMObject> classList, Set<Integer> labels) {
		super(title);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.title = title;
		this.classList = classList;
		this.setXAxisLables(labels);
		setContentPane(getChartPanel(this.classList));
	}
	
	private void setXAxisLables(Set<Integer> labels){
		this.xaxisLabels = new double[labels.size()];
		int pos = 0;
		for (int repoVersion: labels){
			this.xaxisLabels[pos] = repoVersion;
			pos++;
		}
	}

	/**
	 * Creates 3 charts: Positive, Negative, and Neutral. Creates XYDatasets for
	 * each chart, then creates the charts, and adds them to a single JPanel and
	 * returns the JPanel.
	 * 
	 * @param sentimentMap
	 * @return
	 */
	public JPanel getChartPanel(ArrayList<MetricDOMObject> classList) {

		JPanel charts = new JPanel();
		charts.setLayout(new GridLayout(NUM_OF_ROWS, NUM_OF_COLS));

		final XYDataset dataSet = createDataset();
		final JFreeChart chart = createChart(dataSet, this.title);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(CHART_WIDTH, CHART_HEIGHT));
		charts.add(chartPanel);

		return charts;
	}

	/**
	 * Creates the dataset for each location based on the requested type of
	 * dataset.
	 * 
	 * @param type
	 * @return
	 */
	private XYDataset createDataset() {

		final XYSeriesCollection dataset = new XYSeriesCollection();
		for (String metricName : this.metrics) {
			int version = 0;
			final XYSeries series = new XYSeries(metricName);
			for (MetricDOMObject metricDomObj : this.classList) {
				if (metricDomObj == null)
					series.add(this.xaxisLabels[version], -10.0);
				else
					series.add(this.xaxisLabels[version],
							metricDomObj.getValueForMetric(metricName));
				version++;
			}
			dataset.addSeries(series);
		}
		return dataset;
	}

	/**
	 * Creates a JFreeChart based on the given dataset and title.
	 * 
	 * @param dataset
	 * @param title
	 * @return
	 */
	private JFreeChart createChart(final XYDataset dataset, String title) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart(title, // chart
																		// title
				"Version", // x axis label
				"Metric Value", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);

		chart.setBackgroundPaint(Color.white);

		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		plot.getRenderer().setSeriesPaint(0, Color.RED);
		plot.getRenderer().setSeriesPaint(1, Color.BLUE);
		plot.getRenderer().setSeriesPaint(2, Color.GRAY);
		plot.getRenderer().setSeriesPaint(3, Color.GREEN);
		plot.getRenderer().setSeriesPaint(4, Color.MAGENTA);
		plot.getRenderer().setSeriesPaint(5, Color.ORANGE);
		plot.getRenderer().setSeriesPaint(6, Color.CYAN);
		plot.getRenderer().setSeriesPaint(7, Color.BLACK);
		plot.getRenderer().setSeriesPaint(8, Color.YELLOW);
		
		return chart;

	}
}
