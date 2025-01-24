package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Utils.*;

public class MyUtils {
    /** Exit the procedure*/
    public static void exit(String message) {
        message(message);
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
    public static void SetBranch() {

    }

}
