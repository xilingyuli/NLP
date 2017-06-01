import java.io.*;

/**
 * Created by xilingyuli on 2017/6/2.
 */
public class FileUtil {
    public static String readFile(File file){
        String str = "";
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            BufferedInputStream bin = new BufferedInputStream(in);
            int p = (bin.read() << 8) + bin.read();
            bin.close();
            in.close();
            in = new FileInputStream(file);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            in.close();
            String code = null;
            switch (p) {
                case 0xefbb:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                case 0x5c75:
                    code = "ASCII" ;
                    break ;
                default:
                    code = "GBK";
            }
            return new String(buffer,code);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
