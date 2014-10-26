package thesis.tool.main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import thesis.tool.gui.DisplayTable;
import thesis.tool.util.*;
import thesis.tool.xmlparser.*;

public class Main extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel textFieldPanel;
	private JPanel buttonPanel;
	private JPanel svnFieldPanel;

	private JLabel revisionNumberLabel;
	private JLabel releaseNumberLabel;
	private JLabel svnLabel;

	private JTextField revisionNumberField;
	private JTextField releaseNumberField;
	private JTextField svnField;

	private JButton addButton;
	private JButton addMoreButton;

	private Map<Integer, String> versionMap;
	private Map<String, ArrayList<DiffClass>> diffMap;
	private Map<String, ArrayList<String>> classMap;
	
	ArrayList<String> releaseNums = new ArrayList<>();
	ArrayList<String> revisionNums = new ArrayList<>();

	public Main() {
		super("VersionPopulator");
		this.versionMap = new TreeMap<>();
		
		this.textFieldPanel = new JPanel();
		this.buttonPanel = new JPanel();
		this.svnFieldPanel = new JPanel();
		
		this.textFieldPanel.setLayout(new GridLayout(1, 4));
		this.buttonPanel.setLayout(new GridLayout(1, 2));
		this.svnFieldPanel.setLayout(new BorderLayout());

		this.revisionNumberLabel = new JLabel("SVN Revision Number:");
		this.releaseNumberLabel = new JLabel("Product Release Version:");
		this.svnLabel = new JLabel("SVN Repo URL:");
		this.svnLabel.setSize(250, 50);

		this.revisionNumberField = new JTextField();
		this.releaseNumberField = new JTextField();
		this.svnField = new JTextField();
		this.svnField.setSize(450,50);
		
		removeForProduction();
		
		this.addButton = new JButton("Save");
		this.addButton.addActionListener(this);
		this.addMoreButton = new JButton("Save and Add More");
		this.addMoreButton.addActionListener(this);

		this.textFieldPanel.add(this.releaseNumberLabel);
		this.textFieldPanel.add(this.releaseNumberField);
		this.textFieldPanel.add(this.revisionNumberLabel);
		this.textFieldPanel.add(this.revisionNumberField);

		this.buttonPanel.add(this.addButton);
		this.buttonPanel.add(this.addMoreButton);
		
		this.svnFieldPanel.add(this.svnLabel, BorderLayout.WEST);
		this.svnFieldPanel.add(this.svnField, BorderLayout.CENTER);

		setLayout(new GridLayout(3, 1));
		add(this.svnFieldPanel);
		add(this.textFieldPanel);
		add(this.buttonPanel);
		setSize(700, 150);

	}

	private void removeForProduction() {
		this.revisionNumberField.setText("91");
		this.releaseNumberField.setText("1.0.6");
		releaseNums.add("1.0.19");
		releaseNums.add("1.0.18");
		releaseNums.add("1.0.17");
		releaseNums.add("1.0.16");
		releaseNums.add("1.0.15");
		releaseNums.add("1.0.14");
		releaseNums.add("1.0.13");
		releaseNums.add("1.0.12");
		releaseNums.add("1.0.11");
		releaseNums.add("1.0.10");
		releaseNums.add("1.0.9");
		releaseNums.add("1.0.8");
		releaseNums.add("1.0.7");
		
		revisionNums.add("3284");
		revisionNums.add("3247");
		revisionNums.add("3070");
		revisionNums.add("2924");
		revisionNums.add("2763");
		revisionNums.add("2440");
		revisionNums.add("2010");
		revisionNums.add("1769");
		revisionNums.add("1633");
		revisionNums.add("1060");
		revisionNums.add("706");
		revisionNums.add("655");
		revisionNums.add("588");
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String revision = this.revisionNumberField.getText();
		String release = this.releaseNumberField.getText();

		if (revision.length() > 0 && release.length() > 0)
			this.versionMap.put(Integer.parseInt(revision), release);

		if (e.getSource().equals(this.addButton)) {
			this.loadData();
			dispose();
			this.displayTable();
		} else {
			int len = this.releaseNums.size();
			this.revisionNumberField.setText(this.revisionNums.remove(len-1));
			this.releaseNumberField.setText(this.releaseNums.remove(len-1));
		}

	}

	public void parseVersionChanges() {
		File[] files = new File("VersionChanges").listFiles();
		DOMParser parser = new DOMParser();

		for (int i = 0; i < files.length; i++) {
			parser.setFileName(files[i].getAbsolutePath());
			parser.parseXML();
			ArrayList<DiffClass> dcList = parser.getDiffClassList();

			String[] nameParts = files[i].getName().split("-");
			String version = nameParts[nameParts.length - 1];
			version = version.substring(0, version.length() - 4);

			diffMap.put(version, dcList);
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
			DiffClass dc = new DiffClass();
			dc.setName(name);

			if (diffMap.containsKey(release)) {
				dcList = diffMap.get(release);
			}

			dcList.add(dc);
			diffMap.put(release, dcList);
		}

	}

	public static void main(String[] args) throws Exception {

		Main frame = new Main();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

	private void loadData() {
		this.diffMap = new HashMap<>();
		this.classMap = new HashMap<>();

		this.parseBaseVersion();
		this.parseVersionChanges();

		for (String version : this.diffMap.keySet()) {
			ArrayList<DiffClass> dcList = this.diffMap.get(version);
			ArrayList<String> cList = new ArrayList<String>();
			for (DiffClass dc : dcList) {
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
		while (itr.hasNext()){
			toWrite[k] = this.versionMap.get(itr.next());
			k++;
		}

		ArrayList<String[]> values = new ArrayList<>();
		for (String name : classMap.keySet()) {
			boolean isValid = false;
			String className = name.replace("/", "\\");
			String[] nameSplit = className.split("\\\\");
			className = nameSplit[nameSplit.length - 1];

			if (className.contains(".java")) {
				isValid = true;
			}

			for (int i = 0; i < nameSplit.length; i++) {
				if (nameSplit[i].equals("junit")) {
					isValid = false;
				}
			}

			if (isValid) {
				toWrite = new String[len];
				Arrays.fill(toWrite, "");
				for (String version : classMap.get(name)) {
					int j = 0;
					itr = this.versionMap.keySet().iterator();
					while (itr.hasNext()){
						if (version.equals(this.versionMap.get(itr.next()))){
							break;
						}
						j++;
					}
					toWrite[j] = className;
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
