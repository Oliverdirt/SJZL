package com.ctsi.ssdc.util;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.sql.Timestamp;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhaoliangliang
 * @date 2021/9/9
 */
public class SystemClock {
    private final long period;
    private final AtomicLong now;

    private SystemClock(long period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        this.scheduleClockUpdating();
    }

    private static SystemClock instance() {
        return InstanceHolder.INSTANCE;
    }

    public static long now() {
        return instance().currentTimeMillis();
    }

    public static String nowDate() {
        return (new Timestamp(instance().currentTimeMillis())).toString();
    }

    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("System Clock").daemon(true).build());
//        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor((runnable) -> {
//            Thread thread = new Thread(runnable, "System Clock");
//            thread.setDaemon(true);
//            return thread;
//        });
        scheduler.scheduleAtFixedRate(() -> {
            this.now.set(System.currentTimeMillis());
        }, this.period, this.period, TimeUnit.MILLISECONDS);
    }

    private long currentTimeMillis() {
        return this.now.get();
    }

    private static class InstanceHolder {
        public static final SystemClock INSTANCE = new SystemClock(1L);

        private InstanceHolder() {
        }
    }
}
