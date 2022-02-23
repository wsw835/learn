package juc;

/**
 * @author: wensw
 * @description: 线程八锁
 * 1、两个普通同步方法 两个线程 标准打印 -》 one two
 * 2、新增Thread.sleep()给线程A , 标准打印 =》 one two
 * 3、新增普通方法 ，getThree() 打印 ： three one two
 * 4、
 */
public class TestThread8Monitor {

    public static void main(String[] args) {
        Number number = new Number();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    number.getOne();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                number.getTwo();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                number.getThree();
            }
        }).start();
    }

}

class Number {

    public synchronized void getOne() {
        System.out.println("one");
    }

    public synchronized void getTwo() {
        System.out.println("two");
    }

    public void getThree() {
        System.out.println("three");
    }
}
