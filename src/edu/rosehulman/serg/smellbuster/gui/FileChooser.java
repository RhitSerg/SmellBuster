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

	public FileChooser() {
		this.versionMap = new TreeMap<>();
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
	
	public void saveFile(Map<Integer, String> versionMap){
		File saveFile = this.getSaveFile();
		try {
		PrintWriter writer = new PrintWriter(saveFile);
		for (Integer release: versionMap.keySet()){
			writer.println(versionMap.get(release) + ";" + release);
		}
		writer.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private void parseInputFile(File selectedFile) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(selectedFile));
			String line;
			while ((line = br.readLine()) != null && line.length() > 0) {
				String[] inputs = line.split(";");
				this.versionMap.put(Integer.parseInt(inputs[1]), inputs[0]);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<Integer, String> getVersionMap() {
		this.chooseFile();
		return this.versionMap;
	}

}
