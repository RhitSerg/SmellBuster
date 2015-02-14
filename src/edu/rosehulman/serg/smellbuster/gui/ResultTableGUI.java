package edu.rosehulman.serg.smellbuster.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

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
	private ResultTableLogic displayTableLogic;
	private MyTableModel tableModel;
	private String[] metrics;
	private int selectedMetric;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem settingsMenuItem;
	private JMenuItem quitMenuItem;
	private MetricGUI metricGUI;
	private ArrayList<Integer> packageHeaders;

	// Constructor of main frame
	public ResultTableGUI(String[][] dataValues, Map<Integer, String> versionMap, String projectName) {

		this.versionMap = versionMap;
		this.packageHeaders = new ArrayList<>();
		this.displayTableLogic = new ResultTableLogic(this.versionMap, projectName);
		this.selectedMetric = 0;
		this.metricGUI = new MetricGUI(this.displayTableLogic);
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
		table.setDefaultRenderer(Object.class, new MyTableCellRenderer());
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
						ArrayList<MetricDOMObject> classList = ResultTableGUI.this.displayTableLogic
								.createClassListMap(className);
						LineChart chart = new LineChart(className, classList, ResultTableGUI.this.versionMap.keySet());
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

	private class MyTableCellRenderer extends DefaultTableCellRenderer
			implements TableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component c = (Component) super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);
			c.setForeground(Color.WHITE);
			if (value != null && value.toString().length() > 0
					&& !value.toString().contains("Package: ")) {
				Color color = ResultTableGUI.this.displayTableLogic
						.getColorForMetricScore(selectedMetric,
								value.toString(),
								ResultTableGUI.this.columnNames[column]);
				if (color == null) {
					c.setBackground(Color.LIGHT_GRAY);
					c.setForeground(Color.BLACK);
				} else {
					c.setBackground(color);
					int r = color.getRed();
					int g = color.getGreen();
					int b = color.getBlue();
					int d = 0;
					double a = 1 - (0.299 * r + 0.587 * g + 0.114 * b) / 255;
					if (a < 0.5)
						d = 0;
					else
						d = 255;
					Color newColor = new Color(d, d, d);
					c.setForeground(newColor);
				}
			} else if (value != null && value.toString().length() > 0
					&& value.toString().contains("Package: ")) {
				ResultTableGUI.this.packageHeaders.add(row);
				c.setBackground(Color.BLACK);
				c.setForeground(Color.WHITE);
			} else {
				if (ResultTableGUI.this.packageHeaders.contains(row)) {
					c.setBackground(Color.BLACK);
					c.setForeground(Color.WHITE);
				} else {
					c.setBackground(Color.LIGHT_GRAY);
					c.setForeground(Color.BLACK);
				}
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

	public ResultTableLogic getResultTableLogic() {
		return this.displayTableLogic;
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
