package thesis.tool.gui;

import thesis.tool.util.*;
import thesis.tool.xmlparser.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
	static Map<String, ArrayList<JavaClass>> metricMap;

	// Constructor of main frame
	public DisplayTable(String[][] dataValues) {
		// Set the frame characteristics
		metricMap = new HashMap<>();
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
		final String columnNames[] = { "1.0.6", "1.0.7", "1.0.8", "1.0.9",
				"1.0.10", "1.0.11", "1.0.12", "1.0.13", "1.0.14", "1.0.15",
				"1.0.16", "1.0.17", "1.0.18", "1.0.19" };

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
						+ DisplayTable.getWMCValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Number of Children (NOC)"
						+ "</td><td>"
						+ DisplayTable.getNOCValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Coupling Between Object Classes (CBO)"
						+ "</td><td>"
						+ DisplayTable.getCBOValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Lack of Cohesion in Methods (LCOM)"
						+ "</td><td>"
						+ DisplayTable.getLCOMValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Afferent Coupling (Ca)"
						+ "</td><td>"
						+ DisplayTable.getCAValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Efferent Coupling (Ce)"
						+ "</td><td>"
						+ DisplayTable.getCEValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Lack of Cohesion in Methods (LCOM3)"
						+ "</td><td>"
						+ DisplayTable.getLCOM3ValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Cohesion Among Methods of Class (CAM)"
						+ "</td><td>"
						+ DisplayTable.getCAMValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Inheritance Coupling (IC)"
						+ "</td><td>"
						+ DisplayTable.getICValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Coupling Between Methods (CBM)"
						+ "</td><td>"
						+ DisplayTable.getCBMValueFor(version, className)
						+ "</td></tr>"
						+ "<tr><td>Average Method Complexity (AMC)"
						+ "</td><td>"
						+ DisplayTable.getAMCValueFor(version, className)
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
					chart.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					break;
				default:
					break;
				}
			}

		});

		scrollPane = new JScrollPane(table);
		topPanel.add(scrollPane, BorderLayout.CENTER);
	}

	public ArrayList<JavaClass> createClassListMap(String className) {
		ArrayList<JavaClass> list = new ArrayList<JavaClass>();
		JavaClass[] classList = new JavaClass[14];
		for (String version : metricMap.keySet()) {
			ArrayList<JavaClass> jcList = metricMap.get(version);
			for (JavaClass jc : jcList) {
				String name = jc.getName();
				if (name.equals(className)) {
					switch (version) {
					case "1.0.6":
						classList[0] = jc;
						break;
					case "1.0.7":
						classList[1] = jc;
						break;
					case "1.0.8":
						classList[2] = jc;
						break;
					case "1.0.9":
						classList[3] = jc;
						break;
					case "1.0.10":
						classList[4] = jc;
						break;
					case "1.0.11":
						classList[5] = jc;
						break;
					case "1.0.12":
						classList[6] = jc;
						break;
					case "1.0.13":
						classList[7] = jc;
						break;
					case "1.0.14":
						classList[8] = jc;
						break;
					case "1.0.15":
						classList[9] = jc;
						break;
					case "1.0.16":
						classList[10] = jc;
						break;
					case "1.0.17":
						classList[11] = jc;
						break;
					case "1.0.18":
						classList[12] = jc;
						break;
					case "1.0.19":
						classList[13] = jc;
						break;
					default:
						break;
					}
				}
			}
		}
		for (int i=0; i<classList.length; i++){
			list.add(classList[i]);
		}
		
		return list;
	}

	public static void parseMetrics() {
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

	public static String getWMCValueFor(String version, String className) {
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

	public static String getNOCValueFor(String version, String className) {
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

	public static String getCBOValueFor(String version, String className) {
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

	public static String getLCOMValueFor(String version, String className) {
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

	public static String getCAValueFor(String version, String className) {
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

	public static String getCEValueFor(String version, String className) {
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

	public static String getLCOM3ValueFor(String version, String className) {
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

	public static String getCAMValueFor(String version, String className) {
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

	public static String getICValueFor(String version, String className) {
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

	public static String getCBMValueFor(String version, String className) {
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

	public static String getAMCValueFor(String version, String className) {
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
