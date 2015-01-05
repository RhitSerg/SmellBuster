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

public class LineChart extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<MetricDOMObject> classList;
	String[] metrics = new String[] { "wmc", "noc", "cbo", "lcom", "ca", "ce",
			"lcom3", "cam", "ic", "cbm", "amc" };
	private final int CHART_WIDTH = 700;
	private final int CHART_HEIGHT = 270;
	private final int NUM_OF_ROWS = 1;
	private final int NUM_OF_COLS = 1;
	private String title;

	/**
	 * Constructor for initializing the charts with provided title.
	 * 
	 * @param title
	 */
	public LineChart(final String title, ArrayList<MetricDOMObject> classList) {
		super(title);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.title = title;
		this.classList = classList;
		setContentPane(getChartPanel(this.classList));
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
			int version = 1;
			final XYSeries series = new XYSeries(metricName);
			for (MetricDOMObject metricDomObj : this.classList) {
				if (metricDomObj == null)
					series.add(version, -10.0);
				else
					series.add(version, metricDomObj.getValueForMetric(metricName));
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

		return chart;

	}
}
