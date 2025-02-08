package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class OSTaskTest {
    final int default_sleep_time = 10;
    final long default_join_wait_time = 1000L;
    final long default_burst_duration = 1000L;

    @Test
    public void testConstructorAndGetName() {
        String expectedName = "My Test Task";
        OSTask myTask = new OSTask(expectedName, default_burst_duration);
        String actualName = myTask.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testStartStop() throws InterruptedException {

        OSTask myTask = new OSTask("Test Task", default_burst_duration);
        myTask.start();
        Thread.sleep(default_sleep_time);
        assertTrue(myTask.isTaskAlive());
        myTask.stopTask();
        myTask.join(default_join_wait_time); // Wait for the Task to finish
        assertFalse(myTask.isTaskAlive());
    }

    @Test
    public void testIsTaskAliveBeforeStart() {
        OSTask myTask = new OSTask("Test Task", default_burst_duration);
        assertFalse(myTask.isTaskAlive()); // Should initially not be alive
    }

    @Test
    public void testIsTaskAliveAfterStart() throws InterruptedException {
        OSTask myTask = new OSTask("Test Task", default_burst_duration);
        myTask.start();
        Thread.sleep(default_sleep_time);
        assertTrue(myTask.isTaskAlive()); // Should now be alive
    }

    @Test
    public void testIsTaskAliveAfterStop() throws InterruptedException {
        OSTask myTask = new OSTask("Test Task", default_burst_duration);
        myTask.start();
        Thread.sleep(default_sleep_time);
        myTask.stopTask();
        myTask.join(default_join_wait_time);
        assertFalse(myTask.isTaskAlive());
    }

    @Test
    public void testSuspendResume() throws InterruptedException {
        OSTask myTask = new OSTask("Test Task", default_burst_duration);
        myTask.start();

        Thread.sleep(default_sleep_time);
        assertTrue(myTask.isTaskAlive()); // Assert started

        myTask.suspendTask(); // Pause the Task
        assertTrue(myTask.isTaskPaused()); // Assert paused (Should be true immediately–no loop needed)

        myTask.resumeTask();
        assertFalse(myTask.isTaskPaused()); // Assert resumed (Should be false immediately–no loop needed)

        myTask.stopTask();
        myTask.join(default_join_wait_time); // Wait for Task termination
        assertFalse(myTask.isTaskAlive()); // Use assertFalse: Task should have terminated
    }

    @Test
    @Timeout(value = 60, unit = TimeUnit.SECONDS) // Set a reasonable timeout
    public void testPingPongSuspendResume() throws InterruptedException {
        final long quantum = 2000L; // 2sec quantum
        OSTask pingTask = new OSTask("Ping Task", 10000L);
        OSTask pongTask = new OSTask("Pong Task", 10000L);

        // start but immediately put into suspend state
        pingTask.start();
        pingTask.suspendTask();
        pongTask.start();
        pongTask.suspendTask();

        Thread.sleep(default_sleep_time);
        assertTrue(pingTask.isTaskAlive());
        assertTrue(pongTask.isTaskAlive());

        // Assert paused (Should be true immediately)
        assertTrue(pingTask.isTaskPaused());
        assertTrue(pongTask.isTaskPaused());

        // simulated context switch interleaving ping and pong
        while (pingTask.getRemainingBurst() > quantum && pongTask.getRemainingBurst() > quantum) {
            pingTask.resumeTask();
            assertFalse(pingTask.isTaskPaused()); // Assert resumed (Should be false)
            System.out.println("Ping Task Resumed: Burst=" + pingTask.getRemainingBurst());
            Thread.sleep(quantum);
            pingTask.suspendTask();
            assertTrue(pingTask.isTaskPaused());
            System.out.println("Ping Task Suspended: Burst=" + pingTask.getRemainingBurst());

            pongTask.resumeTask();
            assertFalse(pongTask.isTaskPaused()); // Assert resumed (Should be false)
            System.out.println("Pong Task Resumed: Burst=" + pongTask.getRemainingBurst());
            Thread.sleep(quantum);
            pongTask.suspendTask();
            assertTrue(pongTask.isTaskPaused());
            System.out.println("Pong Task Suspended: Burst=" + pongTask.getRemainingBurst());
        }

        pingTask.stopTask();
        pingTask.join(default_join_wait_time); // Wait for Task termination
        assertFalse(pingTask.isTaskAlive()); // Use assertFalse: Task should have terminated

        pongTask.stopTask();
        pongTask.join(default_join_wait_time); // Wait for Task termination
        assertFalse(pongTask.isTaskAlive()); // Use assertFalse: Task should have terminated
    }

    @Test
    public void testBurstSuspendResume() throws InterruptedException {
        final long quantum = 2000L; // 2sec quantum
        OSTask burstTask = new OSTask("Burst Task", 10000L);

        // start but immediately put into suspend state
        System.out.println("Burst Task Started: Burst=" + burstTask.getRemainingBurst());
        burstTask.start();
        burstTask.suspendTask();
        System.out.println("Burst Task Suspended: Burst=" + burstTask.getRemainingBurst());

        Thread.sleep(default_sleep_time);

        // Alive but Paused
        assertTrue(burstTask.isTaskAlive());
        // Assert paused (Should be true immediately)
        assertTrue(burstTask.isTaskPaused());

        // simulated context switch on a single task
        while (burstTask.getRemainingBurst() > quantum) {
            burstTask.resumeTask();
            assertFalse(burstTask.isTaskPaused()); // Assert resumed (Should be false immediately–no loop needed)
            System.out.println("Burst Task Resumed: Burst=" + burstTask.getRemainingBurst());
            Thread.sleep(quantum);
            burstTask.suspendTask();
        }

        burstTask.stopTask();
        burstTask.join(default_join_wait_time); // Wait for Task termination
        System.out.println("Burst Task Completed: Burst=" + burstTask.getRemainingBurst());
        assertFalse(burstTask.isTaskAlive()); // Use assertFalse: Task should have terminated
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS) // Set a reasonable timeout
    public void testMultipleConcurrentTaskBurstTimes() throws InterruptedException {
        long[] bursts = { 500L, 1000L, 2000L, 4000L };
        long allowedVariance = 50L; // Allow some variance for Task scheduling

        OSTask[] tasks = new OSTask[bursts.length];
        long[] startTimes = new long[bursts.length];
        long[] actualBursts = new long[bursts.length];

        // Create and start tasks
        for (int i = 0; i < bursts.length; i++) {
            final int index = i; // For use in lambda
            tasks[index] = new OSTask("Task " + (index + 1), bursts[index]);
            startTimes[index] = System.currentTimeMillis();
            tasks[index].start();
        }

        // Wait for Tasks to finish and measure burst times
        for (int i = 0; i < bursts.length; i++) {
            tasks[i].join(bursts[i] * 2); // Use a timeout to prevent indefinite blocking
            actualBursts[i] = System.currentTimeMillis() - startTimes[i];

        }

        // Assertions
        for (int i = 0; i < bursts.length; i++) {
            long expectedBurst = bursts[i];
            long actualBurst = actualBursts[i];
            System.out.println(
                    "Task " + (i + 1) + ": Burst time (expected " + expectedBurst + " ms): " + actualBurst + " ms");
            assertTrue(actualBurst >= expectedBurst - allowedVariance && actualBurst <= expectedBurst + allowedVariance,
                    "Task " + (i + 1) + " burst time not within acceptable range");
        }

    }
}
