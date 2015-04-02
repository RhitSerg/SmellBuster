package edu.rosehulman.serg.smellbuster.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import edu.rosehulman.serg.smellbuster.smellparser.CodeSmellParser;
import edu.rosehulman.serg.smellbuster.util.MetricDOMObject;

public class TableCellMouseListener extends MouseAdapter {

	private ResultTableGUI resultTableGUI;

	public TableCellMouseListener(ResultTableGUI resultTableGUI) {
		this.resultTableGUI = resultTableGUI;
	}

	public void mouseClicked(MouseEvent e) {

		int row = this.resultTableGUI.getTable().getSelectedRow();
		int column = this.resultTableGUI.getTable().getSelectedColumn();

		final String title = "Track info";
		final String version = this.resultTableGUI.getColumnNames()[column];
		final String className = this.resultTableGUI.getDataValues()[row][column];
		if (className != null && className.length() > 0) {
			final String message = this.resultTableGUI.getMetricGUI()
					.getDisplayMessage(version, className);
			final String[] options = new String[] { "Ok", "Display Graph",
					"Recommendations" };
			int selectedOption = JOptionPane.showOptionDialog(null, message,
					title, JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			switch (selectedOption) {
			case 1:
				ArrayList<MetricDOMObject> classList = this.resultTableGUI
						.getResultTableLogic().createClassListMap(className);
				LineChartGUI chart = new LineChartGUI(className, classList,
						this.resultTableGUI.getVersionMap().keySet());
				chart.setSize(500, 500);
				chart.setVisible(true);
				break;
			case 2:
				String filePath = this.getFilePathFor(className, version);
				RecommendationDisplayerGUI recommendGUI = new RecommendationDisplayerGUI(
						filePath, className);
				recommendGUI.setVisible(true);
				break;
			default:
				break;
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private String getFilePathFor(String className, String version) {
		String location = System.getProperty("user.dir") + File.separator
				+ "repo" + File.separator
				+ this.resultTableGUI.getProjectName() + File.separator
				+ this.resultTableGUI.getRevisionNumberForVersion(version);
		File root = new File(location);
		try {
			boolean recursive = true;

			Collection files = FileUtils.listFiles(root, null, recursive);

			for (Iterator iterator = files.iterator(); iterator.hasNext();) {
				File file = (File) iterator.next();
				if (file.isDirectory()
						&& (file.getName().equalsIgnoreCase("src") || file
								.getName().equalsIgnoreCase("source"))) {
					root = file;
					break;
				}
			}
			
			files = FileUtils.listFiles(root, null, recursive);
			for (Iterator iterator = files.iterator(); iterator.hasNext();) {
				File file = (File) iterator.next();
				if (file.getName().equalsIgnoreCase(className))
					return file.getAbsolutePath();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
