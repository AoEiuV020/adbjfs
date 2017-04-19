package aoeiuv020.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * adb 相关的工具类，
 * Created by AoEiuV020 on 2017/04/17.
 */
@SuppressWarnings({"WeakerAccess", "SameParameterValue", "unused"})
public class AdbUtils {
    private static final Logger logger = LoggerFactory.getLogger(AdbUtils.class);
    private static final ByteArrayInputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);

    public static ExecuteResult execute(String cmd) throws IOException {
        return execute(emptyInputStream, cmd.split(" "));
    }

    static ExecuteResult execute(InputStream input, String[] cmdArray) throws IOException {
        logger.debug("execute {}", (Object) cmdArray);
        ExecuteResult result = new ExecuteResult();
        result.out = new ByteArrayOutputStream();
        result.err = new ByteArrayOutputStream();
        result.process = Runtime.getRuntime().exec(cmdArray);
        //TODO: 用select之类的实现同时读写三个输入输出，
        copy(input, result.process.getOutputStream());
        input.close();
        result.process.getOutputStream().close();
        copy(result.process.getInputStream(), result.out);
        result.process.getInputStream().close();
        copy(result.process.getErrorStream(), result.err);
        result.process.getErrorStream().close();
        logger.trace("process isAlive {}", result.process.isAlive());
        //TODO: 设置等待超时，或者在上面读时设置超时，
        try {
            result.process.waitFor();
        } catch (InterruptedException ignore) {
        }
        logger.debug("process exitValue {}", result.process.exitValue());
        if (result.process.exitValue() != 0) {
            if (result.out.size() < 1000)
                logger.error("execute error out: {}", result.out.toString());
            else
                logger.error("execute error out len: {}", result.out.size());
            if (result.err.size() < 2000)
                logger.error("execute error err: {}", result.err.toString());
            else
                logger.error("execute error err len: {}", result.err.size());
        }
        return result;
    }

    public static ExecuteResult adbShell(String cmd) throws IOException {
        return adbShell(emptyInputStream, cmd.split(" "));
    }

    public static ExecuteResult adbShell(InputStream input, String... cmdArray) throws IOException {
        logger.debug("adbShell {}", (Object) cmdArray);
        //TODO: 判断adb状态，
        //TODO: adb shell cmd args... 后面的参数要转义，暂时不使用参数，
        String[] actuallyCmdArray = new String[2 + cmdArray.length];
        actuallyCmdArray[0] = "adb";
        actuallyCmdArray[1] = "shell";
        System.arraycopy(cmdArray, 0, actuallyCmdArray, 2, cmdArray.length);
        return execute(input, actuallyCmdArray);
    }

    public static ExecuteResult adbPush(File from, String to) throws IOException {
        logger.debug("adbPush <{},{}>", from, to);
        //TODO: from有特殊情况字符的情况，比如引号"，待验证，
        //TODO: 判断文件to是否存在，可以省个push,
        String[] cmdArray = new String[4];
        cmdArray[0] = "adb";
        cmdArray[1] = "push";
        cmdArray[2] = from.getAbsolutePath();
        cmdArray[3] = to;
        return execute(emptyInputStream, cmdArray);
    }

    public static ExecuteResult adbWrite(InputStream input, String to) throws IOException {
        logger.debug("adbWrite {}", to);
        String[] cmds = new String[3];
        cmds[0] = "cat";
        cmds[1] = ">";
        cmds[2] = to;
        return adbShell(input, cmds);
    }

    public static ExecuteResult adbDalvikvm(String classpath, String mainClassName, String... args) throws IOException {
        return adbDalvikvm(classpath, mainClassName, emptyInputStream, args);
    }

    public static ExecuteResult adbDalvikvm(String classpath, String mainClassName, InputStream input, String... args) throws IOException {
        logger.debug("adbDalvikvm {}", mainClassName);
        String[] cmdArray = new String[3 + args.length];
        cmdArray[0] = "CLASSPATH=" + classpath;
        cmdArray[1] = "dalvikvm";
        cmdArray[2] = mainClassName;
        System.arraycopy(args, 0, cmdArray, 3, args.length);
        return adbShell(input, cmdArray);
    }

    @SuppressWarnings("UnusedReturnValue")
    static long copy(InputStream input, OutputStream output) throws IOException {
        long count = 0;
        int len;
        byte[] buf = new byte[4096];
        // adb shell 命令的输出流的available始终为0,
        while ((len = input.read(buf)) > 0) {
            logger.trace("copy #{}", len);
            output.write(buf, 0, len);
            count += len;
        }
        return count;
    }

    /**
     * adb shell 后面的所有参数被当成一个了，有必要转义一下，
     * 比如 adb shell ls "a s"
     * ls a 和 s ，被当成3个参数，
     * 转成 adb shell ls "a\\ s"
     * 就能运正确的 ls "a s"
     * FIXME: 还有各种情况暂不支持，大脑混乱，肯定有问题，但是难搞，
     */
    static String escapeShellCmd(String s) {
        char[] chars = s.toCharArray();
        CharArrayReader reader = new CharArrayReader(chars);
        CharArrayWriter writer = new CharArrayWriter(chars.length);
        try {
            int ch;
            while ((ch = reader.read()) != -1) {
                switch (ch) {
                    case '\'':
                    case '\"':
                    case '\\':
                    case '(':
                    case ')':
                    case '`':
                    case '|':
                    case '&':
                    case ';':
                    case '<':
                    case '>':
                    case '*':
                    case '#':
                    case '%':
                    case '=':
                    case '~':
                        writer.append('\\');
                        writer.append('\\');
                        writer.append((char) ch);
                        break;
                    case '\n':
                        writer.append('\'');
                        writer.append((char) ch);
                        writer.append('\'');
                        break;
                    default:
                        writer.append((char) ch);
                }
            }
        } catch (IOException e) {
            //不可到达，
            logger.error("unknown error", e);
        }
        return s;
    }
}