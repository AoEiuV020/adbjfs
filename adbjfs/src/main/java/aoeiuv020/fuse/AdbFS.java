package aoeiuv020.fuse;

import aoeiuv020.util.AdbContent;
import aoeiuv020.util.AdbUtils;
import aoeiuv020.util.ExecuteResult;
import net.fusejna.DirectoryFiller;
import net.fusejna.ErrorCodes;
import net.fusejna.StructStat;
import net.fusejna.StructStat.StatWrapper;
import net.fusejna.util.FuseFilesystemAdapterFull;
import struct.Stat;

import java.io.*;

/**
 * 实现fuse相关方法的类，
 * Created by AoEiuV020 on 2017/04/16.
 */
public class AdbFS extends FuseFilesystemAdapterFull {
    public AdbFS() {
        super.log(true);
    }

    @Override
    public int getattr(final String path, final StatWrapper statWrapper) {
        ExecuteResult result;
        try {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(buf);
            output.writeUTF(path);
            output.close();
            result = AdbUtils.adbDalvikvm(AdbContent.ANDROID_JAR, AdbContent.CLASS_NAME_GETATTR, new ByteArrayInputStream(buf.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
            return -ErrorCodes.ENOENT();
        }
        int ret;
        Stat stat;
        try {
            ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(result.out.toByteArray()));
            ret = input.readInt();
            stat = (Stat) input.readObject();
            input.close();
        } catch (IOException | ClassNotFoundException e) {
            //TODO: 处理异常，
            e.printStackTrace();
            return -ErrorCodes.ENOENT();
        }
        setTo(stat, statWrapper);
        return ret;
    }

    private void setTo(Stat stat, StructStat.StatWrapper statWrapper) {
        if (stat.st_mode != null) {
            statWrapper.mode(stat.st_mode);
        }
        if (stat.st_size != null) {
            statWrapper.size(stat.st_size);
        }
        if (stat.st_atime != null) {
            statWrapper.atime(stat.st_atime);
        }
        if (stat.st_ctime != null) {
            statWrapper.ctime(stat.st_ctime);
        }
        if (stat.st_mtime != null) {
            statWrapper.mtime(stat.st_mtime);
        }
        if (stat.st_blocks != null) {
            statWrapper.blocks(stat.st_blocks);
        }
        if (stat.st_dev != null) {
            statWrapper.dev(stat.st_dev);
        }
        if (stat.st_blksize != null) {
            statWrapper.blksize(stat.st_blksize);
        }
        if (stat.st_gid != null) {
            statWrapper.gid(stat.st_gid);
        }
        if (stat.st_uid != null) {
            statWrapper.uid(stat.st_uid);
        }
        if (stat.st_ino != null) {
            statWrapper.ino(stat.st_ino);
        }
        if (stat.st_nlink != null) {
            statWrapper.nlink(stat.st_nlink);
        }
        if (stat.st_rdev != null) {
            statWrapper.rdev(stat.st_rdev);
        }
    }

    @Override
    public int readdir(final String path, final DirectoryFiller filler) {
        ExecuteResult result;
        try {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(buf);
            output.writeUTF(path);
            output.close();
            result = AdbUtils.adbDalvikvm(AdbContent.ANDROID_JAR, AdbContent.CLASS_NAME_GETATTR, new ByteArrayInputStream(buf.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
            return -ErrorCodes.ENOENT();
        }
        String[] files;
        try {
            ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(result.out.toByteArray()));
            files = (String[]) input.readObject();
            input.close();
        } catch (IOException | ClassNotFoundException e) {
            //TODO: 处理异常，
            e.printStackTrace();
            return -ErrorCodes.ENOENT();
        }
        filler.add(files);
        return 0;
    }
}

