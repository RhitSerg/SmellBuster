package edu.rosehulman.serg.smellbuster.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JProgressBar;

import edu.rosehulman.serg.smellbuster.gui.ResultTableGUI;
import edu.rosehulman.serg.smellbuster.util.DiffClass;
import edu.rosehulman.serg.smellbuster.versioncontrol.VersionControlParserFactory;

public class SVNLoadLogic {

	private Map<Integer, String> versionMap;
	private String svnURL;
	private JProgressBar progressBar;
	private Map<String, ArrayList<DiffClass>> diffMap;
	private Map<String, ArrayList<String>> classMap;
	private Map<String, ArrayList<String[]>> packageClassMap;

	public SVNLoadLogic(Map<Integer, String> versionMap, String svnURL,
			JProgressBar progressBar) {
		this.versionMap = versionMap;
		this.progressBar = progressBar;
		this.svnURL = svnURL;
		this.diffMap = new HashMap<>();
		this.classMap = new HashMap<>();
		this.packageClassMap = new HashMap<>();
		this.loadData();
	}

	public void parseVersionChanges() {

		Iterator<Integer> itr = this.versionMap.keySet().iterator();
		int start = 0;

		int total = this.versionMap.keySet().size();
		int current = 0;

		while (itr.hasNext()) {
			int end = itr.next();
			VersionControlParserFactory svnParser = new VersionControlParserFactory(
					this.svnURL, (long) start, (long) end);
			svnParser.loadVersionControlInfo();
			ArrayList<DiffClass> dcList = svnParser.getDiffClassList();

			String version = this.versionMap.get(end);

			diffMap.put(version, dcList);
			start = end;

			current++;
			updateProgressBar(total, current);
		}
	}

	private void updateProgressBar(int total, int current) {
		int value = (int) (current * 100 / total);
		progressBar.setStringPainted(true);
		progressBar.setIndeterminate(false);
		progressBar.setValue(value);
		progressBar.setString(value + "%");
	}

	public void loadData() {

		this.parseVersionChanges();

		Iterator<String> itr = this.diffMap.keySet().iterator();
		while (itr.hasNext()) {
			String version = itr.next();
			ArrayList<DiffClass> dcList = this.diffMap.get(version);
			for (DiffClass dc : dcList) {
				ArrayList<String> cList = new ArrayList<String>();
				String name = dc.getName();
				if (this.classMap.containsKey(name)) {
					cList = this.classMap.get(name);
				}
				cList.add(version);
				this.classMap.put(name, cList);
			}
		}
	}

	public boolean didChange(String[] value) {
		int count = 0;
		for (String str : value) {
			if (str.length() > 0) {
				count++;
				if (count > 1) {
					return true;
				}
			}
		}
		return false;
	}

	public void displayTable() {

		int colNum = this.versionMap.keySet().size();

		ArrayList<String[]> values = getTableRowValues(colNum);
		this.groupRowsByPackageName(values);
		int rowNum = getTableRowcount(values);
		String[][] dataValues = getTableDataValues(colNum, rowNum);

		ResultTableGUI resultsTable = new ResultTableGUI(dataValues,
				this.versionMap);
		resultsTable.setVisible(true);
	}

	private String[][] getTableDataValues(int colNum, int rowNum) {
		String[][] dataValues = new String[rowNum][colNum];
		for (String[] row : dataValues)
			Arrays.fill(row, "");
		Iterator<String> itr = this.packageClassMap.keySet().iterator();
		int i = 0;
		while (itr.hasNext()) {
			String packageName = itr.next();
			dataValues[i][0] = packageName;
			i++;
			for (String[] value : this.packageClassMap.get(packageName)) {

				dataValues[i] = value;
				i++;
			}
			i++;
		}
		return dataValues;
	}

	private int getTableRowcount(ArrayList<String[]> values) {
		int size = 0;
		for (String packageName : this.packageClassMap.keySet()) {
			size++;
			ArrayList<String[]> rows = this.packageClassMap.get(packageName);
			size += rows.size();
			size++;
		}
		return size;
	}

	private ArrayList<String[]> getTableRowValues(int len) {
		Iterator<Integer> itr = this.versionMap.keySet().iterator();
		ArrayList<String[]> values = new ArrayList<>();
		for (String name : classMap.keySet()) {
			boolean isValid = false;
			if (name.contains(".java")) {
				isValid = true;
			}

			if (isValid) {
				String[] tableRow = new String[len];
				Arrays.fill(tableRow, "");
				for (String version : classMap.get(name)) {
					int j = 0;
					itr = this.versionMap.keySet().iterator();
					while (itr.hasNext()) {
						if (version.equals(this.versionMap.get(itr.next()))) {
							break;
						}
						j++;
					}
					tableRow[j] = name;
				}
				if (didChange(tableRow))
					values.add(tableRow);
			}
		}
		return values;
	}

	private void groupRowsByPackageName(ArrayList<String[]> tableRows) {

		for (String[] row : tableRows) {
			String className = this.getClassNameFromRow(row);
			DiffClass tempClass = this.getDiffClassFor(className);
			String packageName = tempClass.getPackageName();
			ArrayList<String[]> tempRows = new ArrayList<>();

			if (this.packageClassMap.containsKey(packageName)) {
				tempRows = this.packageClassMap.get(packageName);
			}

			tempRows.add(row);
			this.packageClassMap.put(packageName, tempRows);
		}

	}

	private DiffClass getDiffClassFor(String className) {

		for (String version : this.diffMap.keySet()) {
			ArrayList<DiffClass> diffClassList = this.diffMap.get(version);
			for (DiffClass diffClass : diffClassList) {
				if (className.toLowerCase().equals(
						diffClass.getName().toLowerCase()))
					return diffClass;
			}

		}
		return null;
	}

	private String getClassNameFromRow(String[] row) {
		for (String className : row) {
			if (className.length() > 0) {
				return className;
			}
		}
		return "";
	}

}
