package edu.rosehulman.serg.smellbuster.util;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTable;

import edu.rosehulman.serg.smellbuster.gui.ResultTableGUI;
import edu.rosehulman.serg.smellbuster.logic.ResultTableLogic;

public class StatisticsCalculator {

	private Map<String, int[]> dataMap;
	private String[] columnNames;
	private JTable table;
	private Map<Integer, String> versionMap;
	private ResultTableLogic resultTableLogic;
	private Map<Integer, Double> stats;

	public StatisticsCalculator(ResultTableGUI resultTableGUI) {
		this.dataMap = new HashMap<>();
		this.stats = new TreeMap<>();
		this.columnNames = resultTableGUI.getColumnNames();
		this.table = resultTableGUI.getTable();
		this.versionMap = resultTableGUI.getVersionMap();
		this.resultTableLogic = resultTableGUI.getResultTableLogic();
		initializeDataMap();
		calculateTotalChanges();
		calculateStatsData();
	}

	private void initializeDataMap() {
		for (int ver : this.versionMap.keySet()) {
			String version = this.versionMap.get(ver);
			int[] tempArr = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
			this.dataMap.put(version, tempArr);
		}
	}

	// (10 - noc) - wmc - cbo - lcom3 + (2 * cam) - ic - cbm - (0.5 * amc) - cc

//	private void getData() {
//
//		for (int col = 0; col < this.columnNames.length; col++) {
//
//			double sumNOC = 0;
//			double sumWMC = 0;
//			double sumCBO = 0;
//			double sumLCOM3 = 0;
//			double sumCAM = 0;
//			double sumIC = 0;
//			double sumCBM = 0;
//			double sumAMC = 0;
//			double sumCC = 0;
//			double aggregate = 0;
//			String version = this.columnNames[col];
//			double total = 0;
//
//			for (int i = 0; i < this.table.getRowCount(); i++) {
//
//				String className = this.table.getModel().getValueAt(i, col)
//						.toString();
//
//				if (className.contains(".java")) {
//					sumNOC += this.getDoubleValue(this.resultTableLogic
//							.getMetricValueFor("noc", version, className));
//					sumWMC += this.getDoubleValue(this.resultTableLogic
//							.getMetricValueFor("wmc", version, className));
//					sumCBO += this.getDoubleValue(this.resultTableLogic
//							.getMetricValueFor("cbo", version, className));
//					sumLCOM3 += this.getDoubleValue(this.resultTableLogic
//							.getMetricValueFor("lcom3", version, className));
//					sumCAM += this.getDoubleValue(this.resultTableLogic
//							.getMetricValueFor("cam", version, className));
//					sumIC += this.getDoubleValue(this.resultTableLogic
//							.getMetricValueFor("ic", version, className));
//					sumCBM += this.getDoubleValue(this.resultTableLogic
//							.getMetricValueFor("cbm", version, className));
//					sumAMC += this.getDoubleValue(this.resultTableLogic
//							.getMetricValueFor("amc", version, className));
//					sumCC += this.getDoubleValue(this.resultTableLogic
//							.getMetricValueFor("cc", version, className));
//					aggregate += this.getDoubleValue(this.resultTableLogic
//							.getAggregateValueFor(version, className));
//					
//					double val = this.getDoubleValue(this.resultTableLogic
//							.getAggregateValueFor(version, className));
//					if (val > 0.0 || val < 0.0){
//						total++;
//					}
//				}
//			}
//
//			System.out.println(version + "," + (sumNOC/total) + "," + (sumWMC/total) + ","
//					+ (sumCBO/total) + "," + (sumLCOM3/total) + "," + (sumCAM/total) + "," + (sumIC/total)
//					+ "," + (sumCBM/total) + "," + (sumAMC/total) + "," + (sumCC/total) + "," + (aggregate/total));
//		}
//	}
	
//	private double getDoubleValue(String convert){
//		try{
//			double val = Double.parseDouble(convert);
//			return val;
//		} catch (Exception e){
//			return 0.0;
//		}
//	}
	// private ArrayList<Boolean> getPackageRows(){
	// ArrayList<Boolean> packageRows = new ArrayList<>();
	// boolean value = false;
	// String packageId = "src.org";
	// for (int i=0; i<this.table.getRowCount(); i++){
	// String name = this.table.getModel().getValueAt(i, 0).toString();
	// if (name.contains("Package") && name.contains(packageId)){
	// value = true;
	// } else if (name.contains("Package") && !name.contains(packageId)){
	// value = false;
	// }
	// packageRows.add(value);
	// }
	// return packageRows;
	// }

	private void calculateTotalChanges() {
//		this.getData();
		// ArrayList<Boolean> packageRows = this.getPackageRows();
		for (int col = 0; col < this.columnNames.length; col++) {

			int[] dataArr = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };

			for (int i = 0; i < this.table.getRowCount(); i++) {
				String name = this.table.getModel().getValueAt(i, col)
						.toString();

				// boolean check = packageRows.get(i);
				if (name.contains(".java")) { // && check) {
					dataArr[0] = dataArr[0] + 1;
					Color c = this.resultTableLogic.getColorForMetricScore(0,
							name, this.columnNames[col]);
					int index = this.getColorCategory(c);
					if (index > -1) {
						dataArr[index] = dataArr[index] + 1;
					}
				}
			}
			String version = this.columnNames[col];
			// System.out.println(version + " : " + Arrays.toString(dataArr));
			this.dataMap.put(version, dataArr);
		}
	}

	private void calculateStatsData() {
		int[] dataTotals = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };

		for (String version : this.dataMap.keySet()) {
			int[] dataArr = this.dataMap.get(version);
			dataTotals[0] += dataArr[0];
			dataTotals[1] += dataArr[1];
			dataTotals[2] += dataArr[2];
			dataTotals[3] += dataArr[3];
			dataTotals[4] += dataArr[4];
			dataTotals[5] += dataArr[5];
			dataTotals[6] += dataArr[6];
			dataTotals[7] += dataArr[7];
		}

		for (int i = 1; i < dataTotals.length; i++) {
			double percent = (dataTotals[i] * 100) / dataTotals[0];
			this.stats.put(i, percent);
		}

	}

	public Map<Integer, Double> getStats() {
		return this.stats;
	}

	public int getColorCategory(Color color) {

		if (color.equals(new Color(255, 0, 0))) {
			return 1;
		} else if (color.equals(new Color(204, 0, 0))) {
			return 2;
		} else if (color.equals(new Color(159, 95, 0))) {
			return 3;
		} else if (color.equals(new Color(255, 102, 0))) {
			return 4;
		} else if (color.equals(new Color(255, 204, 0))) {
			return 5;
		} else if (color.equals(new Color(95, 159, 0))) {
			return 6;
		} else if (color.equals(new Color(0, 255, 0))) {
			return 7;
		}
		return -1;
	}

}
