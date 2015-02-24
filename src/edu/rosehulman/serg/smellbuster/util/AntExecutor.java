package edu.rosehulman.serg.smellbuster.util;

import java.io.File;

public class AntExecutor {
	public static void executeAntTask(String buildFilePath, String version,
			String jarProperty) {
		// String currentDir = System.getProperty("user.dir");

		// ant -buildfile svnbuildDirlocation
		// -Djar.file=C:\Users\Dharmin\Desktop\test.jar

		String dirLocation = buildFilePath.substring(0,
				buildFilePath.length() - 9);

		createDirIfNotExists(dirLocation + "target");

		String cmd[] = new String[3];
		cmd[0] = "cmd.exe";
		cmd[1] = "/C";
		if (jarProperty.length() > 0) {
			if (OSDetector.isWindows()) {
				cmd[2] = "ant -buildfile " + buildFilePath + " -D"+jarProperty+"="
						+ dirLocation + "target\\" + version + ".jar";
			} else {
				cmd[2] = "ant -buildfile " + buildFilePath + " -D"+jarProperty+"="
						+ dirLocation + "/target/" + version + ".jar";
			}
		} else {
			if (OSDetector.isWindows()) {
				cmd[2] = "ant -buildfile " + buildFilePath + "target\\" + version + ".jar";
			} else {
				cmd[2] = "ant -buildfile " + buildFilePath + "/target/" + version + ".jar";
			}
		}

		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmd);

			StreamGobbler errorGobbler = new StreamGobbler(
					proc.getErrorStream(), "ERROR");

			StreamGobbler outputGobbler = new StreamGobbler(
					proc.getInputStream(), "OUTPUT");

			errorGobbler.start();
			outputGobbler.start();

			int exitVal = proc.waitFor();
			System.out.println("ExitValue: " + exitVal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDirIfNotExists(String dirLocation) {
		File file = new File(dirLocation);
		if (!file.exists()) {
			try {
				file.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
