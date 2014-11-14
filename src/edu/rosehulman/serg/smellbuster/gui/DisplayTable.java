package edu.rosehulman.serg.smellbuster.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
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
import javax.swing.table.TableCellRenderer;

import edu.rosehulman.serg.smellbuster.util.*;
import edu.rosehulman.serg.smellbuster.xmlparser.*;

public class DisplayTable extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel topPanel;
	private JTable table;
	private JScrollPane scrollPane;
	private Map<Integer, String> versionMap;
	private Map<String, ArrayList<JavaClass>> metricMap;
	private String[][] dataValues;

	// Constructor of main frame
	public DisplayTable(String[][] dataValues, Map<Integer, String> versionMap) {
		// Set the frame characteristics
		metricMap = new HashMap<>();
		this.versionMap = versionMap;
		setTitle("Simple Table Application");
		setSize(1300, 700);
		setBackground(Color.gray);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// Create a panel to hold all other components
		topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		getContentPane().add(topPanel);

		parseMetrics();

		// Create columns names
		final String columnNames[] = getColumnNames();

		this.dataValues = dataValues;
		// Create a new table instance
		table = new JTable(dataValues, columnNames);
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
				if (className.length() > 0) {
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
							+ DisplayTable.this.getWMCValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Number of Children (NOC)"
							+ "</td><td>"
							+ DisplayTable.this.getNOCValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Coupling Between Object Classes (CBO)"
							+ "</td><td>"
							+ DisplayTable.this.getCBOValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Lack of Cohesion in Methods (LCOM)"
							+ "</td><td>"
							+ DisplayTable.this.getLCOMValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Afferent Coupling (Ca)"
							+ "</td><td>"
							+ DisplayTable.this.getCAValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Efferent Coupling (Ce)"
							+ "</td><td>"
							+ DisplayTable.this.getCEValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Lack of Cohesion in Methods (LCOM3)"
							+ "</td><td>"
							+ DisplayTable.this.getLCOM3ValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Cohesion Among Methods of Class (CAM)"
							+ "</td><td>"
							+ DisplayTable.this.getCAMValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Inheritance Coupling (IC)"
							+ "</td><td>"
							+ DisplayTable.this.getICValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Coupling Between Methods (CBM)"
							+ "</td><td>"
							+ DisplayTable.this.getCBMValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Average Method Complexity (AMC)"
							+ "</td><td>"
							+ DisplayTable.this.getAMCValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Cyclomatic Complexoty (CC)"
							+ "</td><td>"
							+ DisplayTable.this.getCCValueFor(version,
									className)
							+ "</td></tr>"
							+ "<tr><td>Aggregate Metrics "
							+ "</td><td>"
							+ DisplayTable.this.getAggregateMetrics(className)
							+ "</td></tr>"
							+ "<tr><td>Aggregate Metrics of Max Values "
							+ "</td><td>"
							+ DisplayTable.this
									.getAggregateMetricsOfMax(className)
							+ "</td></tr>"
							+ "<tr><td>Aggregate Metrics of Min Values "
							+ "</td><td>"
							+ DisplayTable.this
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
						ArrayList<JavaClass> classList = createClassListMap(className);
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
		topPanel.add(scrollPane, BorderLayout.CENTER);
	}

	private class MyTableCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        	Component c = (Component) super.getTableCellRendererComponent(table, 
                    value, isSelected, hasFocus, row, column);
            c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
            return c;
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

	public ArrayList<JavaClass> createClassListMap(String className) {
		ArrayList<JavaClass> list = new ArrayList<JavaClass>();
		JavaClass[] classList = new JavaClass[this.versionMap.keySet().size()];
		for (String version : metricMap.keySet()) {
			ArrayList<JavaClass> jcList = metricMap.get(version);
			for (JavaClass jc : jcList) {
				String name = jc.getName();
				if (name.equals(className)) {

					int j = 0;
					Iterator<Integer> itr = this.versionMap.keySet().iterator();
					while (itr.hasNext()) {
						if (version.equals(this.versionMap.get(itr.next()))) {
							break;
						}
						j++;
					}
					classList[j] = jc;
				}
			}
		}
		for (int i = 0; i < classList.length; i++) {
			list.add(classList[i]);
		}

		return list;
	}

	public void parseMetrics() {
		File[] files = new File("MetricAnalysis").listFiles();
		DOMParser parser = new DOMParser();

		for (int i = 0; i < files.length; i++) {
			parser.setFileName(files[i].getAbsolutePath());
			parser.parseXML();

			ArrayList<JavaClass> jcList = parser.getJavaClassList();

			String[] nameParts = files[i].getName().split("-");
			String version = nameParts[nameParts.length - 1];
			version = version.substring(0, version.length() - 4);

			metricMap.put(version, jcList);
		}
	}

	public String getWMCValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getWmc());
						}
					}
				}
			}
		}
		return value;
	}

	public String getNOCValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getNoc());
						}
					}
				}
			}
		}
		return value;
	}

	public String getCBOValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getCbo());
						}
					}
				}
			}
		}
		return value;
	}

	public String getLCOMValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getLcom());
						}
					}
				}
			}
		}
		return value;
	}

	public String getCAValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getCa());
						}
					}
				}
			}
		}
		return value;
	}

	public String getCEValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getCe());
						}
					}
				}
			}
		}
		return value;
	}

	public String getLCOM3ValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getLcom3());
						}
					}
				}
			}
		}
		return value;
	}

	public String getCAMValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getCam());
						}
					}
				}
			}
		}
		return value;
	}

	public String getICValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getIc());
						}
					}
				}
			}
		}
		return value;
	}

	public String getCBMValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getCbm());
						}
					}
				}
			}
		}
		return value;
	}

	public String getAMCValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getAmc());
						}
					}
				}
			}
		}
		return value;
	}

	public String getCCValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (JavaClass jc : metricMap.get(ver)) {
						if (jc.getName().equals(className)) {
							value = String.valueOf(jc.getCc());
						}
					}
				}
			}
		}
		return value;
	}
	
	public String getAggregateMetrics(String className){
		String value = "N/A";
		double score = 0.0;
		ArrayList<JavaClass> classList = createClassListMap(className);
		for (JavaClass jc: classList){
			double wmc = jc.getWmc();
			double noc = jc.getNoc();
			double cbo = jc.getCbo();
			double lcom = jc.getLcom();
			double ca = jc.getCa();
			double ce = jc.getCe();
			double lcom3 = jc.getLcom3();
			double cam = jc.getCam();
			double ic = jc.getIc();
			double cbm = jc.getCbm();
			double amc = jc.getAmc();
			double cc = jc.getCc();
			score += ((10 - noc) - wmc - cbo - lcom - ca - ce - lcom3 + cam -ic - cbm - amc - cc);
		}
		value = String.valueOf(score);
		return value;
	}
	
	public String getAggregateMetricsOfMax(String className){
		String value = "N/A";
		ArrayList<JavaClass> classList = createClassListMap(className);
		double wmc = Integer.MIN_VALUE;
		double noc = Integer.MIN_VALUE;
		double cbo = Integer.MIN_VALUE;
		double lcom = Integer.MIN_VALUE;
		double ca = Integer.MIN_VALUE;
		double ce = Integer.MIN_VALUE;
		double lcom3 = Integer.MIN_VALUE;
		double cam = Integer.MIN_VALUE;
		double ic = Integer.MIN_VALUE;
		double cbm = Integer.MIN_VALUE;
		double amc = Integer.MIN_VALUE;
		double cc = Integer.MIN_VALUE;
		for (JavaClass jc: classList){
			wmc = Math.max(wmc, jc.getWmc());
			noc = Math.max(noc, jc.getNoc());
			cbo = Math.max(cbo, jc.getCbo());
			lcom = Math.max(lcom, jc.getLcom());
			ca = Math.max(ca, jc.getCa());
			ce = Math.max(ce, jc.getCe());
			lcom3 = Math.max(lcom3, jc.getLcom3());
			cam = Math.max(cam, jc.getCam());
			ic = Math.max(ic, jc.getIc());
			cbm = Math.max(cbm, jc.getCbm());
			amc = Math.max(amc, jc.getAmc());
			cc = Math.max(cc, jc.getCc());
		}
		double score = ((10 - noc) - wmc - cbo - lcom - ca - ce - lcom3 + cam -ic - cbm - amc - cc);
		value = String.valueOf(score);
		return value;
	}
	
	public String getAggregateMetricsOfMin(String className){
		String value = "N/A";
		ArrayList<JavaClass> classList = createClassListMap(className);
		double wmc = Integer.MAX_VALUE;
		double noc = Integer.MAX_VALUE;
		double cbo = Integer.MAX_VALUE;
		double lcom = Integer.MAX_VALUE;
		double ca = Integer.MAX_VALUE;
		double ce = Integer.MAX_VALUE;
		double lcom3 = Integer.MAX_VALUE;
		double cam = Integer.MAX_VALUE;
		double ic = Integer.MAX_VALUE;
		double cbm = Integer.MAX_VALUE;
		double amc = Integer.MAX_VALUE;
		double cc = Integer.MAX_VALUE;
		for (JavaClass jc: classList){
			wmc = Math.min(wmc, jc.getWmc());
			noc = Math.min(noc, jc.getNoc());
			cbo = Math.min(cbo, jc.getCbo());
			lcom = Math.min(lcom, jc.getLcom());
			ca = Math.min(ca, jc.getCa());
			ce = Math.min(ce, jc.getCe());
			lcom3 = Math.min(lcom3, jc.getLcom3());
			cam = Math.min(cam, jc.getCam());
			ic = Math.min(ic, jc.getIc());
			cbm = Math.min(cbm, jc.getCbm());
			amc = Math.min(amc, jc.getAmc());
			cc = Math.min(cc, jc.getCc());
		}
		double score = ((10 - noc) - wmc - cbo - lcom - ca - ce - lcom3 + cam -ic - cbm - amc - cc);
		value = String.valueOf(score);
		return value;
	}
}
