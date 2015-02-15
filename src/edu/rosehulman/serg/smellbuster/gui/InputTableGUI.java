package edu.rosehulman.serg.smellbuster.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class InputTableGUI extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private JScrollPane scrollPane;
	private DefaultTableModel tableModel;
	private String[] columnNames;

	public InputTableGUI() {
		setBackground(Color.gray);
		setLayout(new BorderLayout());

		this.columnNames = new String[] { "Product Release Version",
				"SVN Revision Number" };

		this.tableModel = new DefaultTableModel(new String[][]{}, columnNames);
		this.tableModel.addRow(new Object[] { "", "" });
		table = new JTable(this.tableModel);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		table.setBackground(Color.LIGHT_GRAY);
		table.setForeground(Color.BLACK);

		TableCellListener tcl = new TableCellListener();
		this.table.addPropertyChangeListener(tcl);

		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
	}

	public JTable getTable() {
		return this.table;
	}
	
	public void loadTableValues(Map<Integer, String> versionMap){
		int len = versionMap.keySet().size();
		Iterator<Integer> itr = versionMap.keySet().iterator();
	
		String[][] values = new String[len][2];
		int i = 0;
		
		while(itr.hasNext()){
			int revision = itr.next();
			values[i][0] = versionMap.get(revision);
			values[i][1] = revision + "";
			i++;
		}
		
		this.tableModel = new DefaultTableModel(values, columnNames);
		this.tableModel.addRow(new Object[] { "", "" });
		
		this.table.setModel(tableModel);
	}

	private class TableCellListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent e) {

			if ("tableCellEditor".equals(e.getPropertyName())) {
				int row = InputTableGUI.this.table.getSelectedRow();

				int rows = table.getRowCount();
				int cols = table.getColumnCount();

				if (row == (rows - 1)) {
					boolean isCellEmpty = false;
					for (int i = 0; i < cols; i++) {
						String cell = InputTableGUI.this.table.getValueAt(row, i)
								.toString();
						if (cell.equals(""))
							isCellEmpty = true;
					}
					if (!isCellEmpty) {
						InputTableGUI.this.tableModel
								.addRow(new Object[] { "", "" });
					}
				}

			}
		}
	}
}
