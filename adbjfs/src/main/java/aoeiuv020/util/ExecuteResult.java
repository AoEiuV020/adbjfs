package aoeiuv020.util;

import java.io.ByteArrayOutputStream;

/**
 * adb相关工具方法的返回类型，
 */
@SuppressWarnings("WeakerAccess")
public class ExecuteResult {
    public ByteArrayOutputStream out;
    public ByteArrayOutputStream err;
    public Process process;

    ExecuteResult() {
    }
}