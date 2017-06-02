import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Created by xilingyuli on 2017/6/2.
 */
public class NameIdentification {
    static HashMap<Character,Float> firstNameMap = new HashMap<>();
    static HashMap<Character,Float> lastNameMap = new HashMap<>();
    static HashMap<String,Float> names;
    public static void init(){
        //姓氏
        String str = FileUtil.readFile(new File("Chinese_Family_Name.csv"));
        String[] lines = str.split("\\r\\n");
        for(String l : lines)
        {
            String[] arr = l.split(",");
            firstNameMap.put(arr[0].charAt(0),Float.parseFloat(arr[2]));
        }
        //名字
        /*str = FileUtil.readFile(new File("Chinese_Names_Corpus.txt"));  //原始数据
        lines = str.split("\\r\\n");
        for(String l : lines) {
            for(int i=1;i<l.length();i++)
                lastNameMap.put(l.charAt(i), lastNameMap.getOrDefault(l.charAt(i), 0) + 1);
        }*/
        str = FileUtil.readFile(new File("Chinese_Last_Name.csv"));  //处理后数据
        lines = str.split("\\r\\n");
        for(String l : lines)
        {
            String[] arr = l.split(",");
            lastNameMap.put(arr[0].charAt(0),Float.parseFloat(arr[2]));
        }
    }
    public static void preDeal(){
        names = new HashMap<>();
    }
    public static void nameIdentify(String str){
        if(str.length()<2||str.length()>4||WordSegmentation.map.containsKey(str))
            return;

        //姓概率
        float p1 = firstNameMap.getOrDefault(str.charAt(0),0f);
        if(p1<1)
            return;

        //名概率
        float p2 = 1;
        for(int i=1;i<str.length();i++)
            p2*=lastNameMap.getOrDefault(str.charAt(i),1f);
        p2 = (float) Math.pow(p2,1.0/(str.length()-1));
        if(p2<10)
            return;

        //整体概率
        float p = (float) (Math.pow(p1,0.6)*Math.pow(p2,0.4));
        if(str.length()>3)  //减小长度超过三的词权重
            p/=2;
        if(p<5)
            return;

        if(!names.containsKey(str)) {
            HashSet<String> remove = new HashSet<>();  //需去掉的冗余词
            for(String s:names.keySet()) {
                if (s.contains(str) && names.get(s) + 1 < p)
                    remove.add(s);
                else if(str.contains(s) && names.get(s) > p + 1)
                    return;
            }
            for (String t:remove)
                names.remove(t);
            names.put(str, p);
        }
    }
    public static HashSet<String> endDeal(){
        ArrayList<String> list = new ArrayList<>(names.keySet());
        HashSet<String> res = new HashSet<>(names.keySet());
        Collections.sort(list);
        for(int i=0;i<list.size()-1;i++)
            if(list.get(i+1).contains(list.get(i)))
                res.remove(list.get(i));
        return res;
    }
}
