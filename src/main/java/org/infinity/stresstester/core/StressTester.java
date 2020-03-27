//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.infinity.stresstester.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StressTester {
    protected static Logger log = LoggerFactory.getLogger(SimpleResultFormater.class);
    private int defaultWarmUpTime = 1600;
    private StressTask emptyTestService = new StressTask() {
        public Object doTask() throws Exception {
            return null;
        }
    };

    static {
        warnSelf();
    }

    public StressTester() {
    }

    protected static void warnSelf() {
        for(int i = 0; i < 50; ++i) {
            StressTester benchmark = new StressTester();
            benchmark.test(10, 100, (StressTask)null, 0);
        }

    }

    protected void warmUp(int warmUpTime, StressTask testervice) {
        for(int i = 0; i < warmUpTime; ++i) {
            try {
                testervice.doTask();
            } catch (Exception var5) {
                log.error("Test exception", var5);
            }
        }

    }

    public StressResult test(int concurrencyLevel, int totalRequests, StressTask stressTask) {
        return this.test(concurrencyLevel, totalRequests, stressTask, this.defaultWarmUpTime);
    }

    public StressResult test(int concurrencyLevel, int totalRequests, StressTask stressTask, int warmUpTime) {
        if (stressTask == null) {
            stressTask = this.emptyTestService;
        }

        this.warmUp(warmUpTime, stressTask);
        int everyThreadCount = totalRequests / concurrencyLevel;
        CyclicBarrier threadStartBarrier = new CyclicBarrier(concurrencyLevel);
        CountDownLatch threadEndLatch = new CountDownLatch(concurrencyLevel);
        AtomicInteger failedCounter = new AtomicInteger();
        StressContext stressContext = new StressContext();
        stressContext.setTestService(stressTask);
        stressContext.setEveryThreadCount(everyThreadCount);
        stressContext.setThreadStartBarrier(threadStartBarrier);
        stressContext.setThreadEndLatch(threadEndLatch);
        stressContext.setFailedCounter(failedCounter);
        ExecutorService executorService = Executors.newFixedThreadPool(concurrencyLevel);
        List<StressThreadWorker> workers = new ArrayList(concurrencyLevel);

        int realTotalRequests;
        StressThreadWorker worker;
        for(realTotalRequests = 0; realTotalRequests < concurrencyLevel; ++realTotalRequests) {
            worker = new StressThreadWorker(stressContext, everyThreadCount);
            workers.add(worker);
        }

        for(realTotalRequests = 0; realTotalRequests < concurrencyLevel; ++realTotalRequests) {
            worker = (StressThreadWorker)workers.get(realTotalRequests);
            executorService.submit(worker);
        }

        try {
            threadEndLatch.await();
        } catch (InterruptedException var20) {
            log.error("InterruptedException", var20);
        }

        executorService.shutdownNow();
        realTotalRequests = everyThreadCount * concurrencyLevel;
        int failedRequests = failedCounter.get();
        StressResult stressResult = new StressResult();
        StressTester.SortResult sortResult = this.getSortedTimes(workers);
        List<Long> allTimes = sortResult.allTimes;
        stressResult.setAllTimes(allTimes);
        List<Long> trheadTimes = sortResult.trheadTimes;
        long totalTime = (Long)trheadTimes.get(trheadTimes.size() - 1);
        stressResult.setTestsTakenTime(totalTime);
        stressResult.setFailedRequests(failedRequests);
        stressResult.setTotalRequests(realTotalRequests);
        stressResult.setConcurrencyLevel(concurrencyLevel);
        stressResult.setWorkers(workers);
        return stressResult;
    }

    protected StressTester.SortResult getSortedTimes(List<StressThreadWorker> workers) {
        List<Long> allTimes = new ArrayList();
        List<Long> trheadTimes = new ArrayList();
        Iterator var5 = workers.iterator();

        while(var5.hasNext()) {
            StressThreadWorker worker = (StressThreadWorker)var5.next();
            List<Long> everyWorkerTimes = worker.getEveryTimes();
            long workerTotalTime = StatisticsUtils.getTotal(everyWorkerTimes);
            trheadTimes.add(workerTotalTime);
            Iterator var10 = everyWorkerTimes.iterator();

            while(var10.hasNext()) {
                Long time = (Long)var10.next();
                allTimes.add(time);
            }
        }

        Collections.sort(allTimes);
        Collections.sort(trheadTimes);
        StressTester.SortResult result = new StressTester.SortResult();
        result.allTimes = allTimes;
        result.trheadTimes = trheadTimes;
        return result;
    }

    class SortResult {
        List<Long> allTimes;
        List<Long> trheadTimes;

        SortResult() {
        }
    }
}
