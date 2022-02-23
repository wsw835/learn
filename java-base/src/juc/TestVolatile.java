package juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: wensw
 * @description: 验证线程间共享变量的可见性
 * 1、volatile 修饰共享变量： 当多个线程操作共享数据时，可以保证内存中的数据可见，相较于synchronized是一种较为轻量级的同步策略。
 *   缺点：
 *     （1）不能保证变量的原子性
 *     （2）不具备互斥性
 *
 * 2、synchronized 加同步锁 （重量级锁）
 *
 * 3、ReentrantLock 可重入锁 （轻量级锁）
 *
 */
public class TestVolatile implements Runnable{

    private /*volatile*/ boolean flag = false;

    @Override
    public void run() {
        try {
            Thread.sleep(200);
            flag = true;
            System.out.println("当前flag:"+flag);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public static void main(String[] args) {
        TestVolatile testVolatile = new TestVolatile();
       new Thread(testVolatile).start();
        ReentrantLock lock = new ReentrantLock();
        while(true){
            // synchronized (testVolatile) {
            lock.lock();
            try {
                if (testVolatile.flag) {
                    System.out.println("当前标识为true");
                    break;
                }
                // }
            }finally {
                lock.unlock();
            }
        }

    }
}
