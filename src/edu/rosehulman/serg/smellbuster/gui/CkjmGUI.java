package edu.rosehulman.serg.smellbuster.gui;

import edu.rosehulman.serg.smellbuster.logic.ResultTableLogic;

public class CkjmGUI implements IMetricGUI {

	private String[] metrics;
	private ResultTableLogic displayTableLogic;

	public CkjmGUI(ResultTableLogic displayTableLogic) {
		this.displayTableLogic = displayTableLogic;
		this.metrics = new String[] { "All", "Weighted Method Per Class (WMC)",
				"Number of Children (NOC)",
				"Coupling Between Object Classes (CBO)",
				"Lack of Cohesion in methods (LCOM3)",
				"Cohesion Among Methods of Class (CAM)",
				"Inheritance Coupling (IC)", "Coupling Between Methods (CBM)",
				"Average Method Complexity (AMC)",
				"McCabe's Cyclomatic Complexity (CC)" };
	}

	@Override
	public String[] getMetricDisplayList() {
		return this.metrics;
	}
	
	@Override
	public String getDisplayMessage(String version, String className) {
		return "<html><table>" + getMessageForVersion(version)
				+ getMessageForClassName(className)
				+ getMessageForWMC(version, className)
				+ getMessageForNOC(version, className)
				+ getMessageForCBO(version, className)
				+ getMessageForLCOM(version, className)
				+ getMessageForCA(version, className)
				+ getMessageForCE(version, className)
				+ getMessageForLCOM3(version, className)
				+ getMessageForCAM(version, className)
				+ getMessageForIC(version, className)
				+ getMessageForCBM(version, className)
				+ getMessageForAMC(version, className)
				+ getMessageForCC(version, className)
				+ getMessageForAggregateMetrics(version, className)
				+ getMessageForMaxAggregateValues(version, className)
				+ getMessageForAggregateMinValues(version, className)
				+ "</table></html>";
	}

	private String getMessageForVersion(String version) {
		return "<tr><td>Version" + "</td><td>" + version + "</td></tr>";
	}

	private String getMessageForClassName(String className) {
		return "<tr><td>Class Name" + "</td><td>" + className + "</td></tr>";
	}

	private String getMessageForWMC(String version, String className) {
		return "<tr><td>Weighted Methods per Class (WMC)"
				+ "</td><td>"
				+ this.displayTableLogic.getMetricValueFor("wmc", version,
						className) + "</td></tr>";
	}

	private String getMessageForNOC(String version, String className) {
		return "<tr><td>Number of Children (NOC)"
				+ "</td><td>"
				+ this.displayTableLogic.getMetricValueFor("noc", version,
						className) + "</td></tr>";
	}

	private String getMessageForCBO(String version, String className) {
		return "<tr><td>Coupling Between Object Classes (CBO)"
				+ "</td><td>"
				+ this.displayTableLogic.getMetricValueFor("cbo", version,
						className) + "</td></tr>";
	}

	private String getMessageForLCOM(String version, String className) {
		return "<tr><td>Lack of Cohesion in Methods (LCOM)"
				+ "</td><td>"
				+ this.displayTableLogic.getMetricValueFor("lcom", 
						version, className) + "</td></tr>";
	}

	private String getMessageForCA(String version, String className) {
		return "<tr><td>Afferent Coupling (Ca)"
				+ "</td><td>"
				+ this.displayTableLogic.getMetricValueFor("ca", version,
						className) + "</td></tr>";
	}

	private String getMessageForCE(String version, String className) {
		return "<tr><td>Efferent Coupling (Ce)"
				+ "</td><td>"
				+ this.displayTableLogic.getMetricValueFor("ce", version,
						className) + "</td></tr>";
	}

	private String getMessageForLCOM3(String version, String className) {
		return "<tr><td>Lack of Cohesion in Methods (LCOM3)"
				+ "</td><td>"
				+ this.displayTableLogic.getMetricValueFor("lcom3", 
						version, className) + "</td></tr>";
	}

	private String getMessageForCAM(String version, String className) {
		return "<tr><td>Cohesion Among Methods of Class (CAM)"
				+ "</td><td>"
				+ this.displayTableLogic.getMetricValueFor("cam", version,
						className) + "</td></tr>";
	}

	private String getMessageForIC(String version, String className) {
		return "<tr><td>Inheritance Coupling (IC)"
				+ "</td><td>"
				+ this.displayTableLogic.getMetricValueFor("ic", version,
						className) + "</td></tr>";
	}

	private String getMessageForCBM(String version, String className) {
		return "<tr><td>Coupling Between Methods (CBM)"
				+ "</td><td>"
				+ this.displayTableLogic.getMetricValueFor("cbm", version,
						className) + "</td></tr>";
	}

	private String getMessageForAMC(String version, String className) {
		return "<tr><td>Average Method Complexity (AMC)"
				+ "</td><td>"
				+ this.displayTableLogic.getMetricValueFor("amc", version,
						className) + "</td></tr>";
	}

	private String getMessageForCC(String version, String className) {
		return "<tr><td>Cyclomatic Complexoty (CC)"
				+ "</td><td>"
				+ this.displayTableLogic.getMetricValueFor("cc", version,
						className) + "</td></tr>";
	}

	private String getMessageForAggregateMetrics(String version,
			String className) {
		return "<tr><td>Aggregate Metrics "
				+ "</td><td>"
				+ this.displayTableLogic
						.getAggregateValueFor(version, className) + "</td></tr>";
	}

	private String getMessageForMaxAggregateValues(String version,
			String className) {
		return "<tr><td>Aggregate Metrics of Max Values "
				+ "</td><td>"
				+ this.displayTableLogic
						.getAggregateMetricsOfMax(className) + "</td></tr>";
	}

	private String getMessageForAggregateMinValues(String version,
			String className) {
		return "<tr><td>Aggregate Metrics of Min Values "
				+ "</td><td>"
				+ this.displayTableLogic
						.getAggregateMetricsOfMin(className) + "</td></tr>";
	}

}
