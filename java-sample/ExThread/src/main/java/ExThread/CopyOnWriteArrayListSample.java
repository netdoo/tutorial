package ExThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListSample {
    public static void main(String[] args) throws Exception {

        List<String> dataList = new CopyOnWriteArrayList<>(new ArrayList<String>());

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
                } catch (Exception e) {}
            }
        }).start();

        /// 데이터를 조회하는 동안, 또 다른 스레드에서 데이터가 추가되더라도,
        /// ConcurrentModificationException 이 발생하지는 않지만, 추가된 데이터를
        /// 확인하기 위해서는, 다시한번 iterate를 수행해야 함.
        for (String data : dataList) {
            System.out.println(data);
            Thread.sleep(100);
        }

        System.out.println("==========================================");

        for (String data : dataList) {
            System.out.println(data);
            Thread.sleep(100);
        }
    }
}
