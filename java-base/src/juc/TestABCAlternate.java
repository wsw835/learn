package juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: wensw
 * @description: 线程按序交替
 * 编写一个程序，开启三个线程，这三个线程分别为A,B,C,每个线程将自己的ID再屏幕上个打印10次，要求输出的结果要按顺序显示
 * 例子： ABCABCABC
 */
public class TestABCAlternate {

    public static void main(String[] args) {
        Alternate alternate = new Alternate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 10; i++) {
                    alternate.loopA(i);
                }
            }
        }, "A").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 10; i++) {
                    alternate.loopB(i);
                }
            }
        }, "B").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 10; i++) {
                    alternate.loopC(i);
                    System.out.println("-----------------------");
                }
            }
        }, "C").start();
    }

}

class Alternate {

    /**
     * 标识最先执行的下标
     */
    private int firstFlag = 1;

    private Lock lock = new ReentrantLock();
    private Condition aCondition = lock.newCondition();
    private Condition bCondition = lock.newCondition();
    private Condition cCondition = lock.newCondition();

    public void loopA(int loopTimes) {
        lock.lock();
        try {
            if (firstFlag != 1) {
                aCondition.await();
            }
            for (int i = 0; i < 1; i++) {
                System.out.println(Thread.currentThread().getName() + " : " + "" + i + " : " + loopTimes + "\t");
            }
            firstFlag = 2;
            bCondition.signal();

        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }

    public void loopB(int loopTimes) {
        lock.lock();
        try {
            if (firstFlag != 2) {
                bCondition.await();
            }
            for (int i = 0; i < 1; i++) {
                System.out.println(Thread.currentThread().getName() + " : " + "" + i + " : " + loopTimes + "\t");
            }
            firstFlag = 3;
            cCondition.signal();

        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }

    public void loopC(int loopTimes) {
        lock.lock();
        try {
            if (firstFlag != 3) {
                cCondition.await();
            }
            for (int i = 0; i < 1; i++) {
                System.out.println(Thread.currentThread().getName() + " : " + "" + i + " : " + loopTimes + "\t");
            }
            firstFlag = 1;
            aCondition.signal();
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }

}