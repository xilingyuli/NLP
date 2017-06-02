import java.io.File;
import java.util.*;

/**
 * Created by xilingyuli on 2017/6/2.
 */

public class WordSegmentation {
    public static HashMap<String,Integer> map;  //词表
    static int MAX_LENGTH = 7;  //最大分词长度
    static int INF = -1000000000;

    /**
     * 载入分词词库
     */
    public static void init(){
        map = new HashMap<>();
        String str = FileUtil.readFile(new File("SogouLabDic.dic"));
        String[] lines = str.split("\\n");
        for(String line : lines){
            String[] arr = line.split("\\t");
            map.put(arr[0],Integer.parseInt(arr[1]));
        }
        /*map = new HashMap<>();
        String str = FileUtil.readFile(new File("CorpusWordlist.csv"));
        String[] lines = str.split("\\n");
        for(String line : lines){
            String[] arr = line.split(",");
            map.put(arr[1],Integer.parseInt(arr[2]));
        }*/
    }

    /**
     * 对单句进行分词
     */
    public static String[] divide(String str){
        int n = str.length()+1;
        //带权有向图
        int[][] arr = new int[n][n];
        for(int[] i: arr)
            Arrays.fill(i,INF);
        for(int i=0;i<n;i++){
            for(int j=i;j<n;j++)
                arr[i][j] = 0;
        }
        for(int i=0;i<n;i++){
            for(int j=0;j<MAX_LENGTH&&i+j<n;j++){
                if(map.containsKey(str.substring(i,i+j)))
                    arr[i][i+j] = map.get(str.substring(i,i+j));
            }
        }
        //求单源最大权路径
        boolean[] used = new boolean[n];
        int[] path = new int[n];
        int[] distance = Arrays.copyOf(arr[0],arr[0].length);
        used[0] = true;
        for(int i=0;i<n-1;i++){
            int temp = -1;
            for(int j=0;j<n;j++){
                if(used[j])
                    continue;
                if(temp==-1)
                    temp = j;
                else if(distance[j]>distance[temp])
                    temp = j;
            }
            used[temp] = true;
            for(int j=0;j<n;j++){
                if(!used[j]&&distance[j]<distance[temp]+arr[temp][j]){
                    distance[j] = distance[temp]+arr[temp][j];
                    path[j] = temp;
                }
            }
        }
        //将路径结果转换成分词结果
        ArrayList<String> list = new ArrayList<>();
        int index = n-1;
        while (index!=0) {
            list.add(str.substring(path[index],index));
            index = path[index];
        }
        Collections.reverse(list);
        return list.toArray(new String[0]);
    }

    /**
     * 对单句进行分词和人名标注并以斜线分隔
     */
    public static String divideResult(String str){
        String res = "";
        String[] words = divide(str);
        for(String w:words) {
            NameIdentification.nameIdentify(w);
            res += w + "/";
        }
        return res.substring(0,res.length()-1);
    }

    /**
     * 对文章进行分词和人名标注并以斜线分隔
     */
    public static String divideArticle(String str){
        NameIdentification.preDeal();
        String[] lines = str.split("\\s*[!:;,.?！…；：”“。，、？ 　\\s]+\\s*");
        String res = "";
        for(String l:lines) {
            if(l.isEmpty())
                continue;
            res += divideResult(l) + "\n";
        }
        //人名标注
        HashSet<String> names = NameIdentification.endDeal();
        for(String name:names)
            res = res.replace(name,"$"+name+"$");
        return res;
    }

    public static void run(String path){
        //获取要处理的文本文件
        File file = new File(path);
        File[] files;
        if(file.isDirectory())
            files = file.listFiles();
        else
            files = new File[]{file};

        init();
        NameIdentification.init();
        for(File f:files){
            if(f.isFile()&&f.getName().endsWith(".txt")){
                String str = FileUtil.readFile(f);
                System.out.println(divideArticle(str));
            }
        }
    }

}
