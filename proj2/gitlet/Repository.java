package gitlet;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

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

    public static final File COMMITS_FILE = join(COMMIT_DIR, "commitTree");
    public static final File HEAD_FILE = join(COMMIT_DIR, "HEAD");

    private static HashMap<String, Commit> commitTree;
    private static Commit HEAD;

    public static void readData() {
        if (!(GITLET_DIR.exists() && BLOB_DIR.exists() && COMMIT_DIR.exists())) {
            exitWithErr("Not in an initialized Gitlet directory.");
        }

        // Read in metadata from disk
        // HashMap, HEAD,
        commitTree = readObject(COMMITS_FILE, HashMap.class);
        HEAD = readObject(HEAD_FILE, Commit.class);
    }

    public static void init() {
        if (GITLET_DIR.exists()) {
            exitWithErr("A Gitlet version-control system already exists in the current directory.");
        }
        if (!(GITLET_DIR.mkdir() && BLOB_DIR.mkdir() && COMMIT_DIR.mkdir())) {
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
        saveRepo();
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
    }

    public static void exitWithErr(String msg) {
        System.out.println(msg);
        System.exit(0);
    }

}

