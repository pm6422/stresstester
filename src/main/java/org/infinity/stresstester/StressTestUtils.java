//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.infinity.stresstester;

import org.infinity.stresstester.core.SimpleResultFormater;
import org.infinity.stresstester.core.StressResult;
import org.infinity.stresstester.core.StressResultFormater;
import org.infinity.stresstester.core.StressTask;
import org.infinity.stresstester.core.StressTester;
import java.io.StringWriter;

public class StressTestUtils {
    private static StressTester stressTester = new StressTester();
    private static SimpleResultFormater simpleResultFormater = new SimpleResultFormater();

    public StressTestUtils() {
    }

    public static StressResult test(int concurrencyLevel, int totalRequests, StressTask stressTask) {
        return stressTester.test(concurrencyLevel, totalRequests, stressTask);
    }

    public static StressResult test(int concurrencyLevel, int totalRequests, StressTask stressTask, int warmUpTime) {
        return stressTester.test(concurrencyLevel, totalRequests, stressTask, warmUpTime);
    }

    public static void testAndPrint(int concurrencyLevel, int totalRequests, StressTask stressTask) {
        testAndPrint(concurrencyLevel, totalRequests, stressTask, (String)null);
    }

    public static void testAndPrint(int concurrencyLevel, int totalRequests, StressTask stressTask, String testName) {
        StressResult stressResult = test(concurrencyLevel, totalRequests, stressTask);
        String str = format(stressResult);
        System.out.println(str);
    }

    public static String format(StressResult stressResult) {
        return format(stressResult, simpleResultFormater);
    }

    public static String format(StressResult stressResult, StressResultFormater stressResultFormater) {
        StringWriter sw = new StringWriter();
        stressResultFormater.format(stressResult, sw);
        return sw.toString();
    }
}
