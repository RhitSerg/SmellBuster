package edu.rosehulman.serg.smellbuster.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

import edu.rosehulman.serg.smellbuster.logic.ResultTableLogic;
import edu.rosehulman.serg.smellbuster.util.*;

public class ResultTableGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel topPanel;
	private JTable table;
	private JScrollPane scrollPane;
	private Map<Integer, String> versionMap;
	private String[][] dataValues;
	private ResultTableLogic displayTableLogic;
	private MyTableModel tableModel;
	private Map<String, Color> heatMapColors;
	private String critical = "critical";
	private String highImportance = "high";
	private String mediumImportance = "medium";
	private String lowImportance = "low";
	private String safelyIgnore = "safe";

	// Constructor of main frame
	public ResultTableGUI(String[][] dataValues, Map<Integer, String> versionMap) {

		this.versionMap = versionMap;
		this.displayTableLogic = new ResultTableLogic(this.versionMap);
		this.heatMapColors = new HashMap<>();
		
		this.loadHeatMapColors();
//		double maxAggregateValue = Double.parseDouble(this.displayTableLogic.getAggregateMetrics(dataValues[0][0]));
//		double minAggregateValue = Double.parseDouble(this.displayTableLogic.getAggregateMetrics(dataValues[0][0]));
//		for(String[] row: dataValues){
//			for (String name: row){
//				if (name != null && name.length() > 0){
//					double value = Double.parseDouble(this.displayTableLogic.getAggregateMetrics(name));
//					maxAggregateValue = Math.max(maxAggregateValue, value);
//					minAggregateValue = Math.min(minAggregateValue, value);
//					break;
//				}
//			}
//		}
//		
//		System.out.println(maxAggregateValue+" Max");
//		System.out.println(minAggregateValue+" Min");

		initTable(dataValues);
		initPanel();
		initFrame();

	}
	
	private void loadHeatMapColors(){
		Color critical = new Color(0xAB0000);
		Color high = new Color(0xB24026);
		Color medium = new Color(0xC96164);
		Color low = new Color(0x77AB6B);
		Color safe = new Color(0xBEF29D);
		
		this.heatMapColors.put(this.critical, critical);
		this.heatMapColors.put(this.highImportance, high);
		this.heatMapColors.put(this.mediumImportance, medium);
		this.heatMapColors.put(this.lowImportance, low);
		this.heatMapColors.put(this.safelyIgnore, safe);
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

				int row = ResultTableGUI.this.table.getSelectedRow();
				int column = ResultTableGUI.this.table.getSelectedColumn();

				final String title = "Track info";
				final String version = columnNames[column];
				final String className = ResultTableGUI.this.dataValues[row][column];
				if (className != null && className.length() > 0) {
					final String message = getDisplayMessage(version, className);
					final String[] options = new String[] { "Ok",
							"Display Graph" };
					int selectedOption = JOptionPane.showOptionDialog(null,
							message, title, JOptionPane.DEFAULT_OPTION,
							JOptionPane.PLAIN_MESSAGE, null, options,
							options[0]);
					switch (selectedOption) {
					case 1:
						ArrayList<JavaClass> classList = ResultTableGUI.this.displayTableLogic
								.createClassListMap(className);
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
	
	private Color getColorForMetricScore(double score){
		if (score >= -2457){
			return this.heatMapColors.get(this.safelyIgnore);
		} else if (score < -2457 && score >= -5062){
			return this.heatMapColors.get(this.lowImportance);
		} else if (score < -5062 && score >= -7649){
			return this.heatMapColors.get(this.mediumImportance);
		} else if (score < -7649 && score >= -10236){
			return this.heatMapColors.get(this.highImportance);
		} else {
			return this.heatMapColors.get(this.critical);
		}
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
			if (value != null && value.toString().length() > 0){
				double score = Double.parseDouble(ResultTableGUI.this.displayTableLogic.getAggregateMetrics(value.toString()));
				c.setBackground(ResultTableGUI.this.getColorForMetricScore(score));
			}else{
				c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY: Color.WHITE);
			}
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

	private String getDisplayMessage(String version, String className) {
		return "<html><table>" 
				+ getMessageForVersion(version)
				+ getMessageForClassName(className)
				+ getMessageForWMC(version, className)
				+ getMessageForNOC(version, className)
				+ getMessageForCBO(version, className)
				+ getMessageForLCOM(version, className)
				+ getMessageForCA(version, className)
				+ getMessageForCE(version, className)
				+ getMessageForLCOM3(version, className)
				+ getMessageForCAM(version, className)
				+ getMessageForIC(version, className)
				+ getMessageForCBM(version, className)
				+ getMessageForAMC(version, className)
				+ getMessageForCC(version, className)
				+ getMessageForAggregateMetrics(version, className)
				+ getMessageForMaxAggregateValues(version, className)
				+ getMessageForAggregateMinValues(version, className)
				+ "</table></html>";
	}

	private String getMessageForVersion(String version) {
		return "<tr><td>Version" + "</td><td>" + version + "</td></tr>";
	}

	private String getMessageForClassName(String className) {
		return "<tr><td>Class Name" + "</td><td>" + className + "</td></tr>";
	}

	private String getMessageForWMC(String version, String className) {
		return "<tr><td>Weighted Methods per Class (WMC)"
				+ "</td><td>"
				+ ResultTableGUI.this.displayTableLogic.getWMCValueFor(version,
						className) + "</td></tr>";
	}

	private String getMessageForNOC(String version, String className) {
		return "<tr><td>Number of Children (NOC)"
				+ "</td><td>"
				+ ResultTableGUI.this.displayTableLogic.getNOCValueFor(version,
						className) + "</td></tr>";
	}

	private String getMessageForCBO(String version, String className) {
		return "<tr><td>Coupling Between Object Classes (CBO)"
				+ "</td><td>"
				+ ResultTableGUI.this.displayTableLogic.getCBOValueFor(version,
						className) + "</td></tr>";
	}

	private String getMessageForLCOM(String version, String className) {
		return "<tr><td>Lack of Cohesion in Methods (LCOM)"
				+ "</td><td>"
				+ ResultTableGUI.this.displayTableLogic.getLCOMValueFor(
						version, className) + "</td></tr>";
	}

	private String getMessageForCA(String version, String className) {
		return "<tr><td>Afferent Coupling (Ca)"
				+ "</td><td>"
				+ ResultTableGUI.this.displayTableLogic.getCAValueFor(version,
						className) + "</td></tr>";
	}

	private String getMessageForCE(String version, String className) {
		return "<tr><td>Efferent Coupling (Ce)"
				+ "</td><td>"
				+ ResultTableGUI.this.displayTableLogic.getCEValueFor(version,
						className) + "</td></tr>";
	}

	private String getMessageForLCOM3(String version, String className) {
		return "<tr><td>Lack of Cohesion in Methods (LCOM3)"
				+ "</td><td>"
				+ ResultTableGUI.this.displayTableLogic.getLCOM3ValueFor(
						version, className) + "</td></tr>";
	}

	private String getMessageForCAM(String version, String className) {
		return "<tr><td>Cohesion Among Methods of Class (CAM)"
				+ "</td><td>"
				+ ResultTableGUI.this.displayTableLogic.getCAMValueFor(version,
						className) + "</td></tr>";
	}

	private String getMessageForIC(String version, String className) {
		return "<tr><td>Inheritance Coupling (IC)"
				+ "</td><td>"
				+ ResultTableGUI.this.displayTableLogic.getICValueFor(version,
						className) + "</td></tr>";
	}

	private String getMessageForCBM(String version, String className) {
		return "<tr><td>Coupling Between Methods (CBM)"
				+ "</td><td>"
				+ ResultTableGUI.this.displayTableLogic.getCBMValueFor(version,
						className) + "</td></tr>";
	}

	private String getMessageForAMC(String version, String className) {
		return "<tr><td>Average Method Complexity (AMC)"
				+ "</td><td>"
				+ ResultTableGUI.this.displayTableLogic.getAMCValueFor(version,
						className) + "</td></tr>";
	}

	private String getMessageForCC(String version, String className) {
		return "<tr><td>Cyclomatic Complexoty (CC)"
				+ "</td><td>"
				+ ResultTableGUI.this.displayTableLogic.getCCValueFor(version,
						className) + "</td></tr>";
	}

	private String getMessageForAggregateMetrics(String version,
			String className) {
		return "<tr><td>Aggregate Metrics "
				+ "</td><td>"
				+ ResultTableGUI.this.displayTableLogic
						.getAggregateMetrics(className) + "</td></tr>";
	}

	private String getMessageForMaxAggregateValues(String version,
			String className) {
		return "<tr><td>Aggregate Metrics of Max Values "
				+ "</td><td>"
				+ ResultTableGUI.this.displayTableLogic
						.getAggregateMetricsOfMax(className) + "</td></tr>";
	}

	private String getMessageForAggregateMinValues(String version,
			String className) {
		return "<tr><td>Aggregate Metrics of Min Values "
				+ "</td><td>"
				+ ResultTableGUI.this.displayTableLogic
						.getAggregateMetricsOfMin(className) + "</td></tr>";
	}

}
