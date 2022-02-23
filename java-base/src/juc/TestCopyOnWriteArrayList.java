package juc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author: wensw
 * @description: CopyOnWriteArrayList /CopyOnWriteArraySet 写入并复制
 * 注意：
 * 添加操作多时，效率低，每次添加时都会进行一次复制，开销非常大。
 * 并发迭代多时效率较高。
 */
public class TestCopyOnWriteArrayList {

    public static void main(String[] args) {
        CopyOnWriteThread thread = new CopyOnWriteThread();
        new Thread(thread).start();
    }

}

class CopyOnWriteThread implements Runnable {

    private static List<String> list = Collections.synchronizedList(new ArrayList<>());

    static {
        list.add("123");
        list.add("456");
    }

    @Override
    public void run() {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
            list.add("add");
        }

    }


}
