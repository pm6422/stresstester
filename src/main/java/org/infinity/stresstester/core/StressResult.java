//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.infinity.stresstester.core;

import java.util.List;

public class StressResult {
    private int concurrencyLevel;
    private int totalRequests;
    private long testsTakenTime;
    private int failedRequests;
    private List<Long> allTimes;
    private List<StressThreadWorker> workers;

    public StressResult() {
    }

    public long getTestsTakenTime() {
        return this.testsTakenTime;
    }

    public int getConcurrencyLevel() {
        return this.concurrencyLevel;
    }

    public void setConcurrencyLevel(int concurrencyLevel) {
        this.concurrencyLevel = concurrencyLevel;
    }

    public int getTotalRequests() {
        return this.totalRequests;
    }

    public void setTotalRequests(int totalRequests) {
        this.totalRequests = totalRequests;
    }

    public void setTestsTakenTime(long testsTakenTime) {
        this.testsTakenTime = testsTakenTime;
    }

    public int getFailedRequests() {
        return this.failedRequests;
    }

    public void setFailedRequests(int failedRequests) {
        this.failedRequests = failedRequests;
    }

    public List<Long> getAllTimes() {
        return this.allTimes;
    }

    public void setAllTimes(List<Long> allTimes) {
        this.allTimes = allTimes;
    }

    public List<StressThreadWorker> getWorkers() {
        return this.workers;
    }

    public void setWorkers(List<StressThreadWorker> workers) {
        this.workers = workers;
    }
}
