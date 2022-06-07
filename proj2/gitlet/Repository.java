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
    public static final File RL_FILE = join(COMMIT_DIR, "removeList");
    public static final File BR_FILE = join(COMMIT_DIR, "branches");
    public static final File CUR_BR_FILE = join(COMMIT_DIR, "currentBranch");


    private static HashMap<String, Commit> commitTree;
    private static Commit HEAD;
    private static LinkedList<String> removeList;
    private static HashMap<String, Commit> branches;
    private static String currentBranch;


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

        Commit initial = new Commit("initial commit", date, commitHash, new HashMap<String, File>(), null, null);

        commitTree = new HashMap<>();
        commitTree.put(commitHash, initial);
        HEAD = initial;
        removeList = new LinkedList<>();
        branches = new HashMap<>();
        branches.put("master", initial);
        currentBranch = "master";
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
        saveRepo();
    }

    public static void commit(String message) throws IOException {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        String commitHash = sha1(date.toString());

        HashMap<String, File> fileHashMap = new HashMap<>(HEAD.getFiles());
        List<String> stagedFiles = plainFilenamesIn(STAGE_DIR);

        // Nothing to be commited
        if (stagedFiles == null && removeList.size() == 0) {
            exitWithErr("No changes added to the commit.");
        }

        if (stagedFiles != null) {
            for (String s : stagedFiles) {
                File stagedPath = join(STAGE_DIR, s);
                File commitPath = join(BLOB_DIR, s);
                int versionNumber = 0;
                // Hard code, need to find max version number
                if (!commitPath.exists()) {
                    commitPath.mkdirs();
                } else {
                    // Get max
                    versionNumber = getMaxVersion(s) + 1;
                }

                File blobPath = join(commitPath, String.valueOf(versionNumber));
                blobPath.createNewFile();
                Files.move(stagedPath.toPath(), blobPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
                fileHashMap.put(s, blobPath);
            }
        }

        for (String s : removeList) {
            fileHashMap.remove(s);
            File fileDir = join(BLOB_DIR, s);
            // System.out.println(fileDir);
            if (deleteDirectory(fileDir) == false) {
                exitWithErr("Delete Directory failed");
            }
            removeList.remove(s);
        }

        Commit newCommit = new Commit(message, date, commitHash, fileHashMap, HEAD, null);
        commitTree.put(commitHash, newCommit);
        HEAD = newCommit;
        saveRepo();
    }

    public static void remove(String filename) throws IOException {
        File stageFile = join(STAGE_DIR, filename);
        File workFile = join(CWD, filename);
        boolean isTracked = HEAD.containsFile(filename);

        // If not staged/tracked or if removed already
        if ((!stageFile.exists() && !isTracked) || removeList.contains(filename)) {
            exitWithErr("No reason to remove the file.");
        }
        // Delete from working dir
        if (workFile.delete() == false) {
            exitWithErr("Delete file failed");
        }

        if (stageFile.exists()) {
            Files.delete(stageFile.toPath());
        }
        if (isTracked) {
            removeList.add(filename);
        }

        saveRepo();
    }

    public static void log() {
        Commit commit = HEAD;
        while (commit != null) {
            commit.printLog();
            commit = commit.getFirstParent();
        }
    }

    public static void globalLog() {
        for (Commit c : commitTree.values()) {
            c.printLog();
        }
    }

    public static void find(String message) {
        int match = 0;
        for (Commit c : commitTree.values()) {
            if (message.equals(c.getMessage())) {
                System.out.println(c.getHash());
                match++;
            }
        }
        if (match == 0) {
            exitWithErr("Found no commit with that message.");
        }
    }

    public static void status() {
        System.out.println("=== Branches ===");
        for (String s : branches.keySet()) {
            if (currentBranch.equals(s)) {
                System.out.print("*");
            }
            System.out.println(s);
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        List<String> stagedFiles = plainFilenamesIn(STAGE_DIR);
        if (stagedFiles != null) {
            for (String s : stagedFiles) {
                System.out.println(s);
            }
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        Collections.sort(removeList);
        for (String s : removeList) {
            System.out.println(s);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();

        System.out.println("=== Untracked Files ===");
        System.out.println();


    }

    public static void checkoutFile(String filename) throws IOException {
        checkoutFile(HEAD.getHash(), filename);
    }

    public static void checkoutFile(String commitHash, String filename) throws IOException {
        Commit c = commitTree.get(commitHash);
        if (!c.containsFile(filename)) {
            exitWithErr("File does not exist in that commit.");
        }
        File workFile = join(CWD, filename);
        File commitFile = c.getFile(filename);
        if (!workFile.exists()) {
            workFile.createNewFile();
        }
        Files.copy(commitFile.toPath(), workFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void checkout(String commitHash) {
//        Commit branchHead = branches.get(commitHash);
//
//        HEAD = branchHead;
//        currentBranch =
    }

    public static void saveRepo() {
        writeObject(COMMITS_FILE, commitTree);
        writeObject(HEAD_FILE, HEAD);
        writeObject(RL_FILE, removeList);
        writeObject(BR_FILE, branches);
        writeObject(CUR_BR_FILE, currentBranch);
    }

    public static void readData() {
        if (!(GITLET_DIR.exists() && BLOB_DIR.exists() && COMMIT_DIR.exists())) {
            exitWithErr("Not in an initialized Gitlet directory.");
        }

        // Read in metadata from disk
        // commit tree, HEAD, branch
        commitTree = readObject(COMMITS_FILE, HashMap.class);
        HEAD = readObject(HEAD_FILE, Commit.class);
        removeList = readObject(RL_FILE, LinkedList.class);
        branches = readObject(BR_FILE, HashMap.class);
        currentBranch = readObject(CUR_BR_FILE, String.class);
    }


    public static void exitWithErr(String msg) {
        System.out.println(msg);
        System.exit(0);
    }

    private static int getMaxVersion(String filename) {
        File blobs = join(BLOB_DIR, filename);
        List<String> filenameList = plainFilenamesIn(blobs);
        // No plain file at all
        if (filename == null) {
            return -1;
        }
        return Integer.parseInt(filenameList.get(filenameList.size() - 1));
    }

    private static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}

