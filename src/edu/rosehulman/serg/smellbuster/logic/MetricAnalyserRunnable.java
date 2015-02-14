package edu.rosehulman.serg.smellbuster.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.rosehulman.serg.smellbuster.util.OSDetector;

public class MetricAnalyserRunnable implements Runnable {

	private String jarFileLocation;
	private String destLocation;

	public MetricAnalyserRunnable(String jarFileLocation, String destLocation) {
		this.jarFileLocation = jarFileLocation;
		this.destLocation = destLocation;
	}

	@Override
	public void run() {
		File destFile = new File(this.destLocation);
		if (!destFile.exists()) {
			try {
				File jarFileDir = new File(this.jarFileLocation);
				for (File file : jarFileDir.listFiles()) {
					if (file.getName().contains(".jar")) {

						String currentDir = System.getProperty("user.dir");

						String cmd[] = new String[3];
						cmd[0] = "cmd.exe";
						cmd[1] = "/C";
						if (OSDetector.isWindows()) {
							cmd[2] = "java -jar " + currentDir
									+ "\\lib\\ckjm.jar -x "
									+ file.getAbsolutePath() + " >> "
									+ currentDir + "\\" + this.destLocation;
						} else {
							cmd[2] = "java -jar " + currentDir
									+ "/lib/ckjm.jar -x "
									+ file.getAbsolutePath() + " >> "
									+ currentDir + "/" + this.destLocation;
						}

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

						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		SVNLoadLogic.updateProgressBar();
	}

	class StreamGobbler extends Thread {
		InputStream is;
		String type;

		StreamGobbler(InputStream is, String type) {
			this.is = is;
			this.type = type;
		}

		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null)
					System.out.println(type + ">" + line);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

}
