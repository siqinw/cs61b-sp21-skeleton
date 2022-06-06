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
                if (!validateNumArgs(args, 2)) {
                    exitWithErr("Incorrect operands.");
                }
                Repository.commit(args[1]);
                break;
            case "log":
                if (!validateNumArgs(args, 1)) {
                    exitWithErr("Incorrect operands.");
                }
                Repository.log();
                break;
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
