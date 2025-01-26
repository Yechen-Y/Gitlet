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
        }
    }

    public static void validateNumArg(String[] args, int n) {
        if (args.length != n) {
            MyUtils.exit("Incorrect operands.");
        }
    }
}

