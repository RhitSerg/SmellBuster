package edu.rosehulman.serg.smellbuster.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JProgressBar;

import edu.rosehulman.serg.smellbuster.gui.DisplayTable;
import edu.rosehulman.serg.smellbuster.svn.SVNParser;
import edu.rosehulman.serg.smellbuster.util.DiffClass;

public class SVNLoadLogic {
	
	private Map<Integer, String> versionMap;
	private String svnURL;
	private JProgressBar progressBar;
	private Map<String, ArrayList<DiffClass>> diffMap;
	private Map<String, ArrayList<String>> classMap;
	
	public SVNLoadLogic(Map<Integer, String> versionMap, String svnURL, JProgressBar progressBar){
		this.versionMap = versionMap;
		this.progressBar = progressBar;
		this.svnURL = svnURL;
		this.diffMap = new HashMap<>();
		this.classMap = new HashMap<>();
		this.loadData();
	}
	
	public void parseVersionChanges() {

		Iterator<Integer> itr = this.versionMap.keySet().iterator();
		int start = 0;

		int total = this.versionMap.keySet().size();
		int current = 0;
		
		while (itr.hasNext()) {
			int end = itr.next();
			SVNParser svnParser = new SVNParser(this.svnURL,
					(long) start, (long) end);
			svnParser.loadSVNInfo();
			ArrayList<DiffClass> dcList = svnParser.getDiffClassList();

			String version = this.versionMap.get(end);

			diffMap.put(version, dcList);
			start = end;
			
			current++;
			updateProgressBar(total, current);
		}
	}

	private void updateProgressBar(int total, int current) {
		int value = (int)(current * 100 / total);
		progressBar.setStringPainted(true);
		progressBar.setIndeterminate(false);
		progressBar.setValue(value);
		progressBar.setString(value+"%");
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

		int len = this.versionMap.keySet().size();
		
		ArrayList<String[]> values = getTableRowValues(len);
		int size = getTableRowcount(values);
		String[][] dataValues = getTableDataValues(len, values, size);

		DisplayTable resultsTable = new DisplayTable(dataValues, this.versionMap);
		resultsTable.setVisible(true);
	}

	private String[][] getTableDataValues(int len, ArrayList<String[]> values,
			int size) {
		String[][] dataValues = new String[size * 2][len];
		int i = 0;
		for (String[] value : values) {
			if (didChange(value)) {
				dataValues[i] = value;
				i++;
				dataValues[i] = new String[len];
				i++;
			}
		}
		return dataValues;
	}

	private int getTableRowcount(ArrayList<String[]> values) {
		int size = 0;
		for (String[] val : values) {
			if (didChange(val)) {
				size++;
			}
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
				String[] taleRow = new String[len];
				Arrays.fill(taleRow, "");
				for (String version : classMap.get(name)) {
					int j = 0;
					itr = this.versionMap.keySet().iterator();
					while (itr.hasNext()) {
						if (version.equals(this.versionMap.get(itr.next()))) {
							break;
						}
						j++;
					}
					taleRow[j] = name;
				}
				values.add(taleRow);
			}
		}
		return values;
	}

}
