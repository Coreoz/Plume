package com.coreoz.plume.jersey.grizzly;

import org.glassfish.grizzly.threadpool.AbstractThreadPool;
import org.glassfish.grizzly.threadpool.ThreadPoolProbe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Singleton;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A Grizzly thread pool probe to monitor Grizzly thread pool.<br>
 * <br>
 * Usage:<br>
 * - Usage with Grizzly: <code>httpServer.getServerConfiguration().getMonitoringConfig().getThreadPoolConfig().addProbes(grizzlyThreadPoolProbe);</code><br>
 * - Metrics can be fetched using: {@link #getPoolUsageSize()} and {@link #getPoolMaxSize()} and {@link #getPoolCurrentSize()} and {@link #getTasksWaitingSize()}
 */
@Singleton
public class GrizzlyThreadPoolProbe implements ThreadPoolProbe {
    private static final Logger logger = LoggerFactory.getLogger(GrizzlyThreadPoolProbe.class);

    private final AtomicInteger tasksWaitingSize = new AtomicInteger(0);
    private final AtomicInteger poolUsageSize = new AtomicInteger(0);

    private int poolMaxSize = 0;
    private int poolCurrentSize = 0;

    /* Stats accessors */

    /**
     * The maximum number of worker threads in the HTTP threads pool
     */
    public int getPoolMaxSize() {
        return poolMaxSize;
    }

    /**
     * The current number of worker threads in the HTTP threads pool
     */
    public int getPoolCurrentSize() {
        return poolCurrentSize;
    }

    /**
     * The number of worker threads currently in use
     */
    public int getPoolUsageSize() {
        return poolUsageSize.get();
    }

    /**
     * The number of tasks that are waiting because all the worker threads are in use
     */
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
