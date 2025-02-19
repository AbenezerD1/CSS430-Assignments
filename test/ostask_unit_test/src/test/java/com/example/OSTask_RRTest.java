package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import java.util.Map;
import java.util.HashMap;

public class OSTask_RRTest {
    final int default_sleep_time = 10;
    final long default_join_wait_time = 1000L;
    final long default_burst_duration = 1000L;
    final Map<String, Long[]> taskData = new HashMap<>();
    { // Use an initializer block to populate the map
        taskData.put("Task A", new Long[]{0L, 5000L});
        taskData.put("Task B", new Long[]{0L, 1000L});
        taskData.put("Task C", new Long[]{0L, 3000L});
        taskData.put("Task D", new Long[]{0L, 6000L});
        taskData.put("Task E", new Long[]{0L, 500L});
    }

    public void printTaskData() {
        for (Map.Entry<String, Long[]> entry : taskData.entrySet()) {
            String taskName = entry.getKey();
            Long[] data = entry.getValue();
            System.out.println("Task: " + taskName + ", Data: " + Arrays.toString(data));
        }
    }    

    @Test
    public void testConstructorAndGetName() {
        printTaskData();
        String expectedName = "Foobar";
        OSTask myTask = new OSTask(expectedName, default_burst_duration);
        String actualName = myTask.getName();
        assertEquals(expectedName, actualName);
    }
}