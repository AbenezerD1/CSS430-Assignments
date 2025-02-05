import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;

public class NoBusyWaitFactory {
    private static final int DEGREE_OF_BINOMIAL = 20;
    public static void main(String[] args) {
        SemaphoreNoBusyWait sem = new SemaphoreNoBusyWait(1);
        ConcurrentHashMap<Integer, BigInteger> sharedCalculatedBinomials = new ConcurrentHashMap<>();

        Thread workers[] = new Thread[DEGREE_OF_BINOMIAL+1];

        for(int i = 0; i <= DEGREE_OF_BINOMIAL; i++){
            workers[i] = new Thread(new Worker(sem, DEGREE_OF_BINOMIAL, i, sharedCalculatedBinomials));
        }
        System.out.println("All Threads Created");

        for (Thread w : workers) {
            w.start();
        }
        System.out.println("All Threads Started");

        for (Thread w : workers) {
            try {
                w.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(sharedCalculatedBinomials);
    }
}
