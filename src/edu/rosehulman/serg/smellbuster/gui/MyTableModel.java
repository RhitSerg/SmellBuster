package edu.rosehulman.serg.smellbuster.gui;

import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;

	public MyTableModel(String[][] dataValues, String[] columnNames) {
		super(dataValues, columnNames);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
