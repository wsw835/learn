package juc;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: wensw
 * @description: 读写锁
 * 写写、读写 ：需要互斥
 * 读读 ：无需互斥
 */
public class TestReadWriteLock {


    public static void main(String[] args) {
        ReadWrite readWrite = new ReadWrite();
        new Thread(new Runnable() {
            @Override
            public void run() {
                readWrite.write(10);
            }
        }, "WRITE").start();

        for (int i = 0; i < 20; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    readWrite.read();
                }
            }).start();
        }

    }

}

class ReadWrite {

    private int source = 0;

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void read() {
        readWriteLock.readLock().lock();
        try {
            System.out.println("当前线程" + Thread.currentThread().getName() + "读到资源：" + source);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void write(int newSource) {
        readWriteLock.writeLock().lock();
        try {
            System.out.println("当前线程" + Thread.currentThread().getName() + "更新资源：" + newSource);
            this.source = newSource;
        } finally {
            readWriteLock.writeLock().unlock();
        }

    }

}


