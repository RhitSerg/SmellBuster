package edu.rosehulman.serg.smellbuster.versioncontrol;

import java.util.ArrayList;

import edu.rosehulman.serg.smellbuster.util.DiffClass;

public interface IVersionControlParser {

	public ArrayList<DiffClass> getDiffClassList();
	public long getLatestRevision();
	public void setLatestRevision(long latestRevision);
	public void loadVersionControlInfo();
}
