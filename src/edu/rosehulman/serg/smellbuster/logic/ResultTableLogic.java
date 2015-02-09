package edu.rosehulman.serg.smellbuster.logic;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.rosehulman.serg.smellbuster.metricparser.MetricParserFactory;
import edu.rosehulman.serg.smellbuster.util.MetricDOMObject;

public class ResultTableLogic {

	private Map<String, ArrayList<MetricDOMObject>> metricMap;
	private Map<Integer, String> versionMap;
	private MetricLogic metricLogic;
	private final String metricFilesLocation = "MetricAnalysis";
	private String projectName;

	public ResultTableLogic(Map<Integer, String> versionMap, String projectName) {
		this.metricMap = new HashMap<>();
		this.versionMap = versionMap;
		this.projectName = projectName;
		parseMetrics();
		this.metricLogic = new MetricLogic(this);
	}

	public void parseMetrics() {
		File[] files = new File(this.metricFilesLocation + "\\" + this.projectName).listFiles();
		MetricParserFactory parser = new MetricParserFactory();

		for (int i = 0; i < files.length; i++) {
			parser.initializeDOMFromFile(files[i].getAbsolutePath());
			parser.parseXML();

			ArrayList<MetricDOMObject> domObjList = parser.getDOMObjList();

			String version = files[i].getName();
			version = version.substring(0, version.length() - 4);

			metricMap.put(version, domObjList);
		}
	}

	public ArrayList<MetricDOMObject> createClassListMap(String className) {
		ArrayList<MetricDOMObject> list = new ArrayList<MetricDOMObject>();
		MetricDOMObject[] classList = new MetricDOMObject[this.versionMap
				.keySet().size()];
		for (String version : metricMap.keySet()) {
			ArrayList<MetricDOMObject> domObjList = metricMap.get(version);
			for (MetricDOMObject metricDomObj : domObjList) {
				String name = metricDomObj.getName();
				if (name.equals(className)) {

					int j = 0;
					Iterator<Integer> itr = this.versionMap.keySet().iterator();
					while (itr.hasNext()) {
						if (version.equals(this.versionMap.get(itr.next()))) {
							break;
						}
						j++;
					}
					classList[j] = metricDomObj;
				}
			}
		}
		for (int i = 0; i < classList.length; i++) {
			list.add(classList[i]);
		}

		return list;
	}

	public String getMetricValueFor(String metric, String version,
			String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (MetricDOMObject metricDomObj : metricMap.get(ver)) {
						if (metricDomObj.getName().equals(className)) {
							value = String.valueOf(metricDomObj
									.getValueForMetric(metric));
						}
					}
				}
			}
		}
		return value;
	}

	public String getAggregateValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (MetricDOMObject metricDomObj : metricMap.get(ver)) {
						if (metricDomObj.getName().equals(className)) {
							value = String.valueOf(metricDomObj.getAggregate());
						}
					}
				}
			}
		}
		return value;
	}

	public String getAggregateMetricsOfMin(String className) {
		return this.metricLogic.getAggregateMetricsOfMin(className);
	}

	public String getAggregateMetricsOfMax(String className) {
		return this.metricLogic.getAggregateMetricsOfMax(className);
	}

	public Color getColorForMetricScore(int index, String className,
			String version) {
		return this.metricLogic.getColorForMetricScore(index, className,
				version);
	}

	public void setMaxAggregateValue(double value) {
		this.metricLogic.setMaxAggregateValue(value);
	}

	public double getMaxAggregateValue() {
		return this.metricLogic.getMaxAggregateValue();
	}

	public Map<String, ArrayList<MetricDOMObject>> getMetricMap() {
		return this.metricMap;
	}
}
