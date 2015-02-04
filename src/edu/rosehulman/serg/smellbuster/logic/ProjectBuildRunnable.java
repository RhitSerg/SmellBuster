package edu.rosehulman.serg.smellbuster.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import edu.rosehulman.serg.smellbuster.util.AntExecutor;

public class ProjectBuildRunnable implements Runnable {

	private String svnBuildDirLocation;
	private String target;

	public ProjectBuildRunnable(String svnBuildDirLocation, String target) {
		this.svnBuildDirLocation = svnBuildDirLocation;
		this.target = target;
	}

	@Override
	public void run() {

		if (this.svnBuildDirLocation.contains("build.xml")) {
			try {
//				ProcessBuilder builder = new ProcessBuilder("C:\\apache-ant-1.9.4\\bin\\ant -f "+this.svnBuildDirLocation);
//				builder.redirectErrorStream(true);
//				this.svnBuildDirLocation = this.svnBuildDirLocation.substring(
//						0, this.svnBuildDirLocation.length() - 10);
//				builder.directory(new File(this.svnBuildDirLocation));
//				Process p = builder.start();
//				BufferedReader stdInput = new BufferedReader(
//						new InputStreamReader(p.getInputStream()));
//				String s = "";
//				while ((s = stdInput.readLine()) != null) {
//					// System.out.println(s);
//				}
				 AntExecutor.executeAntTask(svnBuildDirLocation, target);
			} catch (Exception e) {
				// AntExecutor.executeAntTask(svnBuildDirLocation, null);
				e.printStackTrace();
			}
		} else if (this.svnBuildDirLocation.contains("pom.xml")) {
			// do maven build.
		}

		SVNLoadLogic.updateProgressBar();
	}

}
