package apidiff;

import apidiff.enums.Classifier;
import apidiff.internal.analysis.DiffProcessor;
import apidiff.internal.analysis.DiffProcessorImpl;
import apidiff.internal.service.git.GitFile;
import apidiff.internal.service.git.GitService;
import apidiff.internal.service.git.GitServiceImpl;
import apidiff.internal.util.UtilTools;
import apidiff.internal.visitor.APIVersion;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevWalk;
import org.scijava.util.VersionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class APIDiff implements DiffDetector{
	
	private String nameProject;
	
	private String path;
	
	private String url;

	private RevCommit lastCommit = null;

	private Logger logger = LoggerFactory.getLogger(APIDiff.class);

	public APIDiff(final String nameProject, final String url) {
		this.url = url;
		this.nameProject = nameProject;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public Result detectChangeAtCommit(String commitId, Classifier classifierAPI) {
		Result result = new Result();
		try {
			GitService service = new GitServiceImpl();
			Repository repository = service.openRepositoryAndCloneIfNotExists(this.path, this.nameProject, this.url);
			RevCommit commit = service.createRevCommitByCommitId(repository, commitId);
			Result resultByClassifier = this.diffCommit(commit, repository, this.nameProject, classifierAPI);
			result.getChangeType().addAll(resultByClassifier.getChangeType());
			result.getChangeMethod().addAll(resultByClassifier.getChangeMethod());
			result.getChangeField().addAll(resultByClassifier.getChangeField());
		} catch (Exception e) {
			this.logger.error("Error in calculating commitn diff ", e);
		}
		this.logger.info("Finished processing.");
		return result;
	}
	
	@Override
	public Result detectChangeAllHistory(String branch, List<Classifier> classifiers) throws Exception {
		Result result = new Result();
		GitService service = new GitServiceImpl();
		Repository repository = service.openRepositoryAndCloneIfNotExists(this.path, this.nameProject, this.url);
		RevWalk revWalk = service.createAllRevsWalk(repository, branch);
		//Commits.
		Iterator<RevCommit> i = revWalk.iterator();
		while(i.hasNext()){
			RevCommit currentCommit = i.next();
			for(Classifier classifierAPI: classifiers){
				Result resultByClassifier = this.diffCommit(currentCommit, repository, this.nameProject, classifierAPI);
				result.getChangeType().addAll(resultByClassifier.getChangeType());
				result.getChangeMethod().addAll(resultByClassifier.getChangeMethod());
				result.getChangeField().addAll(resultByClassifier.getChangeField());
			}
		}
		this.logger.info("Finished processing.");
		return result;
	}

	@Override
	public Map<String, Result> detectChangeAllReleases(String branch, List<Classifier> classifiers) throws Exception {
		GitService service = new GitServiceImpl();
		Repository repository = service.openRepositoryAndCloneIfNotExists(this.path, this.nameProject, url);
		List<Ref> releases = getReleaseCommits(repository);
//		releases.sort(Comparator.comparing(Ref::getName, VersionUtils::compare));
		List<RevCommit> releaseCommits = toCommits(releases, repository);
		Map<String, RevCommit> releaseCommitMap = new LinkedHashMap<>();
		for (int i = 0; i < releaseCommits.size(); i++) {
			releaseCommitMap.put(releases.get(i).getName().replace("refs/tags/", ""), releaseCommits.get(i));
		}
		Map<String, Result> results = new LinkedHashMap<>();
		releaseCommitMap.entrySet()
			.stream()
			.sorted(Map.Entry.comparingByValue(Comparator.comparing(revCommit -> revCommit.getAuthorIdent().getWhen())))
			.forEach(entry -> {
				RevCommit release = entry.getValue();
				Result resultByClassifier = null;
				try {
					resultByClassifier = diffCommit(this.path, release, repository, Classifier.API);
				} catch (Exception e) {
					e.printStackTrace();
				}
				results.put(entry.getKey(), resultByClassifier);

			});

		return results;
	}
	
	@Override
	public Result detectChangeAllHistory(List<Classifier> classifiers) throws Exception {
		return this.detectChangeAllHistory(null, classifiers);
	}
	
	@Override
	public Result fetchAndDetectChange(List<Classifier> classifiers) {
		Result result = new Result();
		try {
			GitService service = new GitServiceImpl();
			Repository repository = service.openRepositoryAndCloneIfNotExists(this.path, this.nameProject, this.url);
			RevWalk revWalk = service.fetchAndCreateNewRevsWalk(repository, null);
			//Commits.
			Iterator<RevCommit> i = revWalk.iterator();
			while(i.hasNext()){
				RevCommit currentCommit = i.next();
				for(Classifier classifierAPI : classifiers){
					Result resultByClassifier = this.diffCommit(currentCommit, repository, this.nameProject, classifierAPI);
					result.getChangeType().addAll(resultByClassifier.getChangeType());
					result.getChangeMethod().addAll(resultByClassifier.getChangeMethod());
					result.getChangeField().addAll(resultByClassifier.getChangeField());
				}
			}
		} catch (Exception e) {
			this.logger.error("Error in calculating commit diff ", e);
		}

		this.logger.info("Finished processing.");
		return result;
	}
	
	@Override
	public Result detectChangeAllHistory(String branch, Classifier classifier) throws Exception {
		return this.detectChangeAllHistory(branch, Arrays.asList(classifier));
	}

	@Override
	public Result detectChangeAllHistory(Classifier classifier) throws Exception {
		return this.detectChangeAllHistory(Arrays.asList(classifier));
	}

	@Override
	public Result fetchAndDetectChange(Classifier classifier) throws Exception {
		return this.fetchAndDetectChange(Arrays.asList(classifier));
	}
	
	private Result diffCommit(final RevCommit currentCommit, final Repository repository, String nameProject, Classifier classifierAPI) throws Exception{
		File projectFolder = new File(UtilTools.getPathProject(this.path, nameProject));
		if(currentCommit.getParentCount() != 0){//there is at least one parent
			try {
				APIVersion version1 = this.getAPIVersionByCommit(currentCommit.getParent(0).getName(), projectFolder, repository, currentCommit, classifierAPI);//old version
				APIVersion version2 = this.getAPIVersionByCommit(currentCommit.getId().getName(), projectFolder, repository, currentCommit, classifierAPI); //new version
				DiffProcessor diff = new DiffProcessorImpl();
				return diff.detectChange(version1, version2, repository, currentCommit);
			} catch (Exception e) {
				this.logger.error("Error during checkout [commit=" + currentCommit + "]");
			}
		}
		return new Result();
	}
	
	private APIVersion getAPIVersionByCommit(String commit, File projectFolder, Repository repository, RevCommit currentCommit, Classifier classifierAPI) throws Exception{
		
		GitService service = new GitServiceImpl();
		
		//Finding changed files between current commit and parent commit.
		Map<ChangeType, List<GitFile>> mapModifications = service.fileTreeDiff(repository, currentCommit);
		
		service.checkout(repository, commit);
		return new APIVersion(this.path, projectFolder, mapModifications, classifierAPI);
	}

	private List<RevCommit> toCommits(List<Ref> releases, Repository repository) {
		List<RevCommit> res = new ArrayList<>();
		for (Ref release : releases) {
			res.add(getRevCommit(release, repository));
		}
		return res;
	}

	private static ObjectId getActualRefObjectId(Ref ref, Repository repo) {
		final Ref repoPeeled = repo.peel(ref);
		if(repoPeeled.getPeeledObjectId() != null) {
			return repoPeeled.getPeeledObjectId();
		}
		return ref.getObjectId();
	}

	private static List<Ref> getReleaseCommits(Repository repository) throws MissingObjectException, IncorrectObjectTypeException, GitAPIException {
		List<Ref> tagRefs = Git.wrap(repository).tagList().call();
		for (Ref ref : tagRefs) {
			System.out.println("Tag: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName() + " " + getActualRefObjectId(ref, repository));
		}
		return tagRefs;
	}

	private Result diffCommit(String path, final RevCommit currentCommit, final Repository repository, Classifier classifierAPI) throws Exception{
		if(lastCommit != null){
			try {
				APIVersion version1 = getAPIVersionByCommit(path, lastCommit.getName(), repository, classifierAPI);//old version
				APIVersion version2 = getAPIVersionByCommit(path, currentCommit.getId().getName(), repository, classifierAPI); //new version
				DiffProcessor diff = new DiffProcessorImpl();
				lastCommit = currentCommit;
				return diff.detectChange(version1, version2, repository, currentCommit);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error during checkout [commit=" + currentCommit + "]");
			}
		}
		lastCommit = currentCommit;
		return new Result();
	}

	private RevCommit getRevCommit(Ref ref, Repository repo) {
		ObjectId revId = getActualRefObjectId(ref, repo);
		try (RevWalk revWalk = new RevWalk(repo)) {
			RevObject peeled = null;
			try {
				peeled = revWalk.peel(revWalk.parseAny(revId));
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (peeled instanceof RevCommit)
				return (RevCommit) peeled;
			else
				return null;
		}
	}


	private APIVersion getAPIVersionByCommit(String path, String commit, Repository repository, Classifier classifierAPI) throws Exception{

		GitService service = new GitServiceImpl();

		service.checkout(repository, commit);
		return new APIVersion(path, this.nameProject, classifierAPI);
	}

}
