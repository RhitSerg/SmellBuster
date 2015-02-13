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

						String currentDir = System.getProperty("user.dir");
						currentDir = currentDir.replace("\\", "/");
						this.destLocation = this.destLocation
								.replace("\\", "/");

						String filePath = file.getAbsolutePath().replace("\\",
								"/");

//						ProcessBuilder builder = new ProcessBuilder(
//								"java -jar " + currentDir + "/lib/ckjm.jar -x "
//										+ filePath + " >> " + currentDir + "/"
//										+ this.destLocation);
//						builder.redirectErrorStream(true);
//
//						builder.start();

						String[] cmd = {
								"java",
								"-jar",
								currentDir + "/lib/ckjm.jar",
								"-x",
								"C:/Users/Dharmin/Documents/GitHub/SmellBuster/repo/JFreeChart/91/jfreechart-1.0.x-branch/jfreechart-1.0.6.jar",
								">>",
								currentDir + "/" + this.destLocation };
						Runtime.getRuntime().exec(cmd);

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		SVNLoadLogic.updateProgressBar();

	}

}
