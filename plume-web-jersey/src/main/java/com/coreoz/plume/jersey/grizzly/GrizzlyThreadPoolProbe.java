package com.coreoz.plume.jersey.grizzly;

import org.glassfish.grizzly.threadpool.AbstractThreadPool;
import org.glassfish.grizzly.threadpool.ThreadPoolProbe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Singleton;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class GrizzlyThreadPoolProbe implements ThreadPoolProbe {
    private static final Logger logger = LoggerFactory.getLogger(GrizzlyThreadPoolProbe.class);

    private final AtomicInteger tasksWaitingSize = new AtomicInteger(0);
    private final AtomicInteger poolUsageSize = new AtomicInteger(0);

    private int poolMaxSize = 0;
    private int poolCurrentSize = 0;

    /* Stats accessors */

    public int getPoolMaxSize() {
        return poolMaxSize;
    }

    public int getPoolCurrentSize() {
        return poolCurrentSize;
    }

    public int getPoolUsageSize() {
        return poolUsageSize.get();
    }

    public int getTasksWaitingSize() {
        return tasksWaitingSize.get();
    }

    /* Implements ThreadPoolProbe */

    @Override
    public void onThreadPoolStartEvent(AbstractThreadPool threadPool) {
        poolMaxSize = threadPool.getConfig().getMaxPoolSize();
        poolCurrentSize = threadPool.getSize();
    }

    @Override
    public void onThreadPoolStopEvent(AbstractThreadPool threadPool) {
        // Not used
    }

    @Override
    public void onThreadAllocateEvent(AbstractThreadPool threadPool, Thread thread) {
        poolMaxSize = threadPool.getConfig().getMaxPoolSize();
        poolCurrentSize = threadPool.getSize();
    }

    @Override
    public void onThreadReleaseEvent(AbstractThreadPool threadPool, Thread thread) {
        poolCurrentSize = threadPool.getSize();
    }

    @Override
    public void onMaxNumberOfThreadsEvent(AbstractThreadPool threadPool, int maxNumberOfThreads) {
        // Not used
    }

    @Override
    public void onTaskQueueEvent(AbstractThreadPool threadPool, Runnable task) {
        tasksWaitingSize.incrementAndGet();
    }

    @Override
    public void onTaskDequeueEvent(AbstractThreadPool threadPool, Runnable task) {
        tasksWaitingSize.decrementAndGet();
        poolUsageSize.incrementAndGet();
    }

    @Override
    public void onTaskCancelEvent(AbstractThreadPool threadPool, Runnable task) {
        tasksWaitingSize.decrementAndGet();
    }

    @Override
    public void onTaskCompleteEvent(AbstractThreadPool threadPool, Runnable task) {
        poolUsageSize.decrementAndGet();
    }

    @Override
    public void onTaskQueueOverflowEvent(AbstractThreadPool threadPool) {
        logger.error("Grizzly queue overflow");
    }
}
