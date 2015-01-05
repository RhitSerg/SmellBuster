package edu.rosehulman.serg.smellbuster.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CKJMDOMObject implements IMetricDOMObject {
		private String name;
		private String packageName;
		private ArrayList<String> metricList;
		private Map<String, Double> metricValueMap;
		
		public CKJMDOMObject(){
			this.metricList = new ArrayList<>();
			this.metricValueMap = new HashMap<>();
			this.initializeMetricList();
		}
		
		private void initializeMetricList(){
			this.metricList.add("wmc");
			this.metricList.add("dit");
			this.metricList.add("noc");
			this.metricList.add("cbo");
			this.metricList.add("rfc");
			this.metricList.add("lcom");
			this.metricList.add("ca");
			this.metricList.add("ce");
			this.metricList.add("npm");
			this.metricList.add("lcom3");
			this.metricList.add("loc");
			this.metricList.add("dam");
			this.metricList.add("moa");
			this.metricList.add("mfa");
			this.metricList.add("cam");
			this.metricList.add("ic");
			this.metricList.add("cmb");
			this.metricList.add("amc");
			this.metricList.add("cc");
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public void setValueForMetric(String key, double value) {
			this.metricValueMap.put(key, value);
		}

		@Override
		public void setName(String name) {
			String[] nameSplit = name.split("\\.");
			name = nameSplit[nameSplit.length - 1];
			name += ".java";
			this.name = name;
			this.setPackageName("");
			for (int i=0; i<nameSplit.length-1; i++){
				this.setPackageName(this.getPackageName() + nameSplit[i]);
			}
		}
		
		@Override
		public double getValueForMetric(String metric){
			for (String metricName: this.metricValueMap.keySet()){
				if (metric.toLowerCase().equals(metricName.toLowerCase())){
					return this.metricValueMap.get(metric);
				}
			}
			return Integer.MIN_VALUE;
		}

		@Override
		public double getAggregate(){
			double noc = this.getValueForMetric("noc");
			double wmc = this.getValueForMetric("wmc");
			double cbo = this.getValueForMetric("cbo");
			double lcom3 = this.getValueForMetric("lcom3");
			double cam = this.getValueForMetric("cam");
			double ic = this.getValueForMetric("ic");
			double cbm = this.getValueForMetric("cbm");
			double amc = this.getValueForMetric("amc");
			double cc = this.getValueForMetric("cc");
			double score = ((10 - noc) - wmc - cbo - lcom3 + (2 * cam) - ic - cbm
					- (0.5 * amc) - cc);
			return score;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		@Override
		public ArrayList<String> getMetricList() {
			return this.metricList;
		}

		
	}
