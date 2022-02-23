package juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: wensw
 * @description: 用于解决多线程安全问题的方式：
 * 1、synchronized（隐式锁） -> 同步代码块、同步方法
 * 2、同步锁Lock(jdk1.5后)
 * 注意：Lock是一个显式的锁，需要通过lock()方法上锁，必须通过unlock()方法进行释放锁
 */
public class TestLock {

    public static void main(String[] args) {
        LockWrapper lockWrapper = new LockWrapper();
        new Thread(lockWrapper, "张三").start();
        new Thread(lockWrapper, "李四").start();
        new Thread(lockWrapper, "王五").start();
    }

}

class LockWrapper implements Runnable {

    private int total = 100;

    private Lock lock = new ReentrantLock();

    @Override
    public void run() {
        lock.lock();
        try {
            Thread.sleep(200);
            while (total > 0) {
                System.out.println(Thread.currentThread().getName() + "已使用资源，剩余资源：" + --total);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
