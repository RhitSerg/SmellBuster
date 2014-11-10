package thesis.tool.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class InputTable extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private JScrollPane scrollPane;
	private DefaultTableModel tableModel;

	public InputTable() {
		setBackground(Color.gray);
		setLayout(new BorderLayout());

		// Create columns names
		String columnNames[] = new String[] { "Product Release Version",
				"SVN Revision Number" };

		String[][] values = new String[][] { { "1.0.6", "91" },
				{ "1.0.7", "588" }, { "1.0.8", "655" }, { "1.0.9", "706" },
				{ "1.0.10", "1060" }, { "1.0.11", "1633" },
				{ "1.0.12", "1769" }, { "1.0.13", "2010" },
				{ "1.0.14", "2440" }, { "1.0.15", "2763" },
				{ "1.0.16", "2924" }, { "1.0.17", "3070" },
				{ "1.0.18", "3247" }, { "1.0.19", "3284" } };

		// Create a new table instance
		this.tableModel = new DefaultTableModel(values, columnNames);
		this.tableModel.addRow(new Object[]{"", ""});
		table = new JTable(this.tableModel);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		table.setBackground(Color.LIGHT_GRAY);
		
		TableCellListener tcl = new TableCellListener();
		this.table.addPropertyChangeListener(tcl);
		
		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public JTable getTable(){
		return this.table;
	}
	
	private class TableCellListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent e) {

			if ("tableCellEditor".equals(e.getPropertyName())) {
				int row = InputTable.this.table.getSelectedRow();
				
				int rows = table.getRowCount();
			    int cols = table.getColumnCount();
			    
			    if (row == (rows -1)){
			    	boolean isCellEmpty = false;
			    	for (int i=0; i<cols; i++){
			    		String cell = InputTable.this.table.getValueAt(row, i).toString();
			    		if (cell.equals(""))
			    			isCellEmpty = true;
			    	}
			    	if (!isCellEmpty){
			    		InputTable.this.tableModel.addRow(new Object[]{"", ""});
			    	}
			    }
			    
			}
		}
	}
}
