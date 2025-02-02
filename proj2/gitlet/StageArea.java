package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

import static gitlet.Repository.*;
import static gitlet.Utils.*;
import static gitlet.MyUtils.*;

public class StageArea implements Serializable {
    /**
     * Todo: 应该有一个名称对应一个Blob 用HashMap存储 ok!
     * Todo: StageArea应该存储在.gitlet/index 中,ok! 当commit后应该清除index中的内容 ok!
     * Todo: 每次改变暂存区都应该保存起来 使用writecontents?
     * Todo: 解决边缘情况 1.add失败的情况 没有该文件 ok!
     * Todo: 2.即将添加到暂存区的文件和上一个commit的文件相同 ok!
     * Todo: 再添加一个map用来存储删除文件
     */
    public HashMap<String, Blob> Bolbs;
    public HashMap<String, Blob> rmBlobs;


    public StageArea() {
        Bolbs = new HashMap<>();
        mkFile(INDEX);
        convertToFile(this);
    }

    public static void clear(StageArea area) {
        area.Bolbs.clear();
    }

    // 将暂存区转化为file存储起来
    public static void convertToFile(StageArea area) {
        writeObject(INDEX, area);
    }

    // 将Blob对象加到暂存区里然后再将暂存区对象转化为文件
    public static void addToIndex(String filename) {
        File file = join(CWD, filename);
        if (!file.exists()) {
            exit("File does not exist.");
        }
        StageArea area = readObject(INDEX, StageArea.class);
        area.Bolbs.put(filename, new Blob(file));
        convertToFile(area);
    }
}
