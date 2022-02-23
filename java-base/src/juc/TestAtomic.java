package juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: wensw
 * @description:
 * （1）i++ 原子性问题分析：
 *       i++ 的操作实际分为三个步骤： 读 - 改 - 写
 *       int i =1;
 *       i = i++; // i = 1  => int temp = i;
 *                               i = i+1;
 *                               i = temp;
 *
 *  (2)原子变量：jdk1.5后java.util.concurrent.atomic 包下面提供了常用的原子变量：
 *      1、volatile 保证内存可见性
 *      2、CAS(compare and swap)算法保证数据的原子性
 *         CAS算法是硬件对于并发操作共享数据的支持
 *         CAS包含了三个操作数：
 *           内存值：V
 *           预估值：A
 *           更新值：B
 *          当且仅当 V==A时，V = B，否则将不做任何操作
 *
 *
 */
public class TestAtomic implements Runnable{

    //private int autoNum = 0;

    private AtomicInteger autoNum = new AtomicInteger();

    public static void main(String[] args) {
        TestAtomic testAtomic = new TestAtomic();
        for (int i = 0; i < 10; i++) {
            new Thread(testAtomic).start();
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
            System.out.println(getAutoNum());
        }catch (Exception e){

        }
    }

    public int getAutoNum() {
        return autoNum.getAndIncrement();
    }
}
