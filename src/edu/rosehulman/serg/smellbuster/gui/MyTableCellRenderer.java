package edu.rosehulman.serg.smellbuster.gui;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class MyTableCellRenderer extends DefaultTableCellRenderer implements
		TableCellRenderer {

	private static final long serialVersionUID = 1L;
	private ResultTableGUI resultTableGUI;
	private int selectedMetric;
	private ArrayList<Integer> packageHeaders;

	public MyTableCellRenderer(ResultTableGUI resultTableGUI, int selectedMetric) {
		this.resultTableGUI = resultTableGUI;
		this.selectedMetric = selectedMetric;
		this.packageHeaders = new ArrayList<>();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		TableCell cell = new TableCell(value.toString(), row, column,
				this.resultTableGUI.getResultTableLogic());
		cell.setDataValues(this.resultTableGUI.getDataValues());
		cell.setColumnNames(this.resultTableGUI.getColumnNames());
		cell.setSelectedMetric(selectedMetric);
		cell.setForeground(Color.WHITE);
		cell.setBackground(Color.LIGHT_GRAY);

		if (value != null && value.toString().length() > 0
				&& value.toString().contains("Package: ")) {
			this.packageHeaders.add(row);
			cell.setIsHeader(true);
		} else {
			if (this.packageHeaders.contains(row)) {
				cell.setIsHeader(true);
			}
		}

		return cell;
	}
}
