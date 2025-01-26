package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class MyUtils {
    /** Exit the procedure*/
    public static void exit(String message) {
        message(message);
        System.exit(0);
    }

    public static void exit() {
        System.exit(0);
    }

    public static void mkdir(File file) {
        try {
            file.mkdir();
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void mkFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Todo: 创建一个分支并使其指向当前的commit(SHA值)，并存储在.gitlet/heads/中
     * Todo: 改变Head的值 使Head file的内容为branch file的path ok!
     */
    public static void createBranch(String branchName) {
        File newBranch = new File(LOCAL_BRANCH, branchName);
        mkFile(newBranch);
        writeContents(HEAD, newBranch.getAbsolutePath());
    }

}
