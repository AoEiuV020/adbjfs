package aoeiuv020.fuse;
import java.util.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.logging.Logger;
import java.util.logging.Level;
import net.fusejna.DirectoryFiller;
import net.fusejna.ErrorCodes;
import net.fusejna.FuseException;
import net.fusejna.StructFuseFileInfo.FileInfoWrapper;
import net.fusejna.StructStat.StatWrapper;
import net.fusejna.types.TypeMode.NodeType;
import net.fusejna.util.FuseFilesystemAdapterFull;
/**
 * Created by AoEiuV020 on 2017/04/16.
 */
public class AdbFS extends FuseFilesystemAdapterFull{
    private final String filename="/hello.txt";
    private final String contents="Hello World!\n";
    public AdbFS(){
        super.log(true);
    }
	@Override
	public int getattr(final String path, final StatWrapper stat)
	{
		if (path.equals(File.separator)) { // Root directory
			stat.setMode(NodeType.DIRECTORY);
			return 0;
		}
		if (path.equals(filename)) { // hello.txt
			stat.setMode(NodeType.FILE).size(contents.length());
			return 0;
		}
		return -ErrorCodes.ENOENT();
	}

	@Override
	public int read(final String path, final ByteBuffer buffer, final long size, final long offset, final FileInfoWrapper info)
	{
		final String s = contents.substring((int) offset,
				(int) Math.max(offset, Math.min(contents.length(), offset + size)));
		buffer.put(s.getBytes());
		return s.getBytes().length;
	}

	@Override
	public int readdir(final String path, final DirectoryFiller filler)
	{
		filler.add(filename);
		return 0;
	}
}

