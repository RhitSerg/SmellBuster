package edu.rosehulman.serg.smellbuster.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import edu.rosehulman.serg.smellbuster.logic.ResultTableLogic;
import edu.rosehulman.serg.smellbuster.util.*;

public class ResultTableGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private String[] columnNames;
	private JPanel topPanel;
	private JPanel tablePanel;
	private JComboBox<String> metricComboBox;
	private JLabel comboLabel;
	private JTable table;
	private JScrollPane scrollPane;
	private Map<Integer, String> versionMap;
	private String[][] dataValues;
	private ResultTableLogic resultTableLogic;
	private MyTableModel tableModel;
	private String[] metrics;
	private int selectedMetric;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem settingsMenuItem;
	private JMenuItem quitMenuItem;
	private MetricGUI metricGUI;
	//private Map<String, int[]> dataMap;

	// Constructor of main frame
	public ResultTableGUI(String[][] dataValues,
			Map<Integer, String> versionMap, String projectName) {

		this.versionMap = versionMap;
		this.resultTableLogic = new ResultTableLogic(this.versionMap,
				projectName);
		this.selectedMetric = 0;
		this.metricGUI = new MetricGUI(this.resultTableLogic);
		this.metrics = this.metricGUI.getMetricDisplayList();

		this.columnNames = getColumnNames();
		this.dataValues = dataValues;
		this.tableModel = new MyTableModel(dataValues, columnNames);

		/**
		//Uncomment when want to display statistics
		this.dataMap = new HashMap<>();
		initializeDataMap();
		*/
		
		initMenu();
		initTable();
		initComboBox();
		initPanel();
		initFrame();

	}

	/**
	//Uncomment when want to display the statistics
	private void displayData() {
		for (int col = 0; col < this.columnNames.length; col++) {

			int[] dataArr = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };

			for (int i = 0; i < this.table.getRowCount(); i++) {
				String name = this.table.getModel().getValueAt(i, col)
						.toString();
				if (name.contains(".java")) {
					dataArr[0] = dataArr[0] + 1;

					Color c = this.displayTableLogic.getColorForMetricScore(0,
							name, this.columnNames[col]);
					int index = this.getColorCategory(c);
					if (index > -1) {
						dataArr[index] = dataArr[index] + 1;
					}
				}
			}
			String version = this.columnNames[col];
			this.dataMap.put(version, dataArr);
		}

		for (String version : this.dataMap.keySet()) {
			System.out.print(version + "\t");
			int[] dataArr = this.dataMap.get(version);
			for (int num : dataArr)
				System.out.print(num + ",");
			System.out.println();
		}
	}

	private void initializeDataMap() {
		for (int ver : this.versionMap.keySet()) {
			String version = this.versionMap.get(ver);
			int[] tempArr = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
			this.dataMap.put(version, tempArr);
		}
	}*/

	private void initMenu() {
		this.menuBar = new JMenuBar();
		this.fileMenu = new JMenu("File");
		if (OSDetector.isWindows()) {
			this.settingsMenuItem = new JMenuItem("Settings", new ImageIcon(
					"assets\\settings.png"));
			this.quitMenuItem = new JMenuItem("Quit", new ImageIcon(
					"assets\\quit.png"));
		} else {
			this.settingsMenuItem = new JMenuItem("Settings", new ImageIcon(
					"assets/settings.png"));
			this.quitMenuItem = new JMenuItem("Quit", new ImageIcon(
					"assets/quit.png"));
		}

		this.menuBar.add(this.fileMenu);
		this.fileMenu.add(this.settingsMenuItem);
		this.fileMenu.add(this.quitMenuItem);
		this.settingsMenuItem.addActionListener(this);
		this.quitMenuItem.addActionListener(this);
	}

	private void initComboBox() {
		this.metricComboBox = new JComboBox<>();
		this.metricComboBox.addActionListener(this);
		this.comboLabel = new JLabel("Choose metric to display heat map: ");

		for (String metric : this.metrics) {
			this.metricComboBox.addItem(metric);
		}
	}

	private void initTable() {

		table = new JTable(tableModel);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		table.setDefaultRenderer(Object.class, new MyTableCellRenderer(this, this.selectedMetric));
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				int row = ResultTableGUI.this.table.getSelectedRow();
				int column = ResultTableGUI.this.table.getSelectedColumn();

				final String title = "Track info";
				final String version = columnNames[column];
				final String className = ResultTableGUI.this.dataValues[row][column];
				if (className != null && className.length() > 0) {
					final String message = ResultTableGUI.this.metricGUI
							.getDisplayMessage(version, className);
					final String[] options = new String[] { "Ok",
							"Display Graph" };
					int selectedOption = JOptionPane.showOptionDialog(null,
							message, title, JOptionPane.DEFAULT_OPTION,
							JOptionPane.PLAIN_MESSAGE, null, options,
							options[0]);
					switch (selectedOption) {
					case 1:
						ArrayList<MetricDOMObject> classList = ResultTableGUI.this.resultTableLogic
								.createClassListMap(className);
						LineChartGUI chart = new LineChartGUI(className,
								classList, ResultTableGUI.this.versionMap
										.keySet());
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
		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		this.topPanel = new JPanel();
		this.topPanel.setLayout(new BorderLayout());
		this.topPanel.add(this.comboLabel, BorderLayout.WEST);
		this.topPanel.add(this.metricComboBox, BorderLayout.CENTER);
	}

	private void initFrame() {
		setJMenuBar(this.menuBar);
		setLayout(new BorderLayout());
		getContentPane().add(this.topPanel, BorderLayout.NORTH);
		getContentPane().add(tablePanel, BorderLayout.CENTER);
		setTitle("SmellBuster - Metric Analysis Result");
		setSize(1300, 700);
		setBackground(Color.gray);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	public int getColorCategory(Color color) {

		if (color.equals(new Color(255, 0, 0))) {
			return 1;
		} else if (color.equals(new Color(204, 0, 0))) {
			return 2;
		} else if (color.equals(new Color(159, 95, 0))) {
			return 3;
		} else if (color.equals(new Color(255, 102, 0))) {
			return 4;
		} else if (color.equals(new Color(255, 204, 0))) {
			return 5;
		} else if (color.equals(new Color(95, 159, 0))) {
			return 6;
		} else if (color.equals(new Color(0, 255, 0))) {
			return 7;
		}
		return -1;
	}*/

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

	public ResultTableLogic getResultTableLogic() {
		return this.resultTableLogic;
	}
	
	public String[][] getDataValues(){
		return this.dataValues;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.metricComboBox)) {
			updateTable();
		} else if (e.getSource().equals(this.quitMenuItem)) {
			System.exit(0);
		} else if (e.getSource().equals(this.settingsMenuItem)) {
			SettingsGUI settingsGUI = new SettingsGUI(this);
			settingsGUI.setVisible(true);
		}
	}

	public void updateTable() {
		this.selectedMetric = this.metricComboBox.getSelectedIndex();
		this.tableModel.fireTableDataChanged();
	}

}
