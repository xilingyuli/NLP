import java.io.File;
import java.util.*;

/**
 * Created by xilingyuli on 2017/6/1.
 */
public class WordFrequencyStatistic {
    static int sum;
    static HashMap<Character,Integer> map;
    static double[][] top;
    public static void init()
    {
        sum = 0;
        map = new HashMap<>();
        top = new double[][]{{1,0},{20,0},{100,0},{600,0},{2000,0},{3000,0},{6000,0}};
    }
    public static void statistic(String str){
        char[] arr = str.toCharArray();
        for(char ch: arr){
            if(ch < '\u4e00' || ch > '\u9fa5')
                continue;
            map.put(ch,map.getOrDefault(ch,0)+1);
            sum++;
        }
    }
    public static void calResult(){
        double temp = Math.log(2);
        double h = 0;
        ArrayList<Object[]> res = new ArrayList<>();
        for(char key: map.keySet()){
            double val = map.get(key)*1.0/sum;
            h -= val*Math.log(val);
            res.add(new Object[]{key,val});
        }
        Collections.sort(res, (o1, o2) -> {
            if((double)o1[1]<(double)o2[1])
                return 1;
            else
                return -1;
        });
        for(int i=0;i<res.size();i++){
            if(i<100)
                System.out.println(res.get(i)[0]+" "+res.get(i)[1]);
            for(double[] item: top){
                if(i<item[0])
                    item[1]+=(double)res.get(i)[1];
            }
        }
        System.out.println();
        for(double[] item: top)
            System.out.println("top"+((int)item[0])+":"+item[1]);
        System.out.println();
        System.out.println(h/temp);
        System.out.println();
    }
    public static void run(String path){
        File file = new File(path);
        File[] files;
        if(file.isDirectory())
            files = file.listFiles();
        else
            files = new File[]{file};

        init();
        for(File f:files){
            if(f.isFile()&&f.getName().endsWith(".txt")){
                String str = FileUtil.readFile(f);
                statistic(str);
            }
        }
        calResult();
    }
}
