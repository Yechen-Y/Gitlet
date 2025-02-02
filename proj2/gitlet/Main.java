package gitlet;

import java.util.Date;

import static gitlet.Commit.InitCommit;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Yechen
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            MyUtils.exit("Please enter a command.");
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                validateNumArg(args, 1);
                Repository.init();
                break;
            case "add":
                validateNumArg(args, 2);
                Repository.add(args[1]);
                break;
            case "commit":
                validateNumArg(args, 2);
                Repository.commit(args[1]);
                break;
            case "checkout":
                if (args.length == 3 && args[1].equals("--")) {
                    Repository.simpleCheckout(args[2]);
                }
                if (args.length == 4 && args[2].equals("--")) {
                    Repository.specIdCheckout(args[1], args[3]);
                }
                if (args.length == 2) {
                    Repository.checkoutBranch(args[1]);
                }
                break;
            case "log":
                validateNumArg(args, 1);
                Repository.log();
                break;
            case "branch":
                validateNumArg(args, 2);
                Repository.branch(args[1]);
                break;
            case "rm":
                validateNumArg(args, 2);
                Repository.rm(args[1]);
                break;
        }
    }

    public static void validateNumArg(String[] args, int n) {
        if (args.length != n) {
            MyUtils.exit("Incorrect operands.");
        }
    }
}

