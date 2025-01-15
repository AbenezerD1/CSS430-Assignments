/**
 * Fork/join parallelism in Java
 *
 * Figure 4.18
 *
 * @author Stephen Dame 
 * Adapted from Gagne, Galvin, Silberschatz
 * Operating System Concepts  - Tenth Edition
 * Copyright John Wiley & Sons - 2018
 */

 import java.util.concurrent.*;

 public class SumTask extends RecursiveTask<Integer> {
     static final int SIZE = 10000;
     static final int THRESHOLD = 1000;
 
     private int begin;
     private int end;
     private int[] array;
 
     public SumTask(int begin, int end, int[] array) {
         this.begin = begin;
         this.end = end;
         this.array = array;
     }
 
     protected Integer compute() {
         if (end - begin < THRESHOLD) {
             // conquer stage 
             int sum = 0;
             for (int i = begin; i <= end; i++)
                 sum += array[i];
 
             return sum;
         }
         else {
             // divide stage 
             int mid = begin + (end - begin) / 2;
             
             SumTask leftTask = new SumTask(begin, mid, array);
             SumTask rightTask = new SumTask(mid + 1, end, array);
 
             leftTask.fork();
             rightTask.fork();
 
             return rightTask.join() + leftTask.join();
         }
     }
 }