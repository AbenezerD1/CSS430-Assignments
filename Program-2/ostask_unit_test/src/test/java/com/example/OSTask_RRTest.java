package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import java.util.Map;
import java.util.Queue;
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

    @Test
    public void testRRScheduler() {
        Queue<OSTask> taskQueue = new LinkedList<>();
        Logger[] log = new Logger[taskData.size()];

        // Create 5 tasks with unique random burst times between 1 and 5 seconds
        Set<Long> burstTimes = new HashSet<>();
        Random random = new Random(); // Initialize Random only once
        for (char taskName = 'A'; taskName <= 'E'; taskName++) {
            long burstTime;
            do {
                burstTime = (random.nextInt(5) + 1) * 1000L;
            } while (burstTimes.contains(burstTime));
            burstTimes.add(burstTime);
            OSTask task = new OSTask("Task " + taskName, burstTime);
            task.start();
            task.suspendTask();
            taskQueue.offer(task);
            log[taskName - 'A'] = new Logger();
        }
        
        // Print initial task information
        for (OSTask task : taskQueue) {
            System.out.println("Task: " + task.getName() + ", Initial Burst: " + task.getRemainingBurst());
        }
        // RR Scheduling simulation
        while (!taskQueue.isEmpty()) {
            OSTask currentTask = taskQueue.poll();
            if (currentTask == null) {
                break; // No tasks available to run
            }
            long execStart = System.currentTimeMillis();
            int index = currentTask.getName().charAt(5) - 'A';

            System.out.println(currentTask.getName() + " executing.");
            currentTask.resumeTask();

            try {
                currentTask.join(default_join_wait_time);
            } catch (InterruptedException e) {
                e.printStackTrace(); // Handle or rethrow the exception as needed
            }

            currentTask.suspendTask();
            
            long execEnd = System.currentTimeMillis();
            log[index].addExecutionTime(execEnd-execStart);
            if(log[index].responseTime == 0) log[index].responseTime = execStart-log[index].startTime;

            try {
                currentTask.join(100); 
            } catch (InterruptedException e) {
                e.printStackTrace(); // Handle or rethrow the exception as needed
            }

            if (currentTask.getRemainingBurst() > 0) {
                taskQueue.offer(currentTask); // Re-queue if burst time remaining
            } else {
                System.out.println(currentTask.getName() + " finished."); // Indicate task completion
                log[index].endTime = System.currentTimeMillis();
                log[index].setTAT();
                log[index].printLogs();
            }
        }
    }
}