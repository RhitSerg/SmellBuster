package thesis.tool.main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.TableModel;

import thesis.tool.gui.DisplayTable;
import thesis.tool.gui.InputTable;
import thesis.tool.svn.SVNParser;
import thesis.tool.util.*;
import thesis.tool.xmlparser.*;

public class Main extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel buttonPanel;
	private JPanel svnFieldPanel;
	private ImageIcon loadingIcon;

	private JLabel svnLabel;

	private JTextField svnField;

	private JButton addButton;

	private InputTable inputTable;
	private Map<Integer, String> versionMap;
	private Map<String, ArrayList<DiffClass>> diffMap;
	private Map<String, ArrayList<String>> classMap;

	ArrayList<String> releaseNums = new ArrayList<>();
	ArrayList<String> revisionNums = new ArrayList<>();

	public Main() {
		super("VersionPopulator");
		this.versionMap = new TreeMap<>();
		this.inputTable = new InputTable();

		this.buttonPanel = new JPanel();
		this.svnFieldPanel = new JPanel();

		if (OSDetector.isWindows())
			this.loadingIcon = new ImageIcon("assets\\loading.gif");
		else
			this.loadingIcon = new ImageIcon("assets/loading.gif");

		this.buttonPanel.setLayout(new GridLayout(1, 2));
		this.svnFieldPanel.setLayout(new BorderLayout());

		this.svnLabel = new JLabel("SVN Repo URL:");
		this.svnLabel.setSize(250, 50);

		this.svnField = new JTextField(
				"http://svn.code.sf.net/p/jfreechart/code/branches/");
		this.svnField.setSize(450, 50);

		this.addButton = new JButton("Display Table");
		this.addButton.addActionListener(this);

		this.buttonPanel.add(this.addButton);

		this.svnFieldPanel.add(this.svnLabel, BorderLayout.WEST);
		this.svnFieldPanel.add(this.svnField, BorderLayout.CENTER);


		setLayout(new BorderLayout());
		add(this.svnFieldPanel, BorderLayout.NORTH);
		add(this.inputTable, BorderLayout.CENTER);
		add(this.buttonPanel, BorderLayout.SOUTH);
		setSize(700, 450);
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		TableModel model = this.inputTable.getTable().getModel();

		for (int i = 0; i < model.getRowCount(); i++) {

			String release = model.getValueAt(i, 0).toString();
			String revision = model.getValueAt(i, 1).toString();
			if (revision.length() > 0 && release.length() > 0)
				this.versionMap.put(Integer.parseInt(revision), release);
		}

		if (e.getSource().equals(this.addButton)) {
			this.addButton.setText("");
			this.addButton.setIcon(this.loadingIcon);

			Thread t = new Thread() {
				public void run(){
					loadData();
					dispose();
					displayTable();					
				}
			};
			t.start();
		}

	}

	public void parseVersionChanges() {
		// File[] files = new File("VersionChanges").listFiles();
		// DOMParser parser = new DOMParser();

		Iterator<Integer> itr = this.versionMap.keySet().iterator();
		int start = 0;

		while (itr.hasNext()) {
			int end = itr.next();
			SVNParser svnParser = new SVNParser(this.svnField.getText(),
					(long) start, (long) end);
			svnParser.loadSVNInfo();
			ArrayList<DiffClass> dcList = svnParser.getDiffClassList();

			String version = this.versionMap.get(end);

			diffMap.put(version, dcList);
			start = end;
		}
	}

	public void parseBaseVersion() {
		DOMParser parser = new DOMParser();
		parser.setFileName("Version1.xml");
		parser.parseXML();

		int version = this.versionMap.keySet().iterator().next();
		String release = this.versionMap.get(version);

		ArrayList<String> nameList = parser.getBaseVersion();
		ArrayList<DiffClass> dcList = new ArrayList<DiffClass>();

		for (String name : nameList) {
			String className = name.replace("/", "\\");
			String[] nameSplit = className.split("\\\\");
			className = nameSplit[nameSplit.length - 1];

			DiffClass dc = new DiffClass();
			dc.setName(className);

			if (diffMap.containsKey(release)) {
				dcList = diffMap.get(release);
			}

			dcList.add(dc);
			diffMap.put(release, dcList);
		}

	}

	public static void main(String[] args) throws Exception {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		Main frame = new Main();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

	private void loadData() {
		
		this.diffMap = new HashMap<>();
		this.classMap = new HashMap<>();

		this.parseVersionChanges();

		Iterator<String> itr = this.diffMap.keySet().iterator();
		while (itr.hasNext()) {
			String version = itr.next();
			ArrayList<DiffClass> dcList = this.diffMap.get(version);
			for (DiffClass dc : dcList) {
				ArrayList<String> cList = new ArrayList<String>();
				String name = dc.getName();
				if (this.classMap.containsKey(name)) {
					cList = this.classMap.get(name);
				}
				cList.add(version);
				this.classMap.put(name, cList);
			}
		}
	}

	public boolean didChange(String[] value) {
		int count = 0;
		for (String str : value) {
			if (str.length() > 0) {
				count++;
				if (count > 1) {
					return true;
				}
			}
		}
		return false;
	}

	public void displayTable() {

		int len = this.versionMap.keySet().size();
		String[] toWrite = new String[len];
		Arrays.fill(toWrite, "");
		Iterator<Integer> itr = this.versionMap.keySet().iterator();
		int k = 0;
		while (itr.hasNext()) {
			toWrite[k] = this.versionMap.get(itr.next());
			k++;
		}

		ArrayList<String[]> values = new ArrayList<>();
		for (String name : classMap.keySet()) {
			boolean isValid = false;

			if (name.contains(".java")) {
				isValid = true;
			}

			if (isValid) {
				toWrite = new String[len];
				Arrays.fill(toWrite, "");
				for (String version : classMap.get(name)) {
					int j = 0;
					itr = this.versionMap.keySet().iterator();
					while (itr.hasNext()) {
						if (version.equals(this.versionMap.get(itr.next()))) {
							break;
						}
						j++;
					}
					toWrite[j] = name;
				}
				values.add(toWrite);
			}
		}

		int size = 0;

		for (String[] val : values) {
			if (didChange(val)) {
				size++;
			}
		}

		String[][] dataValues = new String[size * 2][len];
		int i = 0;
		for (String[] value : values) {
			if (didChange(value)) {
				dataValues[i] = value;
				i++;
				dataValues[i] = new String[len];
				i++;
			}
		}

		DisplayTable mainFrame = new DisplayTable(dataValues, this.versionMap);
		mainFrame.setVisible(true);
	}
}
