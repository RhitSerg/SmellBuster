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

	private void calculateTotalChanges() {
		for (int col = 0; col < this.columnNames.length; col++) {

			int[] dataArr = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };

			for (int i = 0; i < this.table.getRowCount(); i++) {
				String name = this.table.getModel().getValueAt(i, col)
						.toString();
				if (name.contains(".java")) {
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

		for (int i=1; i<dataTotals.length; i++){
			double percent = (dataTotals[i] * 100) / dataTotals[0];
			this.stats.put(i, percent);
		}
		
	}
	
	public Map<Integer, Double> getStats(){
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
