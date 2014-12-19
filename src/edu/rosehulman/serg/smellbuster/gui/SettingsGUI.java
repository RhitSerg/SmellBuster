package edu.rosehulman.serg.smellbuster.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SettingsGUI extends JFrame implements ActionListener {


	private static final long serialVersionUID = 1L;
	private JPanel maxValueInputPanel;
	private JPanel buttonPanel;
	private JLabel maxValueInputLabel;
	private JTextField maxValueInputField;
	private JButton saveButton;
	private JButton cancelButton;
	private ResultTableGUI resultTableGUI;
	
	public SettingsGUI(ResultTableGUI resultTableGUI){
		this.resultTableGUI = resultTableGUI;
		initComponents();
		initFrame();
	}
	
	public void initComponents(){
		this.maxValueInputLabel = new JLabel("Max Metric Value: ");
		this.maxValueInputField = new JTextField(this.resultTableGUI.getResultTableLogic().getMaxAggregateValue()+"");
		initButtons();
		initPanels();
	}

	private void initPanels() {
		this.maxValueInputPanel = new JPanel();
		this.maxValueInputPanel.setLayout(new GridLayout(1,2));
		this.maxValueInputPanel.add(this.maxValueInputLabel);
		this.maxValueInputPanel.add(this.maxValueInputField);
		
		this.buttonPanel = new JPanel();
		this.buttonPanel.setLayout(new GridLayout(1,2));		
		this.buttonPanel.add(this.saveButton);
		this.buttonPanel.add(this.cancelButton);
	}

	private void initButtons() {
		this.saveButton = new JButton("Save Settings");
		this.cancelButton = new JButton("Cancel");
		
		this.saveButton.addActionListener(this);
		this.cancelButton.addActionListener(this);
	}
	
	private void initFrame(){
		setLayout(new GridLayout(2,1));
		getContentPane().add(this.maxValueInputPanel);
		getContentPane().add(this.buttonPanel);
		setTitle("Settings");
		setSize(250, 100);
		setBackground(Color.gray);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.saveButton)){
			try{
				this.resultTableGUI.getResultTableLogic().setMaxAggregateValue(Double.parseDouble(this.maxValueInputField.getText()));
				this.resultTableGUI.updateTable();
				this.dispose();
			} catch (Exception exp){
				JOptionPane.showMessageDialog(this, "Enter a valid number!");
			}
		} else if(e.getSource().equals(this.cancelButton)){
			this.dispose();
		}
	}
}
