package juc;

/**
 * @author: wensw
 * @description: 生产者、消费者问题：
 */
public class TestProductAndConsumerOfSynchronized {

    public static void main(String[] args) {
        Clerk clerk = new Clerk();
        Product product = new Product(clerk);
        Consumer consumer = new Consumer(clerk);
        new Thread(product, "生产者1").start();
        new Thread(consumer, "消费者1").start();
        new Thread(product, "生产者2").start();
        new Thread(consumer, "消费者2").start();
    }

}

class Clerk {

    private int goods;

    /**
     * 进货
     */
    public synchronized void get() {
        if (goods <= 0) {
            System.out.println("进货：" + ++goods);
            notifyAll();
        } else {
            System.out.println("货物已满，无需进货！");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void sale() {
        if (goods <= 0) {
            System.out.println("缺货！");
            try {
                wait(); // 如果包在if代码块中，容易出现虚假唤醒，导致等待后没有其他线程可以唤醒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("售出货物：" + --goods);
            notifyAll();
        }
    }
}

class Product implements Runnable {

    private Clerk clerk;

    public Product(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            clerk.get();
        }
    }
}

class Consumer implements Runnable {

    private Clerk clerk;

    public Consumer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            clerk.sale();
        }
    }
}
