package com.example;

import java.util.concurrent.CountDownLatch;

class OSTask implements Runnable {
    private Thread internalThread; // A field to hold the internal Java Thread
    private volatile boolean running = false;
    private volatile boolean paused = false;
    private String name;
    private long remainingBurst;

    /**
     * Constructor that also sets the name and burst time of the task.
     * 
     * @param name  The name of the task.
     * @param burst The amount of simulated CPU work time (in milliseconds).
     */
    public OSTask(String name, long burst) {
        this.name = name;
        remainingBurst = burst;
    }

    /**
     * Setter for Thread name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for Thread name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for remaining burst
     */
    public long getRemainingBurst() {
        return this.remainingBurst;
    }

    public void start() { // Add a start() method to OSTask
        if (internalThread == null || !internalThread.isAlive()) {
            internalThread = new Thread(this);
            internalThread.start();
        }
    }

    public void join(long millis) throws InterruptedException {
        if (internalThread != null) {
            internalThread.join(millis); // Correctly use the timeout overload
        }
    }

    /**
     * Simulate some CPU work (approx 4ms)
     */
    private void doWork() {
        double value = 1.0;
        for (int j = 0; j < 10000; j++) { // Inner loop for more work
            value = Math.sqrt(value + j);
        }
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        System.out.println("{ \"task\": \"" + getName() + "\", \"start\": " + startTime + ", \"duration\": " + getRemainingBurst() + "}");

        running = true;
        // simulated CPU burst duration/endTime

        try {
            while (!Thread.interrupted() && running) { // Main work loop
                // Pause Logic
                synchronized (this) {
                    while (paused && !Thread.interrupted() && running) {
                        try {
                            wait();
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                startTime = System.currentTimeMillis();
                doWork();
                long endTime = System.currentTimeMillis();
                long elapsedTimeDoingWork = endTime - startTime;
                remainingBurst -= elapsedTimeDoingWork;
                // System.out.println("Remaining Burst: " + remainingBurst + " ms");

                // check to see if the Task is done
                if (remainingBurst <= 0L) {
                    break;
                }
            }
        } finally {
            running = false; // Ensure running is false when the thread exits
        }
    }

    public void suspendTask() {
        synchronized (this) { // Still synchronized
            paused = true;
        }
    }

    public void resumeTask() {
        synchronized (this) { // Still synchronized
            paused = false;
            notify(); // Only notify *after* releasing the lock to prevent race conditions
        }
    }

    public void stopTask() {
        synchronized (this) {
            running = false;
            paused = false; // Release any pause as well
            if (internalThread != null) {
                internalThread.interrupt(); // Interrupt to exit wait immediately.
            }
            notifyAll();
        }
    }

    public boolean isTaskRunning() {
        return running;
    }

    public boolean isTaskAlive() {
        return internalThread != null && internalThread.isAlive();
    }

    public boolean isTaskPaused() {
        return paused;
    }
}