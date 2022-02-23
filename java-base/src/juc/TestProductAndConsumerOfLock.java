package juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: wensw
 * @description: 生产者、消费者问题：利用显示锁Lock - >ReentrantLock
 */
public class TestProductAndConsumerOfLock {

    public static void main(String[] args) {
        ClerkOfLock clerk = new ClerkOfLock();
        ProductOfLock product = new ProductOfLock(clerk);
        ConsumerOfLock consumer = new ConsumerOfLock(clerk);
        new Thread(product, "生产者1").start();
        new Thread(consumer, "消费者1").start();
        new Thread(product, "生产者2").start();
        new Thread(consumer, "消费者2").start();
    }

}

class ClerkOfLock {

    private int goods;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    /**
     * 进货
     */
    public void get() {
        lock.lock();
        try {
            while (goods <= 0) {
                System.out.println("进货：" + ++goods);
                condition.signalAll();
            }
            System.out.println("货物已满，无需进货！");
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } finally {
            lock.unlock();
        }
    }

    public void sale() {
        lock.lock();
        try {
            while (goods <= 0) {
                System.out.println("缺货！");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("售出货物：" + --goods);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}

class ProductOfLock implements Runnable {

    private ClerkOfLock clerk;

    public ProductOfLock(ClerkOfLock clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            clerk.get();
        }
    }
}

class ConsumerOfLock implements Runnable {

    private ClerkOfLock clerk;

    public ConsumerOfLock(ClerkOfLock clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            clerk.sale();
        }
    }
}
