import java.io.*;

/**
 * Created by xilingyuli on 2017/6/1.
 */
public class Main {
    public static void main(String args[]){
        //WordFrequencyStatistic.run("E:/小说");
        WordSegmentation.init();
        WordSegmentation.divide("全世界每年影片产量高达两三千部");
    }

}
