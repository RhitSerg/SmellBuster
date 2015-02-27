package edu.rosehulman.serg.smellbuster.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;

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
	private JMenuItem statsMenuItem;
	private JMenuItem quitMenuItem;
	private MetricGUI metricGUI;
	private String projectName;

	// Constructor of main frame
	public ResultTableGUI(String[][] dataValues,
			Map<Integer, String> versionMap, String projectName) {

		this.versionMap = versionMap;
		this.projectName = projectName;
		this.resultTableLogic = new ResultTableLogic(this.versionMap,
				this.projectName);
		this.selectedMetric = 0;
		this.metricGUI = new MetricGUI(this.resultTableLogic);
		this.metrics = this.metricGUI.getMetricDisplayList();

		this.columnNames = getColumnNames();
		this.dataValues = dataValues;
		this.tableModel = new MyTableModel(dataValues, columnNames);

		initMenu();
		initTable();
		initComboBox();
		initPanel();
		initFrame();
	}

	private void initMenu() {
		this.menuBar = new JMenuBar();
		this.fileMenu = new JMenu("File");
		if (OSDetector.isWindows()) {
			this.settingsMenuItem = new JMenuItem("Settings", new ImageIcon(
					"assets\\settings.png"));
			this.statsMenuItem = new JMenuItem("Display Statistics",
					new ImageIcon("assets\\statistics.png"));
			this.quitMenuItem = new JMenuItem("Quit", new ImageIcon(
					"assets\\quit.png"));
		} else {
			this.settingsMenuItem = new JMenuItem("Settings", new ImageIcon(
					"assets/settings.png"));
			this.statsMenuItem = new JMenuItem("Display Statistics",
					new ImageIcon("assets/statistics.png"));
			this.quitMenuItem = new JMenuItem("Quit", new ImageIcon(
					"assets/quit.png"));
		}

		this.menuBar.add(this.fileMenu);
		this.fileMenu.add(this.settingsMenuItem);
		this.fileMenu.add(this.statsMenuItem);
		this.fileMenu.add(this.quitMenuItem);
		this.settingsMenuItem.addActionListener(this);
		this.quitMenuItem.addActionListener(this);
		this.statsMenuItem.addActionListener(this);
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

		this.table = new JTable(tableModel);
		this.table.setRowSelectionAllowed(false);
		this.table.setCellSelectionEnabled(true);
		this.table.setDefaultRenderer(Object.class, new MyTableCellRenderer(
				this, this.selectedMetric));

		// Center align table header
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) this.table
				.getTableHeader().getDefaultRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);

		this.table.addMouseListener(new TableCellMouseListener(this));
		scrollPane = new JScrollPane(this.table);
	}

	private void initPanel() {
		this.tablePanel = new JPanel();
		this.tablePanel.setLayout(new BorderLayout());
		this.tablePanel.add(scrollPane, BorderLayout.CENTER);

		this.topPanel = new JPanel();
		this.topPanel.setLayout(new BorderLayout());
		this.topPanel.add(this.comboLabel, BorderLayout.WEST);
		this.topPanel.add(this.metricComboBox, BorderLayout.CENTER);
	}

	private void initFrame() {
		this.setJMenuBar(this.menuBar);
		this.setLayout(new BorderLayout());
		this.getContentPane().add(this.topPanel, BorderLayout.NORTH);
		this.getContentPane().add(this.tablePanel, BorderLayout.CENTER);
		this.setTitle("SmellBuster - Metric Analysis Result");
		this.setSize(1300, 700);
		this.setBackground(Color.gray);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

	public ResultTableLogic getResultTableLogic() {
		return this.resultTableLogic;
	}

	public String[][] getDataValues() {
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
		} else if (e.getSource().equals(this.statsMenuItem)) {
			PieChartGUI pieChart = new PieChartGUI(this.projectName
					+ " Metric Analysis Statistics", this);
			pieChart.setSize(700, 500);
			pieChart.setVisible(true);
		}
	}

	public void updateTable() {
		this.selectedMetric = this.metricComboBox.getSelectedIndex();
		this.tableModel.fireTableDataChanged();
	}

	public JTable getTable() {
		return this.table;
	}

	public MetricGUI getMetricGUI() {
		return this.metricGUI;
	}

	public Map<Integer, String> getVersionMap() {
		return this.versionMap;
	}

}
