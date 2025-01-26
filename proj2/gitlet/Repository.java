package gitlet;

import java.io.File;

import static gitlet.Commit.*;
import static gitlet.Utils.*;
import static gitlet.MyUtils.*;
import static gitlet.StageArea.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Yechen
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The object directory*/
    public static final File OBJECT_DIR = join(GITLET_DIR, "object");
    /** The Head file*/
    public static final File HEAD = join(GITLET_DIR, "HEAD");
    /** The branch directory*/
    public static final File REF = join(GITLET_DIR, "ref");
    /** The Local branch file*/
    public static final File LOCAL_BRANCH = join(REF, "heads");
    /** The StageArea file*/
    public static final File INDEX = join(GITLET_DIR, "index");
    /** The init operation
     *  .gitlet/
     *      object/
     *      Head
     *      Index
     *      ref/
     *          heads/
     * */
    public static void init() {
        if (GITLET_DIR.exists()) {
            exit("A Gitlet version-control system already exists in the current directory.");
        }
        mkdir(GITLET_DIR);
        mkdir(OBJECT_DIR);
        mkdir(REF);
        mkdir(LOCAL_BRANCH);
        new StageArea();
        mkFile(HEAD);
        createBranch("master");
        InitCommit();
    }

    /**
     * Todo add file to the stage area
     */
    public static void add(String fileName) {
        checkIfGitletExits();
        addToIndex(fileName);
    }

    public static void commit(String message) {
        checkIfGitletExits();
        new Commit(message);
    }

    private static void checkIfGitletExits() {
        if (!GITLET_DIR.exists()) {
            exit("Not in an initialized Gitlet directory.");
        }
    }

    //Todo: 如果当前Head指向的commit不存在则报错 ok!
    //Todo: 如果存在则将该文件的历史版本替换到当前的WD(work direction) ok!
    //Todo: 如果已经删除了当文件 然后又要还原前一个版本的文件是否会报错?
    public static void simpleCheckOut(String fileName) {
        Commit pre = preCommit();
        if (!pre.getTracked().containsKey(fileName)) {
            exit("File does not exist in that commit.");
        }
        Blob needFile = pre.getTracked().get(fileName);
        File temp = new File(needFile.getSourceFilePath());
        if (!temp.exists()) {
            mkFile(temp);
        }
        writeContents(new File(needFile.getSourceFilePath()), (Object) needFile.getContents());
    }
}
