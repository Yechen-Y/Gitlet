package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import static gitlet.Commit.preCommit;
import static gitlet.Repository.*;
import static gitlet.Utils.*;
import static gitlet.MyUtils.*;

public class StageArea implements Serializable {
    /**
     * Todo: 应该有一个名称对应一个Blob 用HashMap存储 ok!
     * Todo: StageArea应该存储在.gitlet/index 中,ok! 当commit后应该清除index中的内容 ok!
     * Todo: 每次改变暂存区都应该保存起来 使用writecontents? ok!
     * Todo: 解决边缘情况 1.add失败的情况 没有该文件 ok!
     * Todo: 2.即将添加到暂存区的文件和上一个commit的文件相同 ok!
     * Todo: 再添加一个map用来存储删除文件, 这里的逻辑是如果上一个commit的tracked中存在该文件
     *       则将它添加到rm的map中，然后在CWD中删除该文件，commit的时候和正常一样（或许我们应该改变
     *       一下commit的逻辑，还是正常复制，然后检查rm的列表，然后在tracked的列表中寻找rm中列表中的
     *       每一个文件 然后删除）
     * Todo: 简单的取消暂存可以直接把Blobs中的键值对取消 取消跟踪则要在新的commit中不再跟踪，
     *       按照之前的提交模式，需要重写一些步骤，比如说设置tracked时需要考虑暂存区中的rmBlobs
     */
    public HashMap<String, Blob> Bolbs;
    public HashSet<String> rmBlobs;
    public HashMap<String, Blob> trackedFile;


    public StageArea() {
        Bolbs = new HashMap<>();
        rmBlobs = new HashSet<>();
        trackedFile = new HashMap<>();
        mkFile(INDEX);
        convertToFile(this);
    }

    public static void clear(StageArea area) {
        area.Bolbs.clear();
    }

    //Todo: 报错如果改文件既没有暂存也没有跟踪 则报错 ok!
    //Todo: 如果文件暂存了则取消暂存 ok!
    //Todo: 如果文件跟踪了 则将其添加到rmBlobs中 ok!
    public static void rmFile(StageArea area, String fileName) {
        //判断是否有暂存
        if (area.Bolbs.containsKey(fileName)) {
            area.Bolbs.remove(fileName);
        }
        //Determine if there is tracking
        setTracked(area);
        if (area.trackedFile.containsKey(fileName)) {
            area.rmBlobs.add(fileName);
        }
        exit("No reason to remove the file.");
    }

    //设置暂存区中跟踪的文件 为上一个commit的tracked中的文件
    public static void setTracked(StageArea area) {
        Commit commit = preCommit();
        area.trackedFile = commit.getTracked();
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
