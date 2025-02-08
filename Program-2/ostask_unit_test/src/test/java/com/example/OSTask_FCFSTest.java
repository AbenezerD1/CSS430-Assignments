package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OSTask_FCFSTest {
    private final Random random = new Random();

    @Test
    @Timeout(value = 60, unit = TimeUnit.SECONDS) // Set a timeout for the test
    public void testFCFSScheduling() throws InterruptedException {
        Queue<OSTask> taskQueue = new LinkedList<>();
        // Create 5 tasks with unique random burst times between 1 and 5 seconds
        Set<Long> burstTimes = new HashSet<>(); // Use a Set to ensure unique burst times
        for (char taskName = 'A'; taskName <= 'E'; taskName++) {
            long burstTime;
            do {
                burstTime = (random.nextInt(5) + 1) * 1000L;
            } while (burstTimes.contains(burstTime)); // Keep generating until a unique burst time is found
            burstTimes.add(burstTime); // Add the unique burst time to the set
            taskQueue.offer(new OSTask("Task " + taskName, burstTime));
        }

        // Print out all Tasks
        for (OSTask task : taskQueue) {
            System.out.println("Task: " + task.getName() + ", Remaining Burst: " + task.getRemainingBurst());
        }

        // Start all tasks initially but suspend them immediately
        for (OSTask task : taskQueue) {
            task.start();
            task.suspendTask();
        }

        // FCFS Scheduling simulation
        while (!taskQueue.isEmpty()) {
            OSTask currentTask = taskQueue.poll();
            currentTask.resumeTask(); // Start/resume the Task
            // Now wait. The join() will finish when the burst time runs out.
            currentTask.join(currentTask.getRemainingBurst() + 100);

            assertFalse(currentTask.isTaskRunning(), "Task should not be running after its burst time.");

            currentTask.stopTask(); // Ensure the Task stops if it hasn't already.
            currentTask.join(100); // Wait a short time for the task to fully stop.
            assertFalse(currentTask.isTaskAlive(), "Task should not be alive after stopTask().");
        }
    }
}