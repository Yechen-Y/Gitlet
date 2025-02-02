package gitlet;

// TODO: any imports you need here


import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.MyUtils.exit;
import static gitlet.MyUtils.mkFile;
import static gitlet.Repository.*;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Yechen
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    //Todolist
    /**
     * Todo: 完成commit应该存储的东西
     *  提交信息、父commit(或者用什么数据结构去存储他呢?)、Date时间、Blob的引用(类似于暂存区) ok!
     * Todo: 每个commit应该有一个id并且存储在object中
     * Todo: 处理失败情况 1.没有提交信息 2.暂存区无Blob
     * Todo: 父commit应该为当前Head指向的branch的commit
     * Todo: trackde 应该先复制前一个commit的tracked，然后再根据当前的暂存区来改变
     * Todo: 设置InitCommit i think its very hard to handle!
     * Todo: 一个commit的父commit可能有多个 所以待修改
     */

    public static HashMap<String, Commit> commitTree = new HashMap<>();
    /** The message of this Commit. */
    private String message;
    private Commit parentCommit;
    private Date date;
    private String id;
    private HashMap<String, Blob> tracked;

    public Commit() {
        this.message = "initial commit";
        date = new Date(0);
        tracked = new HashMap<>();
        id = sha1(message, date.toString());
        parentCommit = null;
        writeCommit();
    }

    public Commit(String message) {
        this.message = message;
        setDate();
        setParentCommit();
        setTracked();
        setId();
        writeCommit();
    }

    // Todo: 将Commit 的id作为文件名存储 ok!
    // Todo: 移动branch指针 将branch文件的内容设置为当前commit的id ok!
    // Todo: 将ID - commit存储为一个map? if need? 应该为brach/Head的任务吗？

    public void writeCommit() {
        File commitFile = new File(OBJECT_DIR, id);
        mkFile(commitFile);
        writeObject(commitFile, this);
        String branchPath = readContentsAsString(HEAD);
        writeContents(new File(branchPath), this.getId());
    }

    public void setDate() {
        date = new Date();
    }

    //Todo: 父commit应该为Head指向的branch指向的commit ok!
    public void setParentCommit() {
        parentCommit = preCommit();
    }

    public static Commit preCommit() {
        String curBranchPath = readContentsAsString(HEAD);
        String preCommitPath = readContentsAsString(new File(curBranchPath));
        return readObject(join(OBJECT_DIR, preCommitPath), Commit.class);
    }

    /**
     * Todo: tracked应该为前一个commit的tracked加上当前暂存区的，设置后应该清空暂存区 ok!
     * Todo: 将tracked中的Blob存储到object中 ok!
     * Todo: 有个问题就是无法判断工作区中删除的文件！
     */
    public void setTracked() {
        StageArea area = readObject(INDEX, StageArea.class);
        tracked = (HashMap<String, Blob>) parentCommit.tracked.clone();
        tracked.putAll(area.Bolbs);
        HashSet<Blob> blobs = new HashSet<>(tracked.values());
        for (Blob e : blobs) {
            e.StoreBlob();
        }
        StageArea.clear(area);
        StageArea.convertToFile(area);
    }

    // Todo: Id由提交信息，时间，父commit的id，跟踪对象决定
    public void setId() {
        id = sha1(message, date.toString(), getTrackedId());
    }

    // Todo: 计算tracked的String ok!
    private String getTrackedId() {
        List<Object> ids = new ArrayList<>();
        HashSet<Blob> blobs = new HashSet<>(tracked.values());
        for (Blob e : blobs) {
            ids.add(e.getId());
        }
        return sha1(ids);
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public Commit getParentCommit() {
        return parentCommit;
    }
    /**
     * Todo: 第一次提交比较不同tracked和parent都应该为null ok!
     * Todo: 设置当前branch文件的内容
     */
    public static void InitCommit() {
        new Commit();
    }

    // 返回commit的tracked hashmap
    public HashMap<String, Blob> getTracked() {
        return tracked;
    }

    /**
     * Todo: 返回特定hashId的commit
     * Todo: 解决一个错误情况 commit不存在
     */
    public static Commit specificCommit(String commitId) {
        Commit preCommit = preCommit();
        return specCommitHelper(commitId, preCommit);
    }

    private static Commit specCommitHelper(String commitId, Commit preCommit) {
        if (preCommit == null) {
            exit("No commit with that id exists.");
        }
        if (preCommit.getId().equals(commitId)) {
            return preCommit;
        } else {
            return specCommitHelper(commitId, preCommit.getParentCommit());
        }
    }
}
