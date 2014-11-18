package edu.rosehulman.serg.smellbuster.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import edu.rosehulman.serg.smellbuster.logic.SVNLoadLogic;

public class VersionInputScreen extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JProgressBar progressBar;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem loadMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem quitMenuItem;
	private JPanel bottomPanel;
	private JPanel svnFieldPanel;
	private JLabel svnLabel;
	private JTextField svnField;
	private JButton addButton;
	private InputTable inputTable;
	private Map<Integer, String> versionMap;
	private FileChooser fileChooser;
	
	public VersionInputScreen() {
		super("VersionPopulator");
		
		this.versionMap = new TreeMap<>();
		this.inputTable = new InputTable();
		this.fileChooser = new FileChooser();

		initializeComponents();
		initProgressBar();
		initializeMenu();
		initializeBottomPanel();
		initializeSVNPanel();

		setLayout(new BorderLayout());
		setJMenuBar(this.menuBar);
		add(this.svnFieldPanel, BorderLayout.NORTH);
		add(this.inputTable, BorderLayout.CENTER);
		add(this.bottomPanel, BorderLayout.SOUTH);
		setSize(700, 450);
		setLocationRelativeTo(null);
	}
	
	private void initializeComponents(){
		this.addButton = new JButton("Display Table");
		this.addButton.addActionListener(this);
		
		this.svnLabel = new JLabel("SVN Repo URL:");
		this.svnLabel.setSize(250, 50);
		
		this.svnField = new JTextField(
				"http://svn.code.sf.net/p/jfreechart/code/branches/");
		this.svnField.setSize(450, 50);
	}
	
	private void initProgressBar() {
		this.progressBar = new JProgressBar();
		this.progressBar.setMinimum(0);
		this.progressBar.setMaximum(100);
	}

	private void initializeMenu() {
		this.menuBar = new JMenuBar();
		this.fileMenu = new JMenu("File");
		this.loadMenuItem = new JMenuItem("Load");
		this.saveMenuItem = new JMenuItem("Save");
		this.quitMenuItem = new JMenuItem("Quit");
		
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
		this.bottomPanel.setLayout(new GridLayout(2,1));
		
		this.bottomPanel.add(this.addButton);
		this.bottomPanel.add(this.progressBar);
	}

	private void initializeSVNPanel() {
		this.svnFieldPanel = new JPanel();
		this.svnFieldPanel.setLayout(new BorderLayout());
		
		this.svnFieldPanel.add(this.svnLabel, BorderLayout.WEST);
		this.svnFieldPanel.add(this.svnField, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		loadVersionMap();

		if (e.getSource().equals(this.addButton)) {
			this.progressBar.setIndeterminate(true);
			
			Thread t = new Thread() {
				public void run(){
					SVNLoadLogic svnLoadLogic = new SVNLoadLogic(versionMap, svnField.getText(), progressBar);
					dispose();
					svnLoadLogic.displayTable();				
				}
			};
			t.start();
		}
		else if (e.getSource().equals(this.loadMenuItem)){
			this.versionMap = this.fileChooser.getVersionMap();
			this.inputTable.loadTableValues(this.versionMap);
		} else if (e.getSource().equals(this.saveMenuItem)) {
			loadVersionMap();
			this.fileChooser.saveFile(this.versionMap);
		} else if (e.getSource().equals(this.quitMenuItem)){
			System.exit(0);
		}

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
