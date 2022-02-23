package juc;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * @author: wensw
 * @description: fork / join 框架熟悉
 */
public class TestForkJoin {

    public static void main(String[] args) {
        Instant start = Instant.now();
        ForkJoin forkJoin = new ForkJoin(0L, 50000000000L);
        ForkJoinPool pool = new ForkJoinPool();
        Long result = pool.invoke(forkJoin);
        Instant end = Instant.now();
        System.out.println(result);
        System.out.println(Duration.between(start, end).toMillis());
    }

    @Test
    public void testForEach() {
        Instant start = Instant.now();
        long sum = 0L;
        for (long i = 0L; i < 50000000000L; i++) {
            sum +=  i;
        }
        Instant end = Instant.now();
        System.out.println(sum);
        System.out.println(Duration.between(start, end).toMillis());
    }

    @Test
    public void testJ8Stream() {
        Instant start = Instant.now();
        long result = LongStream.rangeClosed(0L, 50000000000L)
                .parallel()
                .reduce(0L, Long::sum);
        Instant end = Instant.now();
        System.out.println(result);
        System.out.println(Duration.between(start, end).toMillis());
    }

}

class ForkJoin extends RecursiveTask<Long> {

    private Long start;

    private Long end;

    private Long temp = 10000L;

    public ForkJoin(Long start, Long end) {
        this.start = start;
        this.end = end;
    }


    @Override
    protected Long compute() {
        long length = end - start;
        if (length <= temp) {
            long sum = 0L;
            for (long i = start; i < end; i++) {
                sum = +i;
            }
            return sum;
        } else {
            long middle = (start + end) / 2;
            ForkJoin left = new ForkJoin(start, middle);
            ForkJoin right = new ForkJoin(middle + 1, end);
            left.fork();
            right.fork();
            return left.join() + right.join();
        }
    }


}
