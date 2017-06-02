package struct;

import java.io.Serializable;

/**
 * c结构stat,
 * Created by aoeiuv on 17-4-19.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Stat implements Serializable {
    private static final long serialVersionUID = 4764579313740603413L;
    public Long st_dev;
    public Long st_ino;
    public Long st_mode;
    public Long st_nlink;
    public Long st_uid;
    public Long st_gid;
    public Long st_rdev;
    public Long st_atime;
    public Long st_mtime;
    public Long st_ctime;
    public Long st_size;
    public Long st_blocks;
    public Long st_blksize;
}
