package edu.rosehulman.serg.smellbuster.gui;

import java.awt.Dimension;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class RecommendationDisplayerGUI extends JFrame {

	private static final long serialVersionUID = -1767648751660171489L;
	private Map<String, String> smellPatternMap;
	private JTextPane displayPane;

	public RecommendationDisplayerGUI(
			Map<String, String> smellPatternMap, String title) {
		super(title);
		this.setPreferredSize(new Dimension(500, 350));
		this.smellPatternMap = smellPatternMap;
		this.displayPane = new JTextPane();
		this.displayPane.setContentType("text/html");
		this.setPaneContent();
		JScrollPane jsp = new JScrollPane(this.displayPane);
		this.getContentPane().add(jsp);
		this.pack();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void setPaneContent() {
		String text = "";
		for (String pattern : this.smellPatternMap.keySet()) {
			text += this.smellPatternMap.get(pattern);
		}
		this.displayPane.setText(text);
	}

}
