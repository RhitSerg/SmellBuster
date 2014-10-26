package thesis.tool.svn;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;

public class SVNParser {

	private String svnURL;
	private long startRev;
	private long endRev;

	public SVNParser(String url, long startRev, long endRev) {
		DAVRepositoryFactory.setup();

		this.svnURL = url;
		this.startRev = startRev;
		this.endRev = endRev;
	}

	public void loadSVNInfo() {
		SVNRepository repository = null;
		try {
			repository = SVNRepositoryFactory.create(SVNURL
					.parseURIEncoded(this.svnURL));

			Collection<?> logEntries = null;

			logEntries = repository.log(new String[] { "" }, null,
					this.startRev, this.endRev, true, true);

			System.out.println("changed paths:");
			for (Iterator<?> entries = logEntries.iterator(); entries.hasNext();) {
				SVNLogEntry logEntry = (SVNLogEntry) entries.next();

				if (logEntry.getChangedPaths().size() > 0) {
					Set<String> changedPathsSet = logEntry.getChangedPaths()
							.keySet();

					for (Iterator<String> changedPaths = changedPathsSet.iterator(); changedPaths
							.hasNext();) {
						SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry
								.getChangedPaths().get(changedPaths.next());
						if (entryPath.getPath().contains(".java")
								&& !entryPath.getPath().contains("junit")
								&& entryPath.getType() != 'D')
							System.out.println(" " + entryPath.getType() + " "
									+ entryPath.getPath());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SVNParser parser = new SVNParser(
				"http://svn.code.sf.net/p/jfreechart/code/branches/", 91, 588);
		parser.loadSVNInfo();
	}
}
