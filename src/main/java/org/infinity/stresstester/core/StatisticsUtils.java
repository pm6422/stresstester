//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.infinity.stresstester.core;

import java.util.Iterator;
import java.util.List;

public class StatisticsUtils {
    public StatisticsUtils() {
    }

    public static long getTotal(List<Long> times) {
        long total = 0L;

        Long time;
        for(Iterator var4 = times.iterator(); var4.hasNext(); total += time) {
            time = (Long)var4.next();
        }

        return total;
    }

    public static float getAverage(List<Long> allTimes) {
        long total = getTotal(allTimes);
        return getAverage(total, allTimes.size());
    }

    public static float getAverage(long total, int size) {
        return (float)total / (float)size;
    }

    public static float getTps(float ms, int concurrencyLevel) {
        return (float)concurrencyLevel / ms * 1000.0F;
    }

    public static float toMs(long nm) {
        return (float)nm / 1000000.0F;
    }

    public static float toMs(float nm) {
        return nm / 1000000.0F;
    }
}
