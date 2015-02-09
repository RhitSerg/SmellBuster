package edu.rosehulman.serg.smellbuster.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser extends JFrame {

	private static final long serialVersionUID = 1L;
	private Map<Integer, String> versionMap;
	private JFileChooser chooser;
	private Map<String, String> inputMap;

	public FileChooser() {
		this.versionMap = new TreeMap<>();
		this.inputMap = new TreeMap<>();
		this.chooser = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		chooser.setCurrentDirectory(workingDirectory);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"TEXT FILES", "txt", "text");
		chooser.setFileFilter(filter);
	}

	private void chooseFile() {
		int option = chooser.showOpenDialog(FileChooser.this);
		if (option == JFileChooser.APPROVE_OPTION
				&& chooser.getSelectedFile() != null) {
			this.parseInputFile(chooser.getSelectedFile());
		} else {
			System.out.println("Did not select a file");
		}
	}

	private File getSaveFile() {
		int option = chooser.showSaveDialog(FileChooser.this);
		if (option == JFileChooser.APPROVE_OPTION
				&& chooser.getSelectedFile() != null) {
			return chooser.getSelectedFile();
		} else {
			return null;
		}
	}

	public void saveFile(Map<Integer, String> versionMap, Map<String, String> inputMap) {
		File saveFile = this.getSaveFile();
		if (saveFile != null) {
			try {
				PrintWriter writer = new PrintWriter(saveFile);
				writer.println(inputMap.get("project"));
				writer.println(inputMap.get("svnURL"));
				writer.println(inputMap.get("buildFile"));
				writer.println(inputMap.get("mavenHome"));
				for (Integer release : versionMap.keySet()) {
					writer.println(versionMap.get(release) + ";" + release);
				}
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void parseInputFile(File selectedFile) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(selectedFile));
			String line;
			int lineNum = 0;
			while ((line = br.readLine()) != null && line.length() > 0) {
				if (lineNum == 0){
					this.inputMap.put("project", line.trim());
				} else if (lineNum == 1){
					this.inputMap.put("svnURL", line.trim());
				} else if (lineNum == 2){
					this.inputMap.put("buildFile", line.trim());
				} else if (lineNum == 3){
					this.inputMap.put("mavenHome", line.trim());
				}else {
					String[] inputs = line.split(";");
					this.versionMap.put(Integer.parseInt(inputs[1]), inputs[0]);
				}
				lineNum++;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadInput(){
		this.versionMap.clear();
		this.chooseFile();
	}
	
	public Map<Integer, String> getVersionMap() {
		return this.versionMap;
	}
	
	public Map<String, String> getInputMap() {
		return this.inputMap;
	}

}
