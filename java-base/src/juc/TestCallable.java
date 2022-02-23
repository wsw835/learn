package juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author: wensw
 * @description: 创建执行线程的方式3：实现Callable接口
 * <p>
 * 1、与实现Runnable接口的区别： 有返回值，且call方法允许抛出异常
 * 2、执行Callable实现类时需要FutureTask实现类的支持，用于接收运算结果
 */
public class TestCallable {

    public static void main(String[] args) {
        CallableWrapper callableWrapper = new CallableWrapper();
        //
        FutureTask<Object> callResult = new FutureTask<>(callableWrapper);
        //若线程未执行结束，则不会执行下面获取结果的逻辑（闭锁）
        new Thread(callResult).start();
        Object res = null;
        try {
            res = callResult.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(res);
    }

}

class CallableWrapper implements Callable<Object> {

    @Override
    public Object call() throws Exception {
        int sum = 0;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            sum = +i;
        }
        return sum;
    }

}