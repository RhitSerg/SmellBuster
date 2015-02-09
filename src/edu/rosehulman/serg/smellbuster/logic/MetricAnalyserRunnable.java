package edu.rosehulman.serg.smellbuster.logic;

import java.io.File;

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
						// CkjmTask ckjmTask = new CkjmTask();
						// ckjmTask.init();
						// ckjmTask.setFormat("xml");
						// ckjmTask.setOutputfile(destFile);
						//
						// ckjmTask.setClassdir(new
						// File("C:\\Users\\Dharmin\\Documents\\GitHub\\SmellBuster\\repo\\JFreeChart\\91\\jfreechart-1.0.x-branch\\jfreechart-1.0.6.jar"));
						// ckjmTask.execute();

						ProcessBuilder builder = new ProcessBuilder(
								"java -jar " + System.getProperty("user.dir")
										+ "\\lib\\ckjm.jar -x "
										+ file.getAbsolutePath() + " >> "
										+ System.getProperty("user.dir") + "\\"
										+ this.destLocation);
						builder.redirectErrorStream(true);

						builder.start();

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		SVNLoadLogic.updateProgressBar();

	}

}
