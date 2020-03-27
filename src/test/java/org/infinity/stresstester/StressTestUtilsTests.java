package org.infinity.stresstester;

import org.infinity.stresstester.core.StressTask;

public class StressTestUtilsTests {
    public static void main(String[] args) {
        StressTestUtils.testAndPrint(100, 1000, () -> {
            System.out.println("Do my task.");
            return null;
        });
    }
}
