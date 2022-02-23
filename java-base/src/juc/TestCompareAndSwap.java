package juc;

/**
 * @author: wensw
 * @description: 模拟CAS算法
 */
public class TestCompareAndSwap {

    public static void main(String[] args) {
        final CompareAndSwap compareAndSwap = new CompareAndSwap();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int expectValue = compareAndSwap.get();
                    boolean success = compareAndSwap.compareAndSet(expectValue, (int) (Math.random() * 2));
                    System.out.println(success);
                }
            }).start();
        }
    }

}

class CompareAndSwap {

    private int value;

    public synchronized int get() {
        return value;
    }

    /**
     * 比较并交换
     *
     * @param expectValue 预估值
     * @param newValue    更新值
     * @return 旧的持有值
     */
    public synchronized int compareAndSwap(int expectValue, int newValue) {
        int oldValue = value;
        if (oldValue == expectValue) {
            this.value = newValue;
        }
        return oldValue;
    }

    /**
     * 设置
     *
     * @param expectValue 预估值
     * @param newValue    更新值
     * @return true / false
     */
    public synchronized boolean compareAndSet(int expectValue, int newValue) {
        return expectValue == compareAndSwap(expectValue, newValue);
    }

}
