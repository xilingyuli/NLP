import java.io.File;
import java.util.HashMap;

/**
 * Created by xilingyuli on 2017/6/2.
 */
public class WordSegmentation {
    static HashMap<String,Integer> map;
    static int MAX_LENGTH = 7;
    public static void init(){
        map = new HashMap<>();
        String str = FileUtil.readFile(new File("D:\\Documents\\GitHub\\NLP\\out\\production\\NLP\\SogouLabDic.dic"));
        String[] lines = str.split("\\n");
        for(String line : lines){
            String[] arr = line.split("\\t");
            map.put(arr[0],Integer.parseInt(arr[1]));
        }
        System.out.print("");
    }
    public static String[] divide(String str){
        int[][] arr = new int[str.length()][str.length()];
        for(int i=0;i<str.length();i++){
            for(int j=0;j<MAX_LENGTH&&i+j<str.length();j++){
                if(map.containsKey(str.substring(i,i+j)))
                    arr[i][i+j] = map.get(str.substring(i,i+j));
            }
        }
        return null;
    }
}
