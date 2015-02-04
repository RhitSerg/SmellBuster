package edu.rosehulman.serg.smellbuster.versioncontrol;

import java.io.File;
import java.util.ArrayList;

import edu.rosehulman.serg.smellbuster.util.DiffClass;

public interface IVersionControlParser {

	public ArrayList<DiffClass> getDiffClassList();
	public long getLatestRevision();
	public void setLatestRevision(long latestRevision);
	public void loadVersionControlInfo();
	public void checkoutRepo(long revision, File workingDir);
}
