package edu.rosehulman.serg.smellbuster.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import edu.rosehulman.serg.smellbuster.logic.SVNLoadLogic;

public class VersionInputScreenGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JProgressBar progressBar;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem loadMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem quitMenuItem;

	private JPanel bottomPanel;
	private JPanel svnFieldPanel;
	private JPanel buildFilePanel;
	private JPanel projectNamePanel;
	private JPanel jarPropertyPanel;
	private JPanel topPanel;

	private JLabel svnLabel;
	private JLabel buildFileLabel;
	private JLabel projectNameLabel;
	private JLabel jarPropertyLabel;

	private JTextField svnField;
	private JTextField buildFileField;
	private JTextField projectNameField;
	private JTextField jarPropertyField;

	private JButton addButton;
	private InputTableGUI inputTable;
	private Map<Integer, String> versionMap;
	private FileChooserGUI fileChooser;

	public VersionInputScreenGUI() {
		super("SmellBuster");

		this.versionMap = new TreeMap<>();
		this.inputTable = new InputTableGUI();
		this.fileChooser = new FileChooserGUI();

		initializeComponents();
		initProgressBar();
		initializeMenu();
		initializeBottomPanel();
		initializeProjectNamePanel();
		initializeSVNPanel();
		initializeBuildFilePanel();
		initializeJarPropertyPanel();
		initializeTopPanel();

		setLayout(new BorderLayout());
		setJMenuBar(this.menuBar);
		add(this.topPanel, BorderLayout.NORTH);
		add(this.inputTable, BorderLayout.CENTER);
		add(this.bottomPanel, BorderLayout.SOUTH);
		setSize(700, 450);
		setLocationRelativeTo(null);
	}

	private void initializeComponents() {
		this.addButton = new JButton("Display Table");
		this.addButton.addActionListener(this);

		this.svnLabel = new JLabel("SVN Repository Url*:");
		this.svnLabel.setPreferredSize(new Dimension(150, 30));

		this.buildFileLabel = new JLabel("Build File Location*:");
		this.buildFileLabel.setPreferredSize(new Dimension(150, 30));

		this.projectNameLabel = new JLabel("Project Name*:");
		this.projectNameLabel.setPreferredSize(new Dimension(150, 30));

		this.jarPropertyLabel = new JLabel("Ant Target jar Property:");
		this.jarPropertyLabel.setPreferredSize(new Dimension(150, 30));

		this.svnField = new JTextField();
		this.svnField.setPreferredSize(new Dimension(550, 30));

		this.buildFileField = new JTextField();
		this.buildFileField.setPreferredSize(new Dimension(550, 30));

		this.projectNameField = new JTextField();
		this.projectNameField.setPreferredSize(new Dimension(550, 30));

		this.jarPropertyField = new JTextField();
		this.jarPropertyField.setPreferredSize(new Dimension(550, 30));
	}

	private void initProgressBar() {
		this.progressBar = new JProgressBar();
		this.progressBar.setMinimum(0);
		this.progressBar.setMaximum(100);
	}

	private void initializeMenu() {
		this.menuBar = new JMenuBar();
		this.fileMenu = new JMenu("File");

		this.loadMenuItem = new JMenuItem("Load", new ImageIcon("assets"
				+ File.separator + "upload.png"));
		this.saveMenuItem = new JMenuItem("Save", new ImageIcon("assets"
				+ File.separator + "save.png"));
		this.quitMenuItem = new JMenuItem("Quit", new ImageIcon("assets"
				+ File.separator + "quit.png"));

		this.menuBar.add(this.fileMenu);
		this.fileMenu.add(this.loadMenuItem);
		this.fileMenu.add(this.saveMenuItem);
		this.fileMenu.add(this.quitMenuItem);

		this.loadMenuItem.addActionListener(this);
		this.saveMenuItem.addActionListener(this);
		this.quitMenuItem.addActionListener(this);
	}

	private void initializeBottomPanel() {
		this.bottomPanel = new JPanel();
		this.bottomPanel.setLayout(new GridLayout(2, 1));

		this.bottomPanel.add(this.addButton);
		this.bottomPanel.add(this.progressBar);
	}

	private void initializeSVNPanel() {
		this.svnFieldPanel = new JPanel();
		this.svnFieldPanel.setLayout(new BorderLayout());

		this.svnFieldPanel.add(this.svnLabel, BorderLayout.WEST);
		this.svnFieldPanel.add(this.svnField, BorderLayout.CENTER);
	}

	private void initializeBuildFilePanel() {
		this.buildFilePanel = new JPanel();
		this.buildFilePanel.setLayout(new BorderLayout());

		this.buildFilePanel.add(this.buildFileLabel, BorderLayout.WEST);
		this.buildFilePanel.add(this.buildFileField, BorderLayout.CENTER);
	}

	private void initializeProjectNamePanel() {
		this.projectNamePanel = new JPanel();
		this.projectNamePanel.setLayout(new BorderLayout());

		this.projectNamePanel.add(this.projectNameLabel, BorderLayout.WEST);
		this.projectNamePanel.add(this.projectNameField, BorderLayout.CENTER);
	}

	private void initializeJarPropertyPanel() {
		this.jarPropertyPanel = new JPanel();
		this.jarPropertyPanel.setLayout(new BorderLayout());

		this.jarPropertyPanel.add(this.jarPropertyLabel, BorderLayout.WEST);
		this.jarPropertyPanel.add(this.jarPropertyField, BorderLayout.CENTER);

	}

	private void initializeTopPanel() {
		this.topPanel = new JPanel();
		this.topPanel.setLayout(new GridLayout(4, 1));

		this.topPanel.add(this.projectNamePanel);
		this.topPanel.add(this.svnFieldPanel);
		this.topPanel.add(this.buildFilePanel);
		this.topPanel.add(this.jarPropertyPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		loadVersionMap();

		if (e.getSource().equals(this.addButton)) {

			if (this.versionMap.keySet().size() <= 1) {
				JOptionPane
						.showMessageDialog(this,
								"Please enter at least 2 product version and svn revision numbers");
				return;
			}

			this.progressBar.setIndeterminate(true);

			Thread t = new Thread() {
				public void run() {
					SVNLoadLogic svnLoadLogic = new SVNLoadLogic(
							projectNameField.getText(), versionMap,
							svnField.getText(), buildFileField.getText(),
							jarPropertyField.getText(), progressBar);
					dispose();
					svnLoadLogic.displayTable();
				}
			};
			t.start();
		} else if (e.getSource().equals(this.loadMenuItem)) {
			this.fileChooser.loadInput();

			this.versionMap = this.fileChooser.getVersionMap();
			this.inputTable.loadTableValues(this.versionMap);

			Map<String, String> inputMap = this.fileChooser.getInputMap();
			this.projectNameField.setText(inputMap.get("project"));
			this.svnField.setText(inputMap.get("svnURL"));
			this.buildFileField.setText(inputMap.get("buildFile"));
			this.jarPropertyField.setText(inputMap.get("jarProperty"));
		} else if (e.getSource().equals(this.saveMenuItem)) {
			loadVersionMap();
			Map<String, String> inputMap = this.getSaveInputMap();
			this.fileChooser.saveFile(this.versionMap, inputMap);
		} else if (e.getSource().equals(this.quitMenuItem)) {
			System.exit(0);
		}

	}

	private Map<String, String> getSaveInputMap() {
		Map<String, String> inputMap = new TreeMap<>();
		inputMap.put("project", this.projectNameField.getText().trim());
		inputMap.put("svnURL", this.svnField.getText().trim());
		inputMap.put("buildFile", this.buildFileField.getText().trim());
		inputMap.put("jarProperty", this.jarPropertyField.getText().trim());
		return inputMap;
	}

	private void loadVersionMap() {
		TableModel model = this.inputTable.getTable().getModel();

		for (int i = 0; i < model.getRowCount(); i++) {

			String release = model.getValueAt(i, 0).toString();
			String revision = model.getValueAt(i, 1).toString();
			if (revision.length() > 0 && release.length() > 0)
				this.versionMap.put(Integer.parseInt(revision), release);
		}
	}

}
