package edu.rosehulman.serg.smellbuster.runnable;

import java.io.File;

import edu.rosehulman.serg.smellbuster.logic.SVNLoadLogic;
import edu.rosehulman.serg.smellbuster.util.AntExecutor;
import edu.rosehulman.serg.smellbuster.util.MavenExecutor;

public class ProjectBuildRunnable implements Runnable {

	private String svnBuildDirLocation;
	private String version;
	private String jarProperty;

	public ProjectBuildRunnable(String svnBuildDirLocation, String version, String jarProperty) {
		this.svnBuildDirLocation = svnBuildDirLocation;
		this.version = version;
		this.jarProperty = "";
		if (jarProperty != null)
			this.jarProperty = jarProperty;
	}

	@Override
	public void run() {

		if (this.svnBuildDirLocation.contains("pom.xml")) {
			String dirLocation = this.svnBuildDirLocation.substring(0,
					this.svnBuildDirLocation.length() - 7);
			File dir = new File(dirLocation);
			if (!dir.exists()) {
				try {
					MavenExecutor.executeMavenTask(svnBuildDirLocation);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (this.svnBuildDirLocation.contains("build.xml")) {
			String dirLocation = this.svnBuildDirLocation.substring(0,
					this.svnBuildDirLocation.length() - 9);
			File dir = new File(dirLocation);
			if (!dir.exists()) {
				try {
					AntExecutor.executeAntTask(this.svnBuildDirLocation, this.version, this.jarProperty);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}

		SVNLoadLogic.updateProgressBar();
	}

}
