package aoeiuv020.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * 正确连接adb状态下测试，
 * Created by AoEiuV020 on 2017/04/17.
 */
public class AdbUtilsTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void execute() throws Exception {
        byte[] bytes = AdbUtils.execute("echo he llo").out.toByteArray();
        assertArrayEquals("he llo\n".getBytes(), bytes);
    }

    @Test
    public void adbShell() throws Exception {
        int sdkVersion = Integer.parseInt(AdbUtils.adbShell("getprop ro.build.version.sdk").out.toString().trim());
        assertTrue(sdkVersion > 0);
    }

    @Test
    public void adbPush() throws Exception {
        File tmp = folder.newFile();
        String contents = "test adbPush";
        String to = "/data/local/tmp/" + tmp.getName();
        Files.write(Paths.get(tmp.toURI()), contents.getBytes());
        assertEquals(0, AdbUtils.adbPush(tmp, to).process.exitValue());
        assertEquals(0, AdbUtils.adbShell("ls " + to).process.exitValue());
    }

    @Test
    public void adbWrite() throws Exception {
        String contents = "test adbWrite";
        String to = AdbContent.ANDROID_TMP_DIR + "/adbWriteTest.txt";
        assertEquals(0, AdbUtils.adbWrite(new ByteArrayInputStream(contents.getBytes()), to).process.exitValue());
        assertEquals(contents, AdbUtils.adbShell("cat " + to).out.toString());
    }

    @Test
    public void adbDalvikvm() throws Exception {
        String androidJarName = "adbjfs-android.jar";
        String androidJar = AdbContent.ANDROID_TMP_DIR + "/" + androidJarName;
        assertEquals(0, AdbUtils.adbWrite(this.getClass().getClassLoader().getResourceAsStream(androidJarName), androidJar).process.exitValue());
        assertEquals("Hello from android!\n", AdbUtils.adbDalvikvm(androidJar, "Hello").out.toString());
    }
}
