package edu.rosehulman.serg.smellbuster.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import edu.rosehulman.serg.smellbuster.smellparser.CodeSmellParser;

public class RecommendationDisplayerGUI extends JFrame {

	private static final long serialVersionUID = -1767648751660171489L;
	@SuppressWarnings("unused")
	private Map<String, String> smellPatternMap;


	public RecommendationDisplayerGUI(String filePath,
			String title) {
		super(title);
		this.setPreferredSize(new Dimension(500, 350));
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		CodeSmellParser smellParser = new CodeSmellParser();
		this.smellPatternMap = smellParser
				.getCodeSmellsForFile(filePath);

		JEditorPane editorPane = new JEditorPane();
		File file = new File("web" + File.separator + "recommendations.html");
		try {
			editorPane.setPage(file.toURI().toURL());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JScrollPane scrollPane = new JScrollPane(editorPane);
		scrollPane.setPreferredSize(new Dimension(500, 350));
		this.add(scrollPane, BorderLayout.CENTER);

	}

}
