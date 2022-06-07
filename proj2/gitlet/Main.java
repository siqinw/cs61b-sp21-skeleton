package gitlet;


import java.io.IOException;

import static gitlet.Repository.exitWithErr;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author TODO
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            exitWithErr("Please enter a command.");
        }
        String firstArg = args[0];
        if (!firstArg.equals("init")) {
            Repository.readData();
        }
        switch (firstArg) {
            case "init":
                if (!validateNumArgs(args, 1)) {
                    exitWithErr("Incorrect operands.");
                }
                Repository.init();
                break;
            case "add":
                if (!validateNumArgs(args, 2)) {
                    exitWithErr("Incorrect operands.");
                }
                Repository.add(args[1]);
                break;
            case "commit":
                if (args.length == 1) {
                    exitWithErr("Please enter a commit message.");
                } else if (!validateNumArgs(args, 2)) {
                    exitWithErr("Incorrect operands.");
                }
                Repository.commit(args[1]);
                break;
            case "rm":
                if (!validateNumArgs(args, 2)) {
                    exitWithErr("Incorrect operands.");
                }
                Repository.remove(args[1]);
                break;
            case "log":
                if (!validateNumArgs(args, 1)) {
                    exitWithErr("Incorrect operands.");
                }
                Repository.log();
                break;
            case "global-log":
                if (!validateNumArgs(args, 1)) {
                    exitWithErr("Incorrect operands.");
                }
                Repository.globalLog();
                break;
            case "find":
                if (!validateNumArgs(args, 1)) {
                    exitWithErr("Incorrect operands.");
                }
                Repository.find();
                break;
            case "status":
                if (!validateNumArgs(args, 1)) {
                    exitWithErr("Incorrect operands.");
                }
                Repository.status();
                break;
            case "checkout":
                if (args.length == 3 && args[1].equals("==")) {
                    Repository.checkoutFile(args[2]);
                } else if (args.length == 4 && args[2].equals("==")) {
                    Repository.checkoutFile(args[1], args[3]);
                } else if (args.length == 2) {
                    Repository.checkout(args[1]);
                } else {
                    exitWithErr("Incorrect operands.");
                }
                // TODO: FILL THE REST IN
            default:
                exitWithErr("No command with that name exists.");
        }
    }


    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     *
     * @param args Argument array from command line
     * @param n    Number of expected arguments
     */
    public static boolean validateNumArgs(String[] args, int n) {
        return args.length == n;
    }
}
