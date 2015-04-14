package edu.rosehulman.serg.smellbuster.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import edu.rosehulman.serg.smellbuster.smellparser.CodeSmellParser;

public class RecommendationDisplayerGUI extends JFrame {

	private static final long serialVersionUID = -1767648751660171489L;
	private Map<String, String> smellPatternMap;
	private JTextPane displayPane;

	public RecommendationDisplayerGUI(String filePath, String title) {
		super(title);
		this.setSize(new Dimension(500, 350));
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		CodeSmellParser smellParser = new CodeSmellParser();
		this.smellPatternMap = smellParser.getCodeSmellsForFile(filePath);

		this.displayPane = new JTextPane();
		this.displayPane.setContentType("text/html");
		this.setPaneContent();
		
		JScrollPane scrollPane = new JScrollPane(displayPane);
		scrollPane.setPreferredSize(new Dimension(500, 350));
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	private void setPaneContent() {
		String text = "";
		for (String pattern : this.smellPatternMap.keySet()) {
			text += this.smellPatternMap.get(pattern);
		}
		this.displayPane.setText(text);
	}
}
