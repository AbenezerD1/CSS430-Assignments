

## Round Robin CPU Scheduling Test 

### Task Creation: 
Tasks (A through E) are created with predefined burst times (execution times). The Submission Time of 0.0 ms indicates they all start at the same time.

### Round Robin Execution: 
The scheduler runs each task for a certain time slice (1000 ms). After the time slice expires, the task is preempted and moved to the back of the ready queue.

### Completion and Metrics: 
When a task completes its execution, the following metrics are recorded:

* Submission Time: When the task was initially submitted.
* Response Time: The time it took for the task to first start executing.
* TAT (Turnaround Time): The total time between submission and completion.
* Execution Time: The actual time used by the task.

Performance Table: Finally, the collected metrics are presented in a table, along with average values. This allows for analysis of the RR algorithm's performance in terms of response time, turnaround time, and overall efficiency.

## Comparison: 
Comparing this data to the MFQS, it has a higher average reponse time and TAT but about the same execution time. The difference in performance can be explained by the structure of the RR vs the MFQ schedulers. 

The MFQS is faster than the RR since the RR scheduler cycles through the tasks even if they take longer or shorter to execute. The MFQS uses multiple queues to allow tasks with shorter burst time to execute first allowing them to free up CPU time for the tasks that take longer to complete. This imporves the TAT and response time of the tasks.

<pre>
RR - raw output data in alphabetical order
Task A finished.
    Submission Time: 0.0 ms
    Response Time: 13.0 ms
    TAT: 5517.0 ms
    Execution Time: 1002.0 ms

Task B finished.
    Submission Time: 0.0 ms
    Response Time: 1112.0 ms
    TAT: 15448.0 ms
    Execution Time: 4011.0 ms

Task C finished.
    Submission Time: 0.0 ms
    Response Time: 2213.0 ms
    TAT: 11025.0 ms
    Execution Time: 2005.0 ms

Task D finished.
    Submission Time: 0.0 ms
    Response Time: 3312.0 ms
    TAT: 14336.0 ms
    Execution Time: 3011.0 ms

Task E finished.
    Submission Time: 0.0 ms
    Response Time: 4409.0 ms
    TAT: 16556.0 ms
    Execution Time: 5015.0 ms

RR - Performance Data Table
    T-submission    T-response    TAT    T-execution
Ta  0.0             13.0         5517.0      1002.0
Tb  0.0             1112.0       15448.0     4011.0
Tc  0.0             2213.0       11025.0     2005.0
Td  0.0             3312.0       14336.0     3011.0
Te  0.0             4409.0       16556.0     5015.0

AVG 0.0 ms         2211.8 ms     12576.4 ms  3008.8 ms
</pre>