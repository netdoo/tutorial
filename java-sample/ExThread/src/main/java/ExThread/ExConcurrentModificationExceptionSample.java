package ExThread;


import java.util.ArrayList;
import java.util.List;

public class ExConcurrentModificationExceptionSample {
    public static void main(String[] args) throws Exception {

        List<String> dataList = new ArrayList<String>();
        dataList.add("mbc");
        dataList.add("sbs");
        dataList.add("ebs");
        dataList.add("tvn");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    dataList.add("kbs");
                    dataList.add("jtbc");
                } catch (Exception e) {
                }
            }
        }).start();

        /// 데이터를 조회하는 동안, 또 다른 스레드에서 데이터가 추가되기 때문에,
        /// ConcurrentModificationException 이 발생함.
        for (String data : dataList) {
            System.out.println(data);
            Thread.sleep(100);
        }
    }
}
