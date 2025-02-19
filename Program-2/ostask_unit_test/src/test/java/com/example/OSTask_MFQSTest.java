package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

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
import java.util.LinkedList;

public class OSTask_MFQSTest {
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
    public void testMFQScheduler(){
        //queue 0 - 500 ms, queue 1 - 1000ms, queue 2 - 2000ms
        ArrayList<Queue<OSTask>> taskQueues = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            taskQueues.add(new LinkedList<>()); //add three queues to the arraylist of queues
        }
        Logger[] logs = new Logger[taskData.size()];

        //all tasks begin in queue 0
        for (Map.Entry<String, Long[]> entry : taskData.entrySet()) {
            OSTask task = new OSTask(entry.getKey(), entry.getValue()[1]);
            task.start();
            task.suspendTask();
            taskQueues.get(0).offer(task);
            logs[getTaskIndex(entry.getKey())] = new Logger(); // init the logs with start times
        }

        while(!isAllTaskQueuesEmpty(taskQueues)){
            // Queue 0 execution
            if (!taskQueues.get(0).isEmpty()) {
                System.out.println("\nProcessing Queue 0");
                processQueue(taskQueues, 0, default_join_wait_time / 2, 1, logs);
            }

            // Queue 1 execution
            if (taskQueues.get(0).isEmpty() && !taskQueues.get(1).isEmpty()) { // Only run if Queue 0 is empty
                System.out.println("\nProcessing Queue 1");
                processQueue(taskQueues, 1, default_join_wait_time, 2,logs);
            }

            // Queue 2 execution
            if (taskQueues.get(0).isEmpty() && taskQueues.get(1).isEmpty() && !taskQueues.get(2).isEmpty()) { // Only run if Queues 0 and 1 are empty
                System.out.println("\nProcessing Queue 2");
                processQueue(taskQueues, 2, 2 * default_join_wait_time, 2,logs);
            }
        }

    }

    private boolean isAllTaskQueuesEmpty(ArrayList<Queue<OSTask>> taskQueues){
        for (Queue<OSTask>  queue : taskQueues) {
            if(!queue.isEmpty()){
                return false;
            }
        }
        return true;
    }

    private void processQueue(ArrayList<Queue<OSTask>> taskQueues, int queueIndex, long timeSlice, int nextQueue, Logger[] logs) {
        Queue<OSTask> currentQueue = taskQueues.get(queueIndex);
        while (!currentQueue.isEmpty()){
            OSTask task = currentQueue.poll();

            System.out.println(task.getName() + ": executing in queue " + queueIndex);
            long execStart = System.currentTimeMillis();
            int index = getTaskIndex(task.getName());

            task.resumeTask();
            
            try {
                task.join(timeSlice);
                task.suspendTask();
                
                long execEnd = System.currentTimeMillis();
                logs[index].addExecutionTime(execEnd-execStart);
                if(logs[index].responseTime == 0) logs[index].responseTime = execStart-logs[index].startTime;

                if(queueIndex == 2){
                    Thread execTask = new Thread(task); // run long tasks in a thread
                    execTask.start();
                    execTask.join(timeSlice); // 2000 ms
                }
                // Check higher priority queues for new tasks *after* the current task has run its quantum
                for(int j = 0; j < queueIndex; j++) {
                    if (!taskQueues.get(j).isEmpty()) {
                        currentQueue.offer(task);
                        return; // Return to main loop to prioritize higher queues
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(task.getRemainingBurst() > 0){
                taskQueues.get(nextQueue).offer(task);
            }else{
                System.out.println();
                System.out.println(task.getName()+ " finished executing in queue " + queueIndex + ".");

                logs[index].endTime = System.currentTimeMillis();
                logs[index].setTAT();
                logs[index].printLogs();
                System.out.println();
            }
        }
    }

    private int getTaskIndex(String taskName) {
        char taskChar = taskName.charAt(5);
        return taskChar - 65;
    }
}
