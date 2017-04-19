package aoeiuv020.util;

/**
 * 安卓端的常量，
 * Created by aoeiuv on 17-4-18.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class AdbContent {
    public static final String ANDROID_TMP_DIR = "/data/local/tmp";
    public static final String CLASS_NAME_HELLO = "Hello";
    public static final String CLASS_NAME_GETATTR = "Getattr";
    public static final String CLASS_NAME_READDIR = "Readdir";
    public static final String ANDROID_JAR_NAME = "adbjfs-android.jar";
    public static final String ANDROID_JAR = ANDROID_TMP_DIR + "/" + ANDROID_JAR_NAME;
}
