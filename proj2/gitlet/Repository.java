package gitlet;

import java.io.File;
import java.util.*;

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
        checkIfCanCommit();
        new Commit(message);
    }

    private static void checkIfCanCommit() {
        checkIfGitletExits();
        if (checkIfIndexIsEmpty()) {
            exit();
        }
    }

    private static boolean checkIfIndexIsEmpty() {
        StageArea area = readObject(INDEX, StageArea.class);
        HashMap<String, Blob> index = area.Bolbs;
        return index.isEmpty();
    }

    private static void checkIfGitletExits() {
        if (!GITLET_DIR.exists()) {
            exit("Not in an initialized Gitlet directory.");
        }
    }

    //Todo: 如果当前Head指向的commit不存在则报错 ok!
    //Todo: 如果存在则将该文件的历史版本替换到当前的WD(work direction) ok!
    //Todo: 如果已经删除了当文件 然后又要还原前一个版本的文件是否会报错? ok!
    public static void simpleCheckout(String fileName) {
        Commit pre = preCommit();
        getFile(pre, fileName);
    }
    
    public static void specIdCheckout(String commitId, String fileName) {
        Commit specId = specificCommit(commitId);
        getFile(specId, fileName);
    }

    // 得到commit中名为fileName的文件，并且把它恢复到WD（工作区）
    private static void getFile(Commit commit, String fileName) {
        if (!commit.getTracked().containsKey(fileName)) {
            exit("File does not exist in that commit.");
        }
        Blob needFile = commit.getTracked().get(fileName);
        File temp = new File(needFile.getSourceFilePath());
        if (!temp.exists()) {
            mkFile(temp);
        }
        writeContents(temp, (Object) needFile.getContents());
    }

    // Todo:先不处理merge的情况，显示该分支所有的commit记录
    // tips:使用formatter格式化输出
    public static void log() {
        Commit pre = preCommit();
        logHelper(pre);
    }

    private static void logHelper(Commit commit) {
        if (commit != null) {
            Formatter form = new Formatter();
            form.format("===\ncommit %1$s\nDate: %2$ta %2$tb %2$td %2$tT %2$tY %2$tz\n%3$s\n", commit.getId(), commit.getDate(), commit.getMessage());
            System.out.println(form);
            logHelper(commit.getParentCommit());
        }
    }

    /**
     * Todo: 创建一个新分支，该分支指向当前Head指向的branch的commit，但是不切换分支 ok!
     * Todo: 解决报错问题！若该分支已经存在则不会创建
     */
    public static void branch(String branchName) {
        Commit commit = preCommit();
        File newBranch = join(LOCAL_BRANCH, "branchName");
        if (newBranch.exists()) {
            exit("A branch with that name already exists.");
        }
        mkFile(newBranch);
        writeContents(newBranch, commit.getId());
    }

    /**
     * Todo: 切换分支 使当前的Head指向指定的branch
     * Todo: 解决报错问题 1:分支不存在 2:切换的分支为当前分支 3:当前的分支有未跟踪的文件或要被覆盖的文件
     * Todo: 正确切换分支后应该清空暂存区
     */
    public static void checkoutBranch(String branchName) {


    }
}
