package edu.rosehulman.serg.smellbuster.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import edu.rosehulman.serg.smellbuster.logic.DisplayTableLogic;
import edu.rosehulman.serg.smellbuster.util.*;

public class DisplayTable extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel topPanel;
	private JTable table;
	private JScrollPane scrollPane;
	private Map<Integer, String> versionMap;
	private String[][] dataValues;
	private DisplayTableLogic displayTableLogic;
	private MyTableModel tableModel;

	// Constructor of main frame
	public DisplayTable(String[][] dataValues, Map<Integer, String> versionMap) {

		this.versionMap = versionMap;
		this.displayTableLogic = new DisplayTableLogic(this.versionMap);
		
		initTable(dataValues);
		initPanel();
		initFrame();
		
	}

	private void initTable(String[][] dataValues) {
		final String columnNames[] = getColumnNames();
		this.dataValues = dataValues;
		this.tableModel = new MyTableModel(dataValues, columnNames);

		table = new JTable(tableModel);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		table.setDefaultRenderer(Object.class, new MyTableCellRenderer());
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				int row = DisplayTable.this.table.getSelectedRow();
				int column = DisplayTable.this.table.getSelectedColumn();

				final String title = "Track info";
				final String version = columnNames[column];
				final String className = DisplayTable.this.dataValues[row][column];
				if (className != null && className.length() > 0) {
					final String message = "<html><table>" + "<tr><td>Version"
							+ "</td><td>"
							+ version
							+ "</td></tr>"
							+ "<tr><td>Class Name"
							+ "</td><td>"
							+ className
							+ "</td></tr>"
							+ "<tr><td>Weighted Methods per Class (WMC)"
							+ "</td><td>"
							+ DisplayTable.this.displayTableLogic.getWMCValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Number of Children (NOC)"
							+ "</td><td>"
							+ DisplayTable.this.displayTableLogic.getNOCValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Coupling Between Object Classes (CBO)"
							+ "</td><td>"
							+ DisplayTable.this.displayTableLogic.getCBOValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Lack of Cohesion in Methods (LCOM)"
							+ "</td><td>"
							+ DisplayTable.this.displayTableLogic.getLCOMValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Afferent Coupling (Ca)"
							+ "</td><td>"
							+ DisplayTable.this.displayTableLogic.getCAValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Efferent Coupling (Ce)"
							+ "</td><td>"
							+ DisplayTable.this.displayTableLogic.getCEValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Lack of Cohesion in Methods (LCOM3)"
							+ "</td><td>"
							+ DisplayTable.this.displayTableLogic.getLCOM3ValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Cohesion Among Methods of Class (CAM)"
							+ "</td><td>"
							+ DisplayTable.this.displayTableLogic.getCAMValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Inheritance Coupling (IC)"
							+ "</td><td>"
							+ DisplayTable.this.displayTableLogic.getICValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Coupling Between Methods (CBM)"
							+ "</td><td>"
							+ DisplayTable.this.displayTableLogic.getCBMValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Average Method Complexity (AMC)"
							+ "</td><td>"
							+ DisplayTable.this.displayTableLogic.getAMCValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Cyclomatic Complexoty (CC)"
							+ "</td><td>"
							+ DisplayTable.this.displayTableLogic.getCCValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Aggregate Metrics "
							+ "</td><td>"
							+ DisplayTable.this.displayTableLogic.getAggregateMetrics(className)
							+ "</td></tr>"
							+ "<tr><td>Aggregate Metrics of Max Values "
							+ "</td><td>"
							+ DisplayTable.this.displayTableLogic
									.getAggregateMetricsOfMax(className)
							+ "</td></tr>"
							+ "<tr><td>Aggregate Metrics of Min Values "
							+ "</td><td>"
							+ DisplayTable.this.displayTableLogic
									.getAggregateMetricsOfMin(className)
							+ "</td></tr>" + "</table>";
					final String[] options = new String[] { "Ok",
							"Display Graph" };
					int selectedOption = JOptionPane.showOptionDialog(null,
							message, title, JOptionPane.DEFAULT_OPTION,
							JOptionPane.PLAIN_MESSAGE, null, options,
							options[0]);
					switch (selectedOption) {
					case 1:
						ArrayList<JavaClass> classList = DisplayTable.this.displayTableLogic.createClassListMap(className);
						LineChart chart = new LineChart(className, classList);
						chart.setSize(500, 500);
						chart.setVisible(true);
						break;
					default:
						break;
					}
				}
			}

		});
		scrollPane = new JScrollPane(table);
	}

	private void initPanel() {
		topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.add(scrollPane, BorderLayout.CENTER);
	}

	private void initFrame() {		
		getContentPane().add(topPanel);
		setTitle("SmellBuster - Metric Analysis Result");
		setSize(1300, 700);
		setBackground(Color.gray);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private class MyTableCellRenderer extends DefaultTableCellRenderer
			implements TableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component c = (Component) super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);
			c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
			return c;
		}

	}
	
	private class MyTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		public MyTableModel(String[][] dataValues, String[] columnNames) {
			super(dataValues, columnNames);
		}
		@Override
	    public boolean isCellEditable(int row, int column) {
	       return false;
	    }
	}

	public String[] getColumnNames() {
		int len = this.versionMap.keySet().size();
		String[] colNames = new String[len];

		Arrays.fill(colNames, "");
		Iterator<Integer> itr = this.versionMap.keySet().iterator();
		int k = 0;
		while (itr.hasNext()) {
			colNames[k] = this.versionMap.get(itr.next());
			k++;
		}

		return colNames;
	}
}
