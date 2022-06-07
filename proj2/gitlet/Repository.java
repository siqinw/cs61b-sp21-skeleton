package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static gitlet.Utils.*;


/**
 * Represents a gitlet repository.
 * TODO: It's a good idea to give a description here of what else this Class
 * does at a high level.
 *
 * @author TODO
 */
public class Repository {
    /**
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File BLOB_DIR = join(GITLET_DIR, "blobs");
    public static final File COMMIT_DIR = join(GITLET_DIR, "commits");
    public static final File STAGE_DIR = join(GITLET_DIR, "stage");

    public static final File COMMITS_FILE = join(COMMIT_DIR, "commitTree");
    public static final File HEAD_FILE = join(COMMIT_DIR, "HEAD");

    public static final File RL_FILE = join(STAGE_DIR, "removeList");


    private static HashMap<String, Commit> commitTree;
    private static Commit HEAD;
    private static LinkedList<String> removeList;

    public static void readData() {
        if (!(GITLET_DIR.exists() && BLOB_DIR.exists() && COMMIT_DIR.exists())) {
            exitWithErr("Not in an initialized Gitlet directory.");
        }

        // Read in metadata from disk
        // HashMap, HEAD,
        commitTree = readObject(COMMITS_FILE, HashMap.class);
        HEAD = readObject(HEAD_FILE, Commit.class);
        removeList = readObject(RL_FILE, LinkedList.class);
    }

    public static void init() {
        if (GITLET_DIR.exists()) {
            exitWithErr("A Gitlet version-control system already exists in the current directory.");
        }
        if (!(GITLET_DIR.mkdir() && BLOB_DIR.mkdir() && COMMIT_DIR.mkdir() && STAGE_DIR.mkdir())) {
            exitWithErr("Failed to create init directory.");
        }

        Calendar cal = new GregorianCalendar();
        cal.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
        Date date = cal.getTime();
        String commitHash = sha1(date.toString());

        Commit initial = new Commit("initial commit", date, commitHash, null, null, null);

        commitTree = new HashMap<>();
        commitTree.put(commitHash, initial);
        HEAD = initial;
        removeList = new LinkedList<>();
        saveRepo();
    }

    public static void add(String filename) throws IOException {
        File inputFile = new File(filename);
        if (!inputFile.exists()) {
            exitWithErr("File does not exist.");
        }
        File stageFilename = join(STAGE_DIR, filename);
        File oldFile = null;
        boolean isNewFile = !HEAD.containsFile(filename);
        if (!isNewFile) {
            oldFile = HEAD.getFile(filename);
        }

        String oldFileContents = null;
        if (oldFile != null) {
            oldFileContents = readContentsAsString(oldFile);
        }
        String newFileContents = readContentsAsString(inputFile);

        // New file or modified file
        if (isNewFile || !sha1(oldFileContents).equals(sha1(newFileContents))) {
            if (!stageFilename.exists()) {
                Files.createFile(stageFilename.toPath());
            }
            Files.copy(inputFile.toPath(), stageFilename.toPath(), StandardCopyOption.REPLACE_EXISTING);
            // Same, unmodified file
        } else {
            //remove from stage area
            if (stageFilename.exists()) {
                restrictedDelete(stageFilename);
            }
        }
    }

    public static void commit(String message) {
        
    }

    public static void remove(String filename) throws IOException {
        File stageFile = join(STAGE_DIR, filename);

        boolean isTracked = HEAD.containsFile(filename);
        // If not staged/tracked or if removed already
        if ((!stageFile.exists() && !isTracked) || removeList.contains(filename)) {
            exitWithErr("No reason to remove the file.");
        }
        if (stageFile.exists()) {
            Files.delete(stageFile.toPath());
        }
        if (isTracked) {
            removeList.add(filename);
        }

    }

    public static void log() {
        Commit commit = HEAD;
        while (commit != null) {
            commit.printLog();
            commit = commit.getFirstParent();
        }
    }

    public static void saveRepo() {
        writeObject(COMMITS_FILE, commitTree);
        writeObject(HEAD_FILE, HEAD);
        writeObject(RL_FILE, removeList);
    }

    public static void exitWithErr(String msg) {
        System.out.println(msg);
        System.exit(0);
    }

}

