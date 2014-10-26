package thesis.tool.gui;

import thesis.tool.util.*;
import thesis.tool.xmlparser.*;

import java.awt.BorderLayout;
import java.awt.Color;
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

public class DisplayTable extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Instance attributes used in this example
	private JPanel topPanel;
	private JTable table;
	private JScrollPane scrollPane;
	private Map<Integer, String> versionMap;
	private Map<String, ArrayList<JavaClass>> metricMap;

	// Constructor of main frame
	public DisplayTable(String[][] dataValues, Map<Integer, String> versionMap) {
		// Set the frame characteristics
		metricMap = new HashMap<>();
		this.versionMap = versionMap;
		setTitle("Simple Table Application");
		setSize(1000, 700);
		setBackground(Color.gray);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// Create a panel to hold all other components
		topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		getContentPane().add(topPanel);

		parseMetrics();

		// Create columns names
		final String columnNames[] = getColumnNames();

		final String[][] values = dataValues;
		// Create a new table instance
		table = new JTable(dataValues, columnNames);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				int row = DisplayTable.this.table.getSelectedRow();
				int column = DisplayTable.this.table.getSelectedColumn();

				final String title = "Track info";
				final String version = columnNames[column];
				final String className = values[row][column];
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
						+ DisplayTable.this.getWMCValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Number of Children (NOC)"
						+ "</td><td>"
						+ DisplayTable.this.getNOCValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Coupling Between Object Classes (CBO)"
						+ "</td><td>"
						+ DisplayTable.this.getCBOValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Lack of Cohesion in Methods (LCOM)"
						+ "</td><td>"
						+ DisplayTable.this.getLCOMValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Afferent Coupling (Ca)"
						+ "</td><td>"
						+ DisplayTable.this.getCAValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Efferent Coupling (Ce)"
						+ "</td><td>"
						+ DisplayTable.this.getCEValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Lack of Cohesion in Methods (LCOM3)"
						+ "</td><td>"
						+ DisplayTable.this.getLCOM3ValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Cohesion Among Methods of Class (CAM)"
						+ "</td><td>"
						+ DisplayTable.this.getCAMValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Inheritance Coupling (IC)"
						+ "</td><td>"
						+ DisplayTable.this.getICValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Coupling Between Methods (CBM)"
						+ "</td><td>"
						+ DisplayTable.this.getCBMValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Average Method Complexity (AMC)"
						+ "</td><td>"
						+ DisplayTable.this.getAMCValueFor(version, className)
						+ "</td></tr>" + "</table>";
				final String[] options = new String[] { "Ok", "Display Graph" };
				int selectedOption = JOptionPane.showOptionDialog(null,
						message, title, JOptionPane.DEFAULT_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
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

		});

		scrollPane = new JScrollPane(table);
		topPanel.add(scrollPane, BorderLayout.CENTER);
	}
	
	public String[] getColumnNames(){
		int len = this.versionMap.keySet().size();
		String[] colNames = new String[len];
		
		Arrays.fill(colNames, "");
		Iterator<Integer> itr = this.versionMap.keySet().iterator();
		int k = 0;
		while (itr.hasNext()){
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
					while (itr.hasNext()){
						if (version.equals(this.versionMap.get(itr.next()))){
							break;
						}
						j++;
					}
					classList[j] = jc;
				}
			}
		}
		for (int i=0; i<classList.length; i++){
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
}
