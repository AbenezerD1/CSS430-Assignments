

#  MFQS CPU Scheduling Documentation

## Queue Initialization:
It creates an ArrayList named taskQueues containing three LinkedList queues, representing different priority levels (queue 0 w/ 500ms time quantum, queue 1 w/ 1000ms time quantum, and queue 2 w/ 2000ms time quantum).
It initializes a Logger array to keep track of execution metrics for each task.

## Task Population:
It populates the highest priority queue (queue 0) with OSTask objects. The task names and burst times are pulled from the taskData map.
The getTaskIndex helper method translates the task name (e.g., "Task A") into an array index.

## MFQ Simulation Loop:
The while loop continues as long as there are tasks in any of the queues (isAllTaskQueuesEmpty checks for this).
Inside the loop, it processes each queue in order of priority (0, then 1, then 2). It only moves to a lower priority queue if all higher priority queues are empty. 

## Queue Processing (processQueue method):
This method takes the taskQueues, the queueIndex, a timeSlice for how long a task can run in that queue, the nextQueue index (where to move the task if it doesn't complete), and the logs array.

* It dequeues a task from the currentQueue.
* It starts (or resumes) the task.
* It lets the task run for a maximum of timeSlice milliseconds using task.join(timeSlice). Then, it suspends the task.

### Key MFQ Logic: 
After the timeSlice, it checks higher-priority queues. If any higher-priority queue has tasks, it puts the current task back into the currentQueue and returns, thus preempting the current task and giving priority to the higher queue. This implements the "feedback" part of MFQ.

If the task finishes within the timeSlice (remaining burst time is 0), it prints a completion message, logs the end time, calculates turnaround time (TAT), and prints the logs using the Logger object.

If the task doesn't finish, it's moved to the next queue (lower priority). If already at queue 2, it creates a new thread to run the task then it's joined for the timeSlice.

## Logging:
When a task completes its execution, the following metrics are recorded:

* Submission Time: When the task was initially submitted.
* Response Time: The time it took for the task to first start executing.
* TAT (Turnaround Time): The total time between submission and completion.
* Execution Time: The actual CPU time used by the task.

Performance comparision is in the RR-doc.md file.

## MFQS - raw output data in alphabetical order
<pre>
Task A finished executing in queue 2.
    Submission Time: 0.0 ms
    Response Time: 509.0 ms
    TAT: 13045.0 ms
    Execution Time: 5010.0 ms

Task B finished executing in queue 1.
    Submission Time: 0.0 ms
    Response Time: 1.0 ms
    TAT: 3025.0 ms
    Execution Time: 1017.0 ms

Task C finished executing in queue 2.
    Submission Time: 0.0 ms
    Response Time: 2015.0 ms
    TAT: 11536.0 ms
    Execution Time: 3010.0 ms

Task D finished executing in queue 2.
    Submission Time: 0.0 ms
    Response Time: 1515.0 ms
    TAT: 15561.0 ms
    Execution Time: 6017.0 ms

Task E finished executing in queue 0.
    Submission Time: 0.0 ms
    Response Time: 1010.0 ms
    TAT: 1510.0 ms
    Execution Time: 500.0 ms

MFQS - Performance Data Table
    T-submission    T-response    TAT    T-execution
Ta  0.0             509.0        13045.0     5010.0
Tb  0.0             1.0          3025.0      1017.0
Tc  0.0             2015.0       11536.0     3010.0
Td  0.0             1515.0       15561.0     6017.0
Te  0.0             1010.0       1510.0      500.0

AVG 0.0 ms          1010.0 ms    8934.4 ms   3110.8 ms