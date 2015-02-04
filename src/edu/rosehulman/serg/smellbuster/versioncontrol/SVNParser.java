package edu.rosehulman.serg.smellbuster.versioncontrol;

import java.io.File;
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
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc2.SvnCheckout;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnTarget;

import edu.rosehulman.serg.smellbuster.util.DiffClass;

public class SVNParser implements IVersionControlParser {

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SVNParser(String url) {
		this.svnURL = url;	
	}
	
	public void checkoutRepo(long revision, File workingDir){
		final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
		try {
			final SvnCheckout checkout = svnOperationFactory.createCheckout();
			checkout.setSingleTarget(SvnTarget.fromFile(workingDir));
			checkout.setSource(SvnTarget.fromURL(SVNURL
					.parseURIEncoded(this.svnURL)));
			checkout.setRevision(SVNRevision.create(revision));
			checkout.run();
			svnOperationFactory.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			svnOperationFactory.dispose();
		}

	}

	public void loadVersionControlInfo() {
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

							String className = entryPath.getPath().replace("/",
									"\\");
							String[] nameSplit = className.split("\\\\");
							String packageName = "Package: ";
							for (int i = 0; i < nameSplit.length - 1; i++) {
								if (nameSplit[i].length() > 0) {
									packageName += nameSplit[i];
									if (i < nameSplit.length - 2)
										packageName += ".";
								}
							}

							className = nameSplit[nameSplit.length - 1];

							dc.setName(className);
							dc.setType(entryPath.getType());
							dc.setPackageName(packageName);
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
