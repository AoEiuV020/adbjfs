package aoeiuv020;

import aoeiuv020.fuse.AdbFS;
import aoeiuv020.util.AdbContent;
import aoeiuv020.util.AdbUtils;

import java.io.File;
import java.io.IOException;

/**
 * pc端运行的主类，前台启动fuse,
 * Created by AoEiuV020 on 2017/04/16.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        try {
            AdbUtils.adbWrite(Main.class.getClassLoader().getResourceAsStream(AdbContent.ANDROID_JAR_NAME), AdbContent.ANDROID_JAR);
        } catch (IOException e) {
            System.err.println("Error: adb no devices/emulators found ?");
            System.exit(1);
        }
        if (args.length != 1) {
            System.err.println("Usage: adbjfs <MountPoint>");
            System.exit(2);
        }
        File mountPoint = new File(args[0]);
        if (!mountPoint.exists()) {
            System.err.println("adbjfs: mount point " + mountPoint + " does not exist");
        }
        AdbFS adbFS = new AdbFS();
        adbFS.mount(mountPoint, false);
    }
}

