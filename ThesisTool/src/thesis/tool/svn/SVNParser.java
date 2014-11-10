package thesis.tool.svn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;

import thesis.tool.util.DiffClass;

public class SVNParser {

	private String svnURL;
	private long startRev;
	private long endRev;
	private ArrayList<DiffClass> diffClassList;
	private SVNRepository repository;
	private long latestRevision;

	public SVNParser(String url, long startRev, long endRev) {
		DAVRepositoryFactory.setup();
		this.diffClassList = new ArrayList<>();
		this.svnURL = url;
		this.startRev = startRev;
		this.endRev = endRev;
		this.repository = null;
		try {
			this.repository = SVNRepositoryFactory.create(SVNURL
					.parseURIEncoded(this.svnURL));
			SVNDirEntry entry = this.repository.info(".", -1);
			this.setLatestRevision(entry.getRevision());
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void loadSVNInfo() {
		try {			
			Collection<?> logEntries = null;

			logEntries = repository.log(new String[] { "" }, null,
					this.startRev, this.endRev, true, true);

			for (Iterator<?> entries = logEntries.iterator(); entries.hasNext();) {
				SVNLogEntry logEntry = (SVNLogEntry) entries.next();

				if (logEntry.getChangedPaths().size() > 0) {
					Set<String> changedPathsSet = logEntry.getChangedPaths()
							.keySet();

					for (Iterator<String> changedPaths = changedPathsSet
							.iterator(); changedPaths.hasNext();) {
						SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry
								.getChangedPaths().get(changedPaths.next());
						if (entryPath.getPath().contains(".java")
								&& !entryPath.getPath().contains("junit")
								&& entryPath.getType() != 'D'
								&& !(entryPath.getPath().contains("Test") || entryPath
										.getPath().contains("Tests"))) {
							DiffClass dc = new DiffClass();
							
							String className = entryPath.getPath().replace("/", "\\");
							String[] nameSplit = className.split("\\\\");
							className = nameSplit[nameSplit.length - 1];
							
							dc.setName(className);
							dc.setType(entryPath.getType());
							this.diffClassList.add(dc);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<DiffClass> getDiffClassList() {
		return this.diffClassList;
	}

	public long getLatestRevision() {
		return latestRevision;
	}

	public void setLatestRevision(long latestRevision) {
		this.latestRevision = latestRevision;
	}
}
