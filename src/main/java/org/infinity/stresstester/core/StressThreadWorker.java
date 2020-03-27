//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.infinity.stresstester.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class StressThreadWorker implements Runnable {
    private StressTask service;
    private CyclicBarrier threadStartBarrier;
    private CountDownLatch threadEndLatch;
    private AtomicInteger failedCounter = null;
    private int count;
    protected static Logger log = LoggerFactory.getLogger(SimpleResultFormater.class);
    private List<Long> everyTimes;

    public StressThreadWorker(StressContext stressContext, int count) {
        this.threadStartBarrier = stressContext.getThreadStartBarrier();
        this.threadEndLatch = stressContext.getThreadEndLatch();
        this.failedCounter = stressContext.getFailedCounter();
        this.count = count;
        this.everyTimes = new ArrayList(count);
        this.service = stressContext.getTestService();
    }

    public List<Long> getEveryTimes() {
        return this.everyTimes;
    }

    public void run() {
        try {
            this.threadStartBarrier.await();
            this.doRun();
        } catch (Exception var2) {
            log.error("Test exception", var2);
            var2.printStackTrace();
        }

    }

    protected void doRun() throws Exception {
        for(int i = 0; i < this.count; ++i) {
            long start = System.nanoTime();

            try {
                this.service.doTask();
            } catch (Throwable var12) {
                this.failedCounter.incrementAndGet();
            } finally {
                long var6 = System.nanoTime();
                long limit = var6 - start;
                this.everyTimes.add(limit);
            }
        }

        this.threadEndLatch.countDown();
    }
}
