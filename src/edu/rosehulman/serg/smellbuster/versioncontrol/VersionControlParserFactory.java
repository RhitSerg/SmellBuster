package edu.rosehulman.serg.smellbuster.versioncontrol;

import java.util.ArrayList;

import edu.rosehulman.serg.smellbuster.util.DiffClass;

public class VersionControlParserFactory implements IVersionControlParser {
	
	private IVersionControlParser vcParser;
	
	public VersionControlParserFactory(String url, long startRev, long endRev){
		this.vcParser = new SVNParser(url, startRev, endRev);
	}
	
	public ArrayList<DiffClass> getDiffClassList(){
		return this.vcParser.getDiffClassList();
	}
	
	public long getLatestRevision(){
		return this.vcParser.getLatestRevision();
	}
	
	public void setLatestRevision(long latestRevision){
		this.vcParser.setLatestRevision(latestRevision);
	}
	
	public void loadVersionControlInfo(){
		this.vcParser.loadVersionControlInfo();
	}

}
