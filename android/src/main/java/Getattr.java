import struct.Stat;

import java.io.File;

/**
 * 安卓端实现fuse必需的getattr方法，
 * Created by AoEiuV020 on 2017/04/17.
 */
@SuppressWarnings({"WeakerAccess", "OctalInteger"})
public class Getattr extends FuseCallBack {
    public static final long S_IFSOCK = 0140000;
    public static final long S_IFLNK = 0120000;
    public static final long S_IFREG = 0100000;
    public static final long S_IFBLK = 0060000;
    public static final long S_IFDIR = 0040000;
    public static final long S_IFCHR = 0020000;
    public static final long S_IFIFO = 0010000;
    public static final long S_ISUID = 0004000;
    public static final long S_ISGID = 0002000;
    public static final long S_ISVTX = 0001000;
    public static final long S_IRUSR = 00400;
    public static final long S_IWUSR = 00200;
    public static final long S_IXUSR = 00100;
    public static final long S_IRGRP = 00040;
    public static final long S_IWGRP = 00020;
    public static final long S_IXGRP = 00010;
    public static final long S_IROTH = 00004;
    public static final long S_IWOTH = 00002;
    public static final long S_IXOTH = 00001;

    public static void main(String[] args) throws Exception {
        String path = input.readUTF();
        input.close();
        File file = new File(path);
        int ret = -2; //errno: 不存在，
        Stat stat = new Stat();
        if (file.exists()) {
            long mode = file.isDirectory() ? S_IFDIR : S_IFREG;
            mode |= file.canRead() ? (S_IRUSR | S_IRGRP | S_IROTH) : 0L;
            mode |= file.canRead() ? (S_IWUSR | S_IWGRP | S_IWOTH) : 0L;
            mode |= file.canExecute() ? (S_IXUSR | S_IXGRP | S_IXOTH) : 0L;
            stat.st_mode = mode;
            ret = 0;
        }
        output.writeInt(ret);
        output.writeObject(stat);
        output.close();
    }
}

