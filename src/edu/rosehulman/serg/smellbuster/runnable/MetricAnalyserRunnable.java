package edu.rosehulman.serg.smellbuster.runnable;

import java.io.File;

import edu.rosehulman.serg.smellbuster.logic.SVNLoadLogic;
import edu.rosehulman.serg.smellbuster.util.OSDetector;
import edu.rosehulman.serg.smellbuster.util.StreamGobbler;

public class MetricAnalyserRunnable implements Runnable {

	private String jarFileLocation;
	private String destLocation;

	public MetricAnalyserRunnable(String jarFileLocation, String destLocation) {
		this.jarFileLocation = jarFileLocation;
		this.destLocation = destLocation;
	}

	@Override
	public void run() {

		try {
			File jarFileDir = new File(this.jarFileLocation);
			
			checkDestinationLocation();
			
			for (File file : jarFileDir.listFiles()) {
				if (file.getName().contains(".jar")) {

					String currentDir = System.getProperty("user.dir");

					String cmd[] = new String[3];
					cmd[0] = "cmd.exe";
					cmd[1] = "/C";
					if (OSDetector.isWindows()) {
						cmd[2] = "java -jar " + currentDir
								+ "\\lib\\ckjm.jar -x "
								+ file.getAbsolutePath() + " >> " + currentDir
								+ "\\" + this.destLocation;
					} else {
						cmd[2] = "java -jar " + currentDir
								+ "/lib/ckjm.jar -x " + file.getAbsolutePath()
								+ " >> " + currentDir + "/" + this.destLocation;
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

		SVNLoadLogic.updateProgressBar();
	}
	
	public void checkDestinationLocation(){
		File file = new File(this.destLocation);
		if (file.exists()){
			file.delete();
		}
	}
}
