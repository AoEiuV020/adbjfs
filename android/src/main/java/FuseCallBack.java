import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

/**
 * 所有fuse回调方法类的基类，
 * Created by aoeiuv on 17-4-19.
 */
class FuseCallBack {
    static final ObjectOutputStream output;
    static final ObjectInputStream input;

    static {
        System.setProperty("java.io.tmpdir", "/data/local/tmp");
        ObjectOutputStream _output = null;
        ObjectInputStream _input = null;
        try {
            _output = new ObjectOutputStream(System.out);
            _input = new ObjectInputStream(System.in);
        } catch (IOException e) {
            //TODO: 异常处理，
            e.printStackTrace(System.err);
            byte[] buf = new byte[200];
            try {
                //noinspection ResultOfMethodCallIgnored
                System.in.read(buf);
            } catch (IOException e1) {
                e1.printStackTrace(System.err);
            }
            System.err.println(Arrays.toString(buf));
        }
        output = _output;
        input = _input;
    }
}
