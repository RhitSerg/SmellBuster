package edu.rosehulman.serg.smellbuster.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import edu.rosehulman.serg.smellbuster.util.MetricDOMObject;

public class TableCellMouseListener extends MouseAdapter {
	
	private ResultTableGUI resultTableGUI;
	
	public TableCellMouseListener(ResultTableGUI resultTableGUI){
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
			final String[] options = new String[] { "Ok",
					"Display Graph" };
			int selectedOption = JOptionPane.showOptionDialog(null,
					message, title, JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, options,
					options[0]);
			switch (selectedOption) {
			case 1:
				ArrayList<MetricDOMObject> classList = this.resultTableGUI.getResultTableLogic()
						.createClassListMap(className);
				LineChartGUI chart = new LineChartGUI(className,
						classList, this.resultTableGUI.getVersionMap()
								.keySet());
				chart.setSize(500, 500);
				chart.setVisible(true);
				break;
			default:
				break;
			}
		}
	}

}
