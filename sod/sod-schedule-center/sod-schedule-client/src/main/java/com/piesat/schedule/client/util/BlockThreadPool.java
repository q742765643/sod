package com.piesat.schedule.client.util;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName : BlockThreadPool
 * @Description :
 * @Author : zzj
 * @Date: 2020-09-20 15:20
 */
public class BlockThreadPool {
    private ThreadPoolExecutor pool = null;

    public BlockThreadPool(int poolSize) {
        pool = new ThreadPoolExecutor(poolSize, poolSize, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5), new CustomThreadFactory(),
                new CustomRejectedExecutionHandler());
    }

    public void destory() {
        if (pool != null) {
            pool.shutdownNow();
        }
    }
    public void shutdown() {
        if (pool != null) {
            pool.shutdown();
        }
    }
    public boolean isTerminated(){
        return pool.isTerminated();
    }
    private class CustomThreadFactory implements ThreadFactory {
        private AtomicInteger count = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            String threadName = BlockThreadPool.class.getSimpleName() + count.addAndGet(1);
            t.setName(threadName);
            return t;
        }
    }

    private class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                // 核心改造点，由blockingqueue的offer改成put阻塞方法
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(Runnable runnable) {
        this.pool.execute(runnable);
    }

}

