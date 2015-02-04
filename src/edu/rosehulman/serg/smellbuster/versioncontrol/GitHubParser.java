package edu.rosehulman.serg.smellbuster.versioncontrol;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import edu.nyu.cs.javagit.api.*;
import edu.nyu.cs.javagit.api.commands.GitLogResponse.Commit;
import edu.nyu.cs.javagit.api.commands.GitStatus;
import edu.nyu.cs.javagit.api.commands.GitStatusResponse;
import edu.nyu.cs.javagit.api.DotGit;
import edu.rosehulman.serg.smellbuster.util.DiffClass;

public class GitHubParser implements IVersionControlParser {

	public GitHubParser() {

		// RepositoryService service = new RepositoryService();
		try {
			
//			Github github = new RtGithub(/* credentials */);
//			Repos repos = github.repos();
//			Repo repo = repos.get(new Coordinates.Simple("jcabi/jcabi-github"));
			
//			File gitWorkDir = new File("C:\\Users\\Dharmin\\Documents\\GitHub\\SmellBuster");
//		    Git git = Git.open(gitWorkDir);
//		    Repository repo = git.getRepository();
//		 
//		    ObjectId lastCommitId = repo.resolve(Constants.HEAD);
//		 
//		    RevWalk revWalk = new RevWalk(repo);
//		    RevCommit commit = revWalk.parseCommit(lastCommitId);
//		 
//		    RevTree tree = commit.getTree();
//		 
//		    TreeWalk treeWalk = new TreeWalk(repo);
//		    treeWalk.addTree(tree);
//		    treeWalk.setRecursive(true);
//		    //treeWalk.setFilter(PathFilter.create("file1.txt"));
//		    if (!treeWalk.next()) 
//		    {
//		      System.out.println("Nothing found!");
//		      return;
//		    }
//		 
//		    ObjectId objectId = treeWalk.getObjectId(0);
//		    ObjectLoader loader = repo.open(objectId);
//		 
//		    ByteArrayOutputStream out = new ByteArrayOutputStream();
//		    loader.copyTo(out);
//		    System.out.println("file1.txt:\n" + out.toString());
		    
			
			
//			File repositoryDirectory = new File("C:\\Users\\Dharmin\\Documents\\GitHub\\SmellBuster");
//			DotGit dotGit = DotGit.getInstance(repositoryDirectory);
//			
//			GitStatus gitStatus = new GitStatus();
//			GitStatusResponse status = gitStatus.status(repositoryDirectory);
//			
//			for (Commit c : dotGit.getLog()) {
//			    System.out.println(c.getMessage());
//			}
			
			File emptyDir = new File("C:\\Users\\Dharmin\\Downloads\\tempDir");
			if (!emptyDir.exists()){
				emptyDir.mkdir();
			}
			final int size = 25;
			final RepositoryId repo = new RepositoryId("RhitSerg", "SmellBuster");
			final String message = "   {0} by {1} on {2} {3}";
			final CommitService service = new CommitService();
			String[] credentials = getAuthenticationInfo();
			service.getClient().setCredentials(credentials[0], credentials[1]);
			int pages = 1;
//			
//			GitHub gitHub=GitHub.connectUsingPassword(credentials[0], credentials[1]);
//			GHRepository r= gitHub.getRepository("RhitSerg/SmellBuster");
//			GHCommitStatus state;
//
			List<RepositoryCommit> commits = service.getCommits(repo);
			
			for (RepositoryCommit commit : service.getCommits(repo)) {
				// System.out.println("Commit Page " + pages++);
				// for (RepositoryCommit commit : commits) {
				// String sha = commit.getSha().substring(0, 7);
//				org.eclipse.egit.github.core.Commit g = commit.getCommit();
				
				List<CommitFile> commitFiles = commit.getFiles();
				System.out.println(commitFiles);
				System.out.println(commitFiles.size());
				String mess = commit.getCommit().getMessage();
				String sha = commit.getSha();
				
//				List<GHCommitStatus> lst=r.listCommitStatuses(sha).asList();

				String author = commit.getCommit().getAuthor().getName();
				Date date = commit.getCommit().getAuthor().getDate();
				System.out.println(MessageFormat.format(message, sha, author,
						date, mess));

				// CommitStats stats = commit.getStats();
				// System.out.println(stats.getTotal());
				// }
			}

			// for (Repository repo : service.getRepositories("shahdk"))
			// System.out.println(repo.getName() + " Watchers: "
			// + repo.getWatchers());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String[] getAuthenticationInfo() {
		try {
			FileInputStream fstream = new FileInputStream(
					"C:\\Users\\Dharmin\\Desktop\\Thesis\\auth.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					fstream));
			String line = "";
			while ((line = br.readLine()) != null) {
				return line.split(";");
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<DiffClass> getDiffClassList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getLatestRevision() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLatestRevision(long latestRevision) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadVersionControlInfo() {
		// TODO Auto-generated method stub

	}

	public static void main(String args[]) {
		GitHubParser parser = new GitHubParser();
	}

	@Override
	public void checkoutRepo(long revision, File workingDir) {
		// TODO Auto-generated method stub
		
	}

}
