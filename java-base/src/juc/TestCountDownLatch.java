package juc;

import java.util.concurrent.CountDownLatch;

/**
 * @author: wensw
 * @description: 闭锁 ：在完成某些运算时，只有其他所有的线程的运算全部完成，当前运算才继续执行
 */
public class TestCountDownLatch {
    public static void main(String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(5);
        CountDownLatchWrapper countDownLatchWrapper = new CountDownLatchWrapper(countDownLatch);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            new Thread(countDownLatchWrapper).start();
        }
        try {
            countDownLatch.await();
        } catch (Exception ex) {

        }
        System.out.println("耗费时间：" + (System.currentTimeMillis() - start));
    }

}

class CountDownLatchWrapper implements Runnable {

    private CountDownLatch countDownLatch;

    public CountDownLatchWrapper(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }


    @Override
    public void run() {
        synchronized (this) {
            try {
                for (int i = 0; i < 100000; i++) {
                    if (i % 2 == 0) {
                        System.out.println(i);
                    }
                }
            } finally {
                countDownLatch.countDown();
            }
        }
    }

}