package edu.rosehulman.serg.smellbuster.logic;

import java.io.File;

import edu.rosehulman.serg.smellbuster.util.MavenExecutor;

public class ProjectBuildRunnable implements Runnable {

	private String svnBuildDirLocation;
	private String mavenHome;

	public ProjectBuildRunnable(String svnBuildDirLocation, String mavenHome) {
		this.svnBuildDirLocation = svnBuildDirLocation;
		this.mavenHome = mavenHome;
	}

	@Override
	public void run() {

		if (this.svnBuildDirLocation.contains("pom.xml")) {
			String dirLocation = this.svnBuildDirLocation.substring(0,
					this.svnBuildDirLocation.length() - 7);
			File dir = new File(dirLocation);
			if (!dir.exists()) {
				try {
					MavenExecutor.executeMavenTask(svnBuildDirLocation,
							mavenHome);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (this.svnBuildDirLocation.contains("build.xml")) {
			// do ant build.
		}

		SVNLoadLogic.updateProgressBar();
	}

}
