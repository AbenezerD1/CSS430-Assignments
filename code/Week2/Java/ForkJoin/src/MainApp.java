import java.util.concurrent.*;

public class MainApp {
    public static void main(String[] args) throws Exception {
        ForkJoinPool pool = new ForkJoinPool();
        int[] array = new int[SumTask.SIZE];

        // create SIZE random integers between 0 and 9
        java.util.Random rand = new java.util.Random();

        for (int i = 0; i < SumTask.SIZE; i++) {
            array[i] = rand.nextInt(10);
        }

        // use fork-join parallelism to sum the array
        System.out.println("\n\nForkJoin Parallel Summation of " + SumTask.SIZE + " Random Integers");
        SumTask task = new SumTask(0, SumTask.SIZE - 1, array);
        int sum = pool.invoke(task);
        System.out.println("The sum is " + sum + "\n\n");
    }
}