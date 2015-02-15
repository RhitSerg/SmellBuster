package edu.rosehulman.serg.smellbuster.runnable;

import java.io.File;

import edu.rosehulman.serg.smellbuster.logic.SVNLoadLogic;
import edu.rosehulman.serg.smellbuster.versioncontrol.VersionControlParserFactory;

public class RepoCheckoutRunnable implements Runnable {
	private String svnDirLocation;
	private long revision;
	private VersionControlParserFactory vcParser;

	public RepoCheckoutRunnable(String svnDirLocation, long revision,
			VersionControlParserFactory vcParser) {
		this.svnDirLocation = svnDirLocation;
		this.revision = revision;
		this.vcParser = vcParser;
	}

	@Override
	public void run() {
		File svnRepoDir = new File(this.svnDirLocation);
		if (!svnRepoDir.exists()) {
			try {
				svnRepoDir.mkdir();
				vcParser.checkoutRepo((long) revision, svnRepoDir);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		SVNLoadLogic.updateProgressBar();
	}
}
