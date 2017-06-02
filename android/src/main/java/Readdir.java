import java.io.File;

/**
 * 安卓端实现fuse必需的readdir方法，
 * Created by AoEiuV020 on 2017/04/17.
 */
public class Readdir extends FuseCallBack {
    public static void main(String[] args) throws Exception {
        String path = input.readUTF();
        input.close();
        String[] files = new File(path).list();
        output.writeObject(files);
        output.close();
    }
}

