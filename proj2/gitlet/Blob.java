package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

/**
 *  Todo: 将给定文件路径的文件内容存储在Blob中 ok！
 *  Todo: 根据文件的内容来获取哈希值 ok!
 *  Todo: 将Blob序列化为文件 ok!
 *  Todo: 将Blob哈希得到的哈希码作为文件名字存储到.gitlet/objects/中 ok!
 */
public class Blob implements Serializable {
    private final String sourceFilePath;
    private byte[] contents;
    private String id;

    public Blob(File file) {
        sourceFilePath = file.getPath();
        contents = readContents(file);
        id = getId();
    }

    public String getId() {
        return sha1((Object) contents);
    }

    public void StoreBlob() {
        writeObject(join(OBJECT_DIR, id), this);
    }

}
