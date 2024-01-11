package course.concurrency.m5_streams;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTask {

    public ThreadPoolExecutor getLifoExecutor() {
        return new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LIFOBlockingQueue<>()
        );
    }

    public ThreadPoolExecutor getRejectExecutor() {
        return new ThreadPoolExecutor(8, 8,
                60, TimeUnit.SECONDS,
                new SynchronousQueue<>(), new ThreadPoolExecutor.DiscardPolicy());
    }

    private static class LIFOBlockingQueue<E> extends LinkedBlockingDeque<E> {
        @Override
        public E take() throws InterruptedException {
            return super.takeLast();
        }
    }
}
