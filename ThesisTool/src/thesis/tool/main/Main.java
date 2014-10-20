package thesis.tool.main;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import thesis.tool.util.*;
import thesis.tool.xmlparser.*;
import thesis.tool.gui.*;

public class Main {

	static Map<String, ArrayList<DiffClass>> diffMap;
	static Map<String, ArrayList<String>> classMap;

	public static void parseVersionChanges() {
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

	public static void parseBaseVersion() {
		DOMParser parser = new DOMParser();
		parser.setFileName("Version1.xml");
		parser.parseXML();

		ArrayList<String> nameList = parser.getBaseVersion();
		ArrayList<DiffClass> dcList = new ArrayList<DiffClass>();

		for (String name : nameList) {
			DiffClass dc = new DiffClass();
			dc.setName(name);

			if (diffMap.containsKey("1.0.6")) {
				dcList = diffMap.get("1.0.6");
			}

			dcList.add(dc);
			diffMap.put("1.0.6", dcList);
		}

	}

	public static void main(String[] args) throws Exception {

		diffMap = new HashMap<>();
		classMap = new HashMap<>();

		parseBaseVersion();
		parseVersionChanges();

		for (String version : diffMap.keySet()) {
			ArrayList<DiffClass> dcList = diffMap.get(version);
			ArrayList<String> cList = new ArrayList<String>();
			for (DiffClass dc : dcList) {
				String name = dc.getName();
				if (classMap.containsKey(name)) {
					cList = classMap.get(name);
				}
				cList.add(version);
				classMap.put(name, cList);
			}
		}

		
		String[] toWrite = new String[] { "1.0.6", "1.0.7", "1.0.8", "1.0.9",
				"1.0.10", "1.0.11", "1.0.12", "1.0.13", "1.0.14", "1.0.15",
				"1.0.16", "1.0.17", "1.0.18", "1.0.19" };

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
				toWrite = new String[] { "", "", "", "", "", "", "", "", "",
						"", "", "", "", "" };
				for (String version : classMap.get(name)) {
					switch (version) {
					case "1.0.6":
						toWrite[0] = className;
						break;
					case "1.0.7":
						toWrite[1] = className;
						break;
					case "1.0.8":
						toWrite[2] = className;
						break;
					case "1.0.9":
						toWrite[3] = className;
						break;
					case "1.0.10":
						toWrite[4] = className;
						break;
					case "1.0.11":
						toWrite[5] = className;
						break;
					case "1.0.12":
						toWrite[6] = className;
						break;
					case "1.0.13":
						toWrite[7] = className;
						break;
					case "1.0.14":
						toWrite[8] = className;
						break;
					case "1.0.15":
						toWrite[9] = className;
						break;
					case "1.0.16":
						toWrite[10] = className;
						break;
					case "1.0.17":
						toWrite[11] = className;
						break;
					case "1.0.18":
						toWrite[12] = className;
						break;
					case "1.0.19":
						toWrite[13] = className;
						break;
					default:
						break;
					}
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

		String[][] dataValues = new String[size * 2][14];
		int i = 0;
		for (String[] value : values) {
			if (didChange(value)) {
				dataValues[i] = value;
				i++;
				dataValues[i] = new String[] { "", "", "", "", "", "", "", "",
						"", "", "", "", "", "" };
				i++;
			}
		}

		DisplayTable mainFrame = new DisplayTable(dataValues);
		mainFrame.setVisible(true);
	}

	public static boolean didChange(String[] value) {
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
}
