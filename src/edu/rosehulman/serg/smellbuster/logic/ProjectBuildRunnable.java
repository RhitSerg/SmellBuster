package edu.rosehulman.serg.smellbuster.logic;

import java.io.File;

import edu.rosehulman.serg.smellbuster.util.AntExecutor;
import edu.rosehulman.serg.smellbuster.util.MavenExecutor;

public class ProjectBuildRunnable implements Runnable {

	private String svnBuildDirLocation;
	private String version;

	public ProjectBuildRunnable(String svnBuildDirLocation, String version) {
		this.svnBuildDirLocation = svnBuildDirLocation;
		this.version = version;
	}

	@Override
	public void run() {

		if (this.svnBuildDirLocation.contains("pom.xml")) {
			String dirLocation = this.svnBuildDirLocation.substring(0,
					this.svnBuildDirLocation.length() - 7);
			File dir = new File(dirLocation);
			if (dir.exists()) {
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
			if (dir.exists()) {
				try {
					AntExecutor.executeAntTask(this.svnBuildDirLocation, this.version);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}

		SVNLoadLogic.updateProgressBar();
	}

}
