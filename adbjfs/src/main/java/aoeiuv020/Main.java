package aoeiuv020;

import aoeiuv020.fuse.AdbFS;
import aoeiuv020.util.AdbContent;
import aoeiuv020.util.AdbUtils;

import java.io.File;

/**
 * pc端运行的主类，前台启动fuse,
 * Created by AoEiuV020 on 2017/04/16.
 */
public class Main {
    private static final String androidJarName = "adbjfs-android.jar";
    private static final String androidJar = AdbContent.ANDROID_TMP_DIR + "/" + "adbjfs-android.jar";

    public static void main(String[] args) throws Exception {
        AdbUtils.adbWrite(Main.class.getClassLoader().getResourceAsStream(androidJarName), androidJar);
        System.out.println(AdbUtils.adbDalvikvm(androidJar, AdbContent.CLASS_NAME_HELLO).out.toString());
        if (args.length != 1) {
            System.err.println("Usage: adbjfs <MountPoint>");
            System.exit(1);
        }
        File mountPoint=new File(args[0]);
        if (!mountPoint.exists()) {
            System.err.println("adbjfs: mount point " + mountPoint + " does not exist");
        }
        AdbFS adbFS=new AdbFS();
        adbFS.mount(mountPoint,false);
    }
}

