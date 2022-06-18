package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Represents a gitlet commit object.
 * TODO: It's a good idea to give a description here of what else this Class
 * does at a high level.
 *
 * @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /**
     * The message of this Commit.
     */
    private String message;
    private String commitHash;
    private HashMap<String, File> files;
    private Date timestamp;
    private Commit firstParent;
    private Commit secondParent;
//    private boolean


    Commit(String msg, Date tsp, String commitHash, HashMap<String, File> files, Commit firstParent, Commit secondParent) {
        this.message = msg;
        this.timestamp = tsp;
        this.firstParent = firstParent;
        this.secondParent = secondParent;
        this.commitHash = commitHash;
        this.files = files;
    }


    public void printLog() {
        System.out.println("===");
        System.out.println("commit " + commitHash);
        if (secondParent != null) {
            System.out.print("Merge: ");
            char[] firstChars = new char[7], secondChars = new char[7];
            firstParent.commitHash.getChars(0, 7, firstChars, 0);
            secondParent.commitHash.getChars(0, 7, secondChars, 0);
            for (int i = 0; i < 7; i++) {
                System.out.print(firstChars[i]);
            }
            System.out.print(" ");
            for (int i = 0; i < 7; i++) {
                System.out.print(secondChars[i]);
            }
            System.out.println();
        }
        SimpleDateFormat format = new SimpleDateFormat("a b e HH:mm:ss yyyy z");
//        String s = String.format("%a %b %e %HH:mm:ss yyyy %z", timestamp);
        String dateString = format.format(new Date());
        System.out.println("Date: " + dateString);
        System.out.println(message);
        System.out.println();
    }

    public Commit getFirstParent() {
        return firstParent;
    }

    public HashMap<String, File> getFiles() {
        return files;
    }

    public File getFile(String filename) {
        if (files == null) {
            return null;
        }
        return files.get(filename);
    }

    public boolean containsFile(String filename) {
        if (files == null) {
            return false;
        }
        return files.containsKey(filename);
    }

    public String getHash() {
        return commitHash;
    }

    public String getMessage() {
        return message;
    }
}
