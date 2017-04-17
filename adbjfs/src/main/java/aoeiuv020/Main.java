package aoeiuv020;
import java.util.*;
import java.io.*;
import aoeiuv020.fuse.*;
/**
 * Created by AoEiuV020 on 2017/04/16.
 */
public class Main{
    public static void main(String[] args)throws Exception{
		if (args.length != 1) {
			System.err.println("Usage: adbjfs <MountPoint>");
			System.exit(1);
		}
        File mountPoint=new File(args[0]);
        AdbFS adbFS=new AdbFS();
        adbFS.mount(mountPoint,false);
    }
}

