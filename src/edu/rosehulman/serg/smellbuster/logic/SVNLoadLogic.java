package edu.rosehulman.serg.smellbuster.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JProgressBar;

import edu.rosehulman.serg.smellbuster.gui.ResultTableGUI;
import edu.rosehulman.serg.smellbuster.runnable.MetricAnalyserRunnable;
import edu.rosehulman.serg.smellbuster.runnable.ProjectBuildRunnable;
import edu.rosehulman.serg.smellbuster.runnable.RepoCheckoutRunnable;
import edu.rosehulman.serg.smellbuster.util.DiffClass;
import edu.rosehulman.serg.smellbuster.versioncontrol.VersionControlParserFactory;

public class SVNLoadLogic {

	private Map<Integer, String> versionMap;
	private String svnURL;
	private String svnDir;
	private String buildFileLoc;
	private static JProgressBar progressBar;
	public static int totalProgress;
	public static int currentProgress;
	private String projectName;
	private String jarProperty;
	private Map<String, ArrayList<DiffClass>> diffMap;
	private Map<String, ArrayList<String>> classMap;
	private Map<String, ArrayList<String[]>> packageClassMap;

	public SVNLoadLogic(String projectName, Map<Integer, String> versionMap,
			String svnURL, String buildFileLoc, String jarProperty, JProgressBar progressBar) {
		this.projectName = projectName;
		this.versionMap = versionMap;
		SVNLoadLogic.progressBar = progressBar;
		this.svnURL = svnURL;
		this.svnDir = System.getProperty("user.dir") + "\\repo\\"
				+ this.projectName;
		this.buildFileLoc = buildFileLoc;
		this.diffMap = new HashMap<>();
		this.classMap = new HashMap<>();
		this.packageClassMap = new HashMap<>();
		totalProgress = this.versionMap.keySet().size() * 4;
		currentProgress = 0;
		this.jarProperty = jarProperty;
		this.checkoutRepo();
		this.buildProject();
		this.runMetricAnalysis();
		this.loadData();
	}

	private void checkoutRepo() {

		this.createSvnDirIfNotExist();

		VersionControlParserFactory vcParser = new VersionControlParserFactory(
				this.svnURL);

		ExecutorService executor = Executors.newFixedThreadPool(this.versionMap
				.keySet().size());

		for (int revision : this.versionMap.keySet()) {
			String svnDirLocation = this.svnDir + "\\" + revision;

			Runnable worker = new RepoCheckoutRunnable(svnDirLocation,
					(long) revision, vcParser);
			executor.execute(worker);
		}

		executor.shutdown();
		while (!executor.isTerminated()) {
			// Wait until all threads are finish
		}
	}

	private void createSvnDirIfNotExist() {
		File svnRepoDir = new File(this.svnDir);

		if (!svnRepoDir.exists()) {
			try {
				svnRepoDir.mkdir();
			} catch (SecurityException se) {
				se.printStackTrace();
			}
		}
	}

	private void buildProject() {
		ExecutorService executor = Executors.newFixedThreadPool(this.versionMap
				.keySet().size());

		for (int revision : this.versionMap.keySet()) {
			String svnBuildDirLocation = this.svnDir + "\\" + revision + "\\"
					+ this.buildFileLoc;

			Runnable worker = new ProjectBuildRunnable(svnBuildDirLocation, this.versionMap.get(revision), this.jarProperty);
			executor.execute(worker);
		}

		executor.shutdown();
		while (!executor.isTerminated()) {
			// Wait until all threads are finish
		}
	}

	private void createMetricAnalysisDirIfNotExist() {
		File metricAnalysisDir = new File("MetricAnalysis");

		if (!metricAnalysisDir.exists()) {
			try {
				metricAnalysisDir.mkdir();
			} catch (SecurityException se) {
				se.printStackTrace();
			}
		}

		File projectDir = new File("MetricAnalysis\\" + this.projectName);
		if (!projectDir.exists()) {
			try {
				projectDir.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void runMetricAnalysis() {
		ExecutorService executor = Executors.newFixedThreadPool(this.versionMap
				.keySet().size());

		this.createMetricAnalysisDirIfNotExist();
		
		String jarLoc = this.buildFileLoc;
		if(this.buildFileLoc.contains("build.xml")){
			jarLoc = jarLoc.substring(0, jarLoc.length()-9);
		} else {
			jarLoc = jarLoc.substring(0, jarLoc.length()-7);
		}
		
		for (int revision : this.versionMap.keySet()) {
			String jarFileLocation = this.svnDir + "\\" + revision +"\\" + jarLoc + "target";

			Runnable worker = new MetricAnalyserRunnable(jarFileLocation,
					"MetricAnalysis\\" + this.projectName + "\\"
							+ this.versionMap.get(revision) + ".xml");
			executor.execute(worker);
		}

		executor.shutdown();
		while (!executor.isTerminated()) {
			// Wait until all threads are finish
		}
	}

	public void parseVersionChanges() {

		Iterator<Integer> itr = this.versionMap.keySet().iterator();
		int start = 0;

		while (itr.hasNext()) {
			int end = itr.next();
			VersionControlParserFactory svnParser = new VersionControlParserFactory(
					this.svnURL, (long) start, (long) end);
			svnParser.loadVersionControlInfo();
			ArrayList<DiffClass> dcList = svnParser.getDiffClassList();

			String version = this.versionMap.get(end);

			diffMap.put(version, dcList);
			start = end;

			updateProgressBar();
		}
	}

	public static void updateProgressBar() {
		currentProgress++;
		int value = (int) (currentProgress * 100 / totalProgress);
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
				this.versionMap, this.projectName);
		resultsTable.setVisible(true);
	}

	private String[][] getTableDataValues(int colNum, int rowNum) {
		String[][] dataValues = new String[rowNum][colNum];
		for (String[] row : dataValues)
			Arrays.fill(row, "");
		Iterator<String> itr = this.packageClassMap.keySet().iterator();

		ArrayList<String> tempList = new ArrayList<>();
		while (itr.hasNext()) {
			tempList.add(itr.next());
		}

		Comparator<String> comp = getPackageSortComparator();
		Collections.sort(tempList, comp);
		itr = tempList.iterator();

		int i = 0;
		while (itr.hasNext()) {
			String packageName = itr.next();
			dataValues[i][0] = packageName;
			i++;
			ArrayList<String[]> classList = this.packageClassMap
					.get(packageName);

			Comparator<String[]> comparator = getClassSortComparator();

			Collections.sort(classList, comparator);
			for (String[] value : this.packageClassMap.get(packageName)) {

				dataValues[i] = value;
				i++;
			}
			i++;
		}
		return dataValues;
	}

	private Comparator<String[]> getClassSortComparator() {
		Comparator<String[]> comparator = new Comparator<String[]>() {

			@Override
			public int compare(String[] o1, String[] o2) {
				String s1 = "";
				String s2 = "";
				for (String t1 : o1) {
					if (t1.length() > 0) {
						s1 = t1;
						break;
					}
				}
				for (String t2 : o2) {
					if (t2.length() > 0) {
						s2 = t2;
						break;
					}
				}
				return s1.compareTo(s2);
			}
		};
		return comparator;
	}

	private Comparator<String> getPackageSortComparator() {
		Comparator<String> comparator = new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				o1 = o1.substring(9);
				o2 = o2.substring(9);
				o1 = o1.replace(".", "");
				o2 = o2.replace(".", "");
				return o1.compareTo(o2);
			}
		};
		return comparator;
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
